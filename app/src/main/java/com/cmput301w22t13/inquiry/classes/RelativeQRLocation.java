package com.cmput301w22t13.inquiry.classes;

/**
 * A class to represent QR codes with a relative location
 */
public class RelativeQRLocation implements Comparable<RelativeQRLocation> {
    private final QRCode qr;
    private final double dist;

    public RelativeQRLocation(QRCode qr, double dist) {
        this.dist = dist;
        this.qr = qr;
    }

    /**
     * Get distance
     * @return distance in metres
     */
    public double getDist() {
        return dist;
    }

    /**
     * Get QR code
     * @return QR code
     */
    public QRCode getQr() {
        return qr;
    }

    /**
     * Comparison function via distance
     * @param r second RelativeQRLocation
     * @return comparison int
     */
    @Override
    public int compareTo(RelativeQRLocation r) {
        return Double.compare(this.getDist(), r.getDist());
    }
}
