package ua.edu.sumdu.nc.converters.update.issues;

import ua.edu.sumdu.nc.converters.update.UpdateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.update.issues.UpdateIssueRequest;

public class IssueUpdateRequestConverter implements UpdateRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return UpdateIssueRequest.class;
    }
}
