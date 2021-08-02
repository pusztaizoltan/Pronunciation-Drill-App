package com.codelool.pronunciationdrillapp.repository;

import com.codelool.pronunciationdrillapp.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
