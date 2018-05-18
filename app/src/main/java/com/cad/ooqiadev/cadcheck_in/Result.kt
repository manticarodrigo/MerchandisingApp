package com.cad.ooqiadev.cadcheck_in

import java.io.Serializable

class Result : Serializable {
    var isSuccess: Boolean = false
    var message: String? = null
    var exception: Exception? = null

    init {
        isSuccess = false
    }
}
