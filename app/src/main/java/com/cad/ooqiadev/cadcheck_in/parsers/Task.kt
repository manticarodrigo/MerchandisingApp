package com.cad.ooqiadev.cadcheck_in.parsers

import com.cad.ooqiadev.cadcheck_in.Task
import com.cad.ooqiadev.cadcheck_in.Result
import org.apache.commons.csv.CSVRecord
import java.util.ArrayList

class Task {

    fun parse(taskRecords: Iterable<CSVRecord>): ArrayList<Task> {

        var parserResult: Result
        val task = ArrayList<Task>()

        for (record in taskRecords) {
            parserResult = parse(record)
            if (parserResult.success) {
                task.add(parserResult.task!!)
            }
        }

        return task
    }

    protected fun parse(record: CSVRecord): Result {
        val result = Result()

        val task = Task()

        try {

            task.activityId = record.get(0).toLong()
            task.description = record.get(1)
                task.status = record.get(2)
            if(record.get(3) != "") {
                task.comment = record.get(3)
            }
            if(record.get(4) != "") {
                task.endTime = record.get(4).toLong()
            }

            result.task = task
            result.success = true
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result
    }
}