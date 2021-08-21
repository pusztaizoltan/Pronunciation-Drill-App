package com.codelool.pronunciationdrillapp.model.entity;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prefix extends Morpheme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String alphabetic;
	private String phonetic;
//	@Size(min=2)
	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Word> words;


	public PrefixDto toDto() {
		return new PrefixDto(this.alphabetic, this.phonetic);
	}

	public void addWord(Word word) {
		if (isPrefixed(word) && !words.contains(word)) words.add(word);
	}

	public void delWord(Word word) {
		words.removeIf(item -> item.equals(word));
	}

	@Override
	public String toString() {
		return "Prefix{" +
			"alphabetic='" + alphabetic + '\'' +
			", phonetic='" + phonetic + '\'' +
			", wordsCount=" + words.size() + '\'' +
			"}\n";
	}

	public boolean isPrefixed(Word word) {
		return word.getAlphabetic().startsWith(this.getAlphabetic()) &&
			word.getPhonetic().startsWith(this.getPhonetic());
	}
}
