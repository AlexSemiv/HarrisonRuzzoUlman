package typed

import simple.LogoutException

fun main() {
    val matrix = initMatrix()
    //matrix.printMatrix()
    val model = TypedModel(matrix)
    var loginPassed = false
    while (!loginPassed) {
        loginPassed = login(model)
        if (!loginPassed) continue
        var actionPassed = false
        while (!actionPassed) {
            try {
                /*actionPassed = */makeAction(model)
            } catch (e: LogoutException) {
                loginPassed = false
                break
            }
        }
    }
}

private fun initMatrix() = TypedAccessMatrix().apply {
    addCell(
        Type.Engineer,
        Object.S1,
        Action.Execute
    )
    addCell(
        Type.Engineer,
        Object.S4,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Type.Engineer,
        Object.S5,
        Action.Read, Action.Execute
    )

    addCell(
        Type.Manager,
        Object.S1,
        Action.Execute
    )
    addCell(
        Type.Manager,
        Object.S2,
        Action.Execute
    )
    addCell(
        Type.Manager,
        Object.S4,
        Action.Read, Action.Execute
    )
    addCell(
        Type.Manager,
        Object.S5,
        Action.Read, Action.Write, Action.Execute
    )

    addCell(
        Type.Developer,
        Object.S1,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Type.Developer,
        Object.S2,
        Action.Read, Action.Execute
    )
    addCell(
        Type.Developer,
        Object.S3,
        Action.Read, Action.Write, Action.Execute
    )

    addCell(
        Type.Admin,
        Object.S1,
        Action.Read, Action.Execute
    )
    addCell(
        Type.Admin,
        Object.S2,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Type.Admin,
        Object.S3,
        Action.Read, Action.Execute
    )
}


@Throws(LogoutException::class)
private fun makeAction(model: TypedModel): Boolean {
    println("Choose action:")
    for (action in Action.values()) {
        println("${action.ordinal}-${action.name}")
    }
    val selectedActionOrdinal = readLine()?.toIntOrNull() ?: return false
    val selectedAction = Action.values()[selectedActionOrdinal]
    if (selectedAction == Action.Exit) {
        throw LogoutException()
    }

    println("Choose object:")
    for (obj in Object.values()) {
        println("${obj.ordinal}-${obj.name}")
    }
    val selectedObjectOrdinal = readLine()?.toIntOrNull() ?: return false
    val selectedObject = Object.values()[selectedObjectOrdinal]
    println()
    val success = model.action(selectedObject, selectedAction)
    return if (success) {
        println("Action allowed.")
        println()
        true
    } else {
        println("Action DENIED")
        println()
        false
    }
}

enum class Type(val operations: List<Operation>) {
    Admin(
        listOf(
            Operation.ActionAdd,
            Operation.ActionRemove,
            Operation.GroupCreate,
            Operation.ObjCreate,
            Operation.GroupRemove,
            Operation.ObjRemove
        )
    ),
    Developer(
        listOf(
            Operation.GroupCreate,
            Operation.ObjCreate,
            Operation.ActionAdd,
            Operation.ActionRemove
        )
    ),
    Manager(
        listOf(
            Operation.GroupCreate,
            Operation.ObjCreate
        )
    ),
    Engineer(
        listOf(
            Operation.GroupCreate,
            Operation.ObjCreate
        )
    )
}

private fun login(model: TypedModel): Boolean {
    println("Enter name: ")
    val name = readLine() ?: ""
    println("Enter password: ")
    val password = readLine() ?: ""
    val authSubj = model.login(name, password)
    return if (authSubj == null) {
        println("Wrong auth. Try again")
        println()
        false
    } else {
        println("Success login. Hello, ${authSubj.type.name} - ${authSubj.name}!")
        println()
        true
    }
}

enum class Subject(val password: String, val type: Type) {
    User1("Password1", Type.Admin),
    User2("Password2", Type.Developer),
    User3("Password3", Type.Developer),
    User4("Password4", Type.Manager),
    User5("Password5", Type.Engineer)
}

enum class Object {
    S1,
    S2,
    S3,
    S4,
    S5,
}

enum class Action {
    Read,
    Write,
    Execute,
    Exit
}

enum class Operation {
    ActionAdd,
    ActionRemove,
    GroupCreate,
    ObjCreate,
    GroupRemove,
    ObjRemove
}