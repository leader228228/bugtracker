package config;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    private String URL;
    private String user;
    private String password;

    public DatabaseConfig(@Value("${db.connection.url}") String URL,
                          @Value("${db.connection.user}") String user,
                          @Value("${db.connection.password}") String password,
                          @Value("${db.connection.driver}") String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can not load JDBC driver", e);
        }
        this.URL = URL;
        this.user = user;
        this.password = password;
    }

    @Bean
    @Scope(scopeName = "prototype")
    public DataSource DataSource() throws SQLException {
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(URL);
        oracleDataSource.setUser(user);
        oracleDataSource.setPassword(password);
        return oracleDataSource;
    }
}
