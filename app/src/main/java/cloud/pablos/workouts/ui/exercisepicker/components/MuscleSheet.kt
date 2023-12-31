package cloud.pablos.workouts.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import cloud.pablos.workouts.db.MuscleGroup
import cloud.pablos.workouts.ui.exercisepicker.PickerEvent
import cloud.pablos.workouts.utils.Event

@Composable
fun MuscleSheet(
    selectedMusclegroups: List<String>,
    onEvent: (Event) -> Unit,
) {
    Sheet(
        items = MuscleGroup.getAllMuscleGroups().sorted(),
        selectedItems = selectedMusclegroups,
        title = "Filter by Body-part",
        onSelect = { onEvent(PickerEvent.SelectMuscle(it)) },
    ) {
        onEvent(PickerEvent.DeselectMuscles)
    }
}
