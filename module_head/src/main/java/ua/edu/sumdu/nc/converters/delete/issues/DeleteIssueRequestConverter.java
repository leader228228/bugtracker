package ua.edu.sumdu.nc.converters.delete.issues;

import ua.edu.sumdu.nc.converters.delete.DeleteRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.delete.issues.DeleteIssueRequest;

public class DeleteIssueRequestConverter implements DeleteRequestConverter {
  @Override
  public Class<? extends BTRequest> getConvertedClass() {
    return DeleteIssueRequest.class;
  }
}
