package com.codelool.pronunciationdrillapp.integration;

import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class SuffixIntegrationTest {
	WordDto testWord1 = new WordDto("abacus", "ˈæbəkəs");
	WordDto testWord2 = new WordDto("abbreviated", "əˈbriviˌeɪtəd");
	WordDto testWord3 = new WordDto("ability", "əˈbɪləti");
	WordDto testWord4 = new WordDto("background", "ˈbækˌgraʊnd");
	List<WordDto> testList = List.of(testWord1, testWord2, testWord3);


	@LocalServerPort
	private int port;

	private String baseUrl;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {
		this.baseUrl = "http://localhost:" + port + "/dataio/word";
	}

	@Test
	public void findAllInEmptyDB_returnsEmpty() {
		List<WordDto> wordList = List.of(testRestTemplate.getForObject(baseUrl + "/findall", WordDto[].class));
		assertThat(wordList.size()).isEqualTo(0);
	}

	@Test
	public void addWordToEmptyDB_returnOneWord() {
		testRestTemplate.postForObject(baseUrl + "/addone", testWord1, WordDto.class);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/findall", WordDto[].class));
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void addWordToEmptyDB_returnWordById() {
		testRestTemplate.postForObject(baseUrl + "/addone", testWord1, WordDto.class);
		WordDto result = testRestTemplate.getForObject(baseUrl + "/findone/1", WordDto.class);
		assertThat(result.getAlphabetic()).isEqualTo(testWord1.getAlphabetic());
		assertThat(result.getPhonetic()).isEqualTo(testWord1.getPhonetic());
	}

	@Test
	public void deleteWordFromDB_returnRest() {
		fillDBFromList(testList);
		testRestTemplate.delete(baseUrl + "/deleteone/1");
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/findall", WordDto[].class));
		assertThat(result.size()).isEqualTo(2);
		result.forEach(resultDto ->
			assertThat(resultDto.getAlphabetic()).isNotEqualTo(testWord1.getAlphabetic())
		);
	}

	@Test
	public void updateWordInDB_returnUpdated() {
		fillDBFromList(testList);
		testRestTemplate.put(baseUrl + "/updateone/1", testWord4, WordDto.class);
		WordDto result = testRestTemplate.getForObject(baseUrl + "/findone/1", WordDto.class);
		assertThat(result.getAlphabetic()).isEqualTo(testWord4.getAlphabetic());
		assertThat(result.getPhonetic()).isEqualTo(testWord4.getPhonetic());
	}

	public void fillDBFromList(List<WordDto> testList) {
		testList.forEach(wordDto ->
			testRestTemplate.postForObject(baseUrl + "/addone", wordDto, WordDto.class)
		);
	}

}
