package fr.esigelec.gotoesig.ui.quitter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import fr.esigelec.gotoesig.LoginActivity;
import fr.esigelec.gotoesig.MainActivity;
import fr.esigelec.gotoesig.R;


public class QuitterFragment extends Fragment {


    public QuitterFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_quitter, container, false);
    }
}