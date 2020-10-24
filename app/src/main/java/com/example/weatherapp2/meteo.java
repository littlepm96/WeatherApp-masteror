package com.example.weatherapp2;

public class meteo {
    private String data;
    private String meteo;
    private String temp;

    public meteo() {
    }

    public meteo(String data, String meteo,String  temp) {
        this.data = data;
        this.meteo= meteo;
        this.temp= temp;

    }

    public String  getTemp() {
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
}
