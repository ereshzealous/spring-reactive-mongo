package com.eresh.spring.rest;

import com.eresh.spring.persistence.entity.Author;
import com.eresh.spring.persistence.repository.AuthorRepository;
import com.eresh.spring.rest.ws.WSAuthorResponse;
import com.eresh.spring.service.AuthorService;
import reactor.core.publisher.Mono;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@RestController
@RequestMapping("/api")
public class AuthorController {

	@Autowired
	AuthorService authorService;

	@Autowired
	AuthorRepository authorRepository;

	@PutMapping("/author")
	public ResponseEntity<Mono<Author>> saveAuthor(@RequestBody Author author) {
		return ResponseEntity.ok(authorService.saveAuthor(author));
	}

	@GetMapping("/author/{id}")
	public ResponseEntity<Mono<Author>> getAuthor(@PathVariable("id") String id) {
		return ResponseEntity.ok(authorService.findAuthor(id));
	}

	@GetMapping("/testing")
	public ResponseEntity<Mono<String>> testing() {
		List<Author> authors = new ArrayList<>();
		for (Integer index = 0; index < 100000; index++) {
			Author author = new Author();
			author.setGender(index % 5 == 0 ? "Female" : "Male");
			author.setLastName(RandomStringUtils.randomAlphabetic(20));
			author.setFirstName(RandomStringUtils.randomAlphabetic(20));
			author.setCity(RandomStringUtils.randomAlphabetic(20));
			authors.add(author);
		}
		authorRepository.saveAll(authors)
		                .subscribe();
		return ResponseEntity.ok(Mono.justOrEmpty(HttpStatus.OK.getReasonPhrase()));
	}

	@GetMapping(value = "/authors", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public ResponseEntity<Mono<WSAuthorResponse>> getAllAuthors(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
	                                                            @RequestParam(name = "offset", defaultValue = "0") Integer offset) {
		return ResponseEntity.ok(authorService.findAuthors(offset, limit));
	}

	@GetMapping(value = "/v1/authors", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public ResponseEntity<WSAuthorResponse> getAuthors(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
	                                                   @RequestParam(name = "offset", defaultValue = "0") Integer offset) {
		return ResponseEntity.ok(authorService.loadAuthors(offset, limit));
	}

	@GetMapping(value = "/authors/search", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public ResponseEntity<Mono<WSAuthorResponse>> getAuthorsByFirstName(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
	                                                                    @RequestParam(name = "offset", defaultValue = "0") Integer offset,
	                                                                    @RequestParam(name = "query") String query) {
		return ResponseEntity.ok(authorService.findAuthors(offset, limit, query));
	}

	@GetMapping(value = "/v1/authors/search", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public ResponseEntity<WSAuthorResponse> searchAuthors(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
	                                                      @RequestParam(name = "offset", defaultValue = "0") Integer offset,
	                                                      @RequestParam(name = "query") String query) {
		return ResponseEntity.ok(authorService.loadAuthors(offset, limit, query));
	}
}
