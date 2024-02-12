package hu.otottkovi.cashcard

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import hu.otottkovi.cashcard.models.CashCard
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI

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

}
