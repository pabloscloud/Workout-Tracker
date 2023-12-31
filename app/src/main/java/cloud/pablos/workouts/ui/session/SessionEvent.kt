package cloud.pablos.workouts.ui.session

import cloud.pablos.workouts.db.entities.GymSet
import cloud.pablos.workouts.ui.ExerciseWrapper
import cloud.pablos.workouts.utils.Event
import java.time.LocalTime

sealed class SessionEvent : Event {
    data class ExerciseExpanded(val exercise: ExerciseWrapper) : SessionEvent()
    data class ExerciseSelected(val exercise: ExerciseWrapper) : SessionEvent()
    data class SetChanged(val updatedSet: GymSet) : SessionEvent()
    data class SetCreated(val sessionExercise: ExerciseWrapper) : SessionEvent()
    data class SetDeleted(val set: GymSet) : SessionEvent()

    object RemoveSelectedExercises : SessionEvent()
    object RemoveSession : SessionEvent()
    object DeselectExercises : SessionEvent()

    object TimerToggled : SessionEvent()
    object TimerReset : SessionEvent()
    object TimerIncreased : SessionEvent()
    object TimerDecreased : SessionEvent()

    object OpenGuide : SessionEvent()
    object AddExercise : SessionEvent()

    data class StartTimeChanged(val newTime: LocalTime) : SessionEvent()
    data class EndTimeChanged(val newTime: LocalTime) : SessionEvent()
}
