package hu.otottkovi.cashcard.controllers

import hu.otottkovi.cashcard.models.CashCard
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cashcards")
class CashCardController {

    @GetMapping("/{requestedId}")
    fun findById():ResponseEntity<String>{
        val card:CashCard = CashCard(1000L,0.0)
        return ResponseEntity.ok("{}")
    }
}