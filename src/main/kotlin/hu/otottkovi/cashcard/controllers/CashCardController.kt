package hu.otottkovi.cashcard.controllers

import hu.otottkovi.cashcard.models.CashCard
import hu.otottkovi.cashcard.repositories.CashCardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/cashcards")
class CashCardController(val repo:CashCardRepository) {

    @GetMapping
    fun findAll(pageable:Pageable):ResponseEntity<List<CashCard>>{
        val page: Page<CashCard> = repo.findAll(PageRequest.of(pageable.pageNumber,pageable.pageSize,
            pageable.getSortOr(Sort.by(Sort.Direction.ASC,"amount"))))
        return ResponseEntity.ok(page.content)
    }

    @GetMapping("/{requestedId}")
    fun findById(@PathVariable requestedId: Long):ResponseEntity<CashCard> {
        val optionalCard = repo.findById(requestedId)
        return if(optionalCard.isPresent) {
            val card: CashCard = optionalCard.get()
            ResponseEntity.ok(card)
        } else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createCashCard(@RequestBody newCashCard: CashCard, uriBuilder:UriComponentsBuilder):ResponseEntity<Void>{
        val savedCashCard:CashCard = repo.save(newCashCard)
        val location = uriBuilder
            .path("cashcards/{id}")
            .buildAndExpand(savedCashCard.id)
            .toUri()
        return ResponseEntity.created(location).build()
    }
}