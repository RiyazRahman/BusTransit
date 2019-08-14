package com.example.bustransit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusActivity extends AppCompatActivity {
    List<Object> values = new ArrayList<>();
    List<String> key = new ArrayList();
    String cityItem,fromItem,toItem,key1,key2;
    Spinner city,frm, to;
    TextView disp;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        frm = (Spinner) findViewById(R.id.fromSpinner);
        to = (Spinner) findViewById(R.id.toSpinner);
        city = (Spinner) findViewById(R.id.citySpinner);
        disp=(TextView) findViewById(R.id.res);
        b1=(Button) findViewById(R.id.clear);
        key.add(0,"Select City");

       DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> route = (HashMap<String, Object>) postSnapshot.getValue();
                    values.addAll(route.values());
                    key.add(postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, key);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adp);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select City"))
                {

                }
                else
                {
                    cityItem=parent.getItemAtPosition(position).toString();
                    DatabaseReference cityRef=FirebaseDatabase.getInstance().getReference().child(cityItem);
                    cityRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            getStopNames((Map<String,Object>) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            private void  getStopNames(Map<String,Object> stop){
                ArrayList<String> fromStopNames = new ArrayList<>();
                ArrayList<String> toStopNames = new ArrayList<>();
                for (Map.Entry<String, Object> entry : stop.entrySet()){
                     Map singleCity = (Map) entry.getValue();
                    fromStopNames.add( (String)singleCity.get("Stop1"));
                    toStopNames.add( (String)singleCity.get("Stop3"));
                }
                fromStopNames.add(0,"Leaving From");
                toStopNames.add(0,"Going To");
                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(BusActivity.this,android.R.layout.simple_spinner_item, fromStopNames);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                frm.setAdapter(adp1);
                frm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fromItem = parent.getItemAtPosition(position).toString();
                        if (parent.getItemAtPosition(position).equals("Leaving From")) {

                        }
                        else {
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(cityItem);
                            ref1.orderByChild("Stop1").equalTo(fromItem).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                        key1 = datas.getKey();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adp2 = new ArrayAdapter<String>(BusActivity.this,android.R.layout.simple_spinner_item, toStopNames);
                adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                to.setAdapter(adp2);
                to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        toItem = parent.getItemAtPosition(position).toString();

                        if (parent.getItemAtPosition(position).equals("Going To")) {

                        } else {
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(cityItem);
                            ref1.orderByChild("Stop3").equalTo(toItem).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                        key2 = datas.getKey();

                                    }
                                    if (key1.equals(key2)) {
                                        disp.setText("");
                                        disp.setText("Take the bus in " + key1);

                                    } else {
                                        Toast.makeText(BusActivity.this, "No Busses In this Route", Toast.LENGTH_SHORT).show();
                                        disp.setText("");
                                        disp.setText("No Busses In this Route");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      b1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              disp.setText("");
          }
      });
    }
}
