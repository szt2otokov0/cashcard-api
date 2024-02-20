package hu.otottkovi.cashcard

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import hu.otottkovi.cashcard.models.CashCard
import net.minidev.json.JSONArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashCardApplicationTests {

	@Autowired
	lateinit var testRestTemplate:TestRestTemplate

	@Test
	fun shouldReturnACashCard(){
		val response: ResponseEntity<String> = testRestTemplate.getForEntity<String>("/cashcards/99")
		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		val jsonContext:DocumentContext = JsonPath.parse(response.body)
		val id:Number = jsonContext.read("\$.id")
		assertThat(id).isEqualTo(99)
		val amount:Double = jsonContext.read("\$.amount")
		assertThat(amount).isEqualTo(123.45)
	}

	@Test
	fun shouldNotReturnACashCardWithAnUnknownId(){
		val response = testRestTemplate.getForEntity<String>("/cashcard/1000")
		assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
	}

	@Test
	@DirtiesContext
	fun shouldCreateANewCashCard(){
		val cashCard:CashCard = CashCard(44,250.0)
		val createResponse: ResponseEntity<Void> = testRestTemplate.postForEntity<Void>("/cashcards",cashCard)
		assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)
		val locationOfNewCashCard = createResponse.headers.location
		locationOfNewCashCard?.let {
			val getResponse: ResponseEntity<String> = testRestTemplate
				.getForEntity<String>(it)
			assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
		}
		val documentContext:DocumentContext = JsonPath.parse(createResponse.body)
		val id:Number = documentContext.read("\$.id")
		val amount:Double = documentContext.read("\$.amount")
		assertThat(id).isNotNull()
		assertThat(amount).isEqualTo(250.0)

	}

	@Test
	fun shouldReturnAllCashCardsWhenListIsRequested(){
		val response = testRestTemplate.getForEntity<String>("/cashcards")
		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		val jsonContext: DocumentContext = JsonPath.parse(response.body)
		val cashCardCount: Int = jsonContext.read("\$.length()")
		assertThat(cashCardCount).isEqualTo(3)
		val ids: JSONArray = jsonContext.read("\$..id")
		assertThat(ids).containsExactlyInAnyOrder(99,100,101)
		val amounts: JSONArray = jsonContext.read("\$..amount")
		assertThat(amounts).containsExactlyInAnyOrder(123.45,1.0,150.0)
	}

	@Test
	fun shouldReturnAPageOfCashCards(){
		val response:ResponseEntity<String> = testRestTemplate.getForEntity<String>(
			"/cashcards?page=0&size=1&sort=amount,desc")
		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

		val jsonContext:DocumentContext = JsonPath.parse(response.body)
		val page:JSONArray = jsonContext.read("\$[*]")
		assertThat(page.size).isEqualTo(1)

		val amount:Double = jsonContext.read("\$[0].amount")
		assertThat(amount).isEqualTo(150.0)
	}

	@Test
	fun shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues(){
		val response:ResponseEntity<String> = testRestTemplate.getForEntity<String>("/cashcards")
		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

		val jsonContext:DocumentContext = JsonPath.parse(response.body)
		val page:JSONArray = jsonContext.read("\$[*]")
		assertThat(page.size).isEqualTo(3)

		val amounts: JSONArray = jsonContext.read("\$..amount")
		assertThat(amounts).containsExactly(1.0,123.45,150.0)
	}



}
