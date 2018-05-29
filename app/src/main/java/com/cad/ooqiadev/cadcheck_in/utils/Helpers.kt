package com.cad.ooqiadev.cadcheck_in.utils

import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import java.io.File
import android.util.Log

class Helpers {
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

    fun createDirs(filesDir: File, fileName: String) {

        val dirs = fileName.split("/")

        if(dirs.size > 1) {

            try {

                val file = File(filesDir, dirs[0])
                if (!file.exists()) {
                    file.mkdirs()
                }

            } catch (e: Exception) {
                Log.d("Exception", e.message)
            }

        }

    }

}
