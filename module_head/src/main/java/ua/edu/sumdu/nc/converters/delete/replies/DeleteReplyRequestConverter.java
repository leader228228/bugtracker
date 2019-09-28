package ua.edu.sumdu.nc.converters.delete.replies;

import ua.edu.sumdu.nc.converters.delete.DeleteRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.delete.replies.DeleteReplyRequest;

public class DeleteReplyRequestConverter implements DeleteRequestConverter {
  @Override
  public Class<? extends BTRequest> getConvertedClass() {
    return DeleteReplyRequest.class;
  }
}
