package com.kurotkin;

import com.google.gson.Gson;
import com.kurotkin.api.entities.ResponseProvider;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.*;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) throws UnirestException {

        HttpResponse<JsonNode>  f = Unirest.get("https://api.nicehash.com/api")
                .queryString("method", "stats.provider.ex")
                .queryString("addr", "3MocyP1djGcdvyg693nMhsQtNo3AL7Uve1")
                .asJson();

        try {
            ResponseProvider result = new Gson().fromJson(f.getBody().toString(), ResponseProvider.class);
            for(int i = 0; i < result.result.current.size(); i ++){
                System.out.print(i + ": " + result.result.current.get(i).name + " ");
                System.out.print(result.result.current.get(i).profitability + " ");
                System.out.println(result.result.current.get(i).data.toString());


            }
        } catch (Exception E){
            System.out.println("Error ");
            E.printStackTrace();
        }





    }


}
