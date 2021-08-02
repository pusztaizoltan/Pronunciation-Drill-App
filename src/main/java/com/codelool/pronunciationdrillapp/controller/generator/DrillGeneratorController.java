package com.codelool.pronunciationdrillapp.controller.generator;

import com.codelool.pronunciationdrillapp.service.generator.DrillGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generatedata")
public class DrillGeneratorController {

	@Autowired
	DrillGeneratorService drillGeneratorService;

	@GetMapping("/prefix/drill")
	public String generatePrefixDrill() {
		return drillGeneratorService.generatePrefixDrill(4);
	}

	@GetMapping("/suffix/drill")
	public String generateSuffixDrill() {
		return drillGeneratorService.generateSuffixDrill(4);
	}
}

