package app.helpers

interface InputInterface {

    @Throws(Exception::class)
    fun getNecessaryInfo(): Input

    fun setScanner(): Input
}