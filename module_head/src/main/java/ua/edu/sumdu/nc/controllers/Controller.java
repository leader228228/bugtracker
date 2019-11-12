package ua.edu.sumdu.nc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.Entity;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Controller {
    protected static final Logger logger = Logger.getRootLogger();



    /*protected String arrayToString(int [] arr) {
        if (arr == null || arr.length == 0) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : arr) {
            stringBuilder.append(i).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    protected String arrayToString(long [] arr) {
        if (arr == null || arr.length == 0) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (long l : arr) {
            stringBuilder.append(l).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }*/

    protected Entity readEntity(ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException();
    }

    protected Collection<? extends Entity> executeAndParse(PreparedStatement preparedStatement) throws SQLException {
        Collection<Entity> entities = new LinkedList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                entities.add(readEntity(resultSet));
            }
            return entities;
        }
    }
}
