package fr.esigelec.gotoesig.ui.evaluertrajet.placeholder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContentEvaluerTrajets {

    public static final List<PlaceholderEvaluerTrajetsItem> ITEMS = new ArrayList<PlaceholderEvaluerTrajetsItem>();

    public static class PlaceholderEvaluerTrajetsItem {
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
        public final Double ratin;

        public PlaceholderEvaluerTrajetsItem(String nomVille, String addresseComplete, String transportText, Date date, String placesNb, String prix, Boolean checkboxAutoroute, Integer nbPlaceTaken, Double latitude, Double longitude, String trajetId, Boolean isVehicule, Double ratin) {
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
            if (ratin == null)
                ratin = 0.0;
            this.ratin = ratin;
        }
    }
}