package fr.esigelec.gotoesig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.Locale;

import fr.esigelec.gotoesig.model.User;


public class RegisterActivity extends AppCompatActivity {

    Uri selectedImage = null;
    Bitmap selectedImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button selectImage = findViewById(R.id.buttonSelectProfilPicture);
        selectImage.setOnClickListener(view -> {
            getPicture();
        });

        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        EditText inputNom = findViewById(R.id.inputNom);
        EditText inputPrenom = findViewById(R.id.inputPrenom);
        EditText inputTel = findViewById(R.id.inputTel);

        Button register = findViewById(R.id.buttonInscription);
        register.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            User user = new User(inputEmail.getText().toString().toLowerCase(Locale.ROOT),
                    inputNom.getText().toString(),
                    inputTel.getText().toString(),
                    inputPrenom.getText().toString(),
                    ""
            );

            String password = inputPassword.getText().toString();

            if (selectedImage != null) {
                selectedImageBitmap = ImageEncoder.getResizedBitmap(selectedImageBitmap, 852, 480);
                user.setImage(ImageEncoder.encodeImageBitmap(selectedImageBitmap));
            }

            auth.createUserWithEmailAndPassword(user.getEmail(), password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fUser = auth.getCurrentUser();
                                assert fUser != null;
                                db.collection("Users").document(fUser.getUid())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }

    void getPicture() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 4242);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 4242) {
                // Get the url of the image from data
                selectedImage = data.getData();
                if (null != selectedImage) {
                    ImageView preview = findViewById(R.id.imagePreview);
                    preview.setImageURI(selectedImage);
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        selectedImageBitmap = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}