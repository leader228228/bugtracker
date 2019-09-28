package ua.edu.sumdu.nc.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import ua.edu.sumdu.nc.validation.BTRequest;

import java.io.IOException;
import java.io.UncheckedIOException;

public interface BTConverter extends Converter<String, BTRequest> {
  Class<? extends BTRequest> getConvertedClass();
  default BTRequest convert(String source) {
    try {
      return new ObjectMapper().readValue(source, getConvertedClass());
    } catch (IOException e) {
      // todo logger
      throw new UncheckedIOException(e);
    }
  }
}
