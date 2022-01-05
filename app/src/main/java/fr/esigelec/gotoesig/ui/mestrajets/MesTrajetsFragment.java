package fr.esigelec.gotoesig.ui.mestrajets;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

import fr.esigelec.gotoesig.databinding.FragmentMestrajetsListBinding;
import fr.esigelec.gotoesig.ui.mestrajets.placeholder.PlaceholderContentMesTrajets;


public class MesTrajetsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private FragmentMestrajetsListBinding binding;

    public MesTrajetsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        PlaceholderContentMesTrajets.ITEMS.clear();
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser fUser = auth.getCurrentUser();
        assert fUser != null;

        String uid = fUser.getUid();

        db.collection("Trajets").whereArrayContains("usersUid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots){
                    PlaceholderContentMesTrajets.ITEMS.add(new PlaceholderContentMesTrajets.PlaceholderMesTrajetsItem(
                            doc.getString("nomVille"),
                            doc.getString("addresseComplete"),
                            doc.getString("transport"),
                            doc.getDate("dateDepart"),
                            doc.getLong("nombrePlaces").toString(),
                            doc.getLong("contribution").toString(),
                            doc.getBoolean("autoroute")
                    ));
                }

                RecyclerView recyclerView = binding.list;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
                }
                recyclerView.setAdapter(new MesTrajetsRecyclerViewAdapter(PlaceholderContentMesTrajets.ITEMS));

            }
        });
    }
}