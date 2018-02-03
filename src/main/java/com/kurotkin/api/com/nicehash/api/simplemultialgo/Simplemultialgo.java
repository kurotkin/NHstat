package com.kurotkin.api.com.nicehash.api.simplemultialgo;

public class Simplemultialgo {
    public String paying;
    public int port;
    public String name;
    public int algo;

    public double getPaying() {
        return Double.parseDouble(paying);
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public int getAlgo() {
        return algo;
    }
}
