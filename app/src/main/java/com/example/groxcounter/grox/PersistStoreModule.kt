package com.example.groxcounter.grox

import toothpick.config.Module

const val INITIAL_STORE_STATE: Int = 0

class PersistStoreModule : Module() {
    init {
        bind(Int::class.javaObjectType).withName(INITIAL_STATE).toInstance(INITIAL_STORE_STATE)
        bind(PersistStore::class.java).singletonInScope()
    }
}