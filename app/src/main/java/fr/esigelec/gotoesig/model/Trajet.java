package fr.esigelec.gotoesig.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class Trajet implements Serializable {
    Date dateDepart;
    Double latitude;
    Double longitude;
    String transport;
    int retardTolere;
    int nombrePlaces;
    int contribution;
    Boolean autoroute;
    int distance;
    int duree;
    String ownerUid;
    ArrayList<String> usersUid;
    String nomVille;
    String addresseComplete;
    Boolean isVehicule;

    public Trajet(){
        usersUid = new ArrayList<>();
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public int getRetardTolere() {
        return retardTolere;
    }

    public void setRetardTolere(int retardTolere) {
        this.retardTolere = retardTolere;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public Boolean getAutoroute() {
        return autoroute;
    }

    public void setAutoroute(Boolean autoroute) {
        this.autoroute = autoroute;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public ArrayList<String> getUsersUid() {
        return usersUid;
    }

    public void setUsersUid(ArrayList<String> usersUid) {
        this.usersUid = usersUid;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }

    public String getAddresseComplete() {
        return addresseComplete;
    }

    public void setAddresseComplete(String addresseComplete) {
        this.addresseComplete = addresseComplete;
    }

    public Boolean getIsVehicule() {
        return isVehicule;
    }

    public void setIsVehicule(Boolean isVehicule) {
        this.isVehicule = isVehicule;
    }
}
