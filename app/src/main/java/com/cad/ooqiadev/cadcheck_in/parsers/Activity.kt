package com.cad.ooqiadev.cadcheck_in.parsers

import com.cad.ooqiadev.cadcheck_in.Activity
import com.cad.ooqiadev.cadcheck_in.Result
import org.apache.commons.csv.CSVRecord
import java.util.ArrayList

class Activity {

    fun parse(locationRecords: Iterable<CSVRecord>): ArrayList<Activity> {

        var parserResult: Result
        val location = ArrayList<Activity>()

        for (record in locationRecords) {
            parserResult = parse(record)
            if (parserResult.success) {
                location.add(parserResult.activity!!)
            }
        }

        return location
    }

    protected fun parse(record: CSVRecord): Result {
        val result = Result()

        val activity = Activity()

        try {
            activity.id = record.get(0).toLong()
            activity.ownerId = record.get(1).toLong()
            activity.locationId = record.get(2).toLong()
            activity.orderId = record.get(3).toLong()
            activity.title = record.get(4)
            activity.dueTime = record.get(5).toLong()

            if(record.get(6) != "") {
                activity.startTime = record.get(6).toLong()
            }

            if(record.get(7) != "") {
                activity.endTime = record.get(7).toLong()
            }

            result.activity = activity
            result.success = true
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result
    }
}