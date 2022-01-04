package fr.esigelec.gotoesig.ui.mestrajets;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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
        holder.textEnCours.setText("en cours");
        holder.transportText.setText(holder.item.transportText);

        Calendar cal = Calendar.getInstance();
        cal.setTime(holder.item.date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = format.format(cal.getTime());

        holder.date.setText(dateStr);
        holder.placesNb.setText(holder.item.placesNb);
        holder.prix.setText(holder.item.prix);
        holder.checkboxAutoroute.setChecked(holder.item.checkboxAutoroute);

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
        public final CheckBox checkboxAutoroute;
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
            checkboxAutoroute = binding.checkboxAutoroute;
        }
    }
}