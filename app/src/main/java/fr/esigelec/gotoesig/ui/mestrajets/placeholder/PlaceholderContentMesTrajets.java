package fr.esigelec.gotoesig.ui.mestrajets.placeholder;

import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContentMesTrajets {

    public static final List<PlaceholderMesTrajetsItem> ITEMS = new ArrayList<PlaceholderMesTrajetsItem>();

    public static class PlaceholderMesTrajetsItem {
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
        public final Boolean isVehicule;

        public PlaceholderMesTrajetsItem(String nomVille, String addresseComplete, String transportText, Date date, String placesNb, String prix, Boolean checkboxAutoroute, Integer nbPlaceTaken, Double latitude, Double longitude, String trajetId, Boolean isVehicule) {
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
            this.isVehicule = isVehicule;
        }
    }
}