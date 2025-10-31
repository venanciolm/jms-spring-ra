package com.farmafene.jms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources( //
		value = { //
				@PropertySource(//
						value = { //
								"classpath:values.properties"//
						}, //
						ignoreResourceNotFound = true //
				) //
		} //
) //
public class CargaProps {
}
