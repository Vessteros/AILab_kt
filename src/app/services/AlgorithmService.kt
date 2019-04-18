package app.services

import app.entities.CrossoverModel
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
            print("Заканчиваю работу.\n")
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

        this.setCrossover()

        this.startAnalysis()

        this.startSelection()
    }

    /**
     * Получение всей необходимой информации
     */
    @Throws(Exception::class)
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
                    throw Exception("Топология не симметрична, проверьте введенные данные.")
                }
            }
        }

        println("Топология симметрична, начинаю расчет.\n")
    }

    /**
     * Проставим все необходимые значения с консоли
     */
    private fun setStaticFields() {
        Population.apply {
            populationCount = input.`$population`
            generationCount = input.`$generations`
        }

        Individual.Chromosome.apply {
            crossoverType = input.`$crossoverType`
            genomeLength = input.`$genomeLength`
            mutationChance = input.`$mutationChance`

            firstGen = input.`$firstPoint`
            lastGen = input.`$lastPoint`
        }

        Topology.pointsCount = input.`$pointsCount`
    }

    /**
     * Создадим первичную популяцию
     */
    private fun spawnFirstGeneration() {
        Population.spawnFirstGeneration()
    }

    private fun setCrossover() {
        Population.crossover = CrossoverModel.valueOf("Type" + Individual.Chromosome.crossoverType)
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
        Population.population.forEach {
            it.fitness()
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

    /**
     * Сортировка
     */
    private fun sort() {
        Population.population.sortWith(Population.FitnessComparator)
    }

    /**
     * Чем лучше результат особи, тем большее ее шанс скрещивания
     */
    private fun setCrossoverChance() {
        Population.setCrossoverChance()
    }

    /**
     * Вывести популяцию в консоль
     */
    private fun printPopulation() {
        Population.printPopulation()
    }

    private fun startSelection() {
        for (i in 0 until Population.generationCount - 1) {
            Population.crossover()

            this.increaseStep()

            println("Шаг ${Population.step}")
            Population.printPopulation()
        }

        println("Кратчайший путь посе работы программы на ${Population.generationCount} коленах: ${Population.population[0].fitness}")
    }
}