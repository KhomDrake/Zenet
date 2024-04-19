package br.com.khomdrake.request

import br.com.khomdrake.request.cache.DiskVault
import br.com.khomdrake.request.cache.MemoryVault
import br.com.khomdrake.request.exception.RequestNotImplementedException
import br.com.khomdrake.request.log.LogHandler
import br.com.khomdrake.test.robot.Check
import br.com.khomdrake.test.robot.Given
import br.com.khomdrake.test.robot.Launch
import br.com.khomdrake.test.robot.Setup
import br.com.khomdrake.test.robot.Then
import br.com.khomdrake.test.robot.When
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import org.junit.Assert
import kotlin.time.Duration.Companion.minutes

fun RequestHandlerTest.requestHandlerTest(
    fakeApi: FakeApi,
    fakeDatabase: FakeDatabase,
    func: RequestHandlerSetup.() -> Unit
) =
    RequestHandlerSetup(fakeDatabase, fakeApi).apply(func)


class RequestHandlerSetup(
    private val fakeDatabase: FakeDatabase,
    private val fakeApi: FakeApi
) : Given<RequestHandlerLaunch, RequestHandlerCheck> {
    private var cacheType = CacheType.MEMORY
    private var key = "default"
    private var cacheKey = "default"
    private var requestHandler = requestHandler<String>(key)

    override fun createThen(): RequestHandlerCheck {
        return RequestHandlerCheck(fakeDatabase, fakeApi, requestHandler)
    }

    override fun createWhen(): RequestHandlerLaunch {
        return RequestHandlerLaunch(fakeDatabase, fakeApi, requestHandler)
    }

    fun withRequestNotConfigured() {
        requestHandler = requestHandler<String>(key)
            .config {

            }
    }

    fun withRequestConfigured() {
        requestHandler = requestHandler<String>(key)
            .config {
                request {
                    fakeApi.request()
                }
            }
    }

    fun withApiData(expectedData: String) {
        coEvery { fakeApi.request() } returns expectedData
    }

    fun withRemoveCache() {
        coEvery { fakeDatabase.removeCache(cacheKey) } returns true
    }

    fun withRetrieveCache(expectedData: String) {
        coEvery { fakeDatabase.retrieveCache(cacheKey) } returns expectedData
    }

    fun withSaveCache(expectedData: String) {
        coEvery { fakeDatabase.saveCache(cacheKey, expectedData) } returns true
    }

    fun withKey(newKey: String) {
        val oldKey = key
        key = newKey
        if(cacheKey == oldKey || cacheKey == "default") {
            cacheKey = newKey
        }
    }

    fun withRequestCacheConfigured() {
        requestHandler = requestHandler<String>(key)
            .config {
                request {
                    fakeApi.request()
                }
                cache {
                    timeout(
                        2.minutes,
                        cacheType
                    )
                    setCacheKey(cacheKey)
                    save { key, data ->
                        fakeDatabase.saveCache(key, data)
                    }
                    retrieve {
                        fakeDatabase.retrieveCache(it)
                    }
                    remove {
                        fakeDatabase.removeCache(it)
                    }
                }
            }
    }

    fun withCacheExpired(key: String) {
        val time = System.currentTimeMillis() - (10.minutes).inWholeMilliseconds
        coEvery { DiskVault.getValueLong(key) } returns time
        coEvery { MemoryVault.getDataLong(key) } returns time
    }

    fun withCacheNotExpired(key: String) {
        val time = System.currentTimeMillis() + (10.minutes).inWholeMilliseconds
        coEvery { DiskVault.getValueLong(key) } returns time
        coEvery { MemoryVault.getDataLong(key) } returns time
    }

    fun withCacheKey(newCacheKey: String) {
        cacheKey = newCacheKey
    }

    fun withCacheMemory() {
        cacheType = CacheType.MEMORY
    }

    fun withCacheDisk() {
        cacheType = CacheType.DISK
    }

    fun withCacheNotSet(cacheKey: String) {
        coEvery { DiskVault.getValueLong(cacheKey) } returns null
        coEvery { MemoryVault.getDataLong(cacheKey) } returns null
    }
}

class RequestHandlerLaunch(
    private val fakeDatabase: FakeDatabase,
    private val fakeApi: FakeApi,
    private val requestHandler: RequestHandler<String>
) : When<RequestHandlerCheck> {
    override fun createCheck(): RequestHandlerCheck {
        return RequestHandlerCheck(fakeDatabase, fakeApi, requestHandler)
    }

    fun callRequest() {
        requestHandler.execute()
    }

}

class RequestHandlerCheck(
    private val fakeDatabase: FakeDatabase,
    private val fakeApi: FakeApi,
    private val requestHandler: RequestHandler<String>
) : Then {

    fun withRequestNoImplementedException() {
        val error = requestHandler.responseLiveData.awaitError()
        Assert.assertTrue(error is RequestNotImplementedException)
    }

    fun withData(expected: String) {
        val result = requestHandler.responseLiveData.awaitData()
        Assert.assertEquals(expected, result)
    }

    fun withRequestEndedLog(expected: String, key: String) {
        LogHandler.logInfo(
            key,
            "[Config] Request ended, data: $expected"
        )
    }

    fun withDataEmittedLog(expected: String, key: String) {
        LogHandler.logInfo(
            key,
            "[Config] Data emitted: $expected"
        )
    }

    fun cacheSaved(expectedData: String, key: String) {
        requestHandler.responseLiveData.awaitData()
        coVerify {
            fakeDatabase.saveCache(key, expectedData)
        }
    }

    fun saveCacheNotCalled(expectedData: String, key: String) {
        requestHandler.responseLiveData.awaitData()
        coVerify(exactly = 0) {
            fakeDatabase.saveCache(key, expectedData)
        }
    }

    fun removeCacheCalled(key: String) {
        requestHandler.responseLiveData.awaitData()
        verify {
            LogHandler.logInfo(key, "[Config] Request started - Cache expired")
        }
        verify {
            LogHandler.logInfo(key, "[Cache] Cache removed successfully for key $key")
        }
        coVerify {
            fakeDatabase.removeCache(key)
        }
    }

    fun withCacheEmittedLog(expectedData: String, normalKey: String, cacheKey: String) {
        requestHandler.responseLiveData.awaitData()

        verify {
            LogHandler.logInfo(normalKey, "[Cache] cache saved key: $cacheKey")
        }
        verify {
            LogHandler.logInfo(normalKey, "[Cache] cache saved data: $expectedData")
        }
    }

    fun saveCacheMemory(key: String) {
        requestHandler.responseLiveData.awaitData()
        coVerify {
            MemoryVault.setData(key, any())
        }
    }

    fun saveCacheDisk(key: String) {
        requestHandler.responseLiveData.awaitData()
        coVerify {
            DiskVault.setValue(key, any<Long>())
        }
    }

    fun apiNotCalled() {
        coVerify(exactly = 0) {
            fakeApi.request()
        }
    }

    fun apiCalled() {
        coVerify(exactly = 1) {
            fakeApi.request()
        }
    }

}

