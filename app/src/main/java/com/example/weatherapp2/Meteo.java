package com.example.weatherapp2;

public class Meteo {
    private String data;
    private String meteo;
    private String temp;
    private long id;

    public Meteo() {
    }

    public Meteo(String data, String meteo, String temp) {
        this.data = data;
        this.meteo = meteo;
        this.temp = temp;

    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
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

    public void setId(long res) {
        this.id = res;
    }

    public String getId() {
        return String.valueOf(id);

    }
}
