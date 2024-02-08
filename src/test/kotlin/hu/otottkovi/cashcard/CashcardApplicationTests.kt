package hu.otottkovi.cashcard

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashcardApplicationTests {

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
		assertThat(response.body).isBlank()
	}

	@Test
	fun shouldReturnACashCardWhenDataIsSaved(){

	}

}
