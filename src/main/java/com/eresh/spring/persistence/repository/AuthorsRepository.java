package com.eresh.spring.persistence.repository;

import com.eresh.spring.persistence.entity.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created By Gorantla, Eresh on 26/Dec/2019
 **/
@Repository
public interface AuthorsRepository extends MongoRepository<Author, String> {
	Page<Author> findByFirstNameContainsIgnoreCase(String firstName, Pageable pageable);
}
