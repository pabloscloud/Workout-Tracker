package cloud.pablos.workouts.ui.exercisepicker

import cloud.pablos.workouts.db.entities.Exercise
import cloud.pablos.workouts.utils.Event

sealed class PickerEvent : Event {
    data class ExerciseSelected(val exercise: Exercise) : PickerEvent()
    data class OpenGuide(val exercise: Exercise) : PickerEvent()
    object FilterSelected : PickerEvent()
    object FilterUsed : PickerEvent()
    data class SelectMuscle(val muscle: String) : PickerEvent()
    object DeselectMuscles : PickerEvent()
    data class SelectEquipment(val equipment: String) : PickerEvent()
    object DeselectEquipment : PickerEvent()
    object AddExercises : PickerEvent()
    data class SearchChanged(val text: String) : PickerEvent()
}
