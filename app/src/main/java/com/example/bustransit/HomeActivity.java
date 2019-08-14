package com.example.bustransit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    String item;
    Map<String,Object> global;
    List<String> key=new ArrayList();
    List<String> routes=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final TextView stops=(TextView) findViewById(R.id.stops);
        final Spinner citySpinner = (Spinner) findViewById(R.id.citySpinner);
        final Spinner routeSpinner = (Spinner) findViewById(R.id.routeSpinner);
        final TableLayout table=(TableLayout) findViewById(R.id.table);
        routeSpinner.setEnabled(false);
        ref = database.getInstance().getReference();
        key.add(0,"Select City");
        routes.add(0,"Select Route");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    key.add(postSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, key);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adp);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select City"))
                {
                    
                }
                else
                {
                    routes.clear();
                    routes.add(0,"Select Route");
                    item=parent.getItemAtPosition(position).toString();
                    DatabaseReference cityRef=database.getInstance().getReference().child(item);
                    cityRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                Map<String, Object> route = (HashMap<String, Object>) postSnapshot.getValue();
                                global=route;
                                routes.add(postSnapshot.getKey());
                                routeSpinner.setEnabled(true);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                ArrayAdapter<String> routeAdp = new ArrayAdapter<String>(HomeActivity.this,android.R.layout.simple_spinner_item, routes);
                routeAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                routeSpinner.setAdapter(routeAdp);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select Route")) {

                }
                else {

                    String itemc = parent.getItemAtPosition(position).toString();
                    DatabaseReference cityRef = FirebaseDatabase.getInstance().getReference(item);
                    DatabaseReference routeRef = cityRef.child(itemc);
                    routeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                global.put(postSnapshot.getKey(),postSnapshot.getValue());
                            }
                            TableRow head= new TableRow(HomeActivity.this);
                            head.setLayoutParams(new TableLayout.LayoutParams(
                                            TableLayout.LayoutParams.WRAP_CONTENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT,
                                    1.0f
                                    ));
                            TextView label_stops = new TextView(HomeActivity.this);
                            label_stops.setText("STOPS");
                            label_stops.setPadding(5, 5, 5, 5);
                            head.addView(label_stops);
                            TextView label_stopsName = new TextView(HomeActivity.this);
                            label_stopsName.setId((int)21);
                            label_stopsName.setText("STOPS NAME");
                            label_stopsName.setPadding(5, 5, 5, 5);
                            head.addView(label_stopsName);
                            table.addView(head,new TableLayout.LayoutParams(
                                    TableLayout.LayoutParams.WRAP_CONTENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT
                            ));
                            if (table.getRootView() != null) {
                                int i = 1;
                                while (table.getChildCount() != 1) {
                                    table.removeViewAt(i);
                                }
                            }

                            for (Map.Entry<String, Object> entry : global.entrySet()) {
                                TableRow row= new TableRow(HomeActivity.this);
                                row.setBackgroundColor(Color.GRAY);
                                row.setLayoutParams(new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.WRAP_CONTENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT,
                                        1.0f
                                ));
                                TextView tableDataStops=new TextView(HomeActivity.this);
                                tableDataStops.setText(entry.getKey());
                                tableDataStops.setPadding(2, 0, 5, 0);
                                tableDataStops.setTextColor(Color.WHITE);
                                row.addView(tableDataStops);
                                TextView tabledata=new TextView(HomeActivity.this);
                                tabledata.setText(entry.getValue().toString());
                                tabledata.setPadding(2, 0, 5, 0);
                                tabledata.setTextColor(Color.WHITE);
                                row.addView(tabledata);
                                table.addView(row,new TableLayout.LayoutParams(
                                        TableLayout.LayoutParams.WRAP_CONTENT,
                                        TableLayout.LayoutParams.WRAP_CONTENT
                                ));
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



}
