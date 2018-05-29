package com.cad.ooqiadev.cadcheck_in.utils

import com.cad.ooqiadev.cadcheck_in.models.Customer
import com.cad.ooqiadev.cadcheck_in.models.Item
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import org.apache.commons.csv.CSVRecord
import java.io.Serializable

class Result : Serializable {
    var success: Boolean = false
    var message: String? = null
    var exception: Exception? = null
    var rows: Iterable<CSVRecord>? = null

    var customer: Customer? = null
    var taskCatalog: TaskCatalog? = null
    var task: Task? = null
    var item: Item? = Item()

    init {
        success = false
    }
}
