package fr.esigelec.gotoesig.ui.evaluertrajet;

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
import fr.esigelec.gotoesig.databinding.FragmentEvaluerTrajetBinding;
import fr.esigelec.gotoesig.ui.cherchertrajets.ChercherTrajetsFragment;
import fr.esigelec.gotoesig.ui.cherchertrajets.DialogConfirm;
import fr.esigelec.gotoesig.ui.cherchertrajets.placeholder.PlaceholderContentChercherTrajets;
import fr.esigelec.gotoesig.ui.evaluertrajet.placeholder.PlaceholderContentEvaluerTrajets;

public class EvaluerTrajetsRecyclerViewAdapter extends RecyclerView.Adapter<EvaluerTrajetsRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderContentEvaluerTrajets.PlaceholderEvaluerTrajetsItem> mValues;
    private final Activity activity;
    private final EvaluerTrajetsFragment parentFragment;

    public EvaluerTrajetsRecyclerViewAdapter(List<PlaceholderContentEvaluerTrajets.PlaceholderEvaluerTrajetsItem> items, Activity activity, EvaluerTrajetsFragment parentFragment) {
        mValues = items;
        this.activity = activity;
        this.parentFragment = parentFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentEvaluerTrajetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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
            enCours = "Terminé";
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
        public PlaceholderContentEvaluerTrajets.PlaceholderEvaluerTrajetsItem item;

        public ViewHolder(FragmentEvaluerTrajetBinding binding) {
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
}