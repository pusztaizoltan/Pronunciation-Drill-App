package com.codelool.pronunciationdrillapp.model.dto;

import com.codelool.pronunciationdrillapp.model.entity.Word;
import com.codelool.pronunciationdrillapp.model.validator.NonNumeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {

	@NonNumeric(message = "Please provide a author")
	private String alphabetic;
	@NonNumeric(message = "Please provide a author")
	private String phonetic;

	public Word toEntity() {
		return new Word(null, this.alphabetic, this.phonetic);
	}

	public Word toEntity(Word originalWord) {
		if (this.alphabetic != null) originalWord.setAlphabetic(this.alphabetic);
		if (this.phonetic != null) originalWord.setPhonetic(this.phonetic);
		return originalWord;
	}
}
