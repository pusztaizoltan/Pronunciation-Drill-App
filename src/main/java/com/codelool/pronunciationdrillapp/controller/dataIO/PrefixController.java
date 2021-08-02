package com.codelool.pronunciationdrillapp.controller.dataIO;

import com.codelool.pronunciationdrillapp.model.dto.PrefixDto;
import com.codelool.pronunciationdrillapp.model.dto.WordDto;
import com.codelool.pronunciationdrillapp.service.dataIO.PrefixService;
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
@RequestMapping("/dataio/prefix")
public class PrefixController {

	@Autowired
	PrefixService prefixService;

	@GetMapping("/findall")
	public List<PrefixDto> findAll() {
		return prefixService.findAll();
	}

	@GetMapping("/findone/{prefixId}")
	public PrefixDto findById(@PathVariable("prefixId") Long prefixId) {
		return prefixService.findById(prefixId);
	}

	@PostMapping("/addone")
	public void save(@Valid @RequestBody PrefixDto prefixDto) {
		prefixService.save(prefixDto);
	}

	@DeleteMapping("/deleteone/{prefixId}")
	public void deleteById(@PathVariable("prefixId") Long prefixId) {
		prefixService.deleteById(prefixId);
	}

	@GetMapping("/{prefixId}/words")
	public List<WordDto> wordsByPrefixId(@PathVariable("prefixId") Long prefixId) {
		return prefixService.wordsByPrefixId(prefixId);
	}

	@PutMapping("/{prefixId}/addword")
	public void addWordToPrefix(@PathVariable("prefixId") Long prefixId, @Valid @RequestBody WordDto wordDto) {
		prefixService.addWordToPrefix(prefixId, wordDto);
	}

	@DeleteMapping("/{prefixId}/delword")
	public void delWordFromPrefix(@PathVariable("prefixId") Long prefixId, @Valid @RequestBody WordDto wordDto) {
		prefixService.delWordFromPrefix(prefixId, wordDto);
	}
}
