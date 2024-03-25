package br.com.khomdrake.request.cache

import br.com.khomdrake.request.CacheType
import br.com.khomdrake.request.log.LogHandler
import kotlin.math.exp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class Cache<Data> internal constructor(private val key: String) {

    private var type = CacheType.DISK
    private var timeout: Duration = 10.minutes
    private var retrieve: (suspend (String) -> Data?)? = null
    private var save: (suspend (String, Data) -> Unit)? = null

    fun save(function: (suspend (key: String, data: Data) -> Unit)?) = apply {
        save = function
    }

    fun retrieve(function: suspend (key: String) -> Data?) = apply {
        retrieve = function
    }

    fun timeout(
        duration: Duration,
        newType: CacheType
    ) = apply {
        timeout = duration
        type = newType
    }

    suspend fun save(key: String, data: Data) = apply {
        save?.invoke(key, data)
    }

    suspend fun retrieve(key: String) = retrieve?.invoke(key)

    suspend fun saveExpirationDate() {
        val expirationDate = System.currentTimeMillis() + timeout.inWholeMilliseconds

        if(type == CacheType.DISK) {
            DiskVault.setValue(key, expirationDate)
        } else {
            MemoryVault.setData(key, expirationDate)
        }
    }

    suspend fun isExpired(currentTime: Long) : Boolean {
        val expirationDate = (if(type == CacheType.DISK) DiskVault.getValueLong(key)
            else MemoryVault.getDataLong(key)) ?: return true

        return currentTime >= expirationDate
    }

}
