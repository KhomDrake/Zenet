package br.com.khomdrake.request

import android.content.Context
import androidx.annotation.VisibleForTesting
import br.com.arch.toolkit.livedata.response.MutableResponseLiveData
import br.com.arch.toolkit.livedata.response.ResponseLiveData
import br.com.khomdrake.request.cache.DiskVault
import br.com.khomdrake.request.data.Response
import br.com.khomdrake.request.data.flow.MutableResponseStateFlow
import br.com.khomdrake.request.data.flow.ResponseStateFlow
import br.com.khomdrake.request.data.responseData
import br.com.khomdrake.request.data.responseError
import br.com.khomdrake.request.data.responseLoading
import br.com.khomdrake.request.data.toDataResult
import br.com.khomdrake.request.log.LogHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val requestScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

class RequestHandler<Data>(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
    private val key: String = ""
) {

    private val lock = object {}

    private var config: Config<Data> = Config(key)

    private val _responseLiveData = MutableResponseLiveData<Data>()

    private val _responseStateFlow = MutableResponseStateFlow<Data>(Response())

    val responseLiveData: ResponseLiveData<Data>
        get() = _responseLiveData

    val responseStateFlow: ResponseStateFlow<Data>
        get() = _responseStateFlow
            .shareIn(
                requestScope,
                SharingStarted.WhileSubscribed(),
                replay = 0
            )

    fun config(func: Config<Data>.() -> Unit) = apply {
        config = Config<Data>(key).apply(func)
    }

    fun logInfo(info: String) {
        LogHandler.logInfo(key, info)
    }

    fun execute() = synchronized(lock) {
        scope.launch {
            runCatching {
                createExecution().start()
            }.onFailure {
                _responseStateFlow.emitError(it)
                _responseLiveData.postError(it)
            }
        }
        return@synchronized this
    }

    @VisibleForTesting
    fun setData(data: Data) = apply {
        requestScope.launch {
            _responseLiveData.postData(data)
            _responseStateFlow.emit(responseData(data))
        }
    }

    @VisibleForTesting
    fun setLoading() {
        requestScope.launch {
            _responseLiveData.postLoading()
            _responseStateFlow.emit(responseLoading())
        }
    }

    @VisibleForTesting
    fun setError(throwable: Throwable = Throwable()) {
        requestScope.launch {
            _responseLiveData.postError(throwable)
            _responseStateFlow.emit(responseError(throwable))
        }
    }

    private fun createExecution() = scope.launch {
        logInfo("[Execution] creating execution")
        flow {
            config.execute(
                this,
                this@RequestHandler
            )
        }
            .onEach {
                _responseLiveData.postValue(it.toDataResult())
            }
            .catch {
                _responseStateFlow.emitError(it)
            }
            .collect {
                _responseStateFlow.emit(it)
            }

    }

    companion object {

        fun init(context: Context, cleanCacheTimeout: Boolean = false) {
            DiskVault.init(context)
            if(cleanCacheTimeout) DiskVault.clearCache()
        }

    }

}

fun <T>requestHandler(
    key: String = "",
    scope: CoroutineScope = requestScope
) : RequestHandler<T> {
    return RequestHandler(scope, key)
}
