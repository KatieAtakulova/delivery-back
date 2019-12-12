package ua.nure.delivery.service.convert;

import org.springframework.core.convert.ConversionService;

import java.util.Collection;
import java.util.List;

public interface ExtendedConversionService extends ConversionService {

    <T> List<T> convertAll(Collection<?> source, Class<T> targetClass);
}
