package club.dev.app.falconyap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nNameField;
    private EditText nEmailField;
    private EditText nPasswordField;

    private Button nRegisterBtn;

    private FirebaseAuth nAuth;
    private DatabaseReference nDatabase;

    private ProgressDialog nProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        nAuth = FirebaseAuth.getInstance();

        nDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        nProgress = new ProgressDialog(this);

        nNameField = (EditText) findViewById(R.id.nameField);
        nEmailField = (EditText) findViewById(R.id.emailField);
        nPasswordField = (EditText) findViewById(R.id.passwordField);
        nRegisterBtn = (Button) findViewById(R.id.registerBtn);

        nRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startRegister();

            }
        });


    }

        private void startRegister() {

            final String name = nNameField.getText().toString().trim();
            String email = nEmailField.getText().toString().trim() + "@apps.losrios.edu";
            String password = nPasswordField.getText().toString().trim();



            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                    nProgress.setMessage("Signing Up...");
                    nProgress.show();

                    nAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                String user_id = nAuth.getCurrentUser().getUid();

                                DatabaseReference current_user_db = nDatabase.child(user_id);

                                current_user_db.child("name").setValue(name);
                                current_user_db.child("image").setValue("default");

                                nProgress.dismiss();
                                //changed below intent to go to login activity instead of main activity
                                Intent homeIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                                finish();

                            }

                        }
                    });
                }
            }


    }




