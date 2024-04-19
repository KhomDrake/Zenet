package br.com.khomdrake.test.robot

interface Given<W : When<T>, T : Then> {

    fun createThen() : T

    fun createWhen() : W

    fun setupWhen()

    infix fun `when`(func: W.() -> Unit) : W {
        setupWhen()
        return createWhen().apply(func)
    }

    infix fun then(func: T.() -> Unit) : T {
        setupWhen()
        return createThen().apply(func)
    }

}

interface When<T: Then> {

    fun createThen() : T

    infix fun then(func: T.() -> Unit) : T {
        return createThen().apply(func)
    }

}

interface Then
