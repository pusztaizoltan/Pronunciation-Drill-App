package com.codelool.pronunciationdrillapp.controller.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.SuffixDto;
import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.service.dataIO.SuffixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dataio/suffix")
public class SuffixController {

	@Autowired
	SuffixService suffixService;

	@GetMapping("/findall")
	public List<SuffixDto> findAll() {
		return suffixService.findAll();
	}

	@GetMapping("/findone/{suffixId}")
	public SuffixDto findById(@PathVariable("suffixId") Long suffixId) {
		return suffixService.findById(suffixId);
	}

	@PostMapping("/addone")
	public void save(@Valid @RequestBody SuffixDto suffixDto) {
		suffixService.save(suffixDto);
	}

	@DeleteMapping("/deleteone/{suffixId}")
	public void deleteById(@PathVariable("suffixId") Long suffixId) {
		suffixService.deleteById(suffixId);
	}

	@GetMapping("/{suffixId}/words")
	public List<WordDto> wordsBySuffixId(@PathVariable("suffixId") Long suffixId) {
		return suffixService.wordsBySuffixId(suffixId);
	}

	@PutMapping("/{suffixId}/addword")
	public void addWordToSuffix(@PathVariable("suffixId") Long suffixId, @Valid @RequestBody WordDto wordDto) {
		suffixService.addWordToSuffix(suffixId, wordDto);
	}

	@DeleteMapping("/{suffixId}/delword")
	public void delWordFromSuffix(@PathVariable("suffixId") Long suffixId, @Valid @RequestBody WordDto wordDto) {
		suffixService.delWordFromSuffix(suffixId, wordDto);
	}
}
