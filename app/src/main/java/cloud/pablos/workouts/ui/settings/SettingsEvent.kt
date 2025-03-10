package cloud.pablos.workouts.ui.settings

import android.content.Context
import android.net.Uri
import cloud.pablos.workouts.utils.Event

sealed class SettingsEvent : Event {
    data class ExportDatabase(val context: Context, val uri: Uri) : SettingsEvent()
    data class ImportDatabase(val context: Context, val uri: Uri) : SettingsEvent()
    object CreateFile : SettingsEvent()
    object ClearDatabase : SettingsEvent()
}
