package fr.esigelec.gotoesig.ui.statistiques;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import fr.esigelec.gotoesig.R;
import fr.esigelec.gotoesig.databinding.FragmentStatistiquesBinding;
import fr.esigelec.gotoesig.ui.cherchertrajets.placeholder.PlaceholderContentChercherTrajets;


public class StatistiquesFragment extends Fragment {

    private FragmentStatistiquesBinding binding;

    public StatistiquesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStatistiquesBinding.inflate(inflater);

        View view = binding.getRoot();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser fUser = auth.getCurrentUser();
        assert fUser != null;

        String uid = fUser.getUid();


        db.collection("Trajets").whereEqualTo("ownerUid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                binding.nbTrajets.setText("" + queryDocumentSnapshots.size());

                int contributionTotal = 0;
                Double score = 0.0;
                int nbNotes = 0;
                for (DocumentSnapshot doc : queryDocumentSnapshots) {

                    if (doc.getBoolean("isVehicule")) {

                        Date date = doc.getDate("dateDepart");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);

                        if (Calendar.getInstance().getTime().after(cal.getTime())) {
                            Double cout = doc.getDouble("contribution");
                            ArrayList<String> usersUid = (ArrayList<String>) doc.get("usersUid");
                            int nbContributeurs = usersUid.size() - 1;

                            contributionTotal += (nbContributeurs * cout);
                        }
                    }

                    Map<String, Double> notes = (Map<String, Double>) doc.get("notes");
                    for (Map.Entry<String, Double> entry : notes.entrySet()) {
                        score += entry.getValue();
                        nbNotes++;
                    }
                }

                score /= nbNotes;

                binding.contributionTotal.setText("" + contributionTotal);
                binding.score.setText(score.toString());
            }
        });


        return view;
    }
}