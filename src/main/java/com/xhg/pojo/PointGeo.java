package com.xhg.pojo;


import lombok.Data;


@Data
public class PointGeo {

    private double lon;

    private double lat;

    public PointGeo(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
