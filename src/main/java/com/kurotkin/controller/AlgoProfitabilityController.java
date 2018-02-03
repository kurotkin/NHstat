package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.Simplemultialgo;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.SimplemultialgoObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class AlgoProfitabilityController {

    private SimplemultialgoObject simplemultialgo;

    public AlgoProfitabilityController() {
        try {
            String resultString = Unirest.get("https://api.nicehash.com/api")
                    .queryString("method", "simplemultialgo.info")
                    .asJson()
                    .getBody()
                    .toString();
            simplemultialgo = new Gson().fromJson(resultString, SimplemultialgoObject.class);
        } catch (UnirestException e) {
            System.err.println("Прибыльность алгоритмов не скачалось");
        }
    }

    private Simplemultialgo getMaxPayAlgo(){
        return simplemultialgo.result.simplemultialgo.stream()
                .max(Comparator.comparing(Simplemultialgo::getPaying))
                .orElseThrow(NoSuchElementException::new);
    }

    public String getNameMaxAlgo(){
        return getMaxPayAlgo().name;
    }

    public int getIdMaxAlgo(){
        return getMaxPayAlgo().algo;
    }

}
