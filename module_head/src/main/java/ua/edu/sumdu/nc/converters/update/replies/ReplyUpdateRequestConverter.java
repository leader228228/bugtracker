package ua.edu.sumdu.nc.converters.update.replies;

import ua.edu.sumdu.nc.converters.update.UpdateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.update.replies.UpdateReplyRequest;

public class ReplyUpdateRequestConverter implements UpdateRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return UpdateReplyRequest.class;
    }
}
