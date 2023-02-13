package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.session.actions.ActionSpacer
import com.example.android.january2022.ui.session.actions.ActionSpacerStart
import com.example.android.january2022.ui.session.actions.TimerAction
import com.example.android.january2022.utils.Event

@Composable
fun SessionAppBar(
  onEvent: (Event) -> Unit,
  timerVisible: Boolean,
  onTimerPress: () -> Unit
) {
  BottomAppBar(
    actions = {
      ActionSpacerStart()
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
      }
      ActionSpacer()
      TimerAction(timerVisible,onTimerPress)
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = MaterialTheme.colorScheme.primary
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
      }
    }
  )
}