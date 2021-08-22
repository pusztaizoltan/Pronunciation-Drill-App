package com.codelool.pronunciationdrillapp.model.entity;

import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.model.validator.NonNumeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Word {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	private String alphabetic;
	private String phonetic;

	public WordDto toDto() {
		return new WordDto(this.alphabetic, this.phonetic);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Word word = (Word) object;
		return Objects.equals(this.alphabetic, word.alphabetic) && Objects.equals(this.phonetic, word.phonetic);
	}

}
