package app.entities

class IndividualModel {
    val chromosome: IndividualModel.Chromosome = Chromosome()

    class Chromosome {
        var fitnes: Int = 10000

        constructor() {

        }
    }
}