package com.codelool.pronunciationdrillapp.integration.generator;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
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
public class GeneratorIntegrationTest {
	WordDto testWord1 = new WordDto("concatenate", "kənˈkætəˌneɪt");
	WordDto testWord2 = new WordDto("consist", "kənˈsɪst");
	WordDto testWord3 = new WordDto("confusing", "kənˈfjuzɪŋ");
	WordDto testWord4 = new WordDto("connecting", "kəˈnɛktɪŋ");

	WordDto testWord5 = new WordDto("ability", "əˈbɪləti");
	WordDto testWord6 = new WordDto("abacus", "ˈæbəkəs");
	WordDto testWord7 = new WordDto("abbreviated", "əˈbriviˌeɪtəd");

	WordDto testWord8 = new WordDto("computed", "kəmˈpjutəd");
	WordDto testWord9 = new WordDto("background", "ˈbækˌgraʊnd");
	List<WordDto> testList = List.of(testWord1, testWord2, testWord3, testWord4, testWord5, testWord6, testWord7, testWord8, testWord9);

	@LocalServerPort
	private int port;

	private String baseUrl;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	public void initDB() {
		setUp();
		initWordDB();
		initPrefixDB();
		initSuffixDB();
	}

	public void setUp() {
		this.baseUrl = "http://localhost:" + port;
	}

	public void initWordDB() {
		testList.forEach(wordDto ->
			testRestTemplate.postForObject(baseUrl + "/dataio/word/addone", wordDto, WordDto.class)
		);
	}

	public void initPrefixDB() {
		testRestTemplate.getForObject(baseUrl + "/generatedata/prefix", PrefixDto[].class);
	}

	public void initSuffixDB() {
		testRestTemplate.getForObject(baseUrl + "/generatedata/suffix", SuffixDto[].class);
	}

	@Test
	public void prefixDataGenerator_generateExpected() {
		List<PrefixDto> prefixList = List.of(testRestTemplate.getForObject(baseUrl + "/dataio/prefix/findall", PrefixDto[].class));
		assertThat(
			prefixList.stream().anyMatch(prefixDto ->
				testWord1.getAlphabetic().startsWith(prefixDto.getAlphabetic()) &&
					testWord1.getPhonetic().startsWith(prefixDto.getPhonetic())
			)).isTrue();
	}

	@Test
	public void prefixDataGenerator_avoidUniqueSample() {
		int dataLength = testRestTemplate.getForObject(baseUrl + "/dataio/prefix/findall", PrefixDto[].class).length;
		for (int i = 1; i <= dataLength; i++) {
			String url = baseUrl + "/dataio/prefix/" + i + "/words";
			assertThat(testRestTemplate.getForObject(url, WordDto[].class).length).isGreaterThan(1);
		}
	}

	@Test
	public void suffixDataGenerator_generateExpected() {
		List<SuffixDto> suffixList = List.of(testRestTemplate.getForObject(baseUrl + "/dataio/suffix/findall", SuffixDto[].class));
		assertThat(
			suffixList.stream().anyMatch(suffixDto ->
				testWord3.getAlphabetic().endsWith(suffixDto.getAlphabetic()) &&
					testWord3.getPhonetic().endsWith(suffixDto.getPhonetic())
			)).isTrue();
	}

	@Test
	public void suffixDataGenerator_avoidUniqueSample() {
		int dataLength = testRestTemplate.getForObject(baseUrl + "/dataio/suffix/findall", SuffixDto[].class).length;
		for (int i = 1; i <= dataLength; i++) {
			String url = baseUrl + "/dataio/suffix/" + i + "/words";
			assertThat(testRestTemplate.getForObject(url, WordDto[].class).length).isGreaterThan(1);
		}
	}

	@Test
	public void prefixDrillGenerator_containExpected() {
		String result = testRestTemplate.getForObject(baseUrl + "/generatedata/prefix/drill", String.class);
		assertThat(result.toLowerCase().contains(testWord1.getAlphabetic())).isTrue();
		assertThat(result.toLowerCase().contains(testWord2.getAlphabetic())).isTrue();
		assertThat(result.toLowerCase().contains(testWord3.getAlphabetic())).isTrue();
		assertThat(result.toLowerCase().contains(testWord4.getAlphabetic())).isTrue();
	}

	@Test
	public void suffixDrillGenerator_containExpected() {
		String result = testRestTemplate.getForObject(baseUrl + "/generatedata/suffix/drill", String.class);
		assertThat(result.toLowerCase().contains(testWord7.getAlphabetic())).isTrue();
		assertThat(result.toLowerCase().contains(testWord8.getAlphabetic())).isTrue();
	}


	@Test
	public void prefixDrillGenerator_excludeExpected() {
		String result = testRestTemplate.getForObject(baseUrl + "/generatedata/prefix/drill", String.class);
		assertThat(result.toLowerCase().contains(testWord9.getAlphabetic())).isFalse();
	}

	@Test
	public void suffixDrillGenerator_excludeExpected() {
		String result = testRestTemplate.getForObject(baseUrl + "/generatedata/suffix/drill", String.class);
		assertThat(result.toLowerCase().contains(testWord1.getAlphabetic())).isFalse();
		assertThat(result.toLowerCase().contains(testWord5.getAlphabetic())).isFalse();
	}

}
