import android.content.Context
import androidx.room.Room
import com.tenx.backgroundremover.mvvm.database.room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    private const val DATABASE_NAME = "my_database"

    @Provides
    @Singleton
    fun providesDatabaseInstance(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    //Dao will be placed here

}
