package app.helpers

import app.entities.Individual

object CrossoverParents {
    var parent1: Individual = Individual()

    var parent2: Individual = Individual()

    fun check(individual: Individual): Boolean {
        val chance = (0..100).random()

        if (chance < individual.crossoverChance) {
            return this.allow(individual)
        }

        return false
    }

    private fun allow(individual: Individual): Boolean {
        when(true) {
            individual.crossoverChance > parent1.crossoverChance -> parent1 = individual
            else -> parent2 = individual
        }

        return true
    }
}