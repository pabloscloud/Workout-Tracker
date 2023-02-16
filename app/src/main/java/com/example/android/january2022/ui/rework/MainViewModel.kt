package com.example.android.january2022.ui.rework

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.january2022.db.GymRepository
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.ui.exercisepicker.PickerEvent
import com.example.android.january2022.ui.home.HomeEvent
import com.example.android.january2022.ui.session.SessionEvent
import com.example.android.january2022.ui.settings.SettingsEvent
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.Routes
import com.example.android.january2022.utils.UiEvent
import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repo: GymRepository,
  private val workoutTimer: WorkoutTimer
) : ViewModel() {


  private val _uiEvent = Channel<UiEvent>()
  val uiEvent = _uiEvent.receiveAsFlow()

  private val _homeState = MutableStateFlow(
    HomeState(
      sessions = repo.getAllSessions().map {
        Timber.d("Mapping Sessions.")
        it.map { session ->
          SessionWrapper(
            session = session,
            exercises = repo.getExercisesForSession(session).map { list ->
              Timber.d("Wrapping latest available list")
              list.map { se ->
                ExerciseWrapper(
                  sessionExercise = se.sessionExercise,
                  exercise = se.exercise,
                  sets = repo.getSetsForExercise(se.sessionExercise.sessionExerciseId)
                    .stateIn(viewModelScope)
                )
              }
            }.stateIn(viewModelScope),
            muscleGroups = repo.getMuscleGroupsForSession(session).stateIn(viewModelScope)
          )
        }
      }
    )
  )
  val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

  private val _pickerState = MutableStateFlow(
    PickerState(
      exercises = repo.getAllExercises(),
      selectedExercises = emptyList(),
      equipmentFilter = emptyList(),
      muscleFilter = emptyList(),
      filterUsed = false,
      filterSelected = false
    )
  )
  val pickerState = _pickerState.asStateFlow()

  private val _sessionState = MutableStateFlow(
    SessionState(
      currentSession = SessionWrapper(
        Session(),
        MutableStateFlow(emptyList()),
        MutableStateFlow(emptyList())
      ),
      selectedExercise = null
    )
  )
  val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

  private val _timerState = MutableStateFlow(
    TimerState(
      time = workoutTimer.time,
      isRunning = workoutTimer.isRunning,
      maxTime = workoutTimer.maxTime,
      finishedEvent = workoutTimer.finished
    )
  )
  val timerState = _timerState.asStateFlow()


  fun onEvent(event: Event) {
    Timber.d("Received event: $event")
    when (event) {
      /**
       * Home-related events.
       */
      is HomeEvent.SessionClicked -> {
        _sessionState.update {
          it.copy(
            currentSession = event.sessionWrapper
          )
        }
        Timber.d("currentSession updated: ${sessionState.value.currentSession}")
        sendUiEvent(UiEvent.Navigate(Routes.SESSION))
      }
      is HomeEvent.OpenSettings -> {
        sendUiEvent(UiEvent.Navigate(Routes.SETTINGS))
      }
      /**
       * Session-related events.
       */
      is SessionEvent.ExerciseSelection -> {
        event.exercise.let { se ->
          _sessionState.update {
            it.copy(
              selectedExercise = if (se != it.selectedExercise) se else null
            )
          }
        }
      }
      is SessionEvent.SetChanged -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.updateSet(event.updatedSet)
          }
        }
      }
      is SessionEvent.SetCreated -> {
        viewModelScope.launch {
          withContext(Dispatchers.IO) {
            repo.createSet(event.sessionExercise.sessionExercise)
          }
        }
      }
      is SessionEvent.TimerToggled -> workoutTimer.toggle()
      is SessionEvent.TimerReset -> workoutTimer.reset()
      is SessionEvent.TimerIncreased -> workoutTimer.increment()
      is SessionEvent.TimerDecreased -> workoutTimer.decrement()
      is SessionEvent.OpenGuide -> {
        sessionState.value.selectedExercise?.exercise?.let {
          openGuide(it)
        }
      }
      /**
       * ExercisePicker-related events.
       */
      is PickerEvent.OpenGuide -> openGuide(event.exercise)
      is PickerEvent.ExerciseSelected -> {
        _pickerState.update {
          it.copy(
            selectedExercises = buildList {
              if (it.selectedExercises.contains(event.exercise)) {
                addAll(it.selectedExercises.minusElement(event.exercise))
              } else {
                addAll(it.selectedExercises)
                add(event.exercise)
              }
            }
          )
        }
      }
      is PickerEvent.FilterSelected -> {
        _pickerState.update {
          it.copy(filterSelected = !it.filterSelected)
        }
      }
      is PickerEvent.FilterUsed -> {
        _pickerState.update {
          it.copy(filterUsed = !it.filterUsed)
        }
      }
      is PickerEvent.SelectMuscle -> {
        _pickerState.update {
          it.copy(
            muscleFilter =
            if (it.muscleFilter.contains(event.muscle)) {
              it.muscleFilter.minus(event.muscle)
            } else {
              it.muscleFilter.plus(event.muscle)
            }
          )
        }
      }
      is PickerEvent.DeselectMuscles -> {
        _pickerState.update {
          it.copy(muscleFilter = emptyList())
        }
      }
      /**
       * Settings-related events.
       */
      is SettingsEvent.ImportDatabase -> {
        viewModelScope.launch(Dispatchers.IO) {
          importDatabase(event.uri, event.context)
        }
      }
      is SettingsEvent.ExportDatabase -> {
        viewModelScope.launch(Dispatchers.IO) {
          exportDatabase(event.uri, event.context)
        }
      }
      is SettingsEvent.CreateFile -> {
        val date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
        sendUiEvent(UiEvent.FileCreated("workout_db_$date.json"))
      }
      is SettingsEvent.ClearDatabase -> {
        viewModelScope.launch(Dispatchers.IO) {
          repo.clearDatabase()
        }
      }
    }
  }

  private fun sendUiEvent(event: UiEvent) {
    viewModelScope.launch {
      _uiEvent.send(event)
    }
  }

  private fun openGuide(exercise: Exercise) {
    sendUiEvent(UiEvent.OpenWebsite(url = "https://duckduckgo.com/?q=! exrx ${exercise.title}"))
  }

  private fun exportDatabase(uri: Uri, context: Context) {
    val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
    val databaseModel = repo.getDatabaseModel()
    val ob = gson.toJson(databaseModel)
    saveToFile(uri, context.contentResolver, ob)
  }

  private fun importDatabase(uri: Uri, context: Context) {
    viewModelScope.launch {
      val gson = Converters.registerAll(GsonBuilder().setPrettyPrinting()).create()
      loadFromFile(uri, context.contentResolver)?.let {
        val importedDatabase = gson.fromJson(it, DatabaseModel::class.java)
        Timber.d("$importedDatabase")
        importedDatabase.sessions.forEach { session ->
          repo.insertSession(session)
        }
        importedDatabase.exercises.forEach { exercise ->
          repo.insertExercise(exercise)
        }
        importedDatabase.sessionExercises.forEach { sessionExercise ->
          repo.insertSessionExercise(sessionExercise)
        }
        importedDatabase.sets.forEach { set ->
          repo.insertSet(set)
        }
      }
    }
  }

  private fun saveToFile(uri: Uri, contentResolver: ContentResolver, content: String) {
    try {
      contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
        FileOutputStream(parcelFileDescriptor.fileDescriptor).use {
          it.write(content.toByteArray())
        }
      }
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun loadFromFile(uri: Uri, contentResolver: ContentResolver): String? {
    try {
      contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
        FileInputStream(parcelFileDescriptor.fileDescriptor).use {
          return it.readBytes().decodeToString()
        }
      }
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return null
  }
}