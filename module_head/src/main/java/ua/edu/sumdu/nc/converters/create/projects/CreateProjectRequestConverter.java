package ua.edu.sumdu.nc.converters.create.projects;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.create.projects.CreateProjectRequest;

import java.io.IOException;
import java.io.UncheckedIOException;

public class CreateProjectRequestConverter implements CreateRequestConverter {
    @Override
    public BTRequest convert(String source) {
        try {
            return new ObjectMapper().readValue(source, CreateProjectRequest.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
