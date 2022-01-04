package fr.esigelec.gotoesig.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Trajet implements Serializable {
    Date dateDepart;
    LatLng pointDepart;
    String transport;
    int retardTolere;
    int nombrePlaces;
    int contribution;
    Boolean autoroute;
    int distance;
    int duree;
}
