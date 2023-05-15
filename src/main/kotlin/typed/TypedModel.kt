package typed

class TypedModel(private val matrix: TypedAccessMatrix) {

    private var authSubject: Subject? = null

    fun login(name: String, password: String): Subject? {
        return try {
            val user = Subject.valueOf(name)
            if (user.password != password) return null
            user.also { authSubject = it }
        } catch (e: Exception) {
            null
        }
    }

    fun action(obj: Object, action: Action): Boolean {
        val authSubj = authSubject ?: return false
        val objects = matrix.getMatrix()[authSubj.type] ?: return false
        return objects[obj]?.add(action) ?: false
    }
}