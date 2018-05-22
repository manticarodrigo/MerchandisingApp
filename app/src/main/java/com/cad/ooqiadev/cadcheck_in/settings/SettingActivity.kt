package com.cad.ooqiadev.cadcheck_in.settings

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.cad.ooqiadev.cadcheck_in.R
import android.widget.ImageView
import android.widget.TextView
import com.cad.ooqiadev.cadcheck_in.Utils
import org.apache.commons.net.io.Util

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Init activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // Set toolbar title
        supportActionBar?.title = "Configuracion"

        setEventListeners()
        setPreferences()

    }

    private fun setPreferences() {
        var editTextParameter: EditText
        val u = Utils()

        //FTP Host
        editTextParameter = findViewById(R.id.ftpHost)
        editTextParameter.setText(u.getPreferenceValue("ftpHost", baseContext))

        //FTP Host
        editTextParameter = findViewById(R.id.ftpUser)
        editTextParameter.setText(u.getPreferenceValue("ftpUser", baseContext))

        //FTP Host
        editTextParameter = findViewById(R.id.ftpPassword)
        editTextParameter.setText(u.getPreferenceValue("ftpPassword", baseContext))
    }

    private fun setEventListeners() {

        val editTextFTPServer = findViewById<EditText>(R.id.ftpHost)
        val editTextFTPUser = findViewById<EditText>(R.id.ftpUser)
        val editTextFTPPassword = findViewById<EditText>(R.id.ftpPassword)

        setOnFocusChangeListener(editTextFTPServer)
        setOnFocusChangeListener(editTextFTPUser)
        setOnFocusChangeListener(editTextFTPPassword)

    }

    private fun setOnFocusChangeListener(editText: EditText) {
        editText.onFocusChangeListener = View.OnFocusChangeListener(
                function = { view: View, hasFocus: Boolean ->
                    val edit = findViewById<EditText>(view.getId())
                    if (!hasFocus) {

                        when (view.id) {
                            R.id.ftpHost -> {
                                savePreferences("ftpHost", edit.text.toString())
                            }
                            R.id.ftpUser -> {
                                savePreferences("ftpUser", edit.text.toString())
                            }
                            R.id.ftpPassword -> {
                                savePreferences("ftpPassword", edit.text.toString())
                            }
                            else -> { }
                        }

                    }

                }
        )
    }

    private fun savePreferences(key: String, newValue: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()
        editor.putString(key, newValue)
        editor.commit()
    }

}
