package ua.edu.sumdu.nc.converters.delete.projects;

import ua.edu.sumdu.nc.converters.delete.DeleteRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.delete.projects.DeleteProjectRequest;

public class DeleteProjectRequestConverter implements DeleteRequestConverter {
  @Override
  public Class<? extends BTRequest> getConvertedClass() {
    return DeleteProjectRequest.class;
  }
}
