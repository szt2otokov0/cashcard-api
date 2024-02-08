package hu.otottkovi.cashcard.controllers

import hu.otottkovi.cashcard.models.CashCard
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cashcards")
class CashCardController {

    @GetMapping("/{requestedId}")
    fun findById(@PathVariable requestedId: Long):ResponseEntity<CashCard> {
        return if(requestedId == 99L) {
            val card: CashCard = CashCard(99, 123.45)
            ResponseEntity.ok(card)
        } else ResponseEntity.notFound().build()
    }
}