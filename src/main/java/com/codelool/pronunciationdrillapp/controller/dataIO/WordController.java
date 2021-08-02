package com.codelool.pronunciationdrillapp.controller.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.service.dataIO.WordService;
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
@RequestMapping("/dataio/word")
public class WordController {

	@Autowired
	WordService wordService;

	@GetMapping("/findall")
	public List<WordDto> findAll() {
		return wordService.findAll();
	}

	@GetMapping("/findone/{wordId}")
	public WordDto findById(@PathVariable("wordId") Long wordId) {
		return wordService.findById(wordId);
	}

	@PostMapping("/addone")
	public void save(@Valid @RequestBody WordDto wordDto) {
		wordService.save(wordDto);
	}

	@DeleteMapping("/deleteone/{wordId}")
	public void deleteById(@PathVariable("wordId") Long wordId) {
		wordService.deleteById(wordId);
	}

	@PutMapping("/updateone/{wordId}")
	public void updateById(
		@Valid @RequestBody WordDto wordDto,
		@PathVariable("wordId") Long wordId) {
		wordService.updateById(wordId, wordDto);
	}
}
