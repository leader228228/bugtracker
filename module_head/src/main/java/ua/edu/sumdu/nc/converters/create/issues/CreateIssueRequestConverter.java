package ua.edu.sumdu.nc.converters.create.issues;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.sumdu.nc.converters.create.CreateRequestConverter;
import ua.edu.sumdu.nc.validation.create.CreateRequest;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;

import java.io.IOException;
import java.io.UncheckedIOException;

public class CreateIssueRequestConverter implements CreateRequestConverter {
    @Override
    public CreateRequest convert(String source) {
        if (true) {
            throw new RuntimeException(source);
        }
        try {
            return new ObjectMapper().readValue(source, CreateIssueRequest.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
