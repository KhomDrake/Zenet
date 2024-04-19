package br.com.khomdrake.request

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.khomdrake.request.cache.DiskVault
import br.com.khomdrake.request.cache.MemoryVault
import br.com.khomdrake.request.log.LogHandler
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class FakeApi {

    suspend fun request() : String = ""

}

class FakeDatabase {

    suspend fun removeCache(key: String) = true

    suspend fun retrieveCache(key: String) : String = ""

    suspend fun saveCache(key: String, data: String) = true

}

@OptIn(ExperimentalCoroutinesApi::class)
class RequestHandlerTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeApi: FakeApi
    private lateinit var fakeDatabase: FakeDatabase

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mockkObject(LogHandler)
        mockkObject(DiskVault)
        mockkObject(MemoryVault)
        every { LogHandler.logInfo(any(), any()) } returns Unit
        fakeApi = mockk(relaxed = true)
        fakeDatabase = mockk(relaxed = true)
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun `with cache set, should not call request`() = runTest {
        val expectedData = "With-Cache-Configured"
        val key = "normal-key"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withApiData(expectedData)
            withRemoveCache()
            withCacheNotSet(key)
            withSaveCache(expectedData)
            withCacheNotExpired(key)
            withRequestCacheConfigured()
        } `when` {
            callRequest()
        } then {
            apiNotCalled()
        }
    }

    @Test
    fun `with request not configured, should return error`() = runTest {
        requestHandlerTest(fakeApi, fakeDatabase) {
            withRequestNotConfigured()
        } `when` {
            callRequest()
        } then {
            withRequestNoImplementedException()
        }
    }

    @Test
    fun `with request configured, should return data and log correct data`() = runTest {
        val expectedData = "With-Request-Configured"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey("my-key")
            withRequestConfigured()
            withApiData(expectedData)
        } `when` {
            callRequest()
        } then {
            apiCalled()
            withData(expectedData)
            withRequestEndedLog(
                expected = expectedData,
                key = "my-key"
            )
            withDataEmittedLog(
                expected = expectedData,
                key = "my-key"
            )
        }
    }

    @Test
    fun `with cache configured, should call save cache`() = runTest {
        val expectedData = "With-Cache-Configured"
        val key = "normal-key"

        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey(key)
            withRequestCacheConfigured()
            withApiData(expectedData)
            withRemoveCache()
            withCacheNotSet(key)
            withSaveCache(expectedData)
            withRetrieveCache(expectedData)
        } `when` {
            callRequest()
        } then {
            cacheSaved(expectedData, key)
        }
    }

    @Test
    fun `without cache, should not call save cache`() = runTest {
        val expectedData = "Without-Cache-Configured"
        val apiData = "Without-Cache-Configured"
        val key = "normal-key"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey(key)
            withRequestConfigured()
            withRemoveCache()
            withSaveCache(expectedData)
            withRetrieveCache(expectedData)
            withApiData(apiData)
        } `when` {
            callRequest()
        } then {
            saveCacheNotCalled(expectedData, key)
            withData(expectedData)
        }
    }

    @Test
    fun `with cache expired, should call remove cache and call request`() = runTest {
        val expectedData = "With-Cache-Configured"
        val apiData = "With-Cache-Configured"
        val key = "key"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey(key)
            withRequestCacheConfigured()
            withRemoveCache()
            withCacheMemory()
            withSaveCache(expectedData)
            withRetrieveCache(expectedData)
            withApiData(apiData)
            withCacheExpired(key)
        } `when` {
            callRequest()
        } then {
            removeCacheCalled(key)
            withData(expectedData)
        }
    }

    @Test
    fun `with cache with different key, should log correct data`() = runTest {
        val expectedData = "With-Cache-Configured"
        val apiData = "With-Cache-Configured"
        val normalKey = "normal-key"
        val cacheKey = "cache-key"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey(normalKey)
            withCacheKey(cacheKey)
            withRemoveCache()
            withSaveCache(expectedData)
            withRetrieveCache(expectedData)
            withApiData(apiData)
            withCacheNotSet(cacheKey)
            withRequestCacheConfigured()
        } `when` {
            callRequest()
        } then {
            withDataEmittedLog(expectedData, normalKey)
            cacheSaved(expectedData, cacheKey)
            withCacheEmittedLog(expectedData, normalKey, cacheKey)
        }
    }

    @Test
    fun `with cache in memory, should save and retrieve expiration data on memory`() = runTest {
        val expectedData = "Without-Cache-Configured"
        val apiData = "Without-Cache-Configured"
        val normalKey = "normal-key"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey(normalKey)
            withRemoveCache()
            withCacheMemory()
            withSaveCache(expectedData)
            withRetrieveCache(expectedData)
            withApiData(apiData)
            withCacheNotSet(normalKey)
            withRequestCacheConfigured()
        } `when` {
            callRequest()
        } then {
            saveCacheMemory(normalKey)
        }
    }

    @Test
    fun `with cache in disk, should save and retrieve expiration data on disk`() = runTest {
        val expectedData = "Without-Cache-Configured"
        val apiData = "Without-Cache-Configured"
        val normalKey = "normal-key"
        requestHandlerTest(fakeApi, fakeDatabase) {
            withKey(normalKey)
            withCacheDisk()
            withRemoveCache()
            withSaveCache(expectedData)
            withRetrieveCache(expectedData)
            withApiData(apiData)
            withCacheNotSet(normalKey)
            withRequestCacheConfigured()
        } `when` {
            callRequest()
        } then {
            saveCacheDisk(normalKey)
        }
    }

}