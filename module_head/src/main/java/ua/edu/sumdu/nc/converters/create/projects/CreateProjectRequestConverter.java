package ua.edu.sumdu.nc.converters.create.projects;

import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.create.projects.CreateProjectRequest;

public class CreateProjectRequestConverter implements CreateRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return CreateProjectRequest.class;
    }
}
