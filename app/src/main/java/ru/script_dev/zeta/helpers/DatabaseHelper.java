package ru.script_dev.zeta.helpers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Objects;

/*
executeCreate лучше использовать для создания новых таблиц
или добавления новых столбцов в существующую таблицу.

executeRead лучше использовать для операций,
которые получают данные из таблицы.

executeUpdate лучше использовать для операций,
которые изменяют данные в таблице,
такие как добавление, изменение или удаление записей.

executeDelete лучше использовать
для удаления записей из таблицы
или для удаления самой таблицы.
*/
public class DatabaseHelper {

    private final HikariDataSource dataSource;

    public DatabaseHelper() {
        this("127.0.0.1", "3306", "zeta", "root", "root");
    }

    public DatabaseHelper(String host, String port, String database, String user, String password) {
        Objects.requireNonNull(host, "Host name cannot be null");
        Objects.requireNonNull(port, "Port number cannot be null");
        Objects.requireNonNull(database, "Database name cannot be null");
        Objects.requireNonNull(user, "User name cannot be null");
        Objects.requireNonNull(password, "User password cannot be null");

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(String.format("jdbc:mariadb://%s:%s/%s", host, port, database));
        config.setUsername(user);
        config.setPassword(password);
        config.setMinimumIdle(5);
        config.setIdleTimeout(60000);
        config.setMaximumPoolSize(10);

        this.dataSource = new HikariDataSource(config);
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new RuntimeException("[DataHelper] Error: " + exception.getMessage());
        }
    }

    public boolean executeCreate(String sql, Object... params) {
        Objects.requireNonNull(sql, "SQL query cannot be null");
        for (Object param : params) {
            Objects.requireNonNull(param, "Parameter cannot be null");
        }
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("[DataHelper] Error: " + exception.getMessage());
        }
    }

    public ResultSet executeRead(String sql, Object... params) {
        Objects.requireNonNull(sql, "SQL query cannot be null");
        for (Object param : params) {
            Objects.requireNonNull(param, "Parameter cannot be null");
        }
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException exception) {
            throw new RuntimeException("[DataHelper] Error: " + exception.getMessage());
        }
    }

    public boolean executeUpdate(String sql, Object... params) {
        Objects.requireNonNull(sql, "SQL query cannot be null");
        for (Object param : params) {
            Objects.requireNonNull(param, "Parameter cannot be null");
        }
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("[DataHelper] Error: " + exception.getMessage());
        }
    }

    public boolean executeDelete(String sql, Object... params) {
        Objects.requireNonNull(sql, "SQL query cannot be null");
        for (Object param : params) {
            Objects.requireNonNull(param, "Parameter cannot be null");
        }
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("[DataHelper] Error: " + exception.getMessage());
        }
    }

    public void close() {
        dataSource.close();
    }
}