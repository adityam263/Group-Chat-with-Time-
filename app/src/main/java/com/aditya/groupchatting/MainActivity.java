package com.aditya.groupchatting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.aditya.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button btn_addroom;
    private EditText et_roomname;
    private ListView listView;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btn_addroom = (Button) findViewById( R.id.btn_addroom );
        et_roomname = (EditText) findViewById( R.id.et_roomname );
        listView = (ListView) findViewById( R.id.listview );


        arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_expandable_list_item_1, list_of_rooms );

        listView.setAdapter( arrayAdapter );
        request_user_name();

        btn_addroom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hash map is used to store value in key value pair form


                Map<String, Object> map = new HashMap<String, Object>();
                map.put( et_roomname.getText().toString(), " " );
                //We got the root name and the value we don't need
                root.updateChildren( map );
            }
        } );


        root.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //We use iterator to get through the database and read it

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();


                while (i.hasNext())                              // Read Database line by line
                {
                    set.add( ( (DataSnapshot) i.next() ).getKey() );      //Get all the room  name avaialble in firebase

                }

                list_of_rooms.clear();                 // clear the current list
                list_of_rooms.addAll( set );            // append with temperory hash set
                arrayAdapter.notifyDataSetChanged();    // update the listview adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent( getApplicationContext(), Layout2.class );
                intent.putExtra( "room_name", ( (TextView) view ).getText().toString() );
                intent.putExtra( "user_name", name );
                startActivity( intent );

            }
        } );


    }


    // For Displaying dialog box for user name
    private void request_user_name() {

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Enter your name" );

        final EditText et_username = new EditText( this );
        et_username.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView( et_username );
        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = et_username.getText().toString();

            }
        } );

        builder.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();
            }
        } );
        builder.show();
    }


}

