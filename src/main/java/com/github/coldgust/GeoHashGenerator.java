package com.github.coldgust;

public class GeoHashGenerator {

    public final static double MIN_LATITUDE = -90;

    public final static double MAX_LATITUDE = 90;

    public final static double MIN_LONGITUDE = -180;

    public final static double MAX_LONGITUDE = 180;

    private final static char[] BASE32_DICT = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private final int codeLength;

    private final int latBits;

    private final int lngBits;

    private final double latUnit;

    private final double lngUnit;

    public GeoHashGenerator(int codeLength) {
        this.codeLength = codeLength;
        this.latBits = codeLength * 5 / 2;
        this.lngBits = codeLength * 5 - this.latBits;
        this.latUnit = (MAX_LATITUDE - MIN_LATITUDE) / (1 << latBits);
        this.lngUnit = (MAX_LONGITUDE - MIN_LONGITUDE) / (1 << lngBits);
    }

    public String encode (double latitude, double longitude) {
        checkLatitudeAndLongitude(latitude, longitude);
        String latBinCode = toBinCode(MIN_LATITUDE, MAX_LATITUDE, latitude, latBits);
        String lngBinCode = toBinCode(MIN_LONGITUDE, MAX_LONGITUDE, longitude, lngBits);
        String binCode = combineBinCode(latBinCode, lngBinCode);
        return toBase32(binCode);
    }

    private String toBase32(String binCode) {
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < binCode.length(); i += 5) {
            String s = binCode.substring(i, Math.min(i + 5, binCode.length()));
            sb.append(BASE32_DICT[Integer.parseInt(s, 2)]);
        }
        return sb.toString();
    }

    private String toBinCode(double low, double high, double value, int bits) {
        StringBuilder sb = new StringBuilder(bits);
        while (sb.length() < bits) {
            double mid = (low + high) / 2;
            if (value < mid) {
                sb.append('0');
                high = mid;
            } else {
                sb.append('1');
                low = mid;
            }
        }
        return sb.toString();
    }

    private String combineBinCode(String latBinCode, String lngBinCode) {
        StringBuilder sb = new StringBuilder(latBits + lngBits);
        int idx = 0;
        while (idx < latBinCode.length()) {
            sb.append(lngBinCode.charAt(idx)).append(latBinCode.charAt(idx));
            ++idx;
        }
        if (idx < lngBinCode.length()) {
            sb.append(lngBinCode.charAt(idx));
        }
        return sb.toString();
    }

    private void checkLatitudeAndLongitude(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE) {
            throw new IllegalArgumentException("Latitude must be >= " + MIN_LATITUDE);
        }
        if (latitude > MAX_LATITUDE) {
            throw new IllegalArgumentException("Latitude must be <= " + MAX_LATITUDE);
        }
        if (longitude < MIN_LONGITUDE) {
            throw new IllegalArgumentException("Longitude must be >= " + MIN_LONGITUDE);
        }
        if (longitude > MAX_LONGITUDE) {
            throw new IllegalArgumentException("Longitude must be <= " + MAX_LONGITUDE);
        }
    }

    /**
     * @return meter
     */
    public double getAccuracy() {
        return CoordinateUtil.calculateDistance(0, 0, latUnit, lngUnit) * 1000;
    }

    public double getLatUnit() {
        return latUnit;
    }

    public double getLngUnit() {
        return lngUnit;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public int getLatBits() {
        return latBits;
    }

    public int getLngBits() {
        return lngBits;
    }

}