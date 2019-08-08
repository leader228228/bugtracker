package ua.edu.sumdu.nc.controllers.create.parsers;

public interface InputParser<F,T> {
    /**
     *  Converts an instance of F to an instance of T
     * */
    T parse(F from) throws Exception;
}
