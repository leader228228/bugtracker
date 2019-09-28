package ua.edu.sumdu.nc.converters.create.issues;

import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;

public class CreateIssueRequestConverter implements CreateRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return CreateIssueRequest.class;
    }
}
