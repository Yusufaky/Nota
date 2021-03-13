package com.example.android.notas.ViewModel

import androidx.lifecycle.*
import com.example.android.notas.entidade.Nota
import com.example.android.roomwordssample.NotaRepository
import kotlinx.coroutines.launch

class NotaViewModel(private val repository: NotaRepository) : ViewModel() {

    val allNotas: LiveData<List<Nota>> = repository.allNotas.asLiveData()

    fun insert(nota: Nota) = viewModelScope.launch {
        repository.insert(nota)
    }
    fun deleteNota(nota: Nota) = viewModelScope.launch {
        repository.deleteByNota(nota)
    }
}

class NotaViewModelFactory(private val repository: NotaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
