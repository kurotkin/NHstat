package com.kurotkin.algoprof;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.SimplemultialgoObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class AlgoProfController {
    private SimplemultialgoObject simplemultialgo;

    public AlgoProfController() {
        try {
            String resultString = Unirest.get("https://api.nicehash.com/api")
                    .queryString("method", "simplemultialgo.info")
                    .asJson()
                    .getBody()
                    .toString();
            simplemultialgo = new Gson().fromJson(resultString, SimplemultialgoObject.class);
        } catch (UnirestException e) {
            log.error("Прибыльность алгоритмов не скачалось");
        }
    }

    public List<AlgoProfModel> getProfAlgoList(){
        List<AlgoProfModel> list = new ArrayList<>();
        simplemultialgo.result.simplemultialgo.forEach( a -> {
            list.add(new AlgoProfModel(a.algo, a.name, new BigDecimal(a.paying), a.port));
        });
        return list;
    }

}
