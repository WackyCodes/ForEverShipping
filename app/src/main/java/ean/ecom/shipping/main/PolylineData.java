package ean.ecom.shipping.main;

import com.google.android.gms.maps.model.Polyline;
import com.google.maps.model.DirectionsLeg;

/**
 * Created by Shailendra (WackyCodes) on 06/10/2020 12:22
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class PolylineData {

    private Polyline polyline;
    private DirectionsLeg leg;

    public PolylineData(Polyline polyline, DirectionsLeg leg) {
        this.polyline = polyline;
        this.leg = leg;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public DirectionsLeg getLeg() {
        return leg;
    }

    public void setLeg(DirectionsLeg leg) {
        this.leg = leg;
    }

    @Override
    public String toString() {
        return "PolylineData{" +
                "polyline=" + polyline +
                ", leg=" + leg +
                '}';
    }
}
