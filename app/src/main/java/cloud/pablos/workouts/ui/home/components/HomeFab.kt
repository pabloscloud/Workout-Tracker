package cloud.pablos.workouts.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cloud.pablos.workouts.ui.home.HomeEvent
import cloud.pablos.workouts.utils.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFab(
    onEvent: (Event) -> Unit,
) {
    FloatingActionButton(
        onClick = { onEvent(HomeEvent.NewSession) },
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Icon(Icons.Default.Add, "Add Session")
    }
}
