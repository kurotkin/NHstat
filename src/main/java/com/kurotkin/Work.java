package com.kurotkin;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Work {
    public static void main(String[] args) throws InterruptedException {
        printComs();
        writeBytes("1.02 4.14 2.3 150");
        Thread.sleep(1000);
        writeBytes("1.02 4.14 2.3 80");
        Thread.sleep(1000);
        writeBytes("1.02 4.14 2.3 150");
        Thread.sleep(1000);
        writeBytes("1.02 4.14 2.3 150");
        Thread.sleep(1000);
    }

    public static void printComs () {
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    public static void writeBytes(String s) {
        SerialPort serialPort = new SerialPort("COM8");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.writeBytes(s.getBytes());
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}
