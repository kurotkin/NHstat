package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.Simplemultialgo;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.SimplemultialgoObject;
import com.kurotkin.model.NicehashSimplemultialgo;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public List<NicehashSimplemultialgo> getProfAlgoList(){
        List<NicehashSimplemultialgo> list = new ArrayList<>();
        simplemultialgo.result.simplemultialgo.forEach(algo -> {
            NicehashSimplemultialgo nsa = new NicehashSimplemultialgo(new BigDecimal(algo.paying),algo.port, algo.name, algo.algo);
            list.add(nsa);
        });
        return list;
    }

}
