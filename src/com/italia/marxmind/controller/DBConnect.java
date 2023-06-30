package com.italia.marxmind.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	
	public static Connection getConnection() {
        Config conf = Config.getInstance();
        Connection conn = null;
        try {
            
            final String driver = conf.getDbDriver();
            Class.forName(driver);
            String db_url = conf.getDbURL();
            final String port = conf.getDbPort();
            final String dbName = conf.getDateBaseName();
            String timezone = "";
            if (conf.getDbTimeZone() != null && !conf.getDbTimeZone().isEmpty()) {
                timezone = String.valueOf(conf.getDbTimeZone()) + "&";
            }
            final String url = String.valueOf(db_url) + ":" + port + "/" + dbName + "?" + timezone + conf.getSsl();
            System.out.println("URL DATA: " + url);
            final String u_name = conf.getUserName();
            final String pword = conf.getPassword();
            conn = DriverManager.getConnection(url, u_name, pword);
            return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void close(final Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
