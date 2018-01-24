package com.kurotkin;

import com.google.gson.Gson;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.kurotkin.api.entities.ResponseProvider;
import com.kurotkin.api.entities.ResponseProviderWithError;
import com.kurotkin.controller.Nicehash;
import com.kurotkin.controller.Rate;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.math.BigDecimal;
import java.util.Locale;


public class Main {
    public static void main(String[] args) throws UnirestException, InterruptedException {
        while (true){
            Rate rate = new Rate();
            Nicehash nicehash = new Nicehash("3MocyP1djGcdvyg693nMhsQtNo3AL7Uve1");

            BigDecimal profitability = nicehash.getProfitability();
            BigDecimal balance = nicehash.getBalance();
            BigDecimal speed = nicehash.getSpeed();
            int algo = nicehash.getAlgo();


            String toArduino = createStringToSerial(profitability, balance, speed, algo);
            //writeBytes(toArduino);
            System.out.println(rate.getPrice_rub());
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
