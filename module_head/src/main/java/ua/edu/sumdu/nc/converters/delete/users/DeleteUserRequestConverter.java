package ua.edu.sumdu.nc.converters.delete.users;

import ua.edu.sumdu.nc.converters.delete.DeleteRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.delete.users.DeleteUserRequest;

public class DeleteUserRequestConverter implements DeleteRequestConverter {
  @Override
  public Class<? extends BTRequest> getConvertedClass() {
    return DeleteUserRequest.class;
  }
}
