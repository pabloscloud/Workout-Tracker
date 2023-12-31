package cloud.pablos.workouts.ui.home

import cloud.pablos.workouts.ui.SessionWrapper
import cloud.pablos.workouts.utils.Event

sealed class HomeEvent : Event {
    data class SessionClicked(val sessionWrapper: SessionWrapper) : HomeEvent()
    object NewSession : HomeEvent()
    object OpenSettings : HomeEvent()
}
