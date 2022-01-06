package fr.esigelec.gotoesig.ui.ajouttrajet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.esigelec.gotoesig.LoginActivity;
import fr.esigelec.gotoesig.MainActivity;
import fr.esigelec.gotoesig.R;
import fr.esigelec.gotoesig.RegisterActivity;
import fr.esigelec.gotoesig.databinding.FragmentAjoutTrajetBinding;
import fr.esigelec.gotoesig.model.Trajet;
import fr.esigelec.gotoesig.ui.profile.ProfileFragment;


public class AjoutTrajetFragment extends Fragment  implements PlacesAutoCompleteAdapter.ClickListener{

    private MainActivity mainActivity;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private FragmentAjoutTrajetBinding binding;
    private ArrayList<String> transportsList;
    private Boolean adressSetted = false;
    private Boolean adressJustSetted = false;
    private Boolean datePicked = false;
    private Boolean hourPicked = false;
    private LatLng currentLatLng = null;
    private Boolean isVehicule = true;

    private String nomVille = "";
    private String addresseComplete = "";

    private Calendar calendar;

    public AjoutTrajetFragment() {
        // Required empty public constructor
    }

    public static AjoutTrajetFragment newInstance(String param1, String param2) {
        AjoutTrajetFragment fragment = new AjoutTrajetFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        transportsList = new ArrayList<>();

        calendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAjoutTrajetBinding.inflate(inflater);
        View view = binding.getRoot();

        Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));

        recyclerView = binding.placesRecyclerView;
        binding.placeSearch.addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();

        updateSpinner(getContext());

        Button buttonDate = binding.buttonDate;
        Button buttonHeure = binding.buttonHeure;
        EditText editDate = binding.editDate;
        EditText editHeure = binding.editHeure;

        Button buttonValider = binding.buttonValider;

        Spinner spinner = binding.spinnerTransports;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isVehicule = spinner.getSelectedItem().toString().equals("Véhicule");
                updateVehicleSelected(isVehicule);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        buttonDate.setOnClickListener(view1 -> {
            final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay;
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            editDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            datePicked = true;
                            updateValiderButton();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        buttonHeure.setOnClickListener(view1 -> {
            int mHour, mMinute;
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(mainActivity,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            editHeure.setText(hourOfDay + ":" + minute);

                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);

                            hourPicked = true;
                            updateValiderButton();
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        });


        buttonValider.setOnClickListener(view1 -> {
            ajouterTrajet();
        });

        return view;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if(adressJustSetted) {
                adressJustSetted = false;
                return;
            }
            adressSetted = false;
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }

            updateValiderButton();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public void click(Place place) {
        //Toast.makeText(mainActivity, place.getAddress()+", "+ place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
        if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
        adressSetted = true;
        adressJustSetted = true;

        nomVille = place.getName();
        addresseComplete = place.getAddress();

        currentLatLng = place.getLatLng();
        binding.placeSearch.setText(place.getAddress());
        updateValiderButton();


        InputMethodManager inputMethodManager =
                (InputMethodManager) mainActivity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    mainActivity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    private void updateSpinner(Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference transports = db.collection("Transports").document("Transports");
        transports.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    transportsList = (ArrayList<String>) doc.get("noms");

                    Spinner spinner = binding.spinnerTransports;
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                            (context, android.R.layout.simple_spinner_item,
                                    transportsList);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setSelection(0);
                }
            }
        });
    }

    private void updateValiderButton(){
        if(adressSetted && datePicked && hourPicked)
            binding.buttonValider.setVisibility(View.VISIBLE);
        else
            binding.buttonValider.setVisibility(View.INVISIBLE);
    }

    private void updateVehicleSelected(Boolean vehicle){
        if(vehicle)
        {
            binding.autoroute.setVisibility(View.VISIBLE);
            binding.autorouteText.setVisibility(View.VISIBLE);
            binding.contribution.setVisibility(View.VISIBLE);
            binding.contributionText.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.autoroute.setVisibility(View.GONE);
            binding.autorouteText.setVisibility(View.GONE);
            binding.contribution.setVisibility(View.GONE);
            binding.contributionText.setVisibility(View.GONE);
        }

        binding.autoroute.setChecked(false);
        binding.contribution.setText("0");
    }

    private void ajouterTrajet(){
        //binding.buttonValider.setClickable(false); // pour eviter de spam le boutton ( on sait jamais )

        String api_key = getResources().getString(R.string.openrouteservice_key);
        String profile = "driving-car";

        String spinnerValue = binding.spinnerTransports.getSelectedItem().toString();
        if(spinnerValue.equals("Vélo"))
            profile = "cycling-regular";
        else if(spinnerValue.equals("A pied"))
            profile = "foot-walking";

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
            String URL = "https://api.openrouteservice.org/v2/matrix/" + profile;
            JSONObject jsonBody = new JSONObject();
            JSONArray jsonArrayLoc = new JSONArray();

            JSONArray jsonArrayLatLngDepart = new JSONArray();
            jsonArrayLatLngDepart.put(currentLatLng.longitude);
            jsonArrayLatLngDepart.put(currentLatLng.latitude);

            JSONArray jsonArrayLatLngESIG = new JSONArray();
            jsonArrayLatLngESIG.put(1.0768526208801905);
            jsonArrayLatLngESIG.put(49.383452962302734); //coordonnées de l'esigelec

            jsonArrayLoc.put(jsonArrayLatLngDepart);
            jsonArrayLoc.put(jsonArrayLatLngESIG);
            jsonBody.put("locations", jsonArrayLoc);

            JSONArray metrics = new JSONArray();
            metrics.put("distance");
            metrics.put("duration");
            jsonBody.put("metrics", metrics);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        int distance = (int)(Double.parseDouble(obj.getJSONArray("distances").getJSONArray(0).getString(1)) / 1000);
                        int duration = (int)(Double.parseDouble(obj.getJSONArray("durations").getJSONArray(0).getString(1)) / 60);

                        ajouterConfirmation(distance, duration);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
                    params.put("Authorization", api_key);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void ajouterConfirmation(int distance, int duration){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Ajout du trajet?");
        alert.setMessage("Distance: " + distance + "km - Durée: " + duration + "min");
        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //on ajoute a firebase etc

                Trajet trajet = new Trajet();
                trajet.setNomVille(nomVille);
                trajet.setAddresseComplete(addresseComplete);
                trajet.setDistance(distance);
                trajet.setDuree(duration);
                trajet.setLatitude(currentLatLng.latitude);
                trajet.setLongitude(currentLatLng.longitude);
                trajet.setDateDepart(calendar.getTime());
                trajet.setTransport(binding.spinnerTransports.getSelectedItem().toString());
                trajet.setContribution(Integer.parseInt(binding.contribution.getText().toString()));
                trajet.setNombrePlaces(Integer.parseInt(binding.nbPlaces.getText().toString()));
                trajet.setAutoroute(binding.autoroute.isChecked());
                trajet.setRetardTolere(Integer.parseInt(binding.retard.getText().toString()));
                trajet.setIsVehicule(isVehicule);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                FirebaseUser fUser = auth.getCurrentUser();
                assert fUser != null;

                trajet.setOwnerUid(fUser.getUid());
                trajet.getUsersUid().add(fUser.getUid());

                db.collection("Trajets").add(trajet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        ajoutFini();
                    }
                });

                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    void ajoutFini(){
        binding.placeSearch.setText("");
        binding.editDate.setText("");
        binding.editHeure.setText("");
        binding.autoroute.setChecked(false);
        binding.retard.setText("0");
        binding.contribution.setText("0");
        binding.nbPlaces.setText("1");
        adressSetted = false;
        datePicked = false;
        hourPicked = false;
    }
}