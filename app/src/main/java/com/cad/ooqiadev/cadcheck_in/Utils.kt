package com.cad.ooqiadev.cadcheck_in

import android.content.Context
import android.net.ConnectivityManager

class Utils {
    init { }

    fun isInternetAvailable(context : Context): Boolean {
        // get Connectivity Manager object to check connection
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        val activeNetworkInfo = connectivityManager?.getActiveNetworkInfo()
        return activeNetworkInfo != null && activeNetworkInfo!!.isConnected()
    }
}
