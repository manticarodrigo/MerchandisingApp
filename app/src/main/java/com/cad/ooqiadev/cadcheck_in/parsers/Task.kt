package com.cad.ooqiadev.cadcheck_in.parsers

import android.content.Context
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import com.cad.ooqiadev.cadcheck_in.task.TaskActivity
import com.cad.ooqiadev.cadcheck_in.utils.Result
import org.apache.commons.csv.CSVRecord
import kotlin.collections.ArrayList

class Task(val taskCatalogs: ArrayList<TaskCatalog>, val context: Context) {

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

            task.taskCatalogId = record.get(0)
            task.customerId = record.get(1)
            task.description = record.get(2)

            var lastIterationTextLabelsIndex : Int = -1
            var lastIterationImagesIndex : Int = -1
            var nextIterationIndex : Int = -1
            taskCatalogs.forEach { taskCatalog ->

                if(task.taskCatalogId!!.compareTo(taskCatalog.id) == 0) {

                    taskCatalog.textLabels.forEachIndexed { i,it ->
                        var dataVText = record.get(3 + i).toString()
                        task.textValues.add(dataVText)
                        lastIterationTextLabelsIndex = 3 + i
                    }

                    taskCatalog.photoLabels.forEachIndexed { i, it ->
                        var dataVImage = record.get(lastIterationTextLabelsIndex + (i + 1))
                        val localImagePaht = context.getExternalFilesDir("").toString() + "/" + dataVImage
                        task.photoUrls.add(localImagePaht)
                        lastIterationImagesIndex = lastIterationTextLabelsIndex + (i + 1)
                    }

                    taskCatalog.checkboxLabels.forEachIndexed { i, it ->
                        val data = record.get(lastIterationImagesIndex + (i + 1))
                        task.checkboxValues.add(data)
                        nextIterationIndex = lastIterationImagesIndex + (i +1)
                    }

                }

            }

            nextIterationIndex++
            task.comment = record.get(nextIterationIndex)

            nextIterationIndex++
            if(record.get(nextIterationIndex) != "") {
                task.createdAt = record.get(nextIterationIndex).toLong()
            }

            nextIterationIndex++
            if(record.get(nextIterationIndex) != "") {
                task.updatedAt = record.get(nextIterationIndex).toLong()
            }

            nextIterationIndex = -1
            result.task = task
            result.success = true

        } catch (ex: Exception) {

            result.message = ex.message
            result.exception = ex

        }

        return result
    }
}