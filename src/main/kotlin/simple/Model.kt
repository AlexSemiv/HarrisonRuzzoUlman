package simple

class Model(private val matrix: AccessMatrix) {

    private var authSubject: Subject? = null

    fun login(name: String, password: String): Subject? {
        return matrix.getMatrix().keys.find { it.name == name && it.password == password }?.also {
            authSubject = it
        }
    }

    fun action(obj: Object, action: Action): Boolean {
        val authSubject = authSubject ?: return false
        val objects = matrix.getMatrix()[authSubject] ?: return false
        return objects[obj]?.contains(action) ?: return false
    }

    fun trojan(
        targetSubject: Subject,
        targetObject: Object,
        action: Action
    ): Boolean {
        val authSubject = authSubject ?: return false
        return if (authSubject.operations.contains(Operation.AddAction)) {
            matrix.addAction(targetSubject, targetObject, action)
        } else {
            false
        }
    }
}