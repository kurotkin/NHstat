package com.kurotkin.hardware.arduino;

import com.fazecast.jSerialComm.*;

import java.io.PrintWriter;
import java.util.Scanner;

public class Arduino {
    private SerialPort comPort;
    private String portDescription;
    private int baud_rate;

    public Arduino(String portDescription, int baud_rate) {
        this.portDescription = portDescription;
        comPort = SerialPort.getCommPort(this.portDescription);
        this.baud_rate = baud_rate;
        comPort.setBaudRate(this.baud_rate);
    }

    public boolean openConnection(){
        if(comPort.openPort()){
            delay(100);
            return true;
        }
        else {
            System.err.println("Error Connecting. Try Another port");
            return false;
        }
    }

    public void closeConnection() {
        comPort.closePort();
    }


    public String serialRead(){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        String out = "";
        Scanner in = new Scanner(comPort.getInputStream());
        try {
            while(in.hasNext())
                out += (in.next() + "\n");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public String serialRead(int limit){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        String out = "";
        int count = 0;
        Scanner in = new Scanner(comPort.getInputStream());
        try {
            while(in.hasNext()&&count <= limit){
                out += (in.next()+"\n");
                count++;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public void serialWrite(String s){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        delay(5);
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        pout.print(s);
        pout.flush();
    }

    public void serialWriteln(String s){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        delay(5);
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        pout.println(s);
        pout.flush();
    }

    public void serialWrite(String s,int noOfChars, int delay){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        delay(5);
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        for(int i = 0; i < s.length(); i += noOfChars) {
            pout.write(s.substring(i,i+noOfChars));
            pout.flush();
            System.out.println(s.substring(i, i + noOfChars));
            delay(delay);
        }
        pout.write(noOfChars);
        pout.flush();

    }

    public void serialWrite(char c){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        delay(5);
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        pout.write(c);
        pout.flush();
    }

    public void serialWrite(char c, int delay){
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        delay(5);
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        pout.write(c);
        pout.flush();
        delay(delay);
    }

    private void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch(Exception e) {}
    }
}
