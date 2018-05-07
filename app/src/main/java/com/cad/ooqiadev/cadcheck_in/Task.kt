package com.cad.ooqiadev.cadcheck_in

data class Task(val name: String, val status: Status)

enum class Status { PENDING, DONE, PARTIAL, FAILED }