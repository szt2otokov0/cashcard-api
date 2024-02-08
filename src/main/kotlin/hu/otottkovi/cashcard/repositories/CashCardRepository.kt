package hu.otottkovi.cashcard.repositories

import hu.otottkovi.cashcard.models.CashCard
import org.springframework.data.repository.CrudRepository

interface CashCardRepository : CrudRepository<CashCard,Long> {
}