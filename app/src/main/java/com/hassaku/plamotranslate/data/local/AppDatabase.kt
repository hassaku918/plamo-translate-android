package com.hassaku.plamotranslate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hassaku.plamotranslate.data.model.TranslationRecord
import com.hassaku.plamotranslate.data.model.WordRecord

@Database(
    entities = [TranslationRecord::class, WordRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
