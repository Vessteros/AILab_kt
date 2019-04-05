package app.services

import app.entities.Individual
import app.entities.Population
import app.entities.Topology
import app.helpers.Input
import java.lang.Exception

class AlgorithmService(private val input: Input = Input()) {

    private var topology: Topology = Topology

    /**
     * Обертка, ловящая ексепшены
     */
    fun run() {
        try {
            start()
        } catch (e: Exception) {
            println(e.message)
            print("Заканчиваю работу.")
        }

    }

    /**
     * Выполнение всего флоу программы
     */
    private fun start() {
        this.getNecessaryInfo()

        this.setTopology()

        this.checkTopologySymmetrical()

        this.setStaticFields()

        this.spawnFirstGeneration()

        this.startAnalysis()
    }

    /**
     * Получение всей необходимой информации
     */
    private fun getNecessaryInfo() {
        input.getNecessaryInfo()
    }

    /**
     * Проставить топологию
     *
     * Тут магия: при компиляции класса jvc видит, что в начале класса есть свойство topology в которое записан объект Topology
     * в java -> kotlin объекты передаются по ссылке, => после компиляции класса в свойство инициализирована с сылкой на класс Topology.
     * Изначально в Topology пустой массив, следовательно в поле объекта лежит объект Topology c полем - пустым массивом.
     * После того как мы проставили топологию в объекте класса Topology, там лежит заданный с консоли массив значений.
     * Ссылка на объект у нас уже есть => нам не надо еще раз переопледелять здешнее свойсво, оно просто указывает на объект класса
     * в котором есть заполненный данными массив
     *
     * Следующая деталь: это возможно из-за того, что объект Topology - синглтон
     */
    private fun setTopology() {
        Topology.topology = input.`$topology`
        Topology.pointsCount = input.`$pointsCount`
    }

    /**
     * Проверка на симметричность топологии
     */
    private fun checkTopologySymmetrical() {

        for (i in 0 until topology.pointsCount) {
            for (j in 0 until topology.pointsCount) {
                if (topology.topology[i][j] != topology.topology[j][i]) {
                    throw Exception("Топология не симметрична, проверьте введенные данные. Завершаю работу.")
                }
            }
        }

        println("Топология симметрична, начинаю расчет.\n")
    }

    /**
     * Проставим все необходимые значения с консоли
     */
    private fun setStaticFields() {
        Population.populationCount = input.`$population`
        Population.generationCount = input.`$generations`

        Individual.Chromosome.crossoverType = input.`$crossoverType`
        Individual.Chromosome.genomeLength = input.`$genomeLength`
        Individual.Chromosome.mutationChance = input.`$mutationChance`

        Individual.Chromosome.firstGen = input.`$firstPoint`
        Individual.Chromosome.lastGen = input.`$lastPoint`

        Topology.pointsCount = input.`$pointsCount`
    }

    /**
     * Создадим первичную популяцию
     */
    private fun spawnFirstGeneration() {
        Population.spawnFirstGeneration()
    }

    /**
     * Вынес все аналитические необходимости в отдельный метод
     */
    private fun startAnalysis() {
        this.fitness()

        this.increaseStep()

        this.sortByFitness()
    }

    /**
     * Вычисляем пути для всех особей в популяции
     */
    private fun fitness() {
        for (individual in Population.population) {
            individual.fitness()
        }
    }

    /**
     * Увеличим шаг
     */
    private fun increaseStep() = Population.step++

    private fun sortByFitness() {
        this.sort()

        this.setCrossoverChance()

        this.printPopulation()
    }

    private fun sort() {
        Population.population.sortWith(Population.FitnessComparator)
    }

    private fun setCrossoverChance() {
        val point = 100 / Population.populationCount

        var individualNumber = 1

        for (individual in Population.population) {
            individual.crossoverChance = ((100.00 - point * individualNumber).toFloat())
            individualNumber++
        }
    }

    /**
     * Вывести популяцию в консоль
     */
    private fun printPopulation() {
        var individualNumber = 1

        for (ind in Population.population) {
            System.out.printf("Особь %d: ", individualNumber)
            for (gen in ind.getChromosome().genome) {
                print("${gen.graphPoint} ")
            }

            System.out.printf("Путь: %d ", ind.fitness)
            System.out.printf("Шанс ск.: %f\n", ind.crossoverChance)
            individualNumber++
        }
    }
}