package com.example.weatherapp2;

public class meteo {
    private String data;
    private String meteo;

    public meteo() {
    }

    public meteo(String data, String meteo) {
        this.data = data;
        this.meteo= meteo;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMeteo() {
        return meteo;
    }

    public void setMeteo(String meteo) {
        this.meteo = meteo;
    }
}
