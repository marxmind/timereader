package com.italia.marxmind.controller;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;

public class ReadIO {

	 public static SerialPort selectedPort;
	
	public static void main(String[] args) {
		
		//Config.getInstance();
		listenToSerialPort();
		
	}
	
	public static void listenToSerialPort() {
		int len = SerialPort.getCommPorts().length;
        SerialPort serialPorts[] = new SerialPort[len];
        serialPorts = SerialPort.getCommPorts();
        
        if(len>0) {
        for (int i = 0; i < len; i++) {

            String portName = serialPorts[i].getDescriptivePortName();
            System.out.println(serialPorts[i].getSystemPortName() + ": " + portName + ": " + i);

            if (portName.contains(Config.getInstance().getSerialPortName())) {
            	selectedPort = serialPorts[i];
            	selectedPort.openPort();
                System.out.println("connected to: " + portName + "[" + i + "]");
                break;
            }
        }

        SerialListener listener = new SerialListener();
        System.out.println("Packet size: " + listener.getPacketSize());
        selectedPort.addDataListener(listener);
        
        }else {
        	System.out.println("No serial port found...");
        }
	}
	
	
}
