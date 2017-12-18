package club.dev.app.falconyap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends FragmentActivity {

    private EditText nLoginEmailField;
    private EditText nLoginPasswordField;
    private Button nLoginBtn;
    private Button nNewAccountBtn;
    private FirebaseAuth nAuth;
    private ProgressDialog nProgress;
    private DatabaseReference nDatabaseUsers;
    private TextView mForgotpswdLoginTv;


    private static final String TAG="LoginActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);



        nAuth = FirebaseAuth.getInstance();

        nDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        nDatabaseUsers.keepSynced(true);

        nProgress = new ProgressDialog(this);




        nLoginPasswordField = (EditText) findViewById(R.id.loginPasswordField);
        nLoginEmailField = (EditText) findViewById(R.id.loginEmailField);
        //forgot password
        mForgotpswdLoginTv = (TextView) findViewById(R.id.forgotpswdLoginTv);

        nLoginBtn = (Button) findViewById(R.id.loginBtn);

        nNewAccountBtn = (Button) findViewById(R.id.NewAccountbtn);

        mForgotpswdLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
               startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);


                 startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        nNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAccount();
            }
        });


        nLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });





    }





    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void newAccount()  {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        // startActivity(intent);
        startActivityForResult(intent,0);
        overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );


    }




    private void checkLogin() {

        String email = nLoginEmailField.getText().toString().trim() + "@apps.losrios.edu";
        String password = nLoginPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            nProgress.setMessage("Checking Login...");
            nProgress.show();

            nAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {

                        nProgress.dismiss();

                        checkUserExist();
                    } else {

                        nProgress.dismiss();

                        Toast.makeText(LoginActivity.this, "Error Login", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }

    private void checkUserExist() {

        final String user_id = nAuth.getCurrentUser().getUid();
        nDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)) {

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(mainIntent);

                    startActivityForResult(mainIntent,0);
                    overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out );


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
