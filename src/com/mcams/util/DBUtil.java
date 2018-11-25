package com.mcams.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mcams.ui.MCAMS;

public class DBUtil {
	static Logger myLogger =  Logger.getLogger(DBUtil.class.getName());
	public static Connection getConnection() {
		PropertyConfigurator.configure(MCAMS.path);
		Properties properties = new Properties();
		InputStream fin;
		String resource;
		try {
//			resource = "database.properties";
			resource = "db.properties";
    		fin = DBUtil.class.getClassLoader().getResourceAsStream(resource);
			properties.load(fin);
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			System.out.println("Connecting to database...\nPlease Wait...");
			Connection conn = DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),properties.getProperty("password"));
			if(conn!=null) {
				System.out.println("CONNECTED!");
				myLogger.info("Connection established");
			}
			else {
				return null;
			}
			return conn;
		} catch (Exception e) {
			return null;
		}
	}
}
