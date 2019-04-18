package app.entities

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
        return chromosome
    }

    fun fitness() {
        fitness = 0

        val topology = Topology.topology
        val genome = chromosome.genome

        for (i in 0 until genome.size - 1) {
            fitness += topology[genome.get(index = i).graphPoint][genome.get(index = i + 1).graphPoint]
        }
    }


    class Chromosome(isEmpty: Boolean = false) {
        /**
         * Компаньон - синглтон, в котлине это замена статике.
         * Логично, в принципе, создаешь синглтон, и по названию класса синглтона вызываешь его поля,
         * которые везде одинаковы
         */
        companion object {
            var genomeLength: Int = 10
            var crossoverType: Int = 1
            var mutationChance: Int = 0

            var firstGen: Int = 0
            var lastGen: Int = 5
        }

        /**
         * Цепочка генов
         * Слава богу котлин не мудрил с листами и они полностью перекочевали из java
         * Такая боль
         */
        var genome: ArrayList<Gen> = ArrayList()

        /**
         * Если есть какая-то часть кода, которая должна быть выполнена при создании объекта:
         * в java мы пихаем ее в конструктор в том или ином виде
         * в kotlin есть блок инициализации, ни кто не запрещает делать это во вторичном конструкторе,
         * но иде предлагает добровольно-принудительно запихнуть в init
         */
        init {
            setGenome(isEmpty)
        }

        fun checkMutation() {
            val strike = (0..100).random()

            if (strike < mutationChance) {
                mutate()
            }
        }

        private fun mutate() {
            setGenome()
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
         * В модели гена, кстати, есть перегрузка, поэтому блок init использовать нельзя
         */
        private fun setGenome(isEmpty: Boolean = false) {
            genome = ArrayList()

            if (!isEmpty) {
                genome.add(Gen.getFirstGen())

                for (i in 1 until Chromosome.genomeLength - 1) {
                    genome.add(Gen())
                }

                genome.add(Gen.getLastGen())
            }
        }

        /**
         * Внутренний класс генома
         */
        class Gen {
            companion object {
                fun getFirstGen(): Gen = Gen(Chromosome.firstGen)

                fun getLastGen(): Gen = Gen(Chromosome.lastGen)
            }

            /**
             * Вершина графа конкретного гена
             */
            var graphPoint: Int = 0

            /**
             * Тонкое место, если создавать модели до инпута, то тут всегда будет 0
             */
            constructor() {
                this.graphPoint = (Math.random() * Topology.pointsCount).toInt()
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