package com.eresh.spring.service;

import com.eresh.spring.persistence.entity.Author;
import com.eresh.spring.persistence.repository.AuthorRepository;
import com.eresh.spring.persistence.repository.AuthorsRepository;
import com.eresh.spring.rest.ws.WSAuthorResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@Service
public class AuthorService {

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	AuthorsRepository authorsRepository;

	public Mono<Author> saveAuthor(Author author) {
		if (StringUtils.isNotBlank(author.getId())) {
			return authorRepository.findByIdAndDeleteIsFalse(author.getId())
			                       .switchIfEmpty(Mono.error(new Exception("No Author found with Id: " + author.getId())))
			                       .doOnSuccess(author1 -> {
				                       author1 = updateAuthor(author, author1);
				                       authorRepository.save(author1)
				                                       .subscribe();
			                       });
		}
		return authorRepository.save(author);
	}

	public Mono<Author> findAuthor(String id) {
		return authorRepository.findByIdAndDeleteIsFalse(id)
		                       .switchIfEmpty(Mono.error(new Exception("No Author found with Id: " + id)));
	}

	public Flux<Author> getAllAuthors() {
		//return authorRepository.findByFirstNameContainsIgnoreCase("ee").switchIfEmpty(Flux.empty());
		return authorRepository.findAll()
		                       .switchIfEmpty(Mono.error(new Exception("No Blog found with title Containing")));
	}

	public Mono<Page<Author>> findAllUsersPaged(Pageable pageable) {
		return this.authorRepository.count()
		                            .flatMap(userCount -> {
			                            return this.authorRepository.findAll(pageable.getSort())
			                                                        .buffer(pageable.getPageSize(), (pageable.getPageNumber() + 1))
			                                                        .elementAt(pageable.getPageNumber(), new ArrayList<>())
			                                                        .map(users -> new PageImpl<>(users, pageable, userCount));
		                            });
	}

	public Mono<WSAuthorResponse> findAuthors(Integer offset, Integer limit) {
		WSAuthorResponse result = new WSAuthorResponse();
		return authorRepository.count()
		                       .map(totalElements -> {
			                       result.setTotalElements(totalElements);
			                       return totalElements;
		                       })
		                       .flatMapMany(el -> authorRepository.retrieveAllAuthors(PageRequest.of(offset, limit)))
		                       .collectList()
		                       .map(data -> {
			                       result.setAuthors(data);
			                       return result;
		                       });
	}

	private List<Author> generateAuthors() {
		List<Author> authors = new ArrayList<>();
		authors.add(new Author());
		return authors;
	}

	public Mono<WSAuthorResponse> findAuthors(Integer offset, Integer limit, String firstName) {
		WSAuthorResponse result = new WSAuthorResponse();
		return authorRepository.countByFirstNameContainsIgnoreCase(firstName)
		                       .map(totalElements -> {
			                       result.setTotalElements(totalElements);
			                       return result;
		                       })
		                       .flatMapMany(el -> authorRepository.findByFirstNameContainsIgnoreCase(firstName, PageRequest.of(offset, limit)))
		                       .collectList()
		                       .map(data -> {
			                       result.setAuthors(data);
			                       return result;
		                       });
	}

	private WSAuthorResponse toWsAuthorResponse(Long totalElements, List<Author> authors) {
		WSAuthorResponse response = new WSAuthorResponse();
		response.setAuthors(authors);
		response.setTotalElements(totalElements);
		return response;
	}

	public WSAuthorResponse loadAuthors(Integer offset, Integer limit) {
		Page<Author> authorPage = authorsRepository.findAll(PageRequest.of(offset, limit));
		WSAuthorResponse response = new WSAuthorResponse();
		if (authorPage != null) {
			response.setAuthors(authorPage.getContent());
			response.setTotalElements(authorPage.getTotalElements());
		}
		return response;
	}

	public WSAuthorResponse loadAuthors(Integer offset, Integer limit, String query) {
		Page<Author> authorPage = authorsRepository.findByFirstNameContainsIgnoreCase(query, PageRequest.of(offset, limit));
		WSAuthorResponse response = new WSAuthorResponse();
		if (authorPage != null) {
			response.setAuthors(authorPage.getContent());
			response.setTotalElements(authorPage.getTotalElements());
		}
		return response;
	}

	private Author updateAuthor(Author src, Author dest) {
		dest.setCity(src.getCity());
		dest.setFirstName(src.getFirstName());
		dest.setLastName(src.getLastName());
		dest.setGender(src.getGender());
		return dest;
	}
}
