package com.codelool.pronunciationdrillapp.integration.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
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
public class SuffixIntegrationTest {
	SuffixDto testSuffix1 = new SuffixDto("able", "əbl");
	SuffixDto testSuffix2 = new SuffixDto("ed", "ˈəd");
	SuffixDto testSuffix3 = new SuffixDto("ing", "ɪŋ");
	List<SuffixDto> testSuffix1List = List.of(testSuffix1, testSuffix2, testSuffix3);

	WordDto testWord1 = new WordDto("confusing", "kənˈfjuzɪŋ");
	WordDto testWord2 = new WordDto("connecting", "kəˈnɛktɪŋ");
	WordDto testWord3 = new WordDto("computed", "kəmˈpjutəd");
	List<WordDto> testWordList = List.of(testWord1, testWord2, testWord3);

	@LocalServerPort
	private int port;

	private String baseUrl;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {
		this.baseUrl = "http://localhost:" + port + "/dataio/suffix";
	}

	public void initSuffixDB(List<SuffixDto> testList) {
		testList.forEach(suffixDto ->
			testRestTemplate.postForObject(baseUrl + "/addone", suffixDto, SuffixDto.class)
		);
	}

	@Test
	public void findAllInEmptyDB_returnsEmpty() {
		List<SuffixDto> suffixList = List.of(testRestTemplate.getForObject(baseUrl + "/findall", SuffixDto[].class));
		assertThat(suffixList.size()).isEqualTo(0);
	}

	@Test
	public void addSuffixToEmptyDB_returnOneSuffix() {
		testRestTemplate.postForObject(baseUrl + "/addone", testSuffix1, SuffixDto.class);
		List<SuffixDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/findall", SuffixDto[].class));
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void addSuffixToEmptyDB_returnSuffixById() {
		testRestTemplate.postForObject(baseUrl + "/addone", testSuffix1, SuffixDto.class);
		SuffixDto result = testRestTemplate.getForObject(baseUrl + "/findone/1", SuffixDto.class);
		assertThat(result.getAlphabetic()).isEqualTo(testSuffix1.getAlphabetic());
		assertThat(result.getPhonetic()).isEqualTo(testSuffix1.getPhonetic());
	}

	@Test
	public void deleteSuffixFromDB_returnRest() {
		initSuffixDB(testSuffix1List);
		testRestTemplate.delete(baseUrl + "/deleteone/1");
		List<SuffixDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/findall", SuffixDto[].class));
		assertThat(result.size()).isEqualTo(2);
		result.forEach(resultDto ->
			assertThat(resultDto.getAlphabetic()).isNotEqualTo(testSuffix1.getAlphabetic())
		);
	}

	@Test
	public void getWordsFromEmptySuffix_returnsEmpty() {
		initSuffixDB(testSuffix1List);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/1/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	public void addWordToSuffix_returnOneWord() {
		initSuffixDB(testSuffix1List);
		testRestTemplate.put(baseUrl + "/3/addword", testWord1, WordDto.class);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/3/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void addWordToSuffix_notFitDontAdd() {
		initSuffixDB(testSuffix1List);
		testRestTemplate.put(baseUrl + "/3/addword", testWord3, WordDto.class);
		List<WordDto> result = List.of(testRestTemplate.getForObject(baseUrl + "/3/words", WordDto[].class));
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	public void deleteWordFromSuffix_returnRest() {
		initSuffixDB(testSuffix1List);
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
