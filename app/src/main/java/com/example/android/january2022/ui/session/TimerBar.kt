package com.example.android.january2022.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.january2022.ui.rework.TimerState
import com.example.android.january2022.utils.Event

@Composable
fun TimerBar(
  timerState: TimerState,
  onEvent: (Event) -> Unit
) {
  val timerTime by timerState.time.collectAsState(initial = 0L)
  val timerRunning by timerState.isRunning.collectAsState(initial = false)
  val timerMaxTime by timerState.maxTime.collectAsState(initial = 1000L)
  val maxWidth = LocalConfiguration.current.screenWidthDp
  val timerWidth by remember {
    derivedStateOf {
      //maxWidth.times(timerTime.toFloat().div(timerMaxTime)).toInt().dp
      maxWidth.times(0.5).toInt().dp
    }
  }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(50.dp),
    tonalElevation = 8.dp
  ) {
    Box {
      Surface(
        modifier = Modifier
          .width(timerWidth)
          .height(50.dp),
        tonalElevation = 1400.dp
      ) {}
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Remove, "Decrease time")
          }
          Text(
            text = "$timerTime",
            textAlign = TextAlign.Center,
            modifier = Modifier.width(50.dp)
          )
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, "Increase time")
          }
        }
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Refresh, "Reset Timer")
          }
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Pause, "Toggle Timer")
          }
        }
      }
    }
  }
}
