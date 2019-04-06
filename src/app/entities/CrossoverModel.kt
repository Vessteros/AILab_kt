package app.entities

import app.helpers.CrossoverParents

enum class CrossoverModel {
    Type1 {
        var mask: ArrayList<Int> = ArrayList()

        /**
         * Кроссовер, btw
         * Берем сущность родителей, генерируем битовую маску
         * В цикле по длине маски проходимся и выбираем гены для новой хромосомы:
         * 0 - первый родитель
         * 1 - второй родитель
         *
         * Полученную хромосому вставляем в особь, а особь в результат кроссовера,
         * который потом конвертируем в популяцию
         */
        override fun crossover() {
            val chromosome: Individual.Chromosome = Individual.Chromosome(true)
            Population.crossoverResult = Population.population

            this.generateMask()

            chromosome.genome.add(Individual.Chromosome.Gen.getFirstGen())

            (0 until mask.size).forEach { i ->
                val gen: Individual.Chromosome.Gen = if (mask.get(index = i) == 1) {
                    CrossoverParents.parent1.getChromosome().genome.get(index = i)
                } else {
                    CrossoverParents.parent2.getChromosome().genome.get(index = i)
                }

                chromosome.genome.add(gen)
            }

            chromosome.genome.add(Individual.Chromosome.Gen.getLastGen())

            val child = Individual(chromosome)

            Population.crossoverResult.add(child)
        }

        /**
         * Генерация маски
         */
        private fun generateMask() {
            mask = ArrayList()

            repeat((0 until Individual.Chromosome.genomeLength - 2).count()) {
                mask.add((0..1).random())
            }
        }

        override fun checkCrossoverExists(): Boolean = true
    },

    Type2 {
        override fun crossover() {
            throw Exception("Этот типа пока не доступен для использования")
        }

        override fun checkCrossoverExists(): Boolean = false
    },

    Type3 {
        override fun crossover() {
            throw Exception("Этот типа пока не доступен для использования")
        }

        override fun checkCrossoverExists(): Boolean = false
    };

    abstract fun crossover()

    abstract fun checkCrossoverExists(): Boolean
}