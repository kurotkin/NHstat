package com.kurotkin;

import com.kurotkin.controller.NicehashController;
import com.kurotkin.controller.Rate;
import com.mashape.unirest.http.exceptions.UnirestException;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.math.BigDecimal;
import java.util.Locale;


public class Main {
    public static void main(String[] args) throws UnirestException, InterruptedException {
        while (true){
            Rate rate = new Rate();
            NicehashController nicehashController = new NicehashController("3MocyP1djGcdvyg693nMhsQtNo3AL7Uve1", rate);

            BigDecimal profitability = nicehashController.getProfitability();
            BigDecimal balance = nicehashController.getBalance();
            BigDecimal speed = nicehashController.getSpeed();
            int algo = nicehashController.getAlgo();

            String toArduino = createStringToSerial(profitability, balance, speed, algo);
            writeBytes(toArduino);
            System.out.println(toArduino);
            Thread.sleep(60000);
        }

    }
    public static String createStringToSerial(BigDecimal profitability, BigDecimal balance, BigDecimal speed, int algo) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(Locale.US, "%.2f", profitability));
        builder.append(" ");
        builder.append(String.format(Locale.US, "%.2f", balance));
        builder.append(" ");
        builder.append(String.format(Locale.US, "%.2f", speed));
        builder.append(" ");
        builder.append(algo);
        builder.append("\n");
        return builder.toString();
    }

    public static void writeBytes(String s) {
        SerialPort serialPort = new SerialPort("/dev/cu.usbmodem1411");
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            System.out.println("Open port " + serialPort.getPortName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] bytes = s.getBytes();
            serialPort.writeBytes(bytes);
            serialPort.closePort();
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}
