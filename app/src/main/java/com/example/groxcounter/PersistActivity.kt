package com.example.groxcounter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.groxcounter.grox.DecrementCounterCommand
import com.example.groxcounter.grox.IncrementCounterCommand
import com.example.groxcounter.grox.Store
import com.groupon.grox.Action
import com.groupon.grox.RxStores.states
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_counter.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subscriptions.CompositeSubscription
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

class PersistActivity : AppCompatActivity() {

    @Inject lateinit var store: Store
    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        createActivityScope()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        setTitle(R.string.persist_activity_name)

        subscriptions.add(states(store).observeOn(mainThread())
                .subscribe({ it -> counterText.text = it.toString() }, this::onError))

        subscriptions.add(RxView.clicks(incrementButton)
                .flatMap { (IncrementCounterCommand().actions() as Observable<Action<Int>>) }
                .subscribe(store::dispatch, this::onError))

        subscriptions.add(RxView.clicks(decrementButton)
                .flatMap { (DecrementCounterCommand().actions() as Observable<Action<Int>>) }
                .subscribe(store::dispatch, this::onError))
    }

    private fun createActivityScope() {
        val intermediateScope = Toothpick.openScopes(application, INTERMEDIATE_SCOPE)
        intermediateScope.installModules(Module().apply { bind(Store::class.java).toInstance(Store(0)) })
        val scope = Toothpick.openScopes(application, INTERMEDIATE_SCOPE, this)
        Toothpick.inject(this, scope)
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        Toothpick.closeScope(this)
        if (isFinishing) {
            Toothpick.closeScope(INTERMEDIATE_SCOPE)
        }
        super.onDestroy()
    }

    private fun onError(throwable: Throwable) = Unit

    companion object {
        private const val INTERMEDIATE_SCOPE = "INTERMEDIATE_SCOPE"
    }
}
