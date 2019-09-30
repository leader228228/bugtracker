package ua.edu.sumdu.nc.converters.search.replies;

import ua.edu.sumdu.nc.converters.search.SearchRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.search.replies.SearchRepliesRequest;

public class ReplySearchRequestConverter implements SearchRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return SearchRepliesRequest.class;
    }
}
