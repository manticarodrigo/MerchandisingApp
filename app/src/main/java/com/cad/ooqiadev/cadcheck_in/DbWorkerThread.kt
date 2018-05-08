package com.cad.ooqiadev.cadcheck_in

import android.os.Handler
import android.os.HandlerThread

class DbWorkerThread(threadName: String) : HandlerThread(threadName) {

    private lateinit var mWorkerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerHandler = Handler(looper)
    }

    fun postLocation(location: Runnable) {
        mWorkerHandler.post(location)
    }

    fun postActivity(activity: Runnable) {
        mWorkerHandler.post(activity)
    }

}