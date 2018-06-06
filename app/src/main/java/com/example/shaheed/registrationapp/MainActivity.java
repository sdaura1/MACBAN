package com.example.shaheed.registrationapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    public String firstNametxt, surNametxt, dobtxt, mOccupation, kinOccupation, kinFirstNametxt,
            kinSurNametxt, houseAddress, houseNum, gndr, Mstatus, state, lga, kinlga,
            kinstate, id, kinstatus, kingender, kinrel, kinhouseAd, kinCellNo, town, kintown,
            cellNo, regPointStateContent, regPointlgaContent, regPointZone;
    private static final String FOLDER_NAME = "MACBAN";
    Calendar mCalendar = Calendar.getInstance();
    private static final String TAG = "Post Fragment";
    TextView gendertxt, statustxt, statetxt, lgatxt, kingendertxt, kinstatustxt, kinstatetxt,
            kinlgatxt, DOBtxt;
    FirebaseStorage fStorage;
    FirebaseFirestore firestoreRef;
    String stateCode, LGAcode, _code, phone_code;
    Map<String, Object> firestoreData;
    Button snapBtn, saveBtn, dateBtn;
    EditText firstName, surName, cell, kinFirstName, kinSurName, kinCell, houseAddr, kinTown, Town,
            occupation, kinoccupation, houseNo, kinhouseaddress, kinRelationship;
    Spinner genderSpinner, statusSpinner, statesSpinner, lgaSpinner, kingenderSpinner,
            kinstatusSpinner, kinstatesSpinner, kinlgaSpinner, regPointStateSpinner, regPointlgaSpinner;
    ArrayAdapter<CharSequence> genderAdapter, statusAdapter, statesAdapter, lgaAdapter,
            kingenderAdapter, kinstatusAdapter, kinstatesAdapter, kinlgaAdapter, regPointlgaAdapter;
    private StorageReference storageReference;
    FirebaseFirestoreSettings settings;
    byte[] bytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

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
//        regPointStateSpinner = findViewById(R.id.regPointStateSpinner);
//        regPointlgaSpinner = findViewById(R.id.regPointlgaSpinner);

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

        cell = findViewById(R.id.cellEditText);

        snapBtn = findViewById(R.id.snapBtn);
        saveBtn = findViewById(R.id.saveBtn);
        dateBtn = findViewById(R.id.datebtn);

        firestoreRef = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        firestoreData = new HashMap<>();
        storageReference = fStorage.getReference();
        reset();

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
//        regPointStateSpinner.setAdapter(statesAdapter);

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

        /*regPointStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regPointStateContent = parent.getItemAtPosition(position).toString().trim();

                setRegPointlgaSpinner(regPointStateContent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

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

        /*regPointlgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regPointlgaContent = parent.getItemAtPosition(position).toString().trim();

                zoneLogic(regPointlgaContent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        lgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lgatxt.setText(parent.getItemAtPosition(position).toString().trim());
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

                lga4StateSpinner(lga4state);
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

                noklgaSpinner(kinlga4state);
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

                firstNametxt = firstName.getText().toString().trim();
                surNametxt = surName.getText().toString().trim();
                gndr = gendertxt.getText().toString().trim();
                Mstatus = statustxt.getText().toString().trim();
                houseNum = houseNo.getText().toString().trim();
                houseAddress = houseAddr.getText().toString().trim();
                state = statetxt.getText().toString().trim();
                dobtxt = DOBtxt.getText().toString().trim();
                lga = lgatxt.getText().toString().trim();
                mOccupation = occupation.getText().toString().trim();
                cellNo = cell.getText().toString();
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

                final String date = String.valueOf(mCalendar.get(Calendar.DATE) + "/" + (mCalendar.get(Calendar.MONTH) + 1) +
                        "/" + mCalendar.get(Calendar.YEAR));

                if (firstNametxt.length() == 0 || surNametxt.length() == 0 || gndr.length() == 0 || Mstatus.length() == 0
                        || dobtxt.length() == 0 || houseNum.length() == 0 || houseAddress.length() == 0 || state.length() == 0
                        || cellNo.length() == 0 || cellNo.length() < 11 || cellNo.length() > 11 || lga.length() == 0
                        || mOccupation.length() == 0 || town.length() == 0 || kinFirstNametxt.length() == 0
                        || kinSurNametxt.length() == 0 || kingender.length() == 0 || kinstatus.length() == 0
                        || kinstatus.length() == 0 || kinrel.length() == 0 || kinstate.length() == 0
                        || kinlga.length() == 0 || kinhouseAd.length() == 0 || kintown.length() == 0 || kinOccupation.length() == 0
                        || kinCellNo.length() == 0 || bytes.length == 0/* || regPointZone.length() == 0*/) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Error")
                            .setCancelable(false)
                            .setMessage("All fields are required")
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .setPositiveButton("Got It!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
/*

                    if (cellNo.length() > 11 || cellNo.length() < 11){
                        AlertDialog.Builder phone_alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setTitle("Error")
                                .setCancelable(false)
                                .setMessage("Check Phone Number Field")
                                .setIcon(R.drawable.ic_cancel_black_24dp)
                                .setPositiveButton("Got It!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog phone_alertDialog = phone_alertDialogBuilder.create();
                        phone_alertDialog.show();
                    }
*/

                }else {
                    id = "234" + cellNo.substring(1, 11);
                    stateCode = state.substring(0, 3).toUpperCase();
                    LGAcode = lga.substring(0, 3).toUpperCase();
                    phone_code = cellNo.substring(7, 11);

                    _code = stateCode + "/" + LGAcode + "/" + phone_code;

                    String fullName = firstNametxt + " " + surNametxt;

                    uploadData(bytes, firstNametxt, surNametxt, gndr, dobtxt, Mstatus, cellNo, state, lga,
                            town, houseNum, houseAddress, mOccupation, kinFirstNametxt, kinSurNametxt,
                            kingender, kinstatus, kinstate, kinlga, kintown, kinhouseAd, kinCellNo,
                            kinOccupation, kinrel, /*regPointZone,*/ id, _code, date);

                    reset();
                    try {
                        textToImage(fullName, stateCode + "-" + LGAcode, cellNo);
                    } catch (WriterException w) {
                        w.printStackTrace();
                    }
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
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            bytes = outputStream.toByteArray();

            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void uploadData(byte[] mBytes, String firstName, String surName, String gender, String dob, String mStatus,
                            String cellNum, String state, String lga, String town, String houseNo, String houseAd,
                            String occupation, String kinfirstName, String kinsurName, String kingender, String kinmStatus,
                            String kinstate, String kinlga, String kinTown, String kinhouseAd, String kincellNo,
                            String kinoccupation, String kinrelationship, /*String regPointZoneContent,*/ String ID, String reg_code, String date_time) {

        StorageReference ref = storageReference.child("images/" + cellNum);

        ref.putBytes(mBytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Stored Online!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Connect to internet to Sync!", Toast.LENGTH_SHORT).show();
                }
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
        firestoreData.put("ID", ID);
        firestoreData.put("DATE & TIME", date_time);

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
//        firestoreData.put("REGISTRATION POINT", regPointZoneContent);


        firestoreData.put("Reg Code", reg_code);

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
                reset();
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
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.YEAR_FIELD);
        DOBtxt.setText(dateFormat.format(calendar.getTime()));
    }
    public void reset(){
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
    }


    private Bitmap textToImage(String name, String this_code, String phone) throws WriterException,
            NullPointerException {
        BitMatrix bitMatrix;
        final int height = 280;
        final int width = 280;

        String text = name + "\n" + this_code + "\n" + phone;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bytes = outputStream.toByteArray();

        String cellNo = cell.getText().toString().trim();
        id = "234" + cellNo.substring(1, 11);

        StorageReference barCodeRef = storageReference.child("images/Barcodes/" + id);

        barCodeRef.putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Barcode Stored Online!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Barcode not Stored Online!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return bitmap;
    }

    public void noklgaSpinner(String LGA) {

        if (LGA.equals("Adamawa")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_adamawa,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Kano")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kano,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Katsina")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_katsina,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Bauchi")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_bauchi,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Kaduna")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kaduna,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Borno")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_borno,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Sokoto")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_sokoto,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Kogi")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kogi,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Gombe")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_gombe,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Jigawa")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_jigawa,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Niger")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_niger,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Nassarawa")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_nassarawa,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Taraba")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_taraba,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Kebbi")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kebbi,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Plateau")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_plateau,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Yobe")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_yobe,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Kwara")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kwara,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Benue")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_benue,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Abia")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_abia,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Anambra")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_anambra,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Akwa Ibom")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_akwaIbom,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Bayelsa")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_bayelsa,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Cross River")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_crossRiver,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Delta")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_delta,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Ebonyi")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ebonyi,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Enugu")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_enugu,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Edo")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_edo,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Ekiti")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ekiti,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Imo")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_imo,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Lagos")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_lagos,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Ogun")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ogun,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Ondo")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ondo,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Osun")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_osun,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Oyo")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_oyo,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        } else if (LGA.equals("Rivers")) {
            kinlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_rivers,
                    android.R.layout.simple_spinner_item);
            kinlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kinlgaSpinner.setAdapter(kinlgaAdapter);
        }
    }


    public void lga4StateSpinner(String lga4state) {

        if (lga4state.equals("Adamawa")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_adamawa,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Kano")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kano,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Katsina")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_katsina,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Bauchi")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_bauchi,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Kaduna")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kaduna,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Borno")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_borno,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Sokoto")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_sokoto,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Kogi")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kogi,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Gombe")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_gombe,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Jigawa")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_jigawa,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Niger")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_niger,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Nassarawa")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_nassarawa,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Taraba")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_taraba,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Kebbi")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kebbi,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Plateau")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_plateau,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Yobe")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_yobe,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Kwara")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kwara,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Benue")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_benue,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Abia")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_abia,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Anambra")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_anambra,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Akwa Ibom")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_akwaIbom,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Bayelsa")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_bayelsa,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Cross River")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_crossRiver,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Delta")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_delta,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Ebonyi")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ebonyi,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Enugu")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_enugu,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Edo")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_edo,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Ekiti")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ekiti,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Imo")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_imo,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Lagos")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_lagos,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Ogun")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ogun,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Ondo")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ondo,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Osun")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_osun,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Oyo")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_oyo,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        } else if (lga4state.equals("Rivers")) {
            lgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_rivers,
                    android.R.layout.simple_spinner_item);
            lgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lgaSpinner.setAdapter(lgaAdapter);
        }
    }

    public void setRegPointlgaSpinner(String contentX) {

        if (contentX.equals("Adamawa")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_adamawa,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Kano")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kano,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Katsina")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_katsina,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Kaduna")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kaduna,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Borno")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_borno,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Sokoto")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_sokoto,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Bauchi")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_bauchi,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Kogi")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kogi,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Gombe")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_gombe,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Jigawa")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_jigawa,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Niger")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_niger,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Nassarawa")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_nassarawa,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Taraba")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_taraba,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Kebbi")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kebbi,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Plateau")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_plateau,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Yobe")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_yobe,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Kwara")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_kwara,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Benue")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_benue,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Abia")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_abia,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Anambra")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_anambra,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Akwa Ibom")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_akwaIbom,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Bayelsa")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_bayelsa,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Cross River")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_crossRiver,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Delta")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_delta,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Ebonyi")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ebonyi,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Enugu")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_enugu,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Edo")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_edo,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Ekiti")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ekiti,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Imo")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_imo,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Lagos")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_lagos,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Ogun")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ogun,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Ondo")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_ondo,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Osun")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_osun,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Oyo")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_oyo,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        } else if (contentX.equals("Rivers")) {
            regPointlgaAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.lga_rivers,
                    android.R.layout.simple_spinner_item);
            regPointlgaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regPointlgaSpinner.setAdapter(regPointlgaAdapter);
        }
    }


    void zoneLogic(String rPointlga) {

        String caps = rPointlga.toUpperCase();
        if (caps.equals("UMUNNEOCHI") || caps.equals("ISUKWUATO") || caps.equals("OHAFIA")
                || caps.equals("AROCHUKWU") || caps.equals("BENDE")) {

            regPointZone = "Abia_North";

        }

        String[] abia_north = {"UMUNNEOCHI", "ISUKWUATO", "OHAFIA", "AROCHUKWU", "BENDE"};
        String[] abia_central = {"UMUAHIA NORTH", "UMUAHIA SOUTH", "IKWUANO", "ISIALA NGWA NORTH", "ISIALA NGWA SOUTH"};
        String[] abia_south = {"ABA NORTH", "ABA SOUTH", "UGWUNAGBO", "OBINGWA", "UKWA EAST", "UKWA WEST", "OSISIOMA"};

        String[] akwaibom_north_east = {"ETINAN", "IBESIKPO ASUTAN", "IBIONO IBOM", "ITU", "NSIT ATAI", "NSIT IBOM", "NSIT UBIUM", "URUAN", "UYO"};
        String[] akwaibom_north_west = {"ABAK", "ESSIEN UDIM", "ETIM EKPO", "IKA", "IKONO", "IKOT EKPENE", "INI", "OBOT AKARA", "ORUK ANAM", "UKANAFUN"};
        String[] akwaibom_south = {"EASTERN OBOLO", "EKET", "ESIT EKET", "IBENO", "IKOT ABASI", "MBO", "MKPAT ENIN", "OKOBO", "ONNA", "ORON", "UDUNG UKO", "URUE OFFONG", "ORUKO"};

        String[] adamawa_north = {"MADAGALI", "MAIHA", "MICHIKA", "MUBI NORTH", "MUBI SOUTH"};
        String[] adamawa_south = {"DEMSA", "GANYE", "GUYUK", "JADA", "MAYO-BELWA", "NUMAN", "SHELLENG", "TOUNGO", "LAMURDE"};
        String[] adamawa_central = {"HONG", "FUFORE", "SONG", "YOLA NORTH", "YOLA SOUTH", "GERFI"};

        String[] anambra_north = {"ONITSHA NORTH", "ONITSHA SOUTH", "OYI", "OGBARU", "ANAMBRA EAST", "ANAMBRA WEST", "AYAMELUM"};
        String[] anambra_central = {"AWKA NORTH", "AWKA SOUTH", "NJIKOKA", "ANAOCHA", "IDEMILI NORTH", "IDEMILI SOUTH", "DUNUKOFIA"};
        String[] anambra_south = {"IHIALA", "NNEWI NORTH", "NNEWI SOUTH", "ORUMBA SOUTH", "ORUMBA NORTH", "AGUATA", "EKWUSIGO"};

        String[] bauchi_south = {"ALKALERI", "KIRFI", "BAUCHI", "TAFAWA BALEWA", "BOGORO", "DASS", "TORO"};
        String[] bauchi_central = {"NINGI", "WARJI", "DARAZO", "GANJUWA", "MISAU", "DAMBAN", "DARAZO"};
        String[] bauchi_north = {"ZAKI", "GAMAWA", "JAMA’ARE", "ITAS-GADAU", "SHIRA", "GIADE", "KATAGUM", "AZARE"};

        String[] bayelsa_east = {"BRASS", "NEMBE", "OGBIA"};
        String[] bayelsa_central = {"KOLOKUMA", "OPOKUMA", "SOUTHERN IJAW", "YENAGOA"};
        String[] bayelsa_west = {"EKEREMOR", "SAGBAMA"};

        String[] benue_north_east = {"KATSINA-ALA", "KONSHISHA", "KWANDE", "LOGO", "UKUM", "USHONGO", "VANDEIKYA"};
        String[] benue_north_west = {"BURUKU", "GBOKO", "GUMA", "GWER-EAST", "GWER-WEST", "MARKURDI", "TARKA"};
        String[] benue_south = {"ADO", "AGATU", "APA", "OBI", "OGBADIBO", "OHIMINI", "OJU", "OKPOKWU", "OTUKPO"};

        String[] borno_north = {"ABADAM", "GUBIO", "GUZAMALA", "KUKAWA", "MAGUMERI", "MARTE", "MOBBAR", "NGANZA", "MOGUNO"};
        String[] borno_central = {"DIKWA", "JERE", "KALA BALGE", "MAFA", "METROPOLITAN", "NGALA", "KONDUGA", "BAMA", "KAGA"};
        String[] borno_south = {"ASKIRA", "UBA", "BAYO", "BIU", "CHIBOK", "DAMABOA", "GWOZA", "HAWUL", "KWAYA", "SHANI"};

        String[] crossR_north = {"BEKWARRA", "OBANLIKU", "OBUDU", "OGOJA", "YALA", "BIASE"};
        String[] crossR_central = {"ABI", "BOKI", "ETUNG", "IKOM", "OBUBRA", "YAKURR"};
        String[] crossR_south = {"CALABAR MUNICIPAL", "CALABAR SOUTH", "AKAMKPA", "AKPABUYO", "BAKASSI", "ODUKPANI"};

        String[] delta_central = {"ETHIOPE EAST", "ETHIOPE WEST", "OKPE", "SAPELE", "UDU", "UGHELLI NORTH", "UGHELI SOUTH", "UVWIE"};
        String[] delta_north = {"ANIOCHA NORTH", "ANIOCHA SOUTH", "IKA NORTH-EAST", "IKA SOUTH", "NDOKWA EAST", "NDOKWA WEST", "OSHIMILI NORTH", "OSHIMILI SOUTH", "UKWUANI"};
        String[] delta_south = {"BOMADI", "BURUTU", "ISOKO NORTH", "ISOKO SOUTH", "PATANI", "WARRI NORTH", "WARRI SOUTH"};

        String[] ebonyi_north = {"ABAKALIKI", "EBONYI", "IZZI", "OHAUKWU"};
        String[] ebonyi_central = {"EZZA NORTH", "EZZA SOUTH", "IKWO", "ISHIELU"};
        String[] ebonyi_south = {"AFIKPO NORTH", "AFIKPO SOUTH", "IVO", "OHAOZARA", "ONICHA"};

        String[] edo_central = {"ESAN CENTRAL", "ESAN NORTH EAST", "ESAN SOUTH EAST", "ESAN WEST", "IGUEBEN"};
        String[] edo_north = {"AKOKO EDO", "ETSAKO EAST", "ETSAKO CENTRAL", "ETSAKO WEST", "OWAN EAST", "OWAN WEST"};
        String[] edo_south = {"OREDO", "ORHIONMWON", "OVIA NORTH EAST", "OVIA SOUTH WEST", "EGOR", "UHUNMWODE", "IKPOBA OKHA"};

        String[] ekiti_north = {"IKOLE", "OYE", "IDO", "OSI", "MOBA", "ILEJEMEJE"};
        String[] ekiti_central = {"ADO EKITI", "IREPODUN", "IFELODUN", "IJERO", "EFON", "EKITI WEST"};
        String[] ekiti_south = {"EKITI SOUTH WEST", "IKERE", "EMURE", "ISE", "ORUN", "GBONYIN", "EKITI EAST"};

        String[] enugu_east = {"ENUGU EAST", "ENUGU NORTH", "ENUGU SOUTH", "ISI-UZO", "NKANU EAST", "NKANU WEST"};
        String[] enugu_west = {"ANINIRI", "AWGU", "EZEAGU", "OJI RIVER", "UDI"};
        String[] enugu_north = {"IGBO-ETITI", "IGBO-EZE NORTH", "IGO-EZE SOUTH", "UZO UWANI", "UDENU", "NSUKKA"};

        String[] gombe_central = {"AKKO", "YAMALTU", "DEBA"};
        String[] gombe_south = {"BALANGA", "BILLIRI", "KALTUNGO", "SHONGOM"};
        String[] gombe_north = {"FUNAKAYE", "NAFADA", "GOMBE", "KWAMI", "DUKKU"};

        String[] imo_east = {"ABOH MBAISE", "AHIAZU MBAISE", "EZINIHITTE", "IKEDURU", "MBAITOLI", "NGOR OKPALA", "OWERRI MUNICIPAL", "OWERRI NORTH", "OWERRI WEST"};
        String[] imo_west = {"IDEATO NORTH", "IDEATO SOUTH", "ISU", "NJABA", "NKWERE", "NWANGELE", "OGUTA", "OHAJI", "EGBEMA", "ORLU", "ORSU", "ORU WEST", "ORU EAST"};
        String[] imo_north = {"EHIME MBANO", "IHITE", "UBOMA", "ISIALA MBANO", "OBOWO", "OKIGWE", "ONUIMO"};

        String[] jigawa_south_west = {"BIRNIN KUDU", "BUJI", "DUTSE", "GWARAM", "KIYAWA", "JAHUN", "MIGA"};
        String[] jigawa_north_east = {"AUYO", "BIRNIWA", "GURI", "HADEJIA", "KAUGAMA", "KAFIN HAUSA", "KIRI KASAMMA", "MALLAM MADORI"};
        String[] jigawa_north_west = {"BABURA", "BABURA", "GARKI", "GWIWA", "KAZAURE", "MAIGATARI", "RONI", "RINGIM", "SULE TANKARKAR", "TAURA", "YANKWASHI", "GUMEL"};


        String[] kaduna_north = {"KUBAU", "IKARA", "MAKARFI", "SOBA", "SABON GARI", "ZARIA", "LERE", "KUDAN"};
        String[] kaduna_central = {"BIRNIN GWARI", "GIWA", "IGABI", "KADUNA NORTH", "KADUNA SOUTH", "CHIKUN", "KAJURU"};
        String[] kaduna_south = {"JEMA’A", "JABA", "KAURA", "ZANGON-KATAF", "KAURA", "KACHIA", "KAGARKO", "SANGA"};

        String[] kano_central = {"DALA", "GWALE", "DAWAKIN TOFA", "GEZAWA", "TARAUNI", "FAGGE", "GARUM MALLAM",
                "KANO MUNICIPAL", "KUMBOTSO", "KURA", "MADOBI", "MINJIBIR", "NASSARAWA", "UNGOGO", "WARAWA"};
        String[] kano_south = {"BICHI", "SHANONO", "BAGWAI", "DANBATTA", "MAKODA", "DAWAKIN KUDU", "GABASAWA", "GWARZO", "KABO",
                "RIMI GADO", "TOFA", "TSANYAWA", "KUNCHI", "KARAYE"};
        String[] kano_north = {"ALBASU", "BEBEJI", "BUNKURE", "DOGUWA", "GAYA", "KIRU", "RANO", "TAKAI", "AJINGI", "ROGO", "KIBIYA",
                "TUDUN WADA", "GARKO", "WUDIL", "SUMAILA"};

        String[] katsina_north = {"DAURA", "ZANGO", "MAIADUA", "MASHI", "MANI", "INGAWA", "BINDAWA", "SANDAMU", "DUTSI", "KANKIA", "KUSADA", "BAURE"};
        String[] katsina_south = {"FUNTUA", "FASKARI", "DANJA", "DANDUME", "BAKORI", "KANKARA", "MALUMFASHI", "KAFUR", "MUSAWA", "MATAZU", "SABUWA"};
        String[] katsina_central = {"KATSINA", "KAITA", "JIBIA", "BATSARI", "SAFANA", "DUTSIN-MA", "KURFI", "BATAGARAWA", "RIMI", "CHARANCHI", "DANMUSA"};

        String[] kebbi_north = {"AREWA", "ARGUNGU", "AUGIE", "BAGUDO", "DANDI", "SURU", "JEGA"};
        String[] kebbi_central = {"ALEIRO", "BIRNIN KEBBI", "BUNZA", "GWANDU", "KALGO", "KOKO BESSE", "MAIYAMA"};
        String[] kebbi_south = {"FAKAI", "NGASKI", "SAKABA", "SHANGA", "WASAGU", "DANKO", "YAURI", "ZURU"};

        String[] kogi_central = {"ADAVI", "AJAOKUTA", "OGORI", "MAGONGO", "OKEHI", "OKENE"};
        String[] kogi_east = {"ANKPA", "BASSA", "DEKINA", "IBAJI", "IDAH", "IGALAMELA-ODOLU", "OFU", "OLAMABORO", "OMALA"};
        String[] kogi_west = {"IJUMU", "KABBA", "KOGI", "LOKOJA", "MOPAMURO", "YAGBA EAST", "YAGBA WEST"};

        String[] kwara_north = {"BARUTEN", "EDU", "PATIGI", "KAIAMA", "MORO"};
        String[] kwara_central = {"ASA", "ILORIN EAST", "ILORIN SOUTH", "ILORIN WEST", "OFFA"};
        String[] kwara_south = {"EKITI", "OKE-ERO", "IFELODUN", "IREPODUN", "ISIN", "OYUN"};

        String[] lagos_central = {"LAGOS ISLAND", "LAGOS MAINLAND", "SURULERE", "APAPA", "ETI-OSA"};
        String[] lagos_east = {"SHOMOLU", "KOSOFE", "EPE", "IBEJU-LEKKI", "IKORODU"};
        String[] lagos_west = {"AGEGE", "IFAKO-IJAYE", "ALIMOSHO", "BADAGRY", "OJO", "AJEROMI", "IFELODUN", "AMUWO-ODOFIN", "OSHODI", "ISOLO", "IKEJA", "MUSHIN"};

        String[] nassarawa_north = {"AKAWANGA", "NASSARAWA EGGON", "WAMBA"};
        String[] nassarawa_west = {"NASSARAWA", "KEFFI", "KOKONA", "KARU", "TOTO"};
        String[] nassarawa_south = {"LAFIA", "AWE", "DOMA", "KEANA", "OBI"};

        String[] niger_east = {"BOSSO", "CHACHANGA", "GURARA", "PAIKORO", "RAFI", "SHIRORO", "MUYA", "SULEJA", "TAFA"};
        String[] niger_north = {"AGWARA", "BORGU", "KONTOGORA", "MARIGA", "RIJAU", "WUSHISHI", "MASHEGU MAGAMA"};
        String[] niger_south = {"AGAIE", "BIDA", "KATCHA", "BATAGI", "LAPAI", "LAVUN", "EDATI-IDATI", "MOKWA"};

        String[] ogun_central = {"IFO", "EWEKORO", "OBAFEMI", "OWODE", "ABEOKUTA NORTH", "ABEOKUTA SOUTH", "ODEDA"};
        String[] ogun_east = {"SAGAMU", "IKENNE", "REMO NORTH", "IJEBU-ODE", "ODOGBOLU", "IJEBU NORTH EAST", "IJEBU NORTH", "IJEBU EAST", "OGUN WATERSIDE"};
        String[] ogun_west = {"IMEKO AFON", "EGBADO NORTH", "EGBADO SOUTH", "IPOKIA", "ADO-ODO", "OTA"};

        String[] ondo_north = {"AKOKO NORTH EAST", "AKOKO NORTH WEST", "AKOKO SOUTH WEST", "AKOKO SOUTH EAST", "OSE", "OWO"};
        String[] ondo_central = {"AKURE NORTH", "AKURE SOUTH", "IFEDORE", "IDANRE", "ONDO EAST", "ONDO WEST"};
        String[] ondo_south = {"ILEOLUJI", "OKEIGBO", "ODIGBO", "IRELE", "OKITIPUPA", "ESE-ODO", "ILAJE"};

        String[] osun_central = {"BORIPE", "BOLOWADURO", "IFELODUN", "ILA", "IFEDAYO", "IREPODUN", "OROLU", "ODO-OTIN", "OLORUNDA", "OSOGBO"};
        String[] osun_east = {"ATAKUNMOSA EAST", "ATAKUNMOSA WEST", "IFE CENTRAL", "IFE EAST", "IFE NORTH", "IFE SOUTH", "ILESA EAST", "ILESA WEST", "OBOKUN", "ORIADE"};
        String[] osun_west = {"AYADAADE", "AYEDIRE", "EDE NORTH", "EDE SOUTH", "EGBEDORE", "EJIGBO", "IREWOLE", "ISOKAN", "IWO", "OLA-OLUWA"};

        String[] oyo_central = {"AFIJIO", "AKINYELE", "EGBEDA", "OGO OLUWA", "SURULERE", "LAGELU", "OLUYOLE", "ONA-ARA", "OYO EAST", "OYO WEST", "ATIBA"};
        String[] oyo_north = {"SAKI WEST", "SAKI EAST", "ATIGBO", "IREPO", "OLORUNSOGO", "KAJOLA", "IWAJOWA", "OGBOMOSHO NORTH", "OGBOMOSHO SOUTH", "ISEYIN", "OORELOPE", "ORIRE", "ITESIWAJU"};
        String[] oyo_south = {"IBADAN NORTH", "IBADAN NORTH EAST", "IBADAN NORTH WEST", "IBADAN SOUTH EAST", "IBADAN SOUTH WEST", "IBARAPA CENTRAL", "IBARAPA NORTH", "IBARAPA EAST", "IDO"};

        String[] plateau_south = {"LANTANG NORTH", "LANTANG SOUTH", "MIKANG", "QUA’AN PAN", "SHENDAM", "WASE"};
        String[] plateau_central = {"BOKKOS", "KANKE", "MANGU", "PANKSHIN", "KANAM"};
        String[] plateau_north = {"BARKIN LADI", "BASSA", "JOS EAST", "JOS NORTH", "JOS SOUTH", "RIYOM"};

        String[] rivers_east = {"ETCHE", "OMUMA", "IKWERRE", "OBIO AKPOR", "PORT HARCOURT", "OKIRIKA", "OGU BOLO", "EMOHUA"};
        String[] rivers_south_east = {"OPOBO", "NKORO", "ANDONI", "OYIGBO", "TAI", "ELEME", "GOKANA", "KHANA"};
        String[] rivers_west = {"ASARI-TORU", "AKUKU-TORU", "DEGEMA", "OGBA", "EGBEMA", "NDONI", "ABUA-ODUAL", "AHOADA EAST", "AHOADA WEST", "BONNY"};

        String[] sokoto_east = {"ISA", "SABON BIRNI", "WURNO", "GORONYO", "RABAH", "GADA", "ILLELA", "GWADABAWA"};
        String[] sokoto_north = {"TANGAZA", "BINJI", "SILAME", "GUDU", "KWARE", "WAMAKKO", "SOKOTO NORTH", "SOKOTO SOUTH"};
        String[] sokoto_south = {"DANGE SHUNI", "TURETA", "BODINGA", "SHAGARI", "YABO", "TAMBUWA", "KEBBE"};

        String[] taraba_south = {"WUKARI", "IBI", "DONGA", "USSA", "TAKUM"};
        String[] taraba_central = {"SARDAUNA", "KURMI", "BALI", "GASHAKA", "GASSOL"};
        String[] taraba_north = {"JALINGO", "YORRO", "ZING", "LAU", "ARDO-KOLA", "KARIM-LAMIDO"};

        String[] yobe_east = {"BURSARI", "GEIDAM", "GUJBA", "GULANI", "TARMUWA", "YUNUSARI"};
        String[] yobe_north = {"BADE", "JAKUSKO", "MACHINA", "KARASUWA", "NGURU", "YUSUFARI"};
        String[] yobe_south = {"FIKA", "FUNE", "NANGERE", "POTISKUM", "DAMATURU"};

        String[] zamfara_north = {"KAURAN NAMODA", "SHINKAFI", "ZURMI", "BIRNIN MAGAJI", "T/MAFARA"};
        String[] zamfara_central = {};
        String[] zamfara_west = {};
    }
}