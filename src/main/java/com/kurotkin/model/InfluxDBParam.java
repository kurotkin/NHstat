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

    public InfluxDBParam() {
    }

    public InfluxDBParam withUrl(String influxDBUrl) {
        InfluxDBUrl = influxDBUrl;
        return this;
    }

    public InfluxDBParam withDBName(String influxDBdbName) {
        InfluxDBdbName = influxDBdbName;
        return this;
    }

    public InfluxDBParam withDBUser(String influxDBUser) {
        InfluxDBUser = influxDBUser;
        return this;
    }

    public InfluxDBParam withDBPass(String influxDBPass) {
        InfluxDBPass = influxDBPass;
        return this;
    }
}
