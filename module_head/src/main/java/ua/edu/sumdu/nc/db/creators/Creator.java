package ua.edu.sumdu.nc.db.creators;

public interface Creator<T> {
    T create() throws Exception;
}
