package com.example.groxcounter.grox

import com.groupon.grox.Store
import javax.inject.Inject
import javax.inject.Named

const val INITIAL_STATE = "INITIAL_STATE"

class Store @Inject constructor(@Named(INITIAL_STATE) initialState: Int?) : Store<Int>(initialState)