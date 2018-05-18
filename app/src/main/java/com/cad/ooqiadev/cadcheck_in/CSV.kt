package com.cad.ooqiadev.cadcheck_in

import org.apache.commons.csv.CSVFormat
import java.io.FileInputStream
import java.io.InputStreamReader

class CSV {

    fun readFile(file: String): Result {

        val result = Result()
        try {

            val csvFile = InputStreamReader(FileInputStream(file), "Cp1252")

            result.rows = getCsvFormat().parse(csvFile).getRecords()
            if (result.rows != null) {
                result.message = "Archivo leído con éxito."
                result.success = true
            }

        } catch (fileNotFoundException: java.io.FileNotFoundException) {
            result.message = fileNotFoundException.message
            result.exception = fileNotFoundException
        } catch (ioException: java.io.IOException) {
            result.message = ioException.message
            result.exception = ioException
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result

    }

    private fun getCsvFormat(): CSVFormat {

        return CSVFormat.newFormat(',')
                .withAllowMissingColumnNames().withQuote('"')
                .withRecordSeparator("\n")
                .withIgnoreEmptyLines(true)

    }

}
