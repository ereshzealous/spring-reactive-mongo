package com.eresh.spring.persistence.entity;

import lombok.Data;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@Data
@Document(collection = "author")
public class Author extends BaseEntity {

	@TextIndexed
	private String firstName;

	@TextIndexed
	private String lastName;

	private String city;

	private String gender;
}
