package com.example.groxcounter

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import butterknife.ButterKnife
import com.example.groxcounter.grox.DecrementCounterCommand
import com.example.groxcounter.grox.IncrementCounterCommand
import com.example.groxcounter.grox.SimpleStore
import com.groupon.grox.Action
import com.groupon.grox.RxStores.states
import kotterknife.bindView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subscriptions.CompositeSubscription

class SimpleCounterActivity : AppCompatActivity() {

    private val simpleStore = SimpleStore(INITIAL_STORE_STATE)
    private val subscriptions = CompositeSubscription()

    private val counterText: TextView by bindView(R.id.counterText)
    private val incrementButton: Button by bindView(R.id.increment_button)
    private val decrementButton: Button by bindView(R.id.decrement_button)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        ButterKnife.bind(this)

        subscriptions.add(states(simpleStore).observeOn(mainThread())
                .subscribe({ it -> counterText.text = it.toString() }, this::onError))

        incrementButton.setOnClickListener {
            subscriptions.add((IncrementCounterCommand().actions() as Observable<Action<Int>>)
                    .subscribe(simpleStore::dispatch, this::onError))
        }

        decrementButton.setOnClickListener {
            subscriptions.add((DecrementCounterCommand().actions() as Observable<Action<Int>>)
                    .subscribe(simpleStore::dispatch, this::onError))
        }

    }

    @CallSuper
    override fun onDestroy() {
        subscriptions.unsubscribe();
        super.onDestroy()
    }

    private fun onError(throwable: Throwable) {
        // DO NOTHING
    }

    companion object {
        private const val INITIAL_STORE_STATE: Int = 0
    }
}
