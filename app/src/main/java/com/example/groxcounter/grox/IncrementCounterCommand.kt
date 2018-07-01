package com.example.groxcounter.grox

import com.groupon.grox.commands.rxjava1.SingleActionCommand

class IncrementCounterCommand : SingleActionCommand<Int>() {

    override fun newState(oldState: Int): Int {
        return oldState + 1
    }
}
