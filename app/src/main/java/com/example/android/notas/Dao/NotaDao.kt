
package com.example.android.notas.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.notas.entidade.Nota
import kotlinx.coroutines.flow.Flow


@Dao
interface NotaDao {

    @Query("SELECT * FROM nota_database ORDER BY nota ASC")
    fun getAlphabetizedNotas(): Flow<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM nota_database")
    suspend fun deleteAll()
}
