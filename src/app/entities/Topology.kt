package app.entities

object Topology {
    /**
     * Сама топология
     */
    var topology: Array<out IntArray> = Array(0, {IntArray(0, {0})})

    /**
     * Количество вершн топологии
     */
    var pointsCount: Int = 0
}