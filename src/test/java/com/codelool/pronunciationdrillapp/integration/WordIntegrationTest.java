package com.codelool.pronunciationdrillapp.integration;

import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.model.entity.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class WordIntegrationTest {
	WordDto testWord1 = new WordDto("abacus", "ˈæbəkəs");
//	insert into word (alphabetic, phonetic) values ('abacus', 'ˈæbəkəs');
//	insert into word (alphabetic, phonetic) values ('abbreviated', 'əˈbriviˌeɪtəd');
//	insert into word (alphabetic, phonetic) values ('abbreviation', 'əˌbriviˈeɪʃən');
//	insert into word (alphabetic, phonetic) values ('ability', 'əˈbɪləti');
//	insert into word (alphabetic, phonetic) values ('able', 'ˈeɪbəl');

	@LocalServerPort
	private int port;

	private String baseUrl;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {
		this.baseUrl = "http://localhost:" + port + "/dataio/word/findall";
	}

	@Test
	public void getword_emptyDB_returnsEmpty() {
		System.out.println("-------###########-----");
		List<WordDto> wordList = List.of(testRestTemplate
			.getForObject(baseUrl, WordDto[].class));
		assertEquals(0, wordList.size());
	}

//		@Test
//	public void addWordToEmptyDB_returnWord() {
////		Word testSong = new Song(null, "The Nine");
//		System.out.println("-------###########-----");
//		System.out.println(testWord1);
//		WordDto result = testRestTemplate.postForObject(baseUrl, testWord1, WordDto.class);
//		testRestTemplate.
//		assertEquals(testWord1.getAlphabetic(), result.getAlphabetic());
//		assertEquals(testWord1.getPhonetic(), result.getPhonetic());
//	}


//	@Autowired
//	JdbcTemplate jdbcTemplate;


}