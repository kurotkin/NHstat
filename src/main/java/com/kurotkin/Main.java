package com.kurotkin;

import com.google.gson.Gson;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.kurotkin.api.entities.ResponseProvider;
import com.kurotkin.api.entities.ResponseProviderWithError;
import com.kurotkin.hardware.arduino.Arduino;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Locale;


public class Main {
    public static void main(String[] args) throws UnirestException, InterruptedException {

        Arduino arduino = new Arduino("COM3", 9600);

        while (true){
            boolean connected = arduino.openConnection();
            if(connected) System.out.print("connected: ");

            HttpResponse<JsonNode>  f = Unirest.get("https://api.nicehash.com/api")
                    .queryString("method", "stats.provider.ex")
                    .queryString("addr", "3MocyP1djGcdvyg693nMhsQtNo3AL7Uve1")
                    .asJson();
            HttpResponse<JsonNode>  coinHttpResponse = Unirest.get("https://api.coinmarketcap.com/v1/ticker/bitcoin/")
                    .queryString("convert", "RUB")
                    .asJson();

            double profitability = 0.0;
            double balance = 0.0;
            double speed = 0.0;
            int algo = 0;

            goBack:
            try {
                ResponseProvider result = new Gson().fromJson(f.getBody().toString(), ResponseProvider.class);
                for(int i = 0; i < result.result.current.size(); i ++){
                    profitability += result.result.current.get(i).profitability;

                    String s = result.result.current.get(i).data.toString();
                    s = s.substring(1, s.length() - 1);
                    String aS[] = s.split(", ");

                    if(aS[0].length() > 2) {
                        speed += Double.parseDouble(aS[0].substring(4, aS[0].length() - 1));
                        algo = result.result.current.get(i).algo;
                    }

                    balance += Double.parseDouble(aS[1]);
                }
            } catch (Exception E){
                try {
                    ResponseProviderWithError result = new Gson().fromJson(f.getBody().toString(), ResponseProviderWithError.class);
                    String aS[] = result.result.error.split(" ");
                    int timeDelay = Integer.parseInt(aS[13]);
                    System.out.println("Delay " + timeDelay + " sec");
                    Thread.sleep(timeDelay * 1000);
                    break goBack;
                } catch (Exception Er){
                    System.err.println(f.getBody().toString());
                    E.printStackTrace();
                    Er.printStackTrace();
                }
            }


            double rb = 0.0;
            try {
                String coinResultString = "{result:" + coinHttpResponse.getBody().toString() + "}";
                ResponseBitcoinRub coinResult = new Gson().fromJson(coinResultString, ResponseBitcoinRub.class);
                String price_rub = coinResult.result.get(0).price_rub;
                rb = Double.parseDouble(price_rub);
            } catch (Exception E){
                E.printStackTrace();
            }

            profitability *= rb;
            balance *= rb;

            String toArduino = "";
            toArduino += String.format(Locale.US, "%.2f", profitability) + " ";
            toArduino += String.format(Locale.US, "%.2f", balance) + " ";
            toArduino += String.format(Locale.US, "%.2f",speed) + " ";
            toArduino += algo + "\n";
            arduino.serialWriteln(toArduino);
            System.out.print(toArduino);
            arduino.closeConnection();
            Thread.sleep(60000);
        }

    }


}
