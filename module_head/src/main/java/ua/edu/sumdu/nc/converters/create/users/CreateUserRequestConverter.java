package ua.edu.sumdu.nc.converters.create.users;

import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.create.users.CreateUserRequest;

public class CreateUserRequestConverter implements CreateRequestConverter {
  @Override
  public Class<? extends BTRequest> getConvertedClass() {
    return CreateUserRequest.class;
  }
}
