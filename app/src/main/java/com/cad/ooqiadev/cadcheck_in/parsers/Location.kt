package com.cad.ooqiadev.cadcheck_in.parsers

import com.cad.ooqiadev.cadcheck_in.Location
import com.cad.ooqiadev.cadcheck_in.Result
import org.apache.commons.csv.CSVRecord
import java.util.ArrayList

class Location {

    fun parse(locationRecords: Iterable<CSVRecord>): ArrayList<Location> {

        var parserResult: Result
        val location = ArrayList<Location>()

        for (record in locationRecords) {
            parserResult = parse(record)
            if (parserResult.success) {
                location.add(parserResult.location!!)
            }
        }

        return location
    }

    protected fun parse(record: CSVRecord): Result {
        val result = Result()

        val location = Location()

        try {
            location.id = record.get(0).toLong()
            location.name = record.get(1)
            location.direction = record.get(2)

            result.location = location
            result.success = true
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result
    }
}