package cloud.pablos.workouts.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import cloud.pablos.workouts.utils.FuzzySearch
import cloud.pablos.workouts.utils.turnTargetIntoMuscleGroups

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var title: String = "Exercise",
    var type: String? = null,
    var force: List<String> = emptyList(),
    var equipment: List<String> = emptyList(),
    var targets: List<String> = emptyList(),
    var synergists: List<String> = emptyList(),
    var stabilizers: List<String> = emptyList(),
) {
    fun getMuscleGroups(exercise: Exercise = this): List<String> {
        return exercise.targets.flatMap {
            turnTargetIntoMuscleGroups(it)
        }.distinct()
    }

    fun getStringMatch(string: String): Boolean {
        return FuzzySearch.regexMatch(string, title)
    }
}
