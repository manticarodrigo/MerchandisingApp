package com.cad.ooqiadev.cadcheck_in

import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.provider.Settings.Global.getString

class Utils {
    init { }

     fun getPreferenceValue(key: String, context: Context ) : String {
        var name: String? = ""
        try {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            name = sharedPreferences.getString(key, "")
        } catch (ex: Exception) {
            name = null
        }

        return name!!
    }

    fun isInternetAvailable(context : Context): Boolean {
        // get Connectivity Manager object to check connection
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        val activeNetworkInfo = connectivityManager?.getActiveNetworkInfo()
        return activeNetworkInfo != null && activeNetworkInfo!!.isConnected()
    }
}
