package ua.nure.delivery.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import ua.nure.delivery.service.convert.ExtendedConversionService;
import ua.nure.delivery.service.convert.ExtendedConversionServiceImpl;

import java.util.List;

@Configuration
public class ConversionConfig {

    @Bean
    ExtendedConversionService extendedConversionService(List<Converter> converters) {
        ExtendedConversionServiceImpl bean = new ExtendedConversionServiceImpl();
        converters.forEach(bean::addConverter);
        return bean;
    }
}
