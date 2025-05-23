package com.use.steps.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent <T> : MutableLiveData<T>(){
    companion object {
        private val TAG = "SingleLiveEvent"
    }

    private val mPending = AtomicBoolean(false)

    @MainThread
    fun observer(owner : LifecycleOwner, observer: Observer<T>){
        if(hasActiveObservers()){
            Log.w(TAG, "Multiple observer has been registered but only one will work")
        }
        super.observe(owner, Observer {
            if(mPending.compareAndSet(true, false)){
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() { setValue(null) }
}