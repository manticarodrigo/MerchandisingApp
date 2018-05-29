package com.cad.ooqiadev.cadcheck_in.utils

import android.content.Context
import com.cad.ooqiadev.cadcheck_in.AppDatabase
import com.cad.ooqiadev.cadcheck_in.DbWorkerThread
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.*
import kotlin.collections.ArrayList

class CSV {

    private lateinit var mDbWorkerThread: DbWorkerThread
    private var mDb: AppDatabase? = null
    private var context: Context? = null
    var taskCatalogs = ArrayList<TaskCatalog>()

    constructor(){ }

    constructor(context: Context) {
        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("csvWorkerThread")
        mDbWorkerThread.start()
        this.context = context
        mDb = AppDatabase.getInstance(context)
    }

    fun createTaskFile(file: String, tasks : ArrayList<Task>) : Result {

        val result = Result()

        var csvFile: FileWriter? = null
        var csvPrinter: CSVPrinter? = null

        try {

            csvFile = FileWriter(file)
            csvPrinter = CSVPrinter(csvFile, getCsvFormat())
            var trackingRecord: ArrayList<String?>

            tasks?.forEach { task ->

                trackingRecord = ArrayList()

                trackingRecord.add(task.taskCatalogId!!)
                trackingRecord.add(task.customerId!!)
                trackingRecord.add(task.description!!)

                task.textValues?.forEach { taskTextValue ->
                    trackingRecord.add(taskTextValue)
                }

                task.photoUrls?.forEach { taskPhotoUrl ->

                    var photoUrl = taskPhotoUrl
                    if(taskPhotoUrl != "") {
                        val replacedPathUrl = photoUrl?.replace(this.context?.getExternalFilesDir("").toString() + "/", "")
                        photoUrl = replacedPathUrl
                    }

                    trackingRecord.add(photoUrl)
                }

                task.checkboxValues?.forEach { taskCheckValue ->
                    trackingRecord.add(taskCheckValue)
                }

                trackingRecord.add(task.comment!!)

                if(task.createdAt != null) {
                    trackingRecord.add(task.createdAt.toString()!!)
                } else {
                    trackingRecord.add("")
                }

                if(task.updatedAt != null) {
                    trackingRecord.add(task.updatedAt.toString()!!)
                } else {
                    trackingRecord.add("")
                }

                csvPrinter.printRecord(trackingRecord)

            }

        } catch (ioException: java.io.IOException) {
            result.message = ioException.message
            result.exception = ioException
        } finally {
            try {
                csvFile!!.flush()
                csvFile!!.close()
                csvPrinter!!.close()
                result.message = "Archivo creado satisfactoriamente"
                result.success = true
            } catch (ioException: IOException) {
                result.message = ioException.message
                result.exception = ioException
            }

            // Destroy db singleton instance
            AppDatabase.destroyInstance()
            mDbWorkerThread.quit()
        }

        return result
    }

    fun readFile(file: String): Result {

        val result = Result()
        try {

            val csvFile = InputStreamReader(FileInputStream(file), "Cp1252")

            result.rows = getCsvFormat().parse(csvFile as Reader?).getRecords()
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
