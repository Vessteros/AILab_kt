package app.entities

import app.helpers.isZero
import java.lang.Exception

object Population {
    var population: ArrayList<Individual> = ArrayList()

    var populationCount: Int = 0
    var generationCount: Int = 0

    /**
     * Создание первого поколения
     */
    fun spawnFirstGeneration() {
        if (this.populationCount.isZero()) {
            throw Exception("Не указано количество особей в популяции")
        }

        for (i in 1..Population.populationCount) {
            this.population.add(Individual())
        }
    }
}