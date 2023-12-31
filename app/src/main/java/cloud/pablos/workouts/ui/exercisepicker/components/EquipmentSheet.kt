package cloud.pablos.workouts.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import cloud.pablos.workouts.db.Equipment
import cloud.pablos.workouts.ui.exercisepicker.PickerEvent
import cloud.pablos.workouts.utils.Event

@Composable
fun EquipmentSheet(
    selectedEquipment: List<String>,
    onEvent: (Event) -> Unit,
) {
    Sheet(
        items = Equipment.getAllEquipment().sorted(),
        selectedItems = selectedEquipment,
        title = "Filter by Equipment",
        onSelect = { onEvent(PickerEvent.SelectEquipment(it)) },
    ) {
        onEvent(PickerEvent.DeselectEquipment)
    }
}
