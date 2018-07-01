package com.example.groxcounter

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import butterknife.ButterKnife
import com.example.groxcounter.grox.DecrementCounterCommand
import com.example.groxcounter.grox.IncrementCounterCommand
import com.example.groxcounter.grox.PersistStore
import com.example.groxcounter.grox.PersistStoreModule
import com.groupon.grox.Action
import com.groupon.grox.RxStores.states
import kotterknife.bindView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subscriptions.CompositeSubscription
import toothpick.Toothpick
import javax.inject.Inject

class PersistCounterActivity : AppCompatActivity() {

    @Inject
    lateinit var persistStore: PersistStore
    private val subscriptions = CompositeSubscription()

    private val counterText: TextView by bindView(R.id.counterText)
    private val incrementButton: Button by bindView(R.id.increment_button)
    private val decrementButton: Button by bindView(R.id.decrement_button)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        createActivityScope()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        ButterKnife.bind(this)

        subscriptions.add(states(persistStore).observeOn(mainThread())
                .subscribe({ it -> counterText.text = it.toString() }, this::onError))

        incrementButton.setOnClickListener {
            subscriptions.add((IncrementCounterCommand().actions() as Observable<Action<Int>>)
                    .subscribe(persistStore::dispatch, this::onError))
        }

        decrementButton.setOnClickListener {
            subscriptions.add((DecrementCounterCommand().actions() as Observable<Action<Int>>)
                    .subscribe(persistStore::dispatch, this::onError))
        }

    }

    private fun createActivityScope() {
        val intermediateScope = Toothpick.openScopes(application, INTERMEDIATE_SCOPE)
        intermediateScope.installModules(PersistStoreModule())
        val scope = Toothpick.openScopes(INTERMEDIATE_SCOPE, this)
        Toothpick.inject(this, scope)
    }

    @CallSuper
    override fun onDestroy() {
        subscriptions.unsubscribe()
        Toothpick.closeScope(this)
        if (isFinishing) {
            Toothpick.closeScope(INTERMEDIATE_SCOPE)
        }
        super.onDestroy()
    }

    private fun onError(throwable: Throwable) {
        // DO NOTHING
    }

    companion object {
        private const val INTERMEDIATE_SCOPE = "INTERMEDIATE_SCOPE"
    }
}
