package br.com.khomdrake.zenet.request.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.khomdrake.request.data.flow.MutableResponseStateFlow
import br.com.khomdrake.zenet.request.SampleRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WithCacheDiskViewModel(private val repository: SampleRepository): ViewModel() {

    val valueFlow = MutableResponseStateFlow<String>()

    fun withCache() {
        viewModelScope.launch {
            repository.withCacheDisk()
                .responseStateFlow
                .collectLatest {
                    valueFlow.emit(it)
                }
        }
    }

}