package hu.otottkovi.cashcard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CashcardApplication

fun main(args: Array<String>) {
	runApplication<CashcardApplication>(*args)
}
