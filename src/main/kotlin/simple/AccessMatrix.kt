package simple

class AccessMatrix {
    private val matrix = LinkedHashMap<Subject, LinkedHashMap<Object, MutableList<Action>>>()

    fun getMatrix() = matrix

    fun addCell(subject: Subject, obj: Object, vararg actions: Action) {
        val objMap = matrix[subject]
        if (objMap == null) {
            matrix[subject] = LinkedHashMap<Object, MutableList<Action>>().apply {
                put(obj, actions.toMutableList())
            }
        } else {
            objMap[obj] = actions.toMutableList()
            matrix[subject] = objMap
        }
    }

    fun addAction(subject: Subject, obj: Object, action: Action): Boolean {
        return matrix[subject]?.get(obj)?.add(action) ?: false
    }

    fun printMatrix() {
        for (subjRow in matrix) {
            println("${subjRow.key.name}:")
            for (obj in subjRow.value) {
                print("${obj.key.name}: ${obj.value.toList()}, ")
            }
            println()
        }
        println()
    }
}