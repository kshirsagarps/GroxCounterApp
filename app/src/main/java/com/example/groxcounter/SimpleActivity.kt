package com.example.groxcounter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.groxcounter.grox.DecrementCounterCommand
import com.example.groxcounter.grox.IncrementCounterCommand
import com.example.groxcounter.grox.Store
import com.groupon.grox.Action
import com.groupon.grox.RxStores.states
import kotlinx.android.synthetic.main.activity_counter.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subscriptions.CompositeSubscription

class SimpleActivity : AppCompatActivity() {

    private var store = Store(0)
    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        setTitle(R.string.simple_activity_name)

        subscriptions.add(states(store).observeOn(mainThread())
                .subscribe({ it -> counterText.text = it.toString() }, this::onError))

        incrementButton.setOnClickListener {
            subscriptions.add((IncrementCounterCommand().actions() as Observable<Action<Int>>)
                    .subscribe(store::dispatch, this::onError))
        }

        decrementButton.setOnClickListener {
            subscriptions.add((DecrementCounterCommand().actions() as Observable<Action<Int>>)
                    .subscribe(store::dispatch, this::onError))
        }

    }

    override fun onDestroy() {
        subscriptions.unsubscribe();
        super.onDestroy()
    }

    private fun onError(throwable: Throwable) = Unit
}
