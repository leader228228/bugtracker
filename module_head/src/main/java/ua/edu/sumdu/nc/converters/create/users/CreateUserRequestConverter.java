package ua.edu.sumdu.nc.converters.create.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.create.users.CreateUserRequest;

import java.io.IOException;
import java.io.UncheckedIOException;

public class CreateUserRequestConverter implements CreateRequestConverter {
  @Override
  public CreateUserRequest convert(String source) {
    try {
      return new ObjectMapper().readValue(source, CreateUserRequest.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
