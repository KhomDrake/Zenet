package br.com.khomdrake.zenet.request

import br.com.khomdrake.request.CacheType
import br.com.khomdrake.request.requestHandler
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SampleRepository {

    private var memoryData: String? = null

    fun noCache() = requestHandler<List<String>>("No-Cache")
        .config {
            request {
                listOf(
                    "Sample ${Random.nextInt(0, Int.MAX_VALUE)}",
                    "Sample ${Random.nextInt(0, Int.MAX_VALUE)}",
                    "Sample ${Random.nextInt(0, Int.MAX_VALUE)}",
                    "Sample ${Random.nextInt(0, Int.MAX_VALUE)}",
                    "Sample ${Random.nextInt(0, Int.MAX_VALUE)}"
                )
            }
        }
        .execute()

    fun withCacheMemory() = requestHandler<String>("With-Cache")
        .config {
            request {
                "Sample Memory ${Random.nextInt(0, Int.MAX_VALUE)}"
            }
            cache {
                timeout(
                    2.minutes,
                    CacheType.MEMORY
                )
                retrieve { key ->
                    memoryData
                }
                save { key, data ->
                    memoryData = data
                }
            }
        }
        .execute()

    fun withCacheDisk() = requestHandler<String>("With-Cache-Disk")
        .config {
            request {
                "Sample Disk ${Random.nextInt(0, Int.MAX_VALUE)}"
            }
            cache {
                timeout(2.minutes, CacheType.DISK)
                retrieve { key ->
                    DiskData.getValue(key)
                }
                save { key, data ->
                    DiskData.setValue(key, data)
                }
            }
        }
        .execute()

}