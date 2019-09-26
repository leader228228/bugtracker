package ua.edu.sumdu.nc.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import ua.edu.sumdu.nc.validation.BTRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestConverterFactory implements ConverterFactory<String, BTRequest> {
    private Map<Class<? extends BTRequest>, Converter<String, BTRequest>> map = new HashMap<>();

    public RequestConverterFactory(Map<Class<? extends BTRequest>, Converter<String, BTRequest>> map) {
        this.map.putAll(map);
    }

    @Override
    public <T extends BTRequest> Converter<String, T> getConverter(Class<T> aClass) {
        return (Converter<String, T>) map.get(aClass);
    }
}
