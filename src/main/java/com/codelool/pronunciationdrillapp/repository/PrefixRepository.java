package com.codelool.pronunciationdrillapp.repository;

import com.codelool.pronunciationdrillapp.model.entity.Prefix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrefixRepository extends JpaRepository<Prefix, Long> {
}
