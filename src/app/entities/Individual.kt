package app.entities

import app.helpers.setZero

class Individual(private val chromosome: Individual.Chromosome = Chromosome()) {

    /**
     * Шанс скрещивания конкретной особи
     */
    var crossoverChance: Float = 0.1f

    /**
     * Результат работы фитнессфункции
     */
    var fitness: Int = 10000

    fun getChromosome(): Individual.Chromosome {
        return this.chromosome
    }

    fun fitness() {
        this.fitness.setZero()
        val topology = Topology.topology
        val genome = this.chromosome.genome

        for (i in 0 until genome.size - 1) {
            this.fitness += topology[genome.get(index = i).graphPoint][genome.get(index = i + 1).graphPoint]
        }
    }

    companion object {
        var FitnessComparator = { individual1: Individual, individual2: Individual ->
            val fitness1 = individual1.fitness
            val fitness2 = individual2.fitness

            fitness1 - fitness2
        }
    }


    class Chromosome {
        /**
         * Компаньен - синглтон, в котлине это замена статике.
         * Логично, в принципе, создаешь синглтон, и по названию класса синглтона вызываешь его поля,
         * которые везде одинаковы
         */
        companion object {
            var genomeLength: Int = 10
            var crossoverType: Int = 1
            var mutationChance: Float = 0.1f

            var firstGen: Int = 0
            var lastGen: Int = 5
        }

        /**
         * Цепочка генов
         * Слава богу котлин не мудрил с листами и они полностью перекочевали из java
         * Такая боль
         */
        var genome: ArrayList<Genome> = ArrayList()

        /**
         * Если есть какая-то часть кода, которая должна быть выполнена при создании объекта:
         * в java мы пихаем ее в конструктор в том или ином виде
         * в kotlin есть блок инициализации, ни кто не запрещает делать это во вторичном конструкторе,
         * но иде предлагает добровольно-принудительно запихнуть в init
         */
        init {
            this.setGenome()
        }

        /**
         * Генерация цепочки генов
         *
         * Для класса генома сделал компаньон, в него запихнул две простые фабрики
         * (думаю даже фабриками их назвать тяжело, но пускай)
         *
         * Они генерят первый и последний гены статично, потому что эти 2 гена определены на этапе инпута
         * и должны создаваться отдельно от всей цепочки
         *
         * В цикле, вычитая эти 2 гена, создаем обычные объекты через вторичный конструктор.
         * В модели гена , кстати, есть перегрузка, поэтому блок init использовать нельзя
         */
        private fun setGenome() {
            this.genome.add(Genome.getFirstGen())

            for (i in 1 until Chromosome.genomeLength - 1) {
                this.genome.add(Genome())
            }

            this.genome.add(Genome.getLastGen())
        }

        /**
         * Внутренний класс генома
         */
        class Genome {
            companion object {
                fun getFirstGen(): Genome = Genome(Chromosome.firstGen)

                fun getLastGen(): Genome = Genome(Chromosome.lastGen)
            }

            /**
             * Вершина графа конкретного гена
             */
            var graphPoint: Int = 0

            /**
             * Тонкое место, если создавать модели до инпута, то тут всегда будет 0
             */
            constructor() {
                this.graphPoint = (Math.random() * Chromosome.lastGen).toInt()
            }

            /**
             * Перегрузка для создания конкретных генов
             */
            constructor(graphPoint: Int) {
                this.graphPoint = graphPoint
            }
        }
    }
}