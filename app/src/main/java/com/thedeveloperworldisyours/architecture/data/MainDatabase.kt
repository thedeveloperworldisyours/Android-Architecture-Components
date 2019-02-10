package com.thedeveloperworldisyours.architecture.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.content.Context

@Entity
data class Title constructor(val title: String, @PrimaryKey val id: Int = 0)

/***
 * Very small database that will hold one title
 */
@Dao
interface TitleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTitle(title: Title)

    @Query("select * from Title where id = 0")
    fun loadTitle(): LiveData<Title>
}

/**
 * TitleDatabase provides a reference to the dao to repositories
 */
@Database(entities = [Title::class], version = 1, exportSchema = false)
abstract class TitleDatabase : RoomDatabase() {
    abstract val titleDao: TitleDao
}

private lateinit var INSTANCE: TitleDatabase

/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): TitleDatabase {
    synchronized(TitleDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    TitleDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}