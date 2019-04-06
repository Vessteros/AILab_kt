package app.entities

import app.helpers.CrossoverParents
import app.helpers.isZero
import java.lang.Exception

object Population {

    var population: ArrayList<Individual> = ArrayList()

    var crossoverResult: ArrayList<Individual> = ArrayList()

    /**
     * По умолчанию 1 тип
     */
    var crossover: CrossoverModel = CrossoverModel.Type1

    var populationCount: Int = 0
    var generationCount: Int = 0

    var step: Int = 1

    /**
     * Не спрашивай, я не знаю, просто ладно
     */
    var FitnessComparator = Comparator<Individual> { individual1: Individual, individual2: Individual ->
        val fitness1 = individual1.fitness
        val fitness2 = individual2.fitness
        fitness1 - fitness2
    }

    /**
     * Выведем популяцию
     */
    fun printPopulation() {
        var individualNumber = 1

        population.forEach {
            System.out.printf("Особь %d: ", individualNumber)

            it.getChromosome().genome.forEach {
                print("${it.graphPoint} ")
            }

            System.out.printf("Путь: %d ", it.fitness)
            System.out.printf("Шанс ск.: %f\n", it.crossoverChance)

            individualNumber++
        }

        System.out.println("\n")
    }

    /**
     * Каждая особь имеет шанс на скрещивания в зависимости от того,
     * на сколько хорошо она выполняет свою функцию
     *
     * Это можно понять по ее положению в листе особей популяции: чем дальше по листу, тем хуже особь
     * Чем хуже особь, тем меньше шансов на скрещивание
     */
    fun setCrossoverChance() {
        val point = 100 / populationCount

        var individualNumber = 1

        population.forEach {
            // я так подумал: у самой удачной особи всегда почти 100% шанс на скрещивание,
            // она всегда будет учавствовать в скрещивании, так что я решил повысить планку отбора
            // уменьшением шанса на скрещивание
            val chance = (70.00 - point * individualNumber).toFloat()

            it.crossoverChance = if (chance > 0) chance else 0f

            individualNumber++
        }
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

    /**
     * Процесс кроссовера
     *
     * Запихиваем в сущность родителей первые 2 особи из популяции
     *
     * По всем особям из популяции проверим могут ли они скрещиваться
     * Если особь подходит, то она записывается в сущность родителей и учавствует в следующем
     * секс-марафоне, если нет, то текущие родители скрещиваются еще раз
     */
    fun crossover() {
        CrossoverParents.parent1 = population.get(index = 1)
        CrossoverParents.parent2 = population.get(index = 2)

        // по всей популяции
        for (i in 0 until population.size - 1) {
            // проверим может ли особь быть родителем
            CrossoverParents.check(population.get(index = i))

            // скрестим 2 особи, которые сейчас находятся в CrossoverParents
            crossover.crossover()
        }

        setPopulationFromCrossoverResult()
    }

    /**
     * Вся проблема в том, что дети не всегда лучше родителей, а моя цель найти лучших
     * Т.е. при кроссинговере я не могу в популяцию перезаписывать детей
     * Поэтому я создал отдельный лист особей, куда записываю детей и приписываю популяцию родителей
     * После, когда у меня есть набор всех особей после кроссинговера, я проверяю их валидность
     * и отбираю лучших (по количеству особей в популяции, введенной с консоли
     */
    private fun setPopulationFromCrossoverResult() {
        crossoverResult.forEach {
            it.fitness()
        }

        crossoverResult.sortWith(FitnessComparator)

        checkMutationNecessity()

        // обнулим популяцию, чтобы записать лучших из результата кроссинговера
        population = ArrayList()

        (0 until populationCount).forEach { i ->
            population.add(crossoverResult[i])
        }

        setCrossoverChance()
    }

    /**
     * Выбирая лучших, в скором всемени вся популяция станет набором идентичных особей
     * Чтобы этого не произошло, среднячок и плохиши могут подвергнуться мутации,
     * после чего я еще раз просчитываю валидность и сортирую
     */
    private fun checkMutationNecessity() {
        for (i in (crossoverResult.size/3) until crossoverResult.size) {
            crossoverResult[i].apply {
                getChromosome().checkMutation()
                fitness()
            }
        }

        crossoverResult.sortWith(FitnessComparator)
    }
}