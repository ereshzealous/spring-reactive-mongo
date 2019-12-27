package com.eresh.spring.config;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/

@Component("dateTimeProvider")
public class CustomDateTimeProvider implements DateTimeProvider {

	@Override
	public Optional<TemporalAccessor> getNow() {
		return Optional.of(ZonedDateTime.now());
	}
}
