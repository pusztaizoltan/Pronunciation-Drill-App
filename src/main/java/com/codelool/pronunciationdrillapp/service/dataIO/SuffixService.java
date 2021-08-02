package com.codelool.pronunciationdrillapp.service.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.model.entity.Suffix;
import com.codelool.pronunciationdrillapp.repository.SuffixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuffixService {

	@Autowired
	private SuffixRepository suffixRepository;

	public List<SuffixDto> findAll() {
		return suffixRepository.findAll().stream().map(suffix -> suffix.toDto()).collect(Collectors.toList());
	}

	public SuffixDto findById(Long suffixId) {
		Suffix foundSuffix = suffixRepository.findById(suffixId).orElseThrow(() -> new IllegalArgumentException("Provided suffixId does not exist!"));
		return foundSuffix.toDto();
	}

	public void save(SuffixDto suffixDto) {
		suffixRepository.save(suffixDto.toEntity());
	}

	public void deleteById(Long suffixId) {
		suffixRepository.deleteById(suffixId);
	}

	public List<WordDto> wordsBySuffixId(Long suffixId) {
		Suffix foundSuffix = suffixRepository.findById(suffixId).orElseThrow(() -> new IllegalArgumentException("Provided suffixId does not exist!"));
		return foundSuffix.getWords().stream().map(word -> word.toDto()).collect(Collectors.toList());
	}

	public void addWordToSuffix(Long suffixId, WordDto wordDto) {
		Suffix foundSuffix = suffixRepository.findById(suffixId).orElseThrow(() -> new IllegalArgumentException("Provided suffixId does not exist!"));
		foundSuffix.addWord(wordDto.toEntity());
		suffixRepository.save(foundSuffix);
	}

	public void delWordFromSuffix(Long suffixId, WordDto wordDto) {
		Suffix foundSuffix = suffixRepository.findById(suffixId).orElseThrow(() -> new IllegalArgumentException("Provided suffixId does not exist!"));
		foundSuffix.delWord(wordDto.toEntity());
		suffixRepository.save(foundSuffix);
	}
}
