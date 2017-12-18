package club.dev.app.falconyap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.R.attr.name;


public class Chat_Room extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name,room_name;
    private DatabaseReference root ;
    private String temp_key;

    //private DatabaseReference nDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);


        //nDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth= FirebaseAuth.getInstance();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){

                }else{

<<<<<<< HEAD
                    //Toast.makeText(getContext(),"current user null", Toast.LENGTH_SHORT).show();
=======
                     //Toast.makeText(getContext(),"current user null", Toast.LENGTH_SHORT).show();
>>>>>>> origin/master
                }

            }
        };


        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);

        input_msg.setTextColor(Color.WHITE);
        input_msg.setTextSize(getResources().getDimension(R.dimen.textsizeone));
        chat_conversation.setTextColor(Color.WHITE);
        chat_conversation.setTextSize(getResources().getDimension(R.dimen.textsizeone));

        //user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - "+room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());
                input_msg.setText("");

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
<<<<<<< HEAD
            // chat_user_name = "Yapper";
            //chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            // chat_user_name = (String) (mDatabaseUsers.getDatabase().getReference().child(("Users")).child("name")).toString();
=======
           // chat_user_name = "Yapper";
            //chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
           // chat_user_name = (String) (mDatabaseUsers.getDatabase().getReference().child(("Users")).child("name")).toString();
>>>>>>> origin/master

            final String name_val = chat_user_name;
            mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chat_user_name = (String) dataSnapshot.child("name").getValue();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference newPost = mDatabaseUsers.push();

            newPost.child("name").setValue(name_val);


            chat_conversation.append(chat_user_name +" : "+chat_msg +" \n");
            // chat_conversation.append( "+chat_msg +" );
        }


    }
}