package com.codelool.pronunciationdrillapp.model.dto;

import com.codelool.pronunciationdrillapp.model.entity.Suffix;
import com.codelool.pronunciationdrillapp.model.validator.NonNumeric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuffixDto {

	@NonNumeric
	private String alphabetic;
	@NonNumeric
	private String phonetic;

	public Suffix toEntity() {
		return new Suffix(null, this.alphabetic, this.phonetic, List.of());
	}

}
