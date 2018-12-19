import java.util.*

fun main(args: Array<String>) {
    val multiplayer: Boolean
    val input = Scanner(System.`in`)
    System.out.println("Игра с другом? Да/Нет")
    val mode = input.nextLine()
    if (mode == "Да" || mode == "да") {
        multiplayer = true
        System.out.println("Игра с другом начинается!")
    } else {
        multiplayer = false
        System.out.println("Игра с ботом начинается!")
    }
    System.out.println("Введите стартовую дату в виде дд.мм")
    GameRealization(multiplayer).startGame()
}


