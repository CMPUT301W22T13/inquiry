package com.cmput301w22t13.inquiry.classes;

public class RelativeQRLocation implements Comparable<RelativeQRLocation> {
    private final QRCode qr;
    private final double dist;

    public RelativeQRLocation(QRCode qr, double dist) {
        this.dist = dist;
        this.qr = qr;
    }

    public double getDist() {
        return dist;
    }

    public QRCode getQr() {
        return qr;
    }

    @Override
    public int compareTo(RelativeQRLocation r) {
        return Double.compare(this.getDist(), r.getDist());
    }
}
