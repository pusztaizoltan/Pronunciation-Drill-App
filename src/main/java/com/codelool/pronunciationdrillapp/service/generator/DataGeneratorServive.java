package com.codelool.pronunciationdrillapp.service.generator;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
import com.codelool.pronunciationdrillapp.model.entity.Prefix;
import com.codelool.pronunciationdrillapp.model.entity.Suffix;
import com.codelool.pronunciationdrillapp.model.entity.Word;
import com.codelool.pronunciationdrillapp.repository.PrefixRepository;
import com.codelool.pronunciationdrillapp.repository.SuffixRepository;
import com.codelool.pronunciationdrillapp.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DataGeneratorServive {

	private final PrefixRepository prefixRepository;
	private final SuffixRepository suffixRepository;
	private final WordRepository wordRepository;

	public DataGeneratorServive(PrefixRepository prefixRepository,
	                            SuffixRepository suffixRepository,
	                            WordRepository wordRepository) {
		this.prefixRepository = prefixRepository;
		this.suffixRepository = suffixRepository;
		this.wordRepository = wordRepository;
	}

	static public Optional<Prefix> searchPrefix(List<Word> words, String prefix) {
		int maxLength = prefix.length() + 3;
		Optional<Prefix> foundPrefix = Optional.empty();
		List<Word> prefixeds = words.stream()
		                            .filter(word -> word.getAlphabetic().startsWith(prefix))
		                            .collect(Collectors.toList());
		if (prefixeds.size() <= 1) return foundPrefix;
		List<String> potentialPhonetics = new ArrayList<>();
		for (Word prefixed : prefixeds) {
			potentialPhonetics.addAll(extractPrefixes(deAccentuate(prefixed.getPhonetic()), maxLength));
		}
		List<String> uniquePrefixes = potentialPhonetics.stream()
		                                                .distinct()
		                                                .sorted(Comparator.reverseOrder())
		                                                .collect(Collectors.toList());
		for (String uniquePrefix : uniquePrefixes) {
			boolean condition = prefixeds.size() == potentialPhonetics.stream().filter(i -> i.equals(uniquePrefix)).count();
			if (condition) {
				foundPrefix = Optional.of(new Prefix(null, prefix, uniquePrefix, prefixeds));
				break;
			}
		}
		return foundPrefix;
	}

	static public List<Prefix> removeRedundantShortPrefixes(List<Prefix> prefixes) {
		Iterator<Prefix> iterator = prefixes.stream().sorted(Comparator.comparing(Prefix::getAlphabetic).reversed()).iterator();
		Prefix actual = iterator.next();
		Prefix next = iterator.next();
		while (iterator.hasNext()) {
			boolean c1 = actual.getAlphabetic().startsWith(next.getAlphabetic());
			boolean c2 = actual.getPhonetic().startsWith(next.getPhonetic());
			boolean c3 = actual.getWords().size() >= next.getWords().size();
			if (c1 && c2 && c3) {
				prefixes.remove(next);
				next = iterator.next();
			} else {
				actual = next;
				next = iterator.next();
			}
		}
		return prefixes;
	}

	static public List<Prefix> removeRedundantLongPrefixes(List<Prefix> prefixes) {
		Iterator<Prefix> iterator = prefixes.stream().sorted(Comparator.comparing(Prefix::getAlphabetic)).iterator();
		Prefix actual = iterator.next();
		Prefix next = iterator.next();
		while (iterator.hasNext()) {
			boolean c1 = next.getAlphabetic().startsWith(actual.getAlphabetic());
			boolean c2 = next.getPhonetic().startsWith(actual.getPhonetic());
			boolean c3 = next.getWords().size() <= actual.getWords().size();
			if (c1 && c2 && c3) {
				prefixes.remove(next);
				next = iterator.next();
			} else {
				actual = next;
				next = iterator.next();
			}
		}
		return prefixes;
	}

	static public String deAccentuate(String phonetic) {
		return phonetic.replace("ˈ", "").replace("ˌ", "");
	}

	static public List<String> extractPrefixes(String word, int maxLength) {
		return IntStream.rangeClosed(1, maxLength).mapToObj(i -> word.substring(0, Math.min(i, word.length()))).collect(Collectors.toList());
	}

	static public String reverseString(String word) {
		return new StringBuilder(word).reverse().toString();
	}

	static public Word reverseWord(Word word) {
		return new Word(word.getId(),
			reverseString(word.getAlphabetic()),
			reverseString(word.getPhonetic()));
	}

	static public Suffix reverseSuffix(Prefix suffix, List<Word> words) {
		List<Word> suffixeds = suffix.getWords()
		                             .stream()
		                             .map(word -> word.getId())
		                             .map(id -> words.stream().filter(item -> item.getId() == id).findFirst().orElseThrow())
		                             .collect(Collectors.toList());
		return new Suffix(
			suffix.getId(),
			reverseString(suffix.getAlphabetic()),
			reverseString(suffix.getPhonetic()),
			suffixeds);
	}

	public List<PrefixDto> regeneratePrefixes(int maxLength) {
		List<Word> words = wordRepository.findAll();
		List<Prefix> prefixes = getPrefixes(maxLength, words);
		prefixRepository.deleteAll();
		prefixRepository.saveAll(prefixes);
		return prefixes.stream().map(Prefix::toDto).collect(Collectors.toList());
	}

	public List<SuffixDto> regenerateSuffixes(int maxLength) {
		List<Word> words = wordRepository.findAll();
		List<Word> reversed = words.stream().map(DataGeneratorServive::reverseWord).collect(Collectors.toList());
		List<Prefix> reversedSuffixes = getPrefixes(maxLength, reversed);
		List<Suffix> suffixes = reversedSuffixes.stream().map(prefix -> reverseSuffix(prefix, words)).collect(Collectors.toList());
		suffixRepository.deleteAll();
		suffixRepository.saveAll(suffixes);
		return suffixes.stream().map(Suffix::toDto).collect(Collectors.toList());
	}

	public List<Prefix> getPrefixes(int maxLength, List<Word> words) {
		Set<String> alphabetics = getAlphabetics(words, maxLength);
		List<Prefix> prefixes = new ArrayList<>();
		for (String alphabetic : alphabetics) {
			Optional<Prefix> foundPrefix = searchPrefix(words, alphabetic);
			if (foundPrefix.isPresent()) prefixes.add(foundPrefix.get());

		}
		prefixes = removeRedundantShortPrefixes(prefixes);
		prefixes = removeRedundantLongPrefixes(prefixes);
		return prefixes;
	}

	public Set<String> getAlphabetics(List<Word> words, int maxLength) {
		Set<String> alphabetics = new HashSet<String>();
		words.stream().map(word -> extractPrefixes(word.getAlphabetic(), maxLength)).forEach(prList -> prList.forEach(affix -> alphabetics.add(affix)));
		return alphabetics;
	}
}
