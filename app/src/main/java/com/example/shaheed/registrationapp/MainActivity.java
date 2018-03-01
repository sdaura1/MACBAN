package com.example.shaheed.registrationapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    TextView gendertxt, statustxt, statetxt, lgatxt, kingendertxt, kinstatustxt, kinstatetxt, kinlgatxt, memstatetxt, memlgatxt, DOBtxt;
    private static final String TAG = "Post Fragment";
    FirebaseStorage fStorage;
    FirebaseFirestore firestoreRef;
    String firstNametxt, surNametxt, dobtxt, mOccupation, kinOccupation, kinFirstNametxt, kinSurNametxt, houseAddress, houseNum,
            gndr, Mstatus, state, lga, kinlga, kinstate, kinstatus, kingender, kinrel, kinhouseAd, kinCellNo, town, kintown;
    String downloadURL, cellNo, memTown, memCity, memStateStr, memLGAStr, memOccupation;
    Map<String, String> firestoreData;
    Button snapBtn, saveBtn, dateBtn;
    StorageMetadata storageMetadata;
    EditText firstName, surName, cell, kinFirstName, kinSurName, kinCell, houseAddr, kinTown, Town, occupation, kinoccupation, houseNo, kinhouseaddress, kinRelationship,
    memTownEdt, memCityEdt, memOccupatioEdt;
    Spinner genderSpinner, statusSpinner, statesSpinner, lgaSpinner, kingenderSpinner, kinstatusSpinner, kinstatesSpinner, kinlgaSpinner,
    memstateSpinner, memlgaSpinner;
    ArrayAdapter<CharSequence> genderAdapter, statusAdapter, statesAdapter, lgaAdapter, kingenderAdapter, kinstatusAdapter, kinstatesAdapter, kinlgaAdapter,
    memstateAdapter, memlgaAdapter;
    private StorageReference storageReference;
    byte[] bytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);

        kingendertxt = findViewById(R.id.kingenderGetter);
        kinstatetxt = findViewById(R.id.kinstateGetter);
        kinstatustxt = findViewById(R.id.kinstatusGetter);
        kinhouseaddress = findViewById(R.id.kinhouseAddressEditText);
        kinRelationship = findViewById(R.id.kinRelationshipEditText);
        kinlgatxt = findViewById(R.id.kinlgaGetter);
        kinoccupation = findViewById(R.id.kinOccupationEditText);
        kinFirstName = findViewById(R.id.editTextKinFirstName);
        kinSurName = findViewById(R.id.editTextKinSurname);
        kinTown = findViewById(R.id.kintownEditText);
        kinCell = findViewById(R.id.kincellnumEditText);
        kinstatesSpinner = findViewById(R.id.kinstateSpinner);
        kingenderSpinner = findViewById(R.id.kingenderSpinner);
        kinstatusSpinner = findViewById(R.id.kinstatusSpinner);
        kinlgaSpinner = findViewById(R.id.kinlgaSpinner);


        firstName = findViewById(R.id.editTextFirstName);
        surName = findViewById(R.id.editTextSurname);
        statesSpinner = findViewById(R.id.stateSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        houseNo = findViewById(R.id.houseNoEditText);
        lgaSpinner = findViewById(R.id.lgaSpinner);
        Town = findViewById(R.id.townEditText);
        houseAddr = findViewById(R.id.houseaddressEditText);
        occupation = findViewById(R.id.occupationEditText);
        gendertxt = findViewById(R.id.genderGetter);
        DOBtxt = findViewById(R.id.dateofbirthGetter);
        statustxt = findViewById(R.id.statusGetter);
        statetxt = findViewById(R.id.stateGetter);
        lgatxt = findViewById(R.id.lgaGetter);


        cell = findViewById(R.id.memcellEditText);
        memTownEdt = findViewById(R.id.memTownEditText);
        memCityEdt = findViewById(R.id.memCityEditText);
        memOccupatioEdt = findViewById(R.id.memOccupationEditText);
        memstateSpinner = findViewById(R.id.memstateSpinner);
        memlgaSpinner = findViewById(R.id.memlgaSpinner);
        memstatetxt = findViewById(R.id.memstateGetter);
        memlgatxt = findViewById(R.id.memlgaGetter);


        snapBtn = findViewById(R.id.snapBtn);
        saveBtn = findViewById(R.id.saveBtn);
        dateBtn = findViewById(R.id.datebtn);
        firestoreRef = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        firestoreData = new HashMap<>();
        storageReference = fStorage.getReference();


        genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array,
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        statusAdapter = ArrayAdapter.createFromResource(this, R.array.marital_status,
                android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        statesAdapter = ArrayAdapter.createFromResource(this, R.array.states,
                android.R.layout.simple_spinner_item);
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(statesAdapter);

        kingenderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array,
                android.R.layout.simple_spinner_item);
        kingenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kingenderSpinner.setAdapter(kingenderAdapter);

        kinstatusAdapter = ArrayAdapter.createFromResource(this, R.array.marital_status,
                android.R.layout.simple_spinner_item);
        kinstatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kinstatusSpinner.setAdapter(kinstatusAdapter);

        kinstatesAdapter = ArrayAdapter.createFromResource(this, R.array.states,
                android.R.layout.simple_spinner_item);
        kinstatesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kinstatesSpinner.setAdapter(kinstatesAdapter);

        memstateAdapter = ArrayAdapter.createFromResource(this, R.array.states,
                android.R.layout.simple_spinner_item);
        memstateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memstateSpinner.setAdapter(memstateAdapter);

        memlgaAdapter = ArrayAdapter.createFromResource(this, R.array.states,
                android.R.layout.simple_spinner_item);
        memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memlgaSpinner.setAdapter(memlgaAdapter);



        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent();
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gendertxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statustxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kingenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kingendertxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kinstatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kinstatustxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kinlgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kinlgatxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lgatxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        memlgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                memlgatxt.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statetxt.setText(parent.getItemAtPosition(position).toString().trim());

                String lga4state = statetxt.getText().toString().trim();

                if (lga4state.equals("Adamawa")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_adamawa,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Kano")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kano,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Katsina")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_katsina,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Kaduna")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kaduna,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Borno")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_borno,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Sokoto")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_sokoto,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Kogi")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kogi,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Gombe")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_gombe,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Jigawa")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_jigawa,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Niger")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_niger,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Nassarawa")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_nassarawa,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Taraba")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_taraba,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Kebbi")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kebbi,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Plateau")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_plateau,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Yobe")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_yobe,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Kwara")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kwara,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }else if (lga4state.equals("Benue")){
                    lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_benue,
                            android.R.layout.simple_spinner_item);
                    lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lgaSpinner.setAdapter(lgaAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kinstatesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kinstatetxt.setText(parent.getItemAtPosition(position).toString().trim());

                String kinlga4state = kinstatetxt.getText().toString().trim();

                if (kinlga4state.equals("Adamawa")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_adamawa,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Kano")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kano,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Katsina")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_katsina,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Kaduna")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kaduna,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Borno")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_borno,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Sokoto")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_sokoto,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Kogi")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kogi,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Gombe")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_gombe,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Jigawa")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_jigawa,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Niger")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_niger,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Nassarawa")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_nassarawa,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Taraba")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_taraba,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Kebbi")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kebbi,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Plateau")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_plateau,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Yobe")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_yobe,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }else if (kinlga4state.equals("Kwara")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kwara,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }
                else if (kinlga4state.equals("Benue")){
                    kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_benue,
                            android.R.layout.simple_spinner_item);
                    kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kinlgaSpinner.setAdapter(kinlgaAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        memstateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                memstatetxt.setText(parent.getItemAtPosition(position).toString().trim());

                String lga4state = memstatetxt.getText().toString().trim();

                if (lga4state.equals("Adamawa")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_adamawa,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Kano")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kano,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Katsina")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_katsina,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Kaduna")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kaduna,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Borno")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_borno,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Sokoto")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_sokoto,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Kogi")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kogi,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Gombe")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_gombe,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Jigawa")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_jigawa,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Niger")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_niger,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Nassarawa")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_nassarawa,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Taraba")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_taraba,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Kebbi")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kebbi,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Plateau")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_plateau,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Yobe")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_yobe,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Kwara")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kwara,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }else if (lga4state.equals("Benue")){
                    memlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_benue,
                            android.R.layout.simple_spinner_item);
                    memlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memlgaSpinner.setAdapter(memlgaAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), TAG);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dobtxt = String.valueOf(DOBtxt1) + String.valueOf(DOBmonth1) + String.valueOf(DOByear1);
                firstNametxt = firstName.getText().toString().trim();
                surNametxt = surName.getText().toString().trim();
                gndr = gendertxt.getText().toString().trim();
                Mstatus = statustxt.getText().toString().trim();
                houseNum = houseNo.getText().toString().trim();
                houseAddress = houseAddr.getText().toString().trim();
                state = statetxt.getText().toString().trim();
                lga = lgatxt.getText().toString().trim();
                mOccupation = occupation.getText().toString().trim();
                cellNo = cell.getText().toString().trim();
                town = Town.getText().toString().trim();

                kinFirstNametxt = kinFirstName.getText().toString().trim();
                kinSurNametxt = kinSurName.getText().toString().trim();
                kingender  = kingendertxt.getText().toString().trim();
                kinstatus = kinstatustxt.getText().toString().trim();
                kinrel = kinRelationship.getText().toString().trim();
                kinstate = kinstatetxt.getText().toString().trim();
                kinlga = kinlgatxt.getText().toString().trim();
                kinhouseAd = kinhouseaddress.getText().toString().trim();
                kintown = kinTown.getText().toString().trim();
                kinOccupation = kinoccupation.getText().toString().trim();
                kinCellNo = kinCell.getText().toString().trim();

                memStateStr = memstatetxt.getText().toString().trim();
                memLGAStr = memlgatxt.getText().toString().trim();
                memCity = memCityEdt.getText().toString().trim();
                memTown = memTownEdt.getText().toString().trim();
                memOccupation = memOccupatioEdt.getText().toString().trim();

                if (firstNametxt.isEmpty() && surNametxt.isEmpty() && gndr.isEmpty() && Mstatus.isEmpty()
                        && dobtxt.isEmpty() && houseNum.isEmpty() && houseAddress.isEmpty() && state.isEmpty()
                        && lga.isEmpty() && mOccupation.isEmpty() && town.isEmpty() && kinFirstNametxt.isEmpty()
                        && kinSurNametxt.isEmpty() && kingender.isEmpty() && kinstatus.isEmpty() && kinstatus.isEmpty()
                        && kinrel.isEmpty() && kinstate.isEmpty() && kinlga.isEmpty() && kinhouseAd.isEmpty()
                        && kintown.isEmpty() && kinOccupation.isEmpty() && kinCellNo.isEmpty() && bytes.length == 0
                        && memOccupation.isEmpty() && memLGAStr.isEmpty() && memCity.isEmpty() && memTown.isEmpty() && memStateStr.isEmpty()){

                    Toast.makeText(MainActivity.this, "All fields must be entered", Toast.LENGTH_SHORT).show();

                }else {

                    uploadData(bytes, firstNametxt, surNametxt, gndr, dobtxt, Mstatus, cellNo, state, lga,
                            town, houseNum, houseAddress, mOccupation, kinFirstNametxt, kinSurNametxt,
                            kingender, kinstatus, kinstate, kinlga, kintown, kinhouseAd, kinCellNo, kinOccupation, kinrel, memStateStr, memLGAStr,
                            memTown, memCity, memOccupation);
                }
            }
        });
    }

    private void takePictureIntent() {
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(mIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(mIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);//compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            bytes = outputStream.toByteArray();//toByteArray();

            mImageView.setImageBitmap(imageBitmap);
        }
    }


    private void uploadData(byte[] mBytes, String firstName, String surName, String gender, String dob, String mStatus, final String cellNum, String state,
                            String lga, String town, String houseNo, String houseAd, String occupation, String kinfirstName,
                            String kinsurName, String kingender, String kinmStatus, String kinstate, String kinlga,
                            String kinTown, String kinhouseAd, String kincellNo, String kinoccupation, String kinrelationship, String memState, String memLGA,
                            String memtown, String memcity, String memoccupation){

        StorageReference ref = storageReference.child("images/" + "/");

       /* ref.getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
            @Override
            public void onComplete(@NonNull Task<StorageMetadata> task) {
                if (task.isSuccessful()){
                    downloadURL = task.getResult().getMetadataGeneration();
                }
            }
        });*/

        storageMetadata = new StorageMetadata.Builder()
                .setContentType("images/")
                .setCustomMetadata("Custom Data", cellNum)
                .build();

        ref.putBytes(mBytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Stored Online!", Toast.LENGTH_SHORT).show();
                    downloadURL = task.getResult().getDownloadUrl().toString();
                }else {
                    Toast.makeText(MainActivity.this, "Connect to internet to Sync!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ref.updateMetadata(storageMetadata).addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
            @Override
            public void onComplete(@NonNull Task<StorageMetadata> task) {
                if (task.isSuccessful()){
                    downloadURL = task.getResult().toString();
                }
            }
        });
        ref.getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
            @Override
            public void onComplete(@NonNull Task<StorageMetadata> task) {
                downloadURL = task.getResult().toString();
            }
        });

        firestoreData.put("First Name", firstName);
        firestoreData.put("Surname", surName);
        firestoreData.put("Gender", gender);
        firestoreData.put("Marital Status", mStatus);
        firestoreData.put("DOB", dob);
        firestoreData.put("State", state);
        firestoreData.put("L. G. A", lga);
        firestoreData.put("Town", town);
        firestoreData.put("House Address", houseAd);
        firestoreData.put("House No.", houseNo);
        firestoreData.put("Occupation", occupation);
        firestoreData.put("Phone Number", cellNum);

        firestoreData.put("NEXT OF KIN FirstName", kinfirstName);
        firestoreData.put("NEXT OF KIN SurName", kinsurName);
        firestoreData.put("NEXT OF KIN Gender", kingender);
        firestoreData.put("NEXT OF KIN Marital Status", kinmStatus);
        firestoreData.put("NEXT OF KIN State", kinstate);
        firestoreData.put("NEXT OF KIN LGA", kinlga);
        firestoreData.put("NEXT OF KIN Town", kinTown);
        firestoreData.put("NEXT OF KIN House Address", kinhouseAd);
        firestoreData.put("NEXT OF KIN Occupation", kinoccupation);
        firestoreData.put("NEXT OF KIN Cell No", kincellNo);
        firestoreData.put("NEXT OF KIN Relation", kinrelationship);

        firestoreData.put("Membership State", memState);
        firestoreData.put("Membership L. G. A", memLGA);
        firestoreData.put("Membership City", memcity);
        firestoreData.put("Membership Town", memtown);
        firestoreData.put("Membership Occupation", memoccupation);

        firestoreData.put("DownloadURl", downloadURL);

        firestoreRef.collection("Field Data").document().set(firestoreData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Data Saved Successfully!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Connect the Internet to Save Data Online!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addnewMenu:

                int[] image = new int[] {R.drawable.ic_person_black_24dp};

                mImageView.setImageResource(image[0]);

                firstName.setText("");
                surName.setText("");
                gendertxt.setText("");
                statustxt.setText("");
                houseNo.setText("");
                houseAddr.setText("");
                statetxt.setText("");
                lgatxt.setText("");
                occupation.setText("");
                Town.setText("");

                kinFirstName.setText("");
                kinSurName.setText("");
                kingendertxt.setText("");
                kinstatustxt.setText("");
                kinRelationship.setText("");
                kinstatetxt.setText("");
                kinlgatxt.setText("");
                kinhouseaddress.setText("");
                kinTown.setText("");
                kinoccupation.setText("");
                kinCell.setText("");

                memCityEdt.setText("");
                memTownEdt.setText("");
                memstatetxt.setText("");
                memlgatxt.setText("");
                memOccupatioEdt.setText("");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = new GregorianCalendar(year, month, dayOfMonth);
        setDate(mCalendar);
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DOBtxt.setText(dateFormat.format(calendar.getTime()));
    }
}