package com.eresh.spring.config;

import com.mongodb.MongoClient;
import com.mongodb.lang.Nullable;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@Configuration
@EnableMongoAuditing
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private int port;

	@Override
	public MongoClient mongoClient() {
		return new MongoClient(host, port);
	}

	@Override
	protected String getDatabaseName() {
		return database;
	}

	@Bean
	public MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		/*converters.add(new DateToZonedDateTimeConverter());
		converters.add(new ZonedDateTimeToDateConverter());*/
		converters.add(new ZonedDateTimeToDocumentConverter());
		converters.add(new DocumentToZonedDateTimeConverter());
		return new MongoCustomConversions(converters);
	}

	class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

		@Override
		public ZonedDateTime convert(Date source) {
			return source == null ? null :
					ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
		}
	}

	class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

		@Override
		public Date convert(ZonedDateTime source) {
			return source == null ? null : Date.from(source.toInstant());
		}
	}

	@WritingConverter
	public class ZonedDateTimeToDocumentConverter implements Converter<ZonedDateTime, Document> {
		static final String DATE_TIME = "dateTime";
		static final String ZONE = "zone";

		@Override
		public Document convert(@Nullable ZonedDateTime zonedDateTime) {
			if (zonedDateTime == null) return null;

			Document document = new Document();
			document.put(DATE_TIME, Date.from(zonedDateTime.toInstant()));
			document.put(ZONE, zonedDateTime.getZone().getId());
			document.put("offset", zonedDateTime.getOffset().toString());
			return document;
		}
	}

	@ReadingConverter
	public class DocumentToZonedDateTimeConverter implements Converter<Document, ZonedDateTime> {
		static final String DATE_TIME = "dateTime";
		static final String ZONE = "zone";
		@Override
		public ZonedDateTime convert(@Nullable Document document) {
			if (document == null) return null;

			Date dateTime = document.getDate(DATE_TIME);
			String zoneId = document.getString(ZONE);
			ZoneId zone = ZoneId.of(zoneId);

			return ZonedDateTime.ofInstant(dateTime.toInstant(), zone);
		}
	}
}
