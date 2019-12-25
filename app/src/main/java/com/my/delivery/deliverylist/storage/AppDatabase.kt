package com.my.delivery.deliverylist.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.my.delivery.R
import com.my.delivery.deliverylist.model.Delivery

@Database (entities = [Delivery::class], version = 1)
@TypeConverters(Converters::class) // using type converter to parse object as json while storing in database
abstract class AppDatabase : RoomDatabase() {
    abstract fun deliveryDao():DeliveryDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // get singleton instance of this class
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // create a room database instance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    context.getString(R.string.database_name)
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}