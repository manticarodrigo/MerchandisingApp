package com.cad.ooqiadev.cadcheck_in.parsers

import com.cad.ooqiadev.cadcheck_in.models.Customer
import com.cad.ooqiadev.cadcheck_in.models.Item
import com.cad.ooqiadev.cadcheck_in.utils.Result
import org.apache.commons.csv.CSVRecord
import java.util.ArrayList

class Item {

    fun parse(itemRecords: Iterable<CSVRecord>): ArrayList<Item> {

        var parserResult: Result
        val items = ArrayList<Item>()

        for (record in itemRecords) {
            parserResult = parse(record)
            if (parserResult.success) {
                items.add(parserResult.item!!)
            }
        }

        return items
    }

    protected fun parse(record: CSVRecord): Result {

        val result = Result()
        val item = Item()

        try {

            item.number = record.get(0)
            item.description = record.get(1)
            item.um = record.get(2)
            item.upcode = record.get(3)
            item.regPrice = record.get(4).toDouble()
            item.retailPrice = record.get(5).toDouble()

            result.item = item
            result.success = true

        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result
    }

}