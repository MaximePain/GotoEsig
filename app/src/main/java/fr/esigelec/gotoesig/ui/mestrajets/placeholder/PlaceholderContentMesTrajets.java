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

    public static final Map<String, PlaceholderMesTrajetsItem> ITEM_MAP = new HashMap<String, PlaceholderMesTrajetsItem>();

    private static final int COUNT = 25;


    private static void addItem(PlaceholderMesTrajetsItem item) {
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

    public static class PlaceholderMesTrajetsItem {
        public final String nomVille;
        public final String addresseComplete;
        public final String transportText;
        public final Date date;
        public final String placesNb;
        public final String prix;
        public final Boolean checkboxAutoroute;

        public PlaceholderMesTrajetsItem(String nomVille, String addresseComplete, String transportText, Date date, String placesNb, String prix, Boolean checkboxAutoroute) {
            this.nomVille = nomVille;
            this.addresseComplete = addresseComplete;
            this.transportText = transportText;
            this.date = date;
            this.placesNb = placesNb;
            this.prix = prix;
            this.checkboxAutoroute = checkboxAutoroute;
        }
    }
}