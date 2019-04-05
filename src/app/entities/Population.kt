package app.entities

import app.helpers.isZero
import java.lang.Exception

object Population {

    var population: ArrayList<Individual> = ArrayList()

    var populationCount: Int = 0
    var generationCount: Int = 0

    /**
     * Не спрашивай, я не знаю, просто ладно
     */
    var FitnessComparator = Comparator<Individual>{
            individual1: Individual, individual2: Individual ->
        val fitness1 = individual1.fitness
        val fitness2 = individual2.fitness
        fitness1 - fitness2
    }

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