import androidx.room.Database
import androidx.room.RoomDatabase
import com.tenx.backgroundremover.mvvm.database.room.ItemsModelDao
import com.tenx.backgroundremover.mvvm.models.ItemsModel

//change to your model class
@Database(entities = [yourModelClass::class], version = 1 , exportSchema = true)
abstract class Database: RoomDatabase() {
    //place your dao here i.e:  ( abstract fun myDao(): MyDao )
}
