package com.example.android.roomwordssample

import androidx.annotation.WorkerThread
import com.example.android.notas.entidade.Nota
import com.example.android.notas.Dao.NotaDao
import kotlinx.coroutines.flow.Flow


class NotaRepository(private val notaDao: NotaDao) {


    val allNotas: Flow<List<Nota>> = notaDao.getAlphabetizedNotas()

    @Suppress("RedundantSuspendModifier")

    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteNota(id: Int){
        notaDao.deleteByNota(id)
    }
}
