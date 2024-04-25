package ru.courses.pars;

public enum RequestType {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");
    private String name;

    RequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
