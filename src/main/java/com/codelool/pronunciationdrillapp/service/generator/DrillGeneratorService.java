package com.codelool.pronunciationdrillapp.service.generator;

import com.codelool.pronunciationdrillapp.model.entity.Word;
import com.codelool.pronunciationdrillapp.repository.PrefixRepository;
import com.codelool.pronunciationdrillapp.repository.SuffixRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrillGeneratorService {

	private final PrefixRepository prefixRepository;
	private final SuffixRepository suffixRepository;

	public DrillGeneratorService(PrefixRepository prefixRepository, SuffixRepository suffixRepository) {
		this.prefixRepository = prefixRepository;
		this.suffixRepository = suffixRepository;
	}

	public String generatePrefixDrill(int minTypeContent) {
		List<String> drills = prefixRepository
			.findAll()
			.stream()
			.filter(prefix -> prefix.getWords().size() >= minTypeContent)
			.map(prefix -> generateTypeDrill(prefix.getWords()))
			.collect(Collectors.toList());
		Collections.shuffle(drills);
		return drills.stream().collect(Collectors.joining("<br>\n----------------------------------------------------<br>\n"));
	}

	public String generateSuffixDrill(int minTypeContent) {
		List<String> drills = suffixRepository
			.findAll()
			.stream()
			.filter(suffix -> suffix.getWords().size() >= minTypeContent)
			.map(suffix -> generateTypeDrill(suffix.getWords()))
			.collect(Collectors.toList());
		Collections.shuffle(drills);
		return drills.stream().collect(Collectors.joining("<br>\n----------------------------------------------------<br>\n"));
	}

	public String generateTypeDrill(List<Word> words) {
		Collections.shuffle(words);
		List<String> lines = new ArrayList<>();
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < words.size(); i++) {
			line.append(words.get(i).getAlphabetic());
			if (line.length() > 50) {
				line.append(",");
				lines.add(line.toString());
				line = new StringBuilder();
			} else {
				line.append(" ");
			}
		}
		if (line.length() > 1) {
			lines.add(line.toString().replaceAll(" $", ","));
		}
		String typeDrill = lines.stream().collect(Collectors.joining("<br>\n")).replaceAll(",$", ".");
		return typeDrill.substring(0, 1).toUpperCase() + typeDrill.substring(1).toLowerCase() + "\n";
	}
}
