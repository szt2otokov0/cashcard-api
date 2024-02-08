package hu.otottkovi.cashcard.controllers

import hu.otottkovi.cashcard.models.CashCard
import hu.otottkovi.cashcard.repositories.CashCardRepository
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