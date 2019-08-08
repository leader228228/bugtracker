package ua.edu.sumdu.nc.jobs;

import java.util.Map;

public interface Job {
    /**
     * @param           map input parameters
     *
     * @return          results of operation or {@code null} if actions do not have any result
     * */
    Map execute(Map map) throws Exception;
}
