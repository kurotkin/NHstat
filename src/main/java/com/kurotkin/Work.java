package com.kurotkin;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Work {
    public static void main(String[] args) throws InterruptedException {
        //printComs();
        writeBytes("1.02 4.14 2.3 8\n");
        Thread.sleep(7000);
        writeBytes("1.02 4.14 2.3 0\n");
        Thread.sleep(7000);
        writeBytes("1.02 4.14 2.3 8\n");
        Thread.sleep(7000);
        writeBytes("1.02 4.14 2.3 0\n");
        Thread.sleep(7000);
    }

    public static void printComs () {
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    public static void writeBytes(String s) throws InterruptedException {
        SerialPort serialPort = new SerialPort("/dev/cu.usbmodem1411");
        try {
            serialPort.openPort();
            System.out.println("Open port " + serialPort.getPortName());
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            Thread.sleep(5000);
            serialPort.writeString(s);
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}
