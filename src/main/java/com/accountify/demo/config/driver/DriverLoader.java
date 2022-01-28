package com.accountify.demo.config.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverLoader {

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, SQLException, InstantiationException, IllegalAccessException {
        testLoadPostgres();
    }

    public static void testLoadPostgres() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        URL u = new URL("jar:file:/home/wagner.morais/tests/postgresql-42.3.1.jar!/");
        String classname = "org.postgresql.Driver";
        URLClassLoader ucl = new URLClassLoader(new URL[]{u});
        Driver d = (Driver) Class.forName(classname, true, ucl).newInstance();
        DriverManager.registerDriver(new DriverShim(d));
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
        String result = executeQuery("select * from lancamento", connection);

        System.out.println(result);
    }

    public static String executeQuery(String sql, Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            StringBuilder sb = new StringBuilder();

            while (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    sb.append(metaData.getColumnLabel(i) + ": " + resultSet.getString(i));
                }
            }
            sb.append("\n");
            return sb.toString();
        } catch (SQLException e) {
            return "erro!";
        }
    }

    static class DriverShim implements Driver {

        private final Driver driver;

        DriverShim(Driver d) {
            this.driver = d;
        }

        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() {
            return null;
        }
    }

}
