package fr.esigelec.gotoesig.ui.mestrajets;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.esigelec.gotoesig.databinding.FragmentMestrajetsBinding;
import fr.esigelec.gotoesig.ui.mestrajets.placeholder.PlaceholderContentMesTrajets.PlaceholderMesTrajetsItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MesTrajetsRecyclerViewAdapter extends RecyclerView.Adapter<MesTrajetsRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderMesTrajetsItem> mValues;

    public MesTrajetsRecyclerViewAdapter(List<PlaceholderMesTrajetsItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMestrajetsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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
        public PlaceholderMesTrajetsItem item;

        public ViewHolder(FragmentMestrajetsBinding binding) {
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
        }
    }
}