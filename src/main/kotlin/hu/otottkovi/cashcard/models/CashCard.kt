package hu.otottkovi.cashcard.models

import org.springframework.data.annotation.Id

data class CashCard(@Id val id:Long, val amount:Double)