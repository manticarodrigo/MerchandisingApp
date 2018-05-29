package com.cad.ooqiadev.cadcheck_in.parsers

import com.cad.ooqiadev.cadcheck_in.models.Customer
import com.cad.ooqiadev.cadcheck_in.utils.Result
import org.apache.commons.csv.CSVRecord
import java.util.ArrayList

class Customer {

    fun parse(locationRecords: Iterable<CSVRecord>): ArrayList<Customer> {

        var parserResult: Result
        val customers = ArrayList<Customer>()

        for (record in locationRecords) {
            parserResult = parse(record)
            if (parserResult.success) {
                customers.add(parserResult.customer!!)
            }
        }

        return customers
    }

    protected fun parse(record: CSVRecord): Result {

        val result = Result()
        val customer = Customer()

        try {
            customer.id = record.get(0)
            customer.name = record.get(1)
            customer.address1 = record.get(2)
            customer.address2 = record.get(3)
            customer.city = record.get(4)
            customer.state = record.get(5)
            customer.zipCode = record.get(6)
            customer.phone = record.get(7)
            customer.fax = record.get(8)
            customer.contactName = record.get(9)
            customer.salesPerson = record.get(10)
            customer.type = record.get(11)

            result.customer = customer
            result.success = true
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result
    }
}