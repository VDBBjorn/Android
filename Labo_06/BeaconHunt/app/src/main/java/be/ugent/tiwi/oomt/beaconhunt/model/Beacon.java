package be.ugent.tiwi.oomt.beaconhunt.model;

import android.media.Image;

/**
 * Created by Sam Leroux on 14/01/15.
 */
public class Beacon {
    private String name;
    private String address;
    private int iconResource;
    private boolean found;
    private int rssi = 0;

    public Beacon(String name, String address, int iconResource, boolean found) {
        this.name = name;
        this.address = address;
        this.iconResource = iconResource;
        this.found = found;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public double getEstimatedDistance(){
        return 0;
    }
}
