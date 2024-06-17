# A Simple GeoHash Java Implement

## First step：Encode latitude and longitude as binary string

The latitude range is `[-90, 90]`, dividing the interval from the middle, coding it `0` if the current latitude is on the left side of the interval, encoding it as `1` on the right side of the interval, and continuing to repeat the above operation in the new interval until the accuracy meets the requirements.

The longitude range is `[-180, 180]`, and similar encoding is performed.

such as: `(-20.12, 100.13)`, GeoHash length: `12`

- latitude binary：`011000110110001010001000000101`
- longitude binary：`110001110011010000011100001101`

```java
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
```

## Second step: Combine latitude binary and longitude binary

The binary string of latitude and longitude is cross-combined, with the longitude in the even digit (including 0) and the latitude in the odd digit.

such as: `101101000010111100011110001001000100001011100000000010110011`.

```java
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
```

## Third step: Encode binary string as Base32

Converts a binary string to a decimal number every 5 bits, and encodes it into Base32 encoding.

such as: `qhrjw922w05m`

```java
private final static char[] BASE32_DICT = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e',
    'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

private String toBase32(String binCode) {
    StringBuilder sb = new StringBuilder(codeLength);
    for (int i = 0; i < binCode.length(); i += 5) {
        String s = binCode.substring(i, Math.min(i + 5, binCode.length()));
        sb.append(BASE32_DICT[Integer.parseInt(s, 2)]);
    }
    return sb.toString();
}
```