package ua.edu.sumdu.nc.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.io.UncheckedIOException;

public interface BTConverter extends Converter<String, BTRequest> {
  Class<? extends BTRequest> getConvertedClass();
  default BTRequest convert(String source) {
    try {
      return new ObjectMapper().readValue(source, getConvertedClass());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
