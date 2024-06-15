package com.github.coldgust;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GeoHashGeneratorTest {

    @ParameterizedTest
    @CsvSource({"30.26, 120.19, wtmknuxtmbze",
            "-20.12, 100.13, qhrjw922w05m"})
    void encode(double latitude, double longitude, String expected) {
        GeoHashGenerator geoHashGenerator = new GeoHashGenerator(12);
        String code = geoHashGenerator.encode(latitude, longitude);
        Assertions.assertEquals(expected, code);
    }
}