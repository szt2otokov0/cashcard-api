package hu.otottkovi.cashcard.repositories

import hu.otottkovi.cashcard.models.CashCard
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface CashCardRepository : CrudRepository<CashCard,Long>, PagingAndSortingRepository<CashCard,Long> {
}