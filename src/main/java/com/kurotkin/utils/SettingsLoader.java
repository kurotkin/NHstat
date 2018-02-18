package com.kurotkin.utils;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.kurotkin.model.InfluxDBParam;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class SettingsLoader {

    private String Nicehash;
    private String NicehashId;
    private String NicehashKey;
    private InfluxDBParam inflParam;

    public SettingsLoader(String fileName) {
        try {
            YamlReader reader = new YamlReader(new FileReader(fileName));
            Object object = reader.read();
            Map map = (Map)object;
            Nicehash = map.get("Nicehash").toString();
            NicehashId = map.get("NicehashId").toString();
            NicehashKey = map.get("NicehashKeyOnlyRead").toString();
            inflParam = new InfluxDBParam().withUrl(map.get("InfluxDBUrl").toString())
                    .withDBName(map.get("InfluxDBdbName").toString())
                    .withDBUser(map.get("InfluxDBUser").toString())
                    .withDBPass(map.get("InfluxDBPass").toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }

    public String getNicehash() {
        return Nicehash;
    }

    public String getNicehashId() {
        return NicehashId;
    }

    public String getNicehashKey() {
        return NicehashKey;
    }

    public InfluxDBParam getInflParam() {
        return inflParam;
    }

    @Override
    public String toString() {
        return "SettingsLoader{\n" +
                "\tNicehash='" + Nicehash + '\'' +
                ",\tNicehashId='" + NicehashId + '\'' +
                ",\tNicehashKey='" + NicehashKey + '\'' +
                ",\tinflParam=" + inflParam +
                '}';
    }


}
