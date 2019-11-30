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
    //@Value("${db.connection.url}")
    private String URL;
    //@Value("${db.connection.user}")
    private String user;
    //@Value("${db.connection.password}")
    private String password;
    //@Value("${db.connection.driver}")
    private Class JDBCDriver;

    public DatabaseConfig(
        @Value("${db.connection.url}") String URL,
        @Value("${db.connection.user}") String user,
        @Value("${db.connection.password}") String password,
        @Value("${db.connection.driver}") String driverClass) throws ClassNotFoundException {
        this.URL = URL;
        this.user = user;
        this.password = password;
        JDBCDriver = Class.forName(driverClass);
    }

    /*{
        try {
            JDBCDriver = Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can not load JDBC driver", e);
        }
    }*/



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
