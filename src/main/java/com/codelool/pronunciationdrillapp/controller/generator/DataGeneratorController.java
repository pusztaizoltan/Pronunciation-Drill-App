package com.codelool.pronunciationdrillapp.controller.generator;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
import com.codelool.pronunciationdrillapp.service.generator.DataGeneratorServive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/generatedata")
public class DataGeneratorController {

	@Autowired
	private DataGeneratorServive dataGeneratorServive;

	@GetMapping("/prefix")
	public List<PrefixDto> regeneratePrefixes() {
		return dataGeneratorServive.regeneratePrefixes(4);
	}

	@GetMapping("/suffix")
	public List<SuffixDto> regenerateSuffixes() {
		return dataGeneratorServive.regenerateSuffixes(6);
	}
}
