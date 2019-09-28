package ua.edu.sumdu.nc.converters.create.replies;

import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.create.replies.CreateReplyRequest;

public class CreateReplyRequestConverter implements CreateRequestConverter {
  @Override
  public Class<? extends BTRequest> getConvertedClass() {
    return CreateReplyRequest.class;
  }
}
