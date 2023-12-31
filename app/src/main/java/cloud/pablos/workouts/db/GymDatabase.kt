package cloud.pablos.workouts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cloud.pablos.workouts.db.entities.Exercise
import cloud.pablos.workouts.db.entities.GymSet
import cloud.pablos.workouts.db.entities.Session
import cloud.pablos.workouts.db.entities.SessionExercise
import cloud.pablos.workouts.utils.Converters

@Database(
    entities = [
        Session::class,
        Exercise::class,
        SessionExercise::class,
        GymSet::class,
    ],
    autoMigrations = [],
    version = 2,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val dao: GymDAO
}
