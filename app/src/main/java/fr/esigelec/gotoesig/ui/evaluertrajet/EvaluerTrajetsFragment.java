package fr.esigelec.gotoesig.ui.evaluertrajet;

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
import java.util.Calendar;
import java.util.Date;

import fr.esigelec.gotoesig.MainActivity;
import fr.esigelec.gotoesig.databinding.FragmentCherchertrajetsListBinding;
import fr.esigelec.gotoesig.databinding.FragmentEvaluerTrajetListBinding;
import fr.esigelec.gotoesig.model.Trajet;
import fr.esigelec.gotoesig.ui.cherchertrajets.ChercherTrajetsRecyclerViewAdapter;
import fr.esigelec.gotoesig.ui.cherchertrajets.placeholder.PlaceholderContentChercherTrajets;
import fr.esigelec.gotoesig.ui.evaluertrajet.placeholder.PlaceholderContentEvaluerTrajets;


public class EvaluerTrajetsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private FragmentEvaluerTrajetListBinding binding;
    private MainActivity mainActivity;

    public EvaluerTrajetsFragment() {
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

        binding = FragmentEvaluerTrajetListBinding.inflate(inflater);
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

        db.collection("Trajets").whereArrayContains("usersUid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    PlaceholderContentEvaluerTrajets.ITEMS.add(new PlaceholderContentEvaluerTrajets.PlaceholderEvaluerTrajetsItem(
                            doc.getString("nomVille"),
                            doc.getString("addresseComplete"),
                            doc.getString("transport"),
                            doc.getDate("dateDepart"),
                            doc.getLong("nombrePlaces").toString(),
                            doc.getLong("contribution").toString(),
                            doc.getBoolean("autoroute"),
                            ((ArrayList<String>) doc.get("usersUid")).size() - 1,
                            doc.getDouble("latitude"),
                            doc.getDouble("longitude"),
                            doc.getId(),
                            doc.getBoolean("isVehicule")
                    ));
                }

                for (int i = 0; i < PlaceholderContentEvaluerTrajets.ITEMS.size(); i++) {
                    Date date = PlaceholderContentEvaluerTrajets.ITEMS.get(i).date;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);

                    if (Calendar.getInstance().getTime().before(cal.getTime()))
                        PlaceholderContentEvaluerTrajets.ITEMS.remove(i);
                }

                RecyclerView recyclerView = binding.list;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
                }
                recyclerView.setAdapter(new EvaluerTrajetsRecyclerViewAdapter(PlaceholderContentEvaluerTrajets.ITEMS, mainActivity, EvaluerTrajetsFragment.this));

            }
        });
    }
}