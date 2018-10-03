package com.aditya.groupchatting;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.Adapter.BeanClass;
import com.aditya.Adapter.MessageAdapter;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Layout2 extends AppCompatActivity {
    ScrollView scrollView;
    RelativeLayout rl_1;
    LinearLayout ll_1;
    EditText et_message;
    ImageView iv_send;
    String message;
    String currentuser;
    private String temp_key;
    private String uname, rname;
    private DatabaseReference root;
    private String chat_msg, chat_user_name,chat_sent_time;


    RecyclerView recyclerView;
    ArrayList<BeanClass> beanClasses;
    String d_type;
    private MessageAdapter mAdapter;
    String userName,time_Send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_layout2 );
        beanClasses = new ArrayList<>();
        et_message = (EditText) findViewById( R.id.et_typemessage );
        iv_send = (ImageView) findViewById( R.id.iv_sendmessage );
        recyclerView = (RecyclerView) findViewById( R.id.rv_msg );

        uname = getIntent().getExtras().get( "user_name" ).toString();
        rname = getIntent().getExtras().get( "room_name" ).toString();

        root = FirebaseDatabase.getInstance().getReference().child( rname );



        iv_send.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Map<String, Object> map2 = new HashMap<>();
                temp_key = root.push().getKey();            //uniquely generated key
                root.updateChildren( map2 );

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                final String dateAndTime = formatter.format(date);

                DatabaseReference message_root = root.child( temp_key );
                Map<String, Object> map3 = new HashMap<>();
                map3.put( "name", uname );
                map3.put( "message", et_message.getText().toString() );
                map3.put("time", dateAndTime);
                message = et_message.getText().toString();

                message_root.updateChildren( map3 );

                et_message.setText( "" );

            }
        } );
        root.addChildEventListener( new ChildEventListener() {
            @Override

            // whenerver databse is created for first time
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                append_chat_conversation( dataSnapshot );

            }
            // whenever the value of child changes
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_chat_conversation( dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }


    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        String d_type;
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext())
        {
            chat_msg = (String) ( (DataSnapshot) i.next() ).getValue();
            chat_user_name = (String) ( (DataSnapshot) i.next() ).getValue();
            chat_sent_time = (String) ( (DataSnapshot) i.next() ).getValue();


            if(chat_user_name.equalsIgnoreCase(uname)) {
                d_type = "1";
            }
            else
            {
                d_type = "2";
            }
            BeanClass beanClass = new BeanClass( chat_user_name, chat_msg,d_type,chat_sent_time);
            beanClasses.add( beanClass );
        }

        //Set Adapter
        MessageAdapter adapter = new MessageAdapter( Layout2.this, beanClasses );
        recyclerView.setAdapter( adapter );

        recyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );


        recyclerView.getLayoutManager().scrollToPosition(beanClasses.size()-1);




        adapter.notifyDataSetChanged();
    }
}