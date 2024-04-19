package br.com.khomdrake.request.cache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.khomdrake.request.CacheType
import br.com.khomdrake.request.FakeDatabase
import br.com.khomdrake.request.log.LogHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class CacheTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDatabase: FakeDatabase

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mockkObject(LogHandler)
        mockkObject(DiskVault)
        mockkObject(MemoryVault)
        every { LogHandler.logInfo(any(), any()) } returns Unit
        fakeDatabase = mockk(relaxed = true)
    }

    @Test
    fun `when save is successful, should emit the correct logs`() = runTest {
        val data = "data-to-be-saved"
        val cacheKey = "cache-key"
        val mainKey = "main-key"
        coEvery { fakeDatabase.saveCache(cacheKey, data) } returns true
        val cache = Cache<String>(mainKey, cacheKey).apply {
            save { key, data ->
                fakeDatabase.saveCache(key, data)
            }
        }
        cache.save(data)
        verify {
            LogHandler.logInfo(mainKey, "[Cache] cache saved key: $cacheKey")
        }
        verify {
            LogHandler.logInfo(mainKey, "[Cache] cache saved data: $data")
        }
    }

    @Test
    fun `when save is successful, should save expiration date memory`() = runTest {
        val data = "data-to-be-saved"
        val cacheKey = "cache-key"
        val mainKey = "main-key"
        coEvery { fakeDatabase.saveCache(cacheKey, data) } returns true
        coEvery { MemoryVault.setData(cacheKey, any()) } returns Unit
        val cache = Cache<String>(mainKey, cacheKey).apply {
            save { key, data ->
                fakeDatabase.saveCache(key, data)
            }
            timeout(1.minutes, CacheType.MEMORY)
        }
        cache.save(data)
        coVerify {
            MemoryVault.setData(cacheKey, any())
        }
    }

    @Test
    fun `when save is successful, should save expiration date disk`() = runTest {
        val data = "data-to-be-saved"
        val cacheKey = "cache-key"
        val mainKey = "main-key"
        coEvery { fakeDatabase.saveCache(cacheKey, data) } returns true
        coEvery { DiskVault.setValue(cacheKey, any<Long>()) } returns Unit
        val cache = Cache<String>(mainKey, cacheKey).apply {
            save { key, data ->
                fakeDatabase.saveCache(key, data)
            }
            timeout(1.minutes, CacheType.DISK)
        }
        cache.save(data)
        coVerify {
            DiskVault.setValue(cacheKey, any<Long>())
        }
    }

    @Test
    fun `when save is unsuccessful, should emit the correct logs`() = runTest {
        val data = "data-to-be-saved"
        val cacheKey = "cache-key"
        val mainKey = "main-key"
        coEvery { fakeDatabase.saveCache(cacheKey, data) } returns false
        val cache = Cache<String>(mainKey, cacheKey).apply {
            save { key, data ->
                fakeDatabase.saveCache(key, data)
            }
        }
        cache.save(data)
        verify {
            LogHandler.logInfo(
                mainKey,
                """
                                [Cache] Cache not saved successfully with
                                    key: $cacheKey
                                    data: $data
                            """.trimIndent()
            )
        }
    }

    @Test
    fun `when save throws error, should emit the correct logs`() = runTest {
        val data = "data-to-be-saved"
        val cacheKey = "cache-key"
        val mainKey = "main-key"
        val exception = NotImplementedError()
        coEvery { fakeDatabase.saveCache(cacheKey, data) } throws exception
        val cache = Cache<String>(mainKey, cacheKey).apply {
            save { key, data ->
                fakeDatabase.saveCache(key, data)
            }
        }
        cache.save(data)
        verify {
            LogHandler.logInfo(
                mainKey,
                """
                            [Cache] Error while trying to save cache with
                                key: $cacheKey
                                data: $data

                                error: ${exception.stackTraceToString()}
                        """.trimIndent()
            )
        }
    }

    @Test
    fun `when save is called, should call save data configured`() = runTest {
        val data = "data-to-be-saved"
        val cacheKey = "cache-key"
        val mainKey = "main-key"
        coEvery { fakeDatabase.saveCache(cacheKey, data) } returns true
        val cache = Cache<String>(mainKey, cacheKey).apply {
            save { key, data ->
                fakeDatabase.saveCache(key, data)
            }
        }
        cache.save(data)
        coVerify {
            fakeDatabase.saveCache(cacheKey, data)
        }
    }

    @After
    fun shutdown() {
        unmockkAll()
    }

}