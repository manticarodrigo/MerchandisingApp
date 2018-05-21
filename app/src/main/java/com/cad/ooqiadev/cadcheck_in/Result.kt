package com.cad.ooqiadev.cadcheck_in

import org.apache.commons.csv.CSVRecord
import java.io.Serializable

class Result : Serializable {
    var success: Boolean = false
    var message: String? = null
    var exception: Exception? = null
    var rows: Iterable<CSVRecord>? = null

    var location: Location? = null
    var activity: Activity? = null
    var task: Task? = null

    init {
        success = false
    }
}
