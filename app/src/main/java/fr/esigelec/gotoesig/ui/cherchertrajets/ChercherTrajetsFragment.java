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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import fr.esigelec.gotoesig.MainActivity;
import fr.esigelec.gotoesig.databinding.FragmentCherchertrajetsBinding;
import fr.esigelec.gotoesig.databinding.FragmentCherchertrajetsListBinding;
import fr.esigelec.gotoesig.databinding.FragmentMestrajetsListBinding;
import fr.esigelec.gotoesig.model.Trajet;
import fr.esigelec.gotoesig.ui.cherchertrajets.placeholder.PlaceholderContentChercherTrajets;


public class ChercherTrajetsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private FragmentCherchertrajetsListBinding binding;
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

        binding = FragmentCherchertrajetsListBinding.inflate(inflater);
        View view = binding.getRoot();

        initRecyclerView();

        return view;
    }

    public void initRecyclerView() {
        PlaceholderContentChercherTrajets.ITEMS.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser fUser = auth.getCurrentUser();
        assert fUser != null;

        String uid = fUser.getUid();

        db.collection("Trajets").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    if (!((ArrayList<String>) doc.get("usersUid")).contains(uid)) {

                        Date date = doc.getDate("dateDepart");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);

                        int nbplaces = doc.getLong("nombrePlaces").intValue();
                        int nbplacesTaken = ((ArrayList<String>) doc.get("usersUid")).size() - 1;


                        if (Calendar.getInstance().getTime().before(cal.getTime()) && (nbplacesTaken < nbplaces))
                            PlaceholderContentChercherTrajets.ITEMS.add(new PlaceholderContentChercherTrajets.PlaceholderChercherTrajetsItem(
                                    doc.getString("nomVille"),
                                    doc.getString("addresseComplete"),
                                    doc.getString("transport"),
                                    doc.getDate("dateDepart"),
                                    doc.getLong("nombrePlaces").toString(),
                                    doc.getLong("contribution").toString(),
                                    doc.getBoolean("autoroute"),
                                    nbplacesTaken,
                                    doc.getDouble("latitude"),
                                    doc.getDouble("longitude"),
                                    doc.getId(),
                                    doc.getBoolean("isVehicule"),
                                    doc.getLong("distance"),
                                    doc.getLong("duree")
                            ));
                    }
                }

                RecyclerView recyclerView = binding.list;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
                }
                recyclerView.setAdapter(new ChercherTrajetsRecyclerViewAdapter(PlaceholderContentChercherTrajets.ITEMS, mainActivity, ChercherTrajetsFragment.this));

            }
        });
    }

    public void confirmInscription(String trajetId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser fUser = auth.getCurrentUser();
        assert fUser != null;

        String uid = fUser.getUid();
        final DocumentReference trajetDocRef = db.collection("Trajets").document(trajetId);

        db.collection("Trajets").document(trajetId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                int nbPlaces = doc.getLong("nombrePlaces").intValue();
                int placesTaken = ((ArrayList<String>) doc.get("usersUid")).size() - 1;

                Trajet currentTrajet = doc.toObject(Trajet.class);
                ArrayList<String> usersUid = currentTrajet.getUsersUid();
                usersUid.add(uid);
                currentTrajet.setUsersUid(usersUid);
                if (placesTaken < nbPlaces) {
                    trajetDocRef.set(currentTrajet, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            initRecyclerView();
                        }
                    });
                }
            }
        });
    }
}