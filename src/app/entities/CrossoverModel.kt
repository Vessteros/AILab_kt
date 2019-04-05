package app.entities

enum class CrossoverModel {
    Type1 {
        override fun crossover() {

        }
    },

    Type2 {
        override fun crossover() {
            throw Exception("Этот типа пока не доступен для использования")
        }
    },

    Type3 {
        override fun crossover() {
            throw Exception("Этот типа пока не доступен для использования")
        }
    };

    abstract fun crossover()
}