package com.cad.ooqiadev.cadcheck_in

enum class Status(var string: String) {
    PENDING("pending"),
    DONE("done"),
    PARTIAL("partial"),
    FAILED("failed")
}