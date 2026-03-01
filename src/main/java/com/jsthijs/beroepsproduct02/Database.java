package com.jsthijs.beroepsproduct02;

import java.sql.*;

public class Database {

    private String host;
    private String port;
    private String user;
    private String passwd;
    private String dbname;

    private Connection conn;
    private Statement stmt;

    public Database(String host, String port, String user, String passwd, String dbname) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
        this.dbname = dbname;

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname , user, passwd);
            this.stmt = this.conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Database(String host, String user, String passwd, String dbname) {
        this(host, "3306", user, passwd, dbname);
    }

    public Database(String host, String user, String dbname) {
        this(host, "3306", user, "", dbname);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public void executeUpdate(String query) throws SQLException {
        stmt.executeUpdate(query);
    }
}


