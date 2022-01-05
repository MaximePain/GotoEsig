package fr.esigelec.gotoesig.ui.cherchertrajets.placeholder;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContentChercherTrajets {

    public static final List<PlaceholderChercherTrajetsItem> ITEMS = new ArrayList<PlaceholderChercherTrajetsItem>();

    public static final Map<String, PlaceholderChercherTrajetsItem> ITEM_MAP = new HashMap<String, PlaceholderChercherTrajetsItem>();

    private static final int COUNT = 25;


    private static void addItem(PlaceholderChercherTrajetsItem item) {
        ITEMS.add(item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class PlaceholderChercherTrajetsItem {
        public final String nomVille;
        public final String addresseComplete;
        public final String transportText;
        public final Date date;
        public final String placesNb;
        public final String prix;
        public final Boolean checkboxAutoroute;
        public final Integer nbPlaceTaken;
        public final Double latitude;
        public final Double longitude;
        public final String trajetId;

        public PlaceholderChercherTrajetsItem(String nomVille, String addresseComplete, String transportText, Date date, String placesNb, String prix, Boolean checkboxAutoroute, Integer nbPlaceTaken, Double latitude, Double longitude, String trajetId) {
            this.nomVille = nomVille;
            this.addresseComplete = addresseComplete;
            this.transportText = transportText;
            this.date = date;
            this.placesNb = placesNb;
            this.prix = prix;
            this.checkboxAutoroute = checkboxAutoroute;
            this.nbPlaceTaken = nbPlaceTaken;
            this.latitude = latitude;
            this.longitude = longitude;
            this.trajetId = trajetId;
        }
    }
}