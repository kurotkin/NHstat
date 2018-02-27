package com.kurotkin.algoprof;

import com.google.gson.Gson;
import com.kurotkin.algoprof.ScheduledTasks;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.Simplemultialgo;
import com.kurotkin.api.com.nicehash.api.simplemultialgo.SimplemultialgoObject;
import com.kurotkin.model.NicehashSimplemultialgo;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

public class AlgoProfController {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
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

    public List<AlgoProf> getProfAlgoList(){
        List<AlgoProf> list = new ArrayList<>();
        simplemultialgo.result.simplemultialgo.forEach( a -> {
            list.add(new AlgoProf(a.algo, new BigDecimal(a.paying), a.port, a.name));
        });
        return list;
    }

}
