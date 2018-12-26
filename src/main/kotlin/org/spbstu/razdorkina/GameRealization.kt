import java.util.*

class GameRealization(private val mode: Boolean) {

    private var preDate = ""
    private var nextDate = ""
    private var date = 0
    private var day = 0
    private var month = 0

    fun startGame() { //Выбор режима игры
        if (mode) runMultiplayer() else runSolo()
    }

    fun runSolo() { //Запуск игры с ботом
        preDate = ""
        while (true) {
            nextDate = makeMove(1, preDate) //Ход игрока
            if (nextDate == "0") break
            if (nextDate == "31.12")
                break

            nextDate = solve(nextDate) //Ход бота
            System.out.println("Бот: $nextDate")
            preDate = nextDate
            if (nextDate == "31.12") {
                System.out.println("Бот проиграл")
                break
            }
        }
    }

    fun solve(stringDate: String): String { //Логика хода для бота
        date = stringDate.replace(".", "").toInt()
        day = date / 100
        month = date % 100
        when {
            (day >= 28 && month == 1) -> month += 2 //Корректность хода для января и февраля
            (day == 28 && month == 2) -> month += 2 //Корректность хода для февраля
            (day == 31) -> if (month != 7 && month != 11 && month != 12) month += 2 else month++
            (day % 3 == 2) -> if (day < 30) day++ //Формирование "удачного" для бота числа
            (day % 3 == 1) -> if (day < 30) day += 2 //Формирование "удачного" для бота числа
            (day % 3 == 0) -> { //Все остальные случаи, с учетом проверки количества дней в месяцах
                if (month == 12) day++
                if (day == 30 && month != 2 && month != 4 && month != 6 && month != 9 && month != 11) day++
                else if (month != 12 && month != 11) {
                    month += 2
                } else if (month == 11) month++
            }
            (day == 29 && month == 12) -> day += 2
            (day == 29 && month == 12) -> day++
        }
        var stringDay = "$day"
        var stringMonth = "$month"
        if (stringDay.length == 1) stringDay = "0$stringDay"
        if (stringMonth.length == 1) stringMonth = "0$stringMonth"
        return "$stringDay.$stringMonth"
    }

    fun runMultiplayer() { //Запуск игры с другом
        preDate = ""
        while (true) {
            nextDate = makeMove(1, preDate) //Ход первого игрока
            if (nextDate == "0") break
            if (nextDate == "31.12")
                break
            preDate = nextDate

            nextDate = makeMove(2, preDate) //Ход второго игрока
            if (nextDate == "0") break
            if (nextDate == "31.12")
                break
            preDate = nextDate
        }
    }

    fun fixDate(date: String): String { //Метод, приводящий дату в "играбельный" вид
        val partsOfDate = date.split("""\D+""".toRegex())
        val dateStr = partsOfDate.toTypedArray().joinToString("")
        val dayStr: String
        val monthStr: String

        if (partsOfDate.isEmpty()) return "Incorrect date"
        if (dateStr.length > 4 || dateStr.length < 2) return "Incorrect date"

        val parts = mutableListOf<Int>()
        for (i in 0 until partsOfDate.size) {
            if (partsOfDate[i] == "") continue
            parts.add(partsOfDate[i].toInt())
        }

        val chars = mutableListOf<Int>()
        for (i in 0 until dateStr.length)
            chars.add((dateStr[i]) - '0')

        if (parts.size > 4) return "Incorrect date" //Отсекаем даты, в которых 5 и более цифр

        when (dateStr.length) {
            2 -> { //Случай, если введено два числа
                return "0${dateStr[0]}.0${dateStr[1]}"
            }
            3 -> { //Случай, если введено три числа
                if (parts.size == 1 || parts.size == 3) { //Случай, когда разделители между цифрами не влияют на дату
                    when {
                        dateStr[0] == '0' -> return "0${dateStr[1]}.0${dateStr[2]}"
                        dateStr[0] == '1' && dateStr[1] == '0' -> return "Incorrect date"
                        dateStr[1] == '0' -> return "0${dateStr[0]}.0${dateStr[2]}"
                        dateStr[2] == '0' -> {
                            if (dateStr[1] != '1') return "Incorrect date"
                            return "0${dateStr[0]}.10"
                        }
                        (dateStr[0] == '1' && dateStr[2] != '0') || dateStr[0] == '2' || dateStr[0] == '3' ->
                            return "Incorrect date"
                    }
                }
                if (parts.size == 2) { //Случай, когда разделители между цифрами влияют на дату
                    if (parts[0].toString().length == 1) {
                        dayStr = "0${chars[0]}"
                        monthStr = if (dateStr[1] == '0') "0${parts[1]}" else
                            "${parts[1]}"
                        if (monthStr.toInt() > 12) return "Incorrect date"
                        return "$dayStr$monthStr"
                    } else {
                        dayStr = "${dateStr[0]}${dateStr[1]}"
                        monthStr = "0${chars[2]}"
                        if (dayStr.toInt() > 31 || monthStr.toInt() > 12) return "Incorrect date"
                        return "$dayStr.$monthStr"
                    }
                }
                return when {
                    (dateStr[0] == '0') -> "0${chars[1]}.0${chars[2]}"
                    (chars[0] < 2 && chars[1] < 2 && chars[2] < 2) -> "Incorrect date"
                    (chars[0] > 3 && chars[1] > 1) -> "Incorrect date"
                    (chars[0] > 3 && chars[1] < 2 && chars[2] < 3) -> "0${chars[0]}.${chars[1]}${chars[2]}"
                    ((chars[0] * 10 + chars[1]) <= 31) -> "${chars[0]}${chars[1]}.0${chars[2]}"
                    else -> "Incorrect date"
                }
            }
            4 -> { //Случай, если введено четыре цифры
                monthStr = "${dateStr[2]}${dateStr[3]}"
                if (dateStr[0] == '0') {
                    if (monthStr.toInt() > 12) return "Incorrect date" else {
                        if (dateStr[2] == '0') return "0${dateStr[1]}.0${dateStr[3]}"
                        return "0${dateStr[1]}.${dateStr[2]}${dateStr[3]}"
                    }
                }
                dayStr = "${dateStr[0]}${dateStr[1]}"
                return if (dayStr.toInt() > 31 || monthStr.toInt() > 12) "Incorrect date" else {
                    if (monthStr.toInt() < 10) "$dayStr.$monthStr"
                    else "$dayStr.$monthStr"
                }
            }
        }

        return "Incorrect date"
    }

    fun isLegalDate(stringDate: String): Boolean { //Проверка существования даты
        date = stringDate.replace(".", "").toInt()
        day = date / 100
        month = date % 100
        return when {
            (day > 31) -> false
            (day > 28) && (month == 2) -> false
            (day > 30) && (month == 4 || month == 6 || month == 9 || month == 11) -> false
            else -> true
        }
    }

    fun checkRules(preDate: String, nextDate: String): Boolean { //Проверка на соблюдение игроками правил
        date = preDate.replace(".", "").toInt()
        day = date / 100
        month = date % 100
        val date2 = nextDate.replace(".", "").toInt()
        val day2 = date2 / 100
        val month2 = date2 % 100
        when {
            day != day2 && month != month2 -> return false
            day == day2 && month == month2 -> return false
            day != day2 -> if (day2 - day > 2 || day2 - day < 1) return false
            month2 != month -> if (month2 - month > 2 || month2 - month < 1) return false
        }
        return true
    }

    fun isDateFine(preDate: String, nextDate: String): Boolean { //Проверка хода по всем параметрам
        var fixedPreDate = ""
        if (preDate != "") fixedPreDate = fixDate(preDate)
        val fixedNextDate = fixDate(nextDate)
        when {
            fixedNextDate == "Incorrect date" -> {
                System.out.println("Error 3: Некорректная дата")
                return false
            }
            !isLegalDate(fixedNextDate) -> {
                System.out.println("Error 2: Такой даты не существует! Введите корректную дату")
                return false
            }
            preDate == "" -> return true
            !checkRules(fixedPreDate, fixedNextDate) -> {
                System.out.println("Error 4: Играйте по правилам!\nПрибавьте 1 или 2 к числу или к месяцу")
                return false
            }
            else -> return true
        }
    }

    fun makeMove(player: Int, preDate: String): String { //Реализация хода игрока
        val input = Scanner(System.`in`)
        System.out.println("Игрок $player, сделайте ход")
        nextDate = input.nextLine()
        if (nextDate == "exit" || nextDate == "Exit") {
            System.out.println("Game Over")
            return "0"
        }
        while (!isDateFine(preDate, nextDate)) nextDate = input.nextLine()
        nextDate = fixDate(nextDate)
        if (nextDate == "31.12") System.out.println("Игрок $player проиграл")
        return nextDate
    }

}
