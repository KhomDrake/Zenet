package br.com.khomdrake.tests.koin

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin

class KoinRule(
    private val shouldStart: Boolean = true
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    kotlin.runCatching {
                        if(shouldStart) {
                            startKoin {
                                androidContext(ApplicationProvider.getApplicationContext())
                            }
                        }
                    }
                    base.evaluate()
                } finally {
                    stopKoin()
                }
            }
        }
    }

}