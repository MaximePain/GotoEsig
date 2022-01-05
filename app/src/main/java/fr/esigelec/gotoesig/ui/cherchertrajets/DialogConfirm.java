package fr.esigelec.gotoesig.ui.cherchertrajets;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import fr.esigelec.gotoesig.R;

public class DialogConfirm extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog d;
    public Button yes, no;
    public GoogleMap map;
    public MapView mapView;
    public JSONObject JSONData;
    public LatLng pointDepart;

    public DialogConfirm(Activity a, JSONObject obj, LatLng pointDepart) {
        super(a);
        this.activity = a;
        this.JSONData = obj;
        this.pointDepart = pointDepart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMyLocationButtonEnabled(false);
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                map.addMarker(new MarkerOptions().position(new LatLng(49.383452962302734,1.0768526208801905)).title("ESIGELEC"));
                map.addMarker(new MarkerOptions().position(pointDepart).title("Point de départ"));
                LatLng latLng = new LatLng(49.383452962302734,1.0768526208801905);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                GeoJsonLayer layer = null;
                layer = new GeoJsonLayer(map, JSONData);
                layer.addLayerToMap();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                map.clear();
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
