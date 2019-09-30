package ua.edu.sumdu.nc.converters.search.users;

import ua.edu.sumdu.nc.converters.search.SearchRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.search.users.SearchUsersRequest;

public class UserSearchRequestConverter implements SearchRequestConverter {
    @Override
    public Class<? extends BTRequest> getConvertedClass() {
        return SearchUsersRequest.class;
    }
}
