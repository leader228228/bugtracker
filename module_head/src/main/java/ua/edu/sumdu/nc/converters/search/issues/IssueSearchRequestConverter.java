package ua.edu.sumdu.nc.converters.search.issues;

import ua.edu.sumdu.nc.converters.search.SearchRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.search.issues.SearchIssuesRequest;

public class IssueSearchRequestConverter implements SearchRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return SearchIssuesRequest.class;
    }
}
