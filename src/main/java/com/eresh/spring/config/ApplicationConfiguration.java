package com.eresh.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.nio.charset.Charset;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Created By Gorantla, Eresh on 12/Dec/2019
 **/
@Configuration
public class ApplicationConfiguration implements WebFluxConfigurer {

	@Bean(name = "jsonMapper")
	@Primary
	public ObjectMapper jsonMapper() {
		return new CustomObjectMapper();
	}

	/*@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter(jsonMapper()));
	}*/

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer codecs) {
		codecs.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(jsonMapper()));
		codecs.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(jsonMapper()));
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		OptionalInt index = IntStream.range(0, restTemplate.getMessageConverters()
		                                                   .size())
		                             .filter(i -> restTemplate.getMessageConverters()
		                                                      .get(i) instanceof MappingJackson2HttpMessageConverter)
		                             .findFirst();

		if (index.isPresent()) {
			restTemplate.getMessageConverters()
			            .set(index.getAsInt(), new MappingJackson2HttpMessageConverter(jsonMapper()));
		}
		restTemplate.getMessageConverters()
		            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
}
