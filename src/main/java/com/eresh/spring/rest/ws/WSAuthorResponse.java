package com.eresh.spring.rest.ws;

import com.eresh.spring.persistence.entity.Author;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Gorantla, Eresh on 24/Dec/2019
 **/
@Getter
@Setter
@NoArgsConstructor
public class WSAuthorResponse {
	private Long totalElements;
	public List<Author> authors = new ArrayList<>();
}
