package com.cad.ooqiadev.cadcheck_in.parsers

import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import com.cad.ooqiadev.cadcheck_in.utils.Result
import org.apache.commons.csv.CSVRecord
import java.util.ArrayList

class TaskCatalog {

    fun parse(taskCatalogR: Iterable<CSVRecord>, taskCatalogRTexts: Iterable<CSVRecord>,
              taskCatalogRImages: Iterable<CSVRecord>, taskCatalogRCheckBoxes: Iterable<CSVRecord>) : ArrayList<TaskCatalog> {

        var parserResult: Result
        val taskCatalogs = ArrayList<TaskCatalog>()
        var arrayText: ArrayList<String?> = ArrayList()
        var arrayImage: ArrayList<String?> = ArrayList()
        var arrayCheckbox: ArrayList<String?> = ArrayList()

        taskCatalogR.forEachIndexed { indexRecord, record ->

            var aText: List<CSVRecord> = taskCatalogRTexts.filter { s -> s.get(0) == record.get(0) }

            aText.forEach { text->
                text.forEachIndexed { j, value ->
                    arrayText.add(text.get(j))
                }
            }

            var aImage: List<CSVRecord> = taskCatalogRImages.filter { s -> s.get(0) == record.get(0) }

            aImage.forEach { image ->
                image.forEachIndexed { h, value ->
                    arrayImage.add(image.get(h))
                }
            }

            var aCheckBox: List<CSVRecord> = taskCatalogRCheckBoxes.filter { s -> s.get(0) == record.get(0)}
            aCheckBox.forEach { checkBox ->
                checkBox.forEachIndexed { k, value ->
                    arrayCheckbox.add(checkBox.get(k))
                }
            }

            arrayText.remove(record.get(0))
            arrayImage.remove(record.get(0))
            arrayCheckbox.remove(record.get(0))

            parserResult = parse(record, arrayText!!, arrayImage!!, arrayCheckbox!!)

            arrayText = ArrayList()
            arrayImage = ArrayList()
            arrayCheckbox = ArrayList()

            if (parserResult.success) {
                taskCatalogs.add(parserResult.taskCatalog!!)
            }
        }

        return taskCatalogs
    }


    protected fun parse(record: CSVRecord, aText: ArrayList<String?>, aImage: ArrayList<String?>, aCheckBox: ArrayList<String?>): Result {
        val result = Result()

        val taskCatalog = TaskCatalog()

        try {

            taskCatalog.id = record.get(0)
            taskCatalog.description = record.get(1)
            taskCatalog.isInventory = record.get(2).toBoolean()

            if(aText?.size?.compareTo(0) != 0) {
                taskCatalog.textLabels = aText
            }

            if(aImage?.size?.compareTo(0) != 0) {
                taskCatalog.photoLabels = aImage
            }

            if(aCheckBox?.size?.compareTo(0) != 0) {
                taskCatalog.checkboxLabels = aCheckBox
            }

            result.taskCatalog = taskCatalog
            result.success = true
        } catch (ex: Exception) {
            result.message = ex.message
            result.exception = ex
        }

        return result
    }

}