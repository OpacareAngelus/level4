package util

import java.util.*

object ParsingEMailToName {

    fun parseMail(mail: String): String {
        val (firstName, secondName) = mail.split("@").first().split(".")
        return "${
            firstName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        } ${
            secondName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }"
    }
}