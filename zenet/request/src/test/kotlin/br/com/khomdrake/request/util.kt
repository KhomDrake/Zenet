package br.com.khomdrake.request

import br.com.arch.toolkit.livedata.response.ResponseLiveData

fun ResponseLiveData<*>.awaitData() = run {
    while(value?.data == null) {
        Thread.sleep(50)
    }

    value?.data
}

fun ResponseLiveData<*>.awaitError() = run {
    while(value?.error == null) {
        Thread.sleep(50)
    }

    value?.error
}
