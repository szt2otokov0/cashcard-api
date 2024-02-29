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
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.security.Principal
import java.util.Optional

@RestController
@RequestMapping("/cashcards")
class CashCardController(val repo:CashCardRepository) {

    private val principalIds = mapOf<String,Long>("Tomika" to 100,"Patrick" to 101, "not a card owner" to 999)

    @GetMapping
    fun findAll(pageable: Pageable, principal: Principal): ResponseEntity<List<CashCard>>{
        val page: Page<CashCard> = repo.findByOwnerId(
            principalIds.getOrDefault(principal.name,0),
            PageRequest.of(
                pageable.pageNumber, pageable.pageSize,
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
            )
        )
        return ResponseEntity.ok(page.content)
    }

    @GetMapping("/{requestedId}")
    fun findById(@PathVariable requestedId: Long,principal: Principal):ResponseEntity<CashCard> {
        val ownerId = principalIds
            .getOrDefault(principal.name, 0)
        val optionalCard = Optional.ofNullable(repo.findByIdAndOwnerId(requestedId, ownerId))
        return if(optionalCard.isPresent) {
            val card: CashCard = optionalCard.get()
            ResponseEntity.ok(card)
        } else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createCashCard(@RequestBody newCashCard: CashCard, uriBuilder:UriComponentsBuilder,principal: Principal
    ):ResponseEntity<Void>{
        val cashCardToSave = CashCard(newCashCard.id,newCashCard.amount,principalIds.getOrDefault(principal.name,
            0))
        val savedCashCard:CashCard = repo.save(cashCardToSave)
        val location = uriBuilder
            .path("cashcards/{id}")
            .buildAndExpand(savedCashCard.id)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    @PutMapping("/{requestedId}")
    fun updateCashCard(@PathVariable requestedId: Long,@RequestBody cardToUpdate:CashCard):ResponseEntity<Void>{

    }
}