package com.codelool.pronunciationdrillapp.service.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.model.entity.Word;
import com.codelool.pronunciationdrillapp.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordService {

	@Autowired
	private WordRepository wordRepository;

	public List<WordDto> findAll() {
		return wordRepository.findAll().stream().map(word -> word.toDto()).collect(Collectors.toList());
	}

	public WordDto findById(Long wordId) {
		Word foundWord = wordRepository.findById(wordId).orElseThrow(() -> new IllegalArgumentException("Provided wordId does not exist!"));
		return foundWord.toDto();
	}

	public void save(WordDto wordDto) {
		wordRepository.save(wordDto.toEntity());
	}

	public void deleteById(Long wordId) {
		wordRepository.deleteById(wordId);
	}

	public void updateById(Long wordId, WordDto wordDto) {
		Word foundWord = wordRepository.findById(wordId).orElseThrow(() -> new IllegalArgumentException("Provided wordId does not exist!"));
		wordRepository.save(wordDto.toEntity(foundWord));
	}
}
