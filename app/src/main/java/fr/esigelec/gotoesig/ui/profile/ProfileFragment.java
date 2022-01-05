package fr.esigelec.gotoesig.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

import fr.esigelec.gotoesig.ImageEncoder;
import fr.esigelec.gotoesig.MainActivity;
import fr.esigelec.gotoesig.databinding.FragmentProfileBinding;
import fr.esigelec.gotoesig.model.User;

public class ProfileFragment extends Fragment {

    MainActivity mainActivity;

    private FragmentProfileBinding binding;
    private User user;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentVille;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        user = mainActivity.currentUser;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater);
        View view = binding.getRoot();

        ImageView profilePicture = binding.profilePicture;
        TextView profileEmail = binding.profileEmail;
        TextView profileNom = binding.profileNom;
        TextView profilePrenom = binding.profilePrenom;
        TextView profileTel = binding.profileTel;
        TextView profileVille = binding.profileVille;

        //on affiche les données
        Bitmap bm = ImageEncoder.decodeImageBitmap(user.getImage());
        profilePicture.setImageBitmap(bm);
        profileEmail.setText(profileEmail.getText() + user.getEmail());
        profileNom.setText(profileNom.getText() + user.getNom());
        profilePrenom.setText(profilePrenom.getText() + user.getPrenom());


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            currentVille = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getLocality();
                            profileVille.setText(profileVille.getText() + currentVille);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        TelephonyManager tMgr = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String numTel = tMgr.getLine1Number();

        if(numTel != null)
            binding.profileTel.setText(numTel);

        return view;
    }

}