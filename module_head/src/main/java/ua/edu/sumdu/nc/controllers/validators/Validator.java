package ua.edu.sumdu.nc.controllers.validators;

public interface Validator <T> {
    boolean validate(T param);
}
