
package com.example.android.notas

import android.app.Application
import com.example.android.notas.bd.WordRoomDatabase
import com.example.android.roomwordssample.NotaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotasApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NotaRepository(database.wordDao()) }
}
