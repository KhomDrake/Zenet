package br.com.khomdrake.test.robot

interface Given<W : When<T>, T : Then> {

    fun createThen() : T

    fun createWhen() : W

    infix fun `when`(func: W.() -> Unit) : W {
        return createWhen().apply(func)
    }

    infix fun then(func: T.() -> Unit) : T {
        return createThen().apply(func)
    }

}

interface When<C: Then> {

    fun createCheck() : C

    infix fun then(func: C.() -> Unit) : C {
        return createCheck().apply(func)
    }

}

interface Then
