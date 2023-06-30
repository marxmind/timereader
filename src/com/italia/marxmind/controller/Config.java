package com.italia.marxmind.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Data;

@Data
public class Config  {
	
	private static volatile Config conf;
	private String serialPortName;
	private int packetSizeByte;
	private String dateBaseName;
	private String password;
	private String userName;
	private String dbPort;
	private String ssl;
	private String dbDriver;
	private String dbTimeZone;
	private String dbURL;
	
	private Config() {
        System.out.println("initializing config information...");
    }
    
    public static Config getInstance() {
        if (Config.conf == null) {
            synchronized (Config.class) {
                if (Config.conf == null) {
                    (Config.conf = new Config()).readConf();
                    System.out.println("reading configuration information");
                }
            }
            
        }
        return Config.conf;
    }
	
	private void readConf() {
        Properties prop = new Properties();
        InputStream propertiesStream = Config.class.getResourceAsStream("/prop.properties");

        if (propertiesStream != null) {
            try {
            	prop.load(propertiesStream);
            	Config.conf.setSerialPortName(prop.getProperty("SERIAL_PORT_NAME"));
            	Config.conf.setPacketSizeByte(Integer.valueOf(prop.getProperty("PACKET_SIZE_IN_BYTES").trim()));
                String u_name = SecureChar.decode(prop.getProperty("DATABASE_UNAME"));
                u_name = u_name.replaceAll("mark", "");
                u_name = u_name.replaceAll("rivera", "");
                u_name = u_name.replaceAll("italia", "");
                String pword = SecureChar.decode(prop.getProperty("DATABASE_PASSWORD"));
                pword = pword.replaceAll("mark", "");
                pword = pword.replaceAll("rivera", "");
                pword = pword.replaceAll("italia", "");
                Config.conf.setUserName(u_name);
                Config.conf.setPassword(pword);
                Config.conf.setDateBaseName(prop.getProperty("DATABASE_NAME_MAIN"));
                Config.conf.setDbPort(prop.getProperty("DATABASE_PORT"));
                Config.conf.setDbDriver(prop.getProperty("DATABASE_DRIVER"));
                Config.conf.setSsl(prop.getProperty("DATABASE_SSL"));
                Config.conf.setDbTimeZone(prop.getProperty("DATABASE_SERVER_TIME_ZONE"));
                Config.conf.setDbURL(prop.getProperty("DATABASE_URL"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("file not found");
        }
    }
}