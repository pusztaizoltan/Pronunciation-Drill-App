package com.codelool.pronunciationdrillapp.integration.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PrefixIntegrationTest {
	PrefixDto testPrefix1 = new PrefixDto("com", "ˈkɑm");
	PrefixDto testPrefix2 = new PrefixDto("de", "ˈdɛ");
	PrefixDto testPrefix3 = new PrefixDto("con", "kən");
	List<PrefixDto> testPrefix1List = List.of(testPrefix1, testPrefix2, testPrefix3);

	WordDto testWord1 = new WordDto("concatenate", "kənˈkætəˌneɪt");
	WordDto testWord2 = new WordDto("consist", "kənˈsɪst");
	WordDto testWord3 = new WordDto("ability", "əˈbɪləti");
	List<WordDto> testWordList = List.of(testWord1, testWord2, testWord3);

	@LocalServerPort
	private int port;

	private String baseUrl;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {
		this.baseUrl = "http://localhost:" + port + "/dataio/prefix";
	}

	public void initPrefixDB(List<PrefixDto> testList) {
		testList.forEach(prefixDto ->
			testRestTemplate.postForObject(baseUrl + "/addone", prefixDto, PrefixDto.class)
		);
	}

	@Test
	public void findAllInEmptyDB_returnsEmpty() {
		List<PrefixDto> prefixList = List.of(testRestTemplate.getForObject(baseUrl + "/findall", PrefixDto[].class));
		assertThat(prefixList.size()).isEqualTo(0);
	}

	@Test
	public void addPrefixToEmptyDB_returnOnePrefix() {
		testRestTemplate.postForObject(baseUrl + "/addone", testPrefix1, PrefixDto.class);
		List<PrefixDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/findall", PrefixDto[].class));
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void addPrefixToEmptyDB_returnPrefixById() {
		testRestTemplate.postForObject(baseUrl + "/addone", testPrefix1, PrefixDto.class);
		PrefixDto result = testRestTemplate.getForObject(baseUrl + "/findone/1", PrefixDto.class);
		assertThat(result.getAlphabetic()).isEqualTo(testPrefix1.getAlphabetic());
		assertThat(result.getPhonetic()).isEqualTo(testPrefix1.getPhonetic());
	}

	@Test
	public void deletePrefixFromDB_returnRest() {
		initPrefixDB(testPrefix1List);
		testRestTemplate.delete(baseUrl + "/deleteone/1");
		List<PrefixDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/findall", PrefixDto[].class));
		assertThat(result.size()).isEqualTo(2);
		result.forEach(resultDto ->
			assertThat(resultDto.getAlphabetic()).isNotEqualTo(testPrefix1.getAlphabetic())
		);
	}

	@Test
	public void getWordsFromEmptyPrefix_returnsEmpty() {
		initPrefixDB(testPrefix1List);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/1/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	public void addWordToPrefix_returnOneWord() {
		initPrefixDB(testPrefix1List);
		testRestTemplate.put(baseUrl + "/3/addword", testWord1, WordDto.class);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/3/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void addWordToPrefix_notFitDontAdd() {
		initPrefixDB(testPrefix1List);
		testRestTemplate.put(baseUrl + "/3/addword", testWord3, WordDto.class);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/3/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	public void deleteWordFromPrefix_returnRest() {
		initPrefixDB(testPrefix1List);
		testWordList.forEach(wordDto ->
			testRestTemplate.put(baseUrl + "/3/addword", wordDto, WordDto.class)
		);
		HttpEntity<WordDto> request = new HttpEntity<WordDto>(testWord1);
		testRestTemplate.exchange(baseUrl + "/3/delword", HttpMethod.DELETE, request, WordDto.class);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/3/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(1);
		result.forEach(resultDto ->
			assertThat(resultDto.getAlphabetic()).isNotEqualTo(testWord1.getAlphabetic())
		);
	}

}
