package typed

class TypedAccessMatrix {
    private val matrix = LinkedHashMap<Type, LinkedHashMap<Object, MutableList<Action>>>()

    fun getMatrix() = matrix

    fun addCell(type: Type, obj: Object, vararg actions: Action) {
        val objMap = matrix[type]
        if (objMap == null) {
            matrix[type] = LinkedHashMap<Object, MutableList<Action>>().apply {
                put(obj, actions.toMutableList())
            }
        } else {
            objMap[obj] = actions.toMutableList()
            matrix[type] = objMap
        }
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