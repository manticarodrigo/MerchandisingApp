package com.cad.ooqiadev.cadcheck_in.settings

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.cad.ooqiadev.cadcheck_in.R
import com.cad.ooqiadev.cadcheck_in.utils.Helpers
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {

    private val keyFields = listOf("plannedCode", "customerType", "ftpHost", "ftpUser", "ftpPassword")

    override fun onCreate(savedInstanceState: Bundle?) {

        // Init activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set toolbar title
        supportActionBar?.title = "Configuracion"

        setEventListeners()
        setPreferences()

    }

    fun onSaveClickBtn(v: View) {

        var currentEditText: EditText

        keyFields.forEach {
            currentEditText = findViewById(getStringResourceByName(it))
            savePreferences(it, currentEditText.text.toString())
        }

        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
    }

    private fun getStringResourceByName(aString: String): Int {
        val packageName = packageName
        val resId = resources.getIdentifier(aString, "id", packageName)
        return resId
    }

    private fun setPreferences() {
        var editTextParameter: EditText
        val u = Helpers()

        //Planned Code
        editTextParameter = findViewById(R.id.plannedCode)
        editTextParameter.setText(u.getPreferenceValue("plannedCode", baseContext))

        // customer Type
        editTextParameter = findViewById(R.id.customerType)
        editTextParameter.setText(u.getPreferenceValue("customerType", baseContext))

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

        val editTextPlannedCode = findViewById<EditText>(R.id.plannedCode)
        val editTextCustomerType = findViewById<EditText>(R.id.customerType)
        val editTextFTPServer = findViewById<EditText>(R.id.ftpHost)
        val editTextFTPUser = findViewById<EditText>(R.id.ftpUser)
        val editTextFTPPassword = findViewById<EditText>(R.id.ftpPassword)

        setOnFocusChangeListener(editTextPlannedCode)
        setOnFocusChangeListener(editTextCustomerType)
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
                            R.id.plannedCode -> {
                                savePreferences("plannedCode", edit.text.toString())
                            }
                            R.id.customerType -> {
                                savePreferences("customerType", edit.text.toString())
                            }
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
