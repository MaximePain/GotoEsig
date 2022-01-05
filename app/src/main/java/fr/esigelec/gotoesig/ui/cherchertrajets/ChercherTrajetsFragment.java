package fr.esigelec.gotoesig.ui.cherchertrajets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import fr.esigelec.gotoesig.MainActivity;
import fr.esigelec.gotoesig.databinding.FragmentMestrajetsListBinding;
import fr.esigelec.gotoesig.ui.cherchertrajets.placeholder.PlaceholderContentChercherTrajets;


public class ChercherTrajetsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private FragmentMestrajetsListBinding binding;
    private MainActivity mainActivity;

    public ChercherTrajetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMestrajetsListBinding.inflate(inflater);
        View view = binding.getRoot();

        initRecyclerView();

        return view;
    }

    public void initRecyclerView(){
        PlaceholderContentChercherTrajets.ITEMS.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser fUser = auth.getCurrentUser();
        assert fUser != null;

        String uid = fUser.getUid();

        db.collection("Trajets").whereNotEqualTo("ownerUid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots){
                    PlaceholderContentChercherTrajets.ITEMS.add(new PlaceholderContentChercherTrajets.PlaceholderChercherTrajetsItem(
                            doc.getString("nomVille"),
                            doc.getString("addresseComplete"),
                            doc.getString("transport"),
                            doc.getDate("dateDepart"),
                            doc.getLong("nombrePlaces").toString(),
                            doc.getLong("contribution").toString(),
                            doc.getBoolean("autoroute"),
                            ((ArrayList<String>)doc.get("usersUid")).size() - 1,
                            ((Map<String, Double>)doc.get("pointDepart")).get("latitude"),
                            ((Map<String, Double>)doc.get("pointDepart")).get("longitude")
                    ));
                }

                for(int i = 0; i < PlaceholderContentChercherTrajets.ITEMS.size(); i++)
                {
                    Date date = PlaceholderContentChercherTrajets.ITEMS.get(i).date;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    if(Calendar.getInstance().getTime().after(cal.getTime()))
                        PlaceholderContentChercherTrajets.ITEMS.remove(i);
                }

                RecyclerView recyclerView = binding.list;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
                }
                recyclerView.setAdapter(new ChercherTrajetsRecyclerViewAdapter(PlaceholderContentChercherTrajets.ITEMS, mainActivity));

            }
        });
    }
}