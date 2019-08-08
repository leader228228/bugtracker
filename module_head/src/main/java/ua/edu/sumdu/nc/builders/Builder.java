package ua.edu.sumdu.nc.builders;

import ua.edu.sumdu.nc.exceptions.BuilderException;

public abstract class Builder<T> {
    public abstract T build() throws BuilderException;
}
