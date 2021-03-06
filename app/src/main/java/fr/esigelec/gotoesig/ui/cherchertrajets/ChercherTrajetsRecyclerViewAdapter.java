package fr.esigelec.gotoesig.ui.cherchertrajets;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import fr.esigelec.gotoesig.R;
import fr.esigelec.gotoesig.databinding.FragmentCherchertrajetsBinding;
import fr.esigelec.gotoesig.ui.cherchertrajets.placeholder.PlaceholderContentChercherTrajets;

public class ChercherTrajetsRecyclerViewAdapter extends RecyclerView.Adapter<ChercherTrajetsRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderContentChercherTrajets.PlaceholderChercherTrajetsItem> mValues;
    private final Activity activity;
    private final ChercherTrajetsFragment parentFragment;

    public ChercherTrajetsRecyclerViewAdapter(List<PlaceholderContentChercherTrajets.PlaceholderChercherTrajetsItem> items, Activity activity, ChercherTrajetsFragment parentFragment) {
        mValues = items;
        this.activity = activity;
        this.parentFragment = parentFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentCherchertrajetsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = mValues.get(position);

        holder.nomVille.setText(holder.item.nomVille);
        holder.addresseComplete.setText(holder.item.addresseComplete);
        holder.transportText.setText(holder.item.transportText);

        Calendar cal = Calendar.getInstance();
        cal.setTime(holder.item.date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = format.format(cal.getTime());

        holder.date.setText(dateStr);


        String enCours = "Disponible";

        if (Calendar.getInstance().getTime().after(cal.getTime())) {
            enCours = "Termin??";
            holder.textEnCours.setTextColor(Color.rgb(200, 0, 0));
        }
        holder.textEnCours.setText(enCours);

        holder.placesNb.setText(holder.item.nbPlaceTaken + "/" + holder.item.placesNb);

        if(holder.item.isVehicule) {
            holder.prix.setText(holder.item.prix);
            holder.checkboxAutoroute.setChecked(holder.item.checkboxAutoroute);
        }
        else{
            holder.prix.setVisibility(View.INVISIBLE);
            holder.prixText.setVisibility(View.INVISIBLE);
            holder.checkboxAutoroute.setVisibility(View.INVISIBLE);
            holder.checkboxAutorouteText.setVisibility(View.INVISIBLE);
        }


        holder.inscriptionTrajet.setOnClickListener(view -> {
            //on clique sur le bouton inscription

            LatLng latLng = new LatLng(holder.item.latitude, holder.item.longitude);
            showDialog(latLng, holder.item.trajetId, holder.item.distance, holder.item.duree, holder.item.transportText);
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nomVille;
        public final TextView addresseComplete;
        public final TextView textEnCours;
        public final TextView transportText;
        public final TextView date;
        public final TextView placesNb;
        public final TextView prix;
        public final TextView prixText;
        public final CheckBox checkboxAutoroute;
        public final TextView checkboxAutorouteText;
        public final Button inscriptionTrajet;
        public PlaceholderContentChercherTrajets.PlaceholderChercherTrajetsItem item;

        public ViewHolder(FragmentCherchertrajetsBinding binding) {
            super(binding.getRoot());
            nomVille = binding.nomVille;
            addresseComplete = binding.addresseComplete;
            textEnCours = binding.textEnCours;
            transportText = binding.transportText;
            date = binding.date;
            placesNb = binding.placesNb;
            prix = binding.prix;
            prixText = binding.prixText;
            checkboxAutoroute = binding.checkboxAutoroute;
            checkboxAutorouteText = binding.autorouteText;
            inscriptionTrajet = binding.inscriptionTrajet;
        }
    }

    public void showDialog(LatLng pointDepart, String trajetUid, Long distance, Long duree, String transport) {

        RequestQueue queue = Volley.newRequestQueue(activity);

        String api_key = activity.getResources().getString(R.string.openrouteservice_key);
        String profile = "driving-car";

        if(transport.equals("V??lo"))
            profile = "cycling-regular";
        else if(transport.equals("A pied"))
            profile = "foot-walking";

        String url = "https://api.openrouteservice.org/v2/directions/" + profile + "?api_key=" + api_key + "&start=" + pointDepart.longitude + "," + pointDepart.latitude + "&end=1.0768526208801905,49.383452962302734";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            DialogConfirm dialogConfirm = new DialogConfirm(activity, obj, pointDepart, trajetUid, distance, duree, parentFragment);
                            dialogConfirm.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);


    }
}