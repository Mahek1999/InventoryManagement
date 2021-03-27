package com.higradius.main.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.Driver;

public class JdbcConnectionFactory {

	private static final String DATABASE_CONFIG_FILENAME = "dbconfig.properties";

	// Load the dbconfig.properties file.
	public static Properties loadPropertiesFile() throws Exception {

		Properties dbProperties = new Properties();
//		InputStream in = new FileInputStream("");
		InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(DATABASE_CONFIG_FILENAME);
		dbProperties.load(inStream);
		inStream.close();
		return dbProperties;
	}

	public static Connection getConnection() {
		try {

			Properties dbProperties = loadPropertiesFile();
//			Configure the database
			final String URL = dbProperties.getProperty("db.url");
			final String USER = dbProperties.getProperty("db.username");
			final String PASSWORD = dbProperties.getProperty("db.password");
			Class.forName("com.mysql.cj.jdbc.Driver");
			DriverManager.registerDriver(new Driver());
			System.out.println("Connection Established...");
			return DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("MySql Driver Not found");
		} catch (SQLException ex) {
			throw new RuntimeException("Error connecting to the database", ex);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error!");
		}

	}
}
