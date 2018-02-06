package com.kurotkin.model;

public class InfluxDBParam {
    public String InfluxDBUrl;
    public String InfluxDBdbName;
    public String InfluxDBUser;
    public String InfluxDBPass;

    public InfluxDBParam(String influxDBUrl, String influxDBdbName, String influxDBUser, String influxDBPass) {
        InfluxDBUrl = influxDBUrl;
        InfluxDBdbName = influxDBdbName;
        InfluxDBUser = influxDBUser;
        InfluxDBPass = influxDBPass;
    }
}
