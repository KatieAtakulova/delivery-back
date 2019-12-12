package ua.nure.delivery.service.convert;

import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExtendedConversionServiceImpl extends DefaultConversionService implements ExtendedConversionService {

    public <T> List<T> convertAll(Collection<?> source, Class<T> targetClass) {
        return source.stream()
                .map(o -> convert(o, targetClass))
                .collect(Collectors.toList());
    }
}
