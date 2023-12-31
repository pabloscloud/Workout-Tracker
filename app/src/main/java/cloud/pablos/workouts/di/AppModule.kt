package cloud.pablos.workouts.di

import android.app.Application
import androidx.room.Room
import cloud.pablos.workouts.db.GymDatabase
import cloud.pablos.workouts.db.GymRepository
import cloud.pablos.workouts.db.StartingExercises
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGymDatabase(
        app: Application,
        callback: StartingExercises,
    ): GymDatabase {
        return Room
            .databaseBuilder(
                app,
                GymDatabase::class.java,
                "gym_database.db",
            )
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    @Singleton
    fun provideGymRepository(db: GymDatabase): GymRepository {
        return GymRepository(db.dao)
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

// detta används typ för att man ska kunna använda olika provideApplicationScopes
// behövs tekniskt sätt inte då jag bara har ett scope
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
