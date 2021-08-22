package com.codelool.pronunciationdrillapp.model.entity;

import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
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
public class Suffix extends Morpheme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String alphabetic;
	private String phonetic;
	@Size(min=2)
	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Word> words;

	public SuffixDto toDto() {
		return new SuffixDto(this.alphabetic, this.phonetic);
	}

	public void addWord(Word word) {
		if (isSuffixed(word) && !words.contains(word)) words.add(word);
	}

	public void delWord(Word word) {
		words.removeIf(item -> item.equals(word));
	}

	public boolean isSuffixed(Word word) {
		return word.getAlphabetic().endsWith(this.getAlphabetic()) &&
			word.getPhonetic().endsWith(this.getPhonetic());
	}

}
