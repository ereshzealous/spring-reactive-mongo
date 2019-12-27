package com.eresh.spring.persistence.repository;

import com.eresh.spring.persistence.entity.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

	Mono<Author> findByIdAndDeleteIsFalse(String id);

	Flux<Author> findByGender(String gender);

	Flux<Author> findByFirstName(String name);

	Flux<Author> findByFirstNameContainsIgnoreCase(String name);

	@Query("{ id: { $exists: true }}")
	Flux<Author> retrieveAllAuthors(final Pageable page);

	Mono<Long> countByFirstNameContainsIgnoreCase(String firstName);

	Flux<Author> findByFirstNameContainsIgnoreCase(String firstName, Pageable pageable);

}
