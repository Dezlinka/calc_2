package ru.itis2016;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CalculatorHistoryManager {
    private static CalculatorHistoryManager manager;
    private static Context ctx = null;
    private static DataSource dataSource =null;
    private CalculatorHistoryManager() {
    }

    public static String getSavedMathaction() {
        return savedMathaction;
    }

    public static void setSavedMathaction(String savedMathaction) {
        CalculatorHistoryManager.savedMathaction = savedMathaction;
    }

    private static String savedMathaction;

    public Connection createConnection() {
        Context ctx = null;
        try {
            ctx = new InitialContext();
            return ((DataSource) ctx.lookup("java:comp/env/jdbc/Calculator")).getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Использовать DRIVER и DBURL для создания соединения
        // Рекомендовать реализацию/использование пула соединений
        return null;
    }

    public Connection getConnection() throws SQLException {
        return createConnection();
    }

    public static synchronized CalculatorHistoryManager getManager() {
        if (manager == null) {
            manager = new CalculatorHistoryManager();
        }
        return manager;
    }

    public void addRecord(String mathaction) throws SQLException {
        Connection connect = getConnection();
        PreparedStatement prst = connect.prepareStatement("insert into history " + "(mathaction)" + "values(?)");
        prst.setString(1, mathaction);
        prst.execute();
        connect.close();
    }

    public void removeAllRecords() throws SQLException {
        Connection connect = getConnection();
        PreparedStatement prst = connect.prepareStatement("delete from history");
        prst.execute();
        connect.close();
    }
}
