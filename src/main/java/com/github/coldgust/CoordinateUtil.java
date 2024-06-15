package com.github.coldgust;

public class CoordinateUtil {

    public final static double EARTH_RADIUS = 6378.137;

    /**
     * @return kilometers
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lng1);
        double lon2Rad = Math.toRadians(lng2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);

        return Math.sqrt(x * x + y * y) * EARTH_RADIUS;
    }
}
