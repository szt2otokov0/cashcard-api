package hu.otottkovi.cashcard.repositories

import hu.otottkovi.cashcard.models.CashCard
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface CashCardRepository : CrudRepository<CashCard,Long>, PagingAndSortingRepository<CashCard,Long> {
    fun findByIdAndOwnerId(id:Long,ownerId:Long):CashCard?
    fun findByOwnerId(ownerId: Long,pageRequest: PageRequest):Page<CashCard>
}