package dev.stupak.ui.ext

fun String.replaceMonthWithNumber(): String {
    val monthMap = mapOf(
        "Jan" to "01",
        "Feb" to "02",
        "Mar" to "03",
        "Apr" to "04",
        "May" to "05",
        "Jun" to "06",
        "Jul" to "07",
        "Aug" to "08",
        "Sep" to "09",
        "Oct" to "10",
        "Nov" to "11",
        "Dec" to "12"
    )

    var result = this
    for ((shortName, numericValue) in monthMap) {
        result = result.replace(shortName, numericValue)
    }

    return result
}
fun String.removeBrackets(): String {
    val regex = Regex("^\\((.*)\\)\$")
    val matchResult = regex.find(this)
    return matchResult?.groups?.get(1)?.value ?: this
}