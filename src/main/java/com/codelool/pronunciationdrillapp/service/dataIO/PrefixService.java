package com.codelool.pronunciationdrillapp.service.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.model.entity.Prefix;
import com.codelool.pronunciationdrillapp.repository.PrefixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrefixService {

	@Autowired
	private PrefixRepository prefixRepository;

	public List<PrefixDto> findAll() {
		return prefixRepository.findAll().stream().map(prefix -> prefix.toDto()).collect(Collectors.toList());
	}

	public PrefixDto findById(Long prefixId) {
		Prefix foundPrefix = prefixRepository.findById(prefixId).orElseThrow(() -> new IllegalArgumentException("Provided prefixId does not exist!"));
		return foundPrefix.toDto();
	}

	public void save(PrefixDto prefixDto) {
		prefixRepository.save(prefixDto.toEntity());
	}

	public void deleteById(Long prefixId) {
		prefixRepository.deleteById(prefixId);
	}

	public List<WordDto> wordsByPrefixId(Long prefixId) {
		Prefix foundPrefix = prefixRepository.findById(prefixId).orElseThrow(() -> new IllegalArgumentException("Provided prefixId does not exist!"));
		return foundPrefix.getWords().stream().map(word -> word.toDto()).collect(Collectors.toList());
	}

	public void addWordToPrefix(Long prefixId, WordDto wordDto) {
		Prefix foundPrefix = prefixRepository.findById(prefixId).orElseThrow(() -> new IllegalArgumentException("Provided prefixId does not exist!"));
		foundPrefix.addWord(wordDto.toEntity());
		prefixRepository.save(foundPrefix);
	}

	public void delWordFromPrefix(Long prefixId, WordDto wordDto) {
		Prefix foundPrefix = prefixRepository.findById(prefixId).orElseThrow(() -> new IllegalArgumentException("Provided prefixId does not exist!"));
		foundPrefix.delWord(wordDto.toEntity());
		prefixRepository.save(foundPrefix);
	}
}
