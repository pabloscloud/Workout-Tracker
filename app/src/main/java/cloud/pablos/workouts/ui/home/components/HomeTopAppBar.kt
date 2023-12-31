package cloud.pablos.workouts.ui.home.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cloud.pablos.workouts.R
import cloud.pablos.workouts.utils.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onEvent: (Event) -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
    )
}
