package com.hectoruiz.domain.commons

sealed class Error {

    data object Other : Error()

    data object Network : Error()

    data object NoError : Error()
}
