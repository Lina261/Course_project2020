package Data;

import java.sql.*;

public class WorkWithDB implements DB{
    private static WorkWithDB instance;
    private String drivName = "com.mysql.jdbc.Driver";

    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;

    private WorkWithDB(String url, String login, String password) throws ClassNotFoundException, SQLException {
        try {

            connection = DriverManager.getConnection(url, login, password);

            stmt = connection.createStatement();

            this.stmt.execute("set character set utf8");
            this.stmt.execute("set names utf8");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    public static WorkWithDB getInstance(String url, String login, String password) {
        if (instance == null) {
            try {
                instance = new WorkWithDB(url, login, password);
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println(classNotFoundException);
            }
        }
        return instance;
    }

    @Override
    public void insert(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void delete(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void update(String sql) {
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public ResultSet select(String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return rs;
    }

    @Override
    public void close() {
        try {
            connection.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}


