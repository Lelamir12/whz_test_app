package de.whz.mobile.client;

import com.google.android.gms.maps.model.LatLng;

public class TLocation {

    private LatLng latLng;

    private String addressLine1;

    private String addressLine2;

    private String street;

    private String city;

    private String countryName;

    private String adminArea;

    public TLocation() {

    }

    public String getAddress1() {
        return (street == null ? "" : street);
    }

    public String getAddress2() {
        return (city == null ? "" : (city + ",")) + (adminArea == null ? "" : adminArea);
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public TLocation(LatLng latLng, String street, String adminArea) {
        this.latLng = latLng;
        this.street = street;
        this.adminArea = adminArea;
    }

    public static String asLatLongString(LatLng ll) {
        return ll.latitude + "," + ll.longitude;
    }

    public static LatLng parseLatLngString(String ll) {
        String[] llArr = ll.split(",");
        if (llArr.length != 2) {
            throw new IllegalArgumentException("input must be in a form [lat,lon] instead got " + ll);
        }
        return new LatLng(Double.parseDouble(llArr[0]), Double.parseDouble(llArr[1]));
    }

    public String toLatLngSring() {
        return latLng != null ? asLatLongString(latLng) : null;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean hasLatLng() {
        return latLng != null;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public boolean hasStreetAndAdminArea() {
        return street != null && adminArea != null;
    }

    public boolean isValid() {
        return hasLatLng() && hasStreetAndAdminArea();
    }
}
