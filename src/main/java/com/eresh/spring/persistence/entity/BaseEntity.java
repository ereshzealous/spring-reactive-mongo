package com.eresh.spring.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity {

	@Id
	private String id;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Date createdDate;

	@LastModifiedBy
	private String updatedBy;

	@LastModifiedDate
	private Date updatedDate;

	@Version
	private Long version;

	private Boolean delete = Boolean.FALSE;

}
