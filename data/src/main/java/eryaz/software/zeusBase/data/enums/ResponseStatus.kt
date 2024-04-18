package eryaz.software.zeusBase.data.enums

enum class ResponseStatus(private val success: Boolean?) {
    OK(true),
    FAILED(false),
    NULL_DATA(null);
}