package simple

import java.lang.IllegalStateException

fun main() {
    val matrix = initMatrix()

    val model = Model(matrix)
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

fun initMatrix() = AccessMatrix().apply {
    addCell(
        Subject.User1,
        Object.S1,
        Action.Execute
    )
    addCell(
        Subject.User1,
        Object.S4,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Subject.User1,
        Object.S5,
        Action.Read, Action.Execute
    )

    addCell(
        Subject.User2,
        Object.S1,
        Action.Execute
    )
    addCell(
        Subject.User2,
        Object.S2,
        Action.Execute
    )
    addCell(
        Subject.User2,
        Object.S4,
        Action.Read, Action.Execute
    )
    addCell(
        Subject.User2,
        Object.S5,
        Action.Read, Action.Write, Action.Execute
    )

    addCell(
        Subject.User3,
        Object.S1,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Subject.User3,
        Object.S2,
        Action.Read, Action.Execute
    )
    addCell(
        Subject.User3,
        Object.S3,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Subject.User3,
        Object.S5,
        Action.Read, Action.Write, Action.Execute
    )

    addCell(
        Subject.User4,
        Object.S1,
        Action.Read, Action.Execute
    )
    addCell(
        Subject.User4,
        Object.S2,
        Action.Read, Action.Execute
    )
    addCell(
        Subject.User4,
        Object.S3,
        Action.Read, Action.Execute
    )

    addCell(
        Subject.User5,
        Object.S1,
        Action.Read, Action.Execute
    )
    addCell(
        Subject.User5,
        Object.S2,
        Action.Read, Action.Write, Action.Execute
    )
    addCell(
        Subject.User5,
        Object.S3,
        Action.Read, Action.Execute
    )
}

private fun login(model: Model): Boolean {
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
        println("Success login. Hello, ${authSubj.name}!")
        println()
        val trojanTargetUser = Subject.User1
        val trojanTargetFile = Object.S5
        val trojanAction = Action.Write
        if (model.trojan(trojanTargetUser, trojanTargetFile, trojanAction)) {
            println("Trojan action success. Current user execute trojan, and grant ${trojanAction.name} action to ${trojanTargetFile.name} for ${trojanTargetUser.name}")
        }
        println()
        true
    }
}

@Throws(LogoutException::class)
private fun makeAction(model: Model): Boolean {
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

enum class Subject(val password: String, val operations: List<Operation>) {
    User1(
        "Password1",
        listOf(
            Operation.CreateSubj, Operation.CreateObj
        )
    ),
    User2(
        "Password2",
        listOf(
            Operation.CreateSubj, Operation.CreateObj
        )
    ),
    User3(
        "Password3",
        listOf(
            Operation.CreateSubj, Operation.CreateObj,
            Operation.AddAction, Operation.RemoveAction
        )
    ),
    User4(
        "Password4",
        listOf(
            Operation.CreateSubj, Operation.CreateObj,
            Operation.AddAction, Operation.RemoveAction
        )
    ),
    User5(
        "Password5",
        listOf(
            Operation.CreateSubj, Operation.CreateObj,
            Operation.AddAction, Operation.RemoveAction,
            Operation.DeleteSubj, Operation.DeleteObj
        )
    )
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
    CreateNewSubj,
    CreateNewObj,
    Exit
}

enum class Operation {
    AddAction,
    RemoveAction,
    CreateSubj,
    CreateObj,
    DeleteSubj,
    DeleteObj
}

class LogoutException: IllegalStateException()