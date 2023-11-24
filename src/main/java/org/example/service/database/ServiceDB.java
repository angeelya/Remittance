package org.example.service.database;

import org.example.exception.DatabaseServiceException;
import org.example.model.Report;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceDB {
    private Connection connection;
    private Statement statement;
    private final String DRIVER = "com.mysql.cj.jdbc.Driver", DB_NAME = "remittance",
            DB_URL = "jdbc:mysql://localhost/", NAME = "root", PASS = "mysql";

    public ServiceDB() throws DatabaseServiceException {
        connectionDatabase();
    }

    private void createDatabase() throws DatabaseServiceException {
        try {
            Connection connWithoutDB = DriverManager.getConnection(DB_URL, NAME, PASS);
            Statement stmWithoutDB = connWithoutDB.createStatement();
            stmWithoutDB.executeUpdate("create database IF NOT EXISTS " + DB_NAME);
            stmWithoutDB.close();
            connWithoutDB.close();
            connection = DriverManager.getConnection(DB_URL + DB_NAME, NAME, PASS);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new DatabaseServiceException("Database isn't created. "+e.getMessage());
        }
    }

    private void createTable() throws DatabaseServiceException {
        try {
            statement.executeUpdate("create table if not exists report (id bigint primary key auto_increment ,from_account varchar(11), " +
                    "to_account varchar(11), sum_remittance decimal(65,2), " +
                    "name_file varchar(200), status varchar(5000), date_time datetime)");
        } catch (SQLException e) {
            throw new DatabaseServiceException("Table isn't created. "+e.getMessage());
        }
    }

    private void connectionDatabase() throws DatabaseServiceException {
        try {
            Class.forName(DRIVER).getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(DB_URL + DB_NAME, NAME, PASS);
            statement = connection.createStatement();
        } catch (SQLException e) {
            createDatabase();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException
                 | NoSuchMethodException e) {
            throw new DatabaseServiceException(e.getMessage());
        }
    }

    private boolean hasTableReport() {
        try {
            statement.execute("select * from report");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean insertReport(Report report) throws DatabaseServiceException {
        if (!hasTableReport()) createTable();
        try {
            if (statement.executeUpdate("insert into report (from_account ,to_account , sum_remittance," +
                    "name_file, status, date_time) value ('" + report.getFromAccount() + "','" + report.getToAccount() +
                    "'," + report.getSumRemittance().toString() + ",'" + report.getNameInputFile() + "','" + report.getStatusRemittance() +
                    "','" + report.getDataTime() + "')") > 0) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println();
            return false;
        }
    }

}
