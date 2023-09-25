package com.suraj.accurateotp2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.suraj.accurateotp2023.newLicensePeriod.NewLicensePeriodDialogHelper;
import com.suraj.accurateotp2023.newServiceOTP.GenerateNewServiceOTPLogic;
import com.suraj.accurateotp2023.oldPrintCountOTP.GenerateOldPrintCountOTPLogic;
import com.suraj.accurateotp2023.oldPrintCountOTP.OldPrintCountDialogHelper;
import com.suraj.accurateotp2023.util.ConnectionCheck;
import com.suraj.accurateotp2023.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private OldPrintCountDialogHelper printCountDialogHelper;
    private GenerateOldPrintCountOTPLogic oldPrintCountGenerator;

    private GenerateNewServiceOTPLogic newServiceOTPGenerator;
    ImageView img, imglogo;
    TextView txtresult, nameser, wnames, Or;
    Button scan, ServiceScan, ChooseCode;
    Uri uri, imageUri;
    RequestQueue requestQueue;
    public Bitmap mbitmap;
    public static final int PICK_IMAGE = 1;
    CardView cardViewButton = null;
    CardView cardViewButton1 = null;
    CardView gcardViewButton = null;
    CardView gcardViewButton1 = null;
    EditText editText = null;
    String clientName = " ";

    String oldPrintCountClientName = "";



    String newPrintCountDealerName = "";
    int newPrintCountDealerId = 0;

    String gname = "";
    String gversion = "";
    String gOTPCODE = "", Rights = "";
    TextView textViewVersion, RemainingCount;
    EditText textViewName, ServiceEdit, gtextViewName, PrintID,gtextViewVersion, EditTxtDays, EnterManualPrintId;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    int remainingCounts = 0, currentLoggedInUserId = 0;
    String currentLoggedInUserName = "";
    String wname;
    Dialog dialofForOldPrintCountOTP, pdialog, dialogForNewPrintCountOTP;
    ProgressDialog pd;
    EditText A3PrintCountEditTxt, A4PrintCountEditTxt, A5PrintCountEditTxt;
    TextView RawDialogCode, RawVersion;
    EditText RawDialogVersion;
    int TotalCount;
    String phone ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RemainingCount = findViewById(R.id.remainpc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        preferences = getSharedPreferences(Util.AcuPrefs, MODE_PRIVATE);
        editor = preferences.edit();
        remainingCounts = Integer.parseInt(preferences.getString(Util.count, ""));
        wname = preferences.getString(Util.Name, "");

        currentLoggedInUserId = preferences.getInt(Util.id, 0);
        currentLoggedInUserName = preferences.getString(Util.Name, "");
        Rights = preferences.getString(Util.Rights,"");
        phone = preferences.getString(Util.Phone,"");
        Log.w("Rights", Rights);
        requestQueue = Volley.newRequestQueue(this);
        scan = (findViewById(R.id.scan));
        img = (findViewById(R.id.imgview));
        ServiceEdit = findViewById(R.id.editser);
        Or = findViewById(R.id.tvs);
        wnames = findViewById(R.id.wname);
        nameser = findViewById(R.id.namenn);
        imglogo = findViewById(R.id.logoimg);
        ServiceScan = findViewById(R.id.scanservice);
        ChooseCode = findViewById(R.id.slectimg);
        txtresult = (findViewById(R.id.txtResult));
        scan.setVisibility(View.GONE);
        pd = new ProgressDialog(this);
        pd.setMessage("Generating OTP..");
        pd.setCancelable(false);
        oldPrintCountGenerator = new GenerateOldPrintCountOTPLogic();
        newServiceOTPGenerator = new GenerateNewServiceOTPLogic();
        printCountDialogHelper = new OldPrintCountDialogHelper(this, this); // Pass the instance of MainActivity

        ServiceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ser = ServiceEdit.getText().toString();
                int ll = ser.length();
                if (ll > 11) {
                    ServiceScan.setVisibility(View.VISIBLE);
                    Or.setVisibility(View.GONE);
                    ChooseCode.setVisibility(View.GONE);
                }
                if (ll < 12) {
                    ServiceScan.setVisibility(View.GONE);
                    Or.setVisibility(View.VISIBLE);
                    ChooseCode.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getUserRights();
    }


    public void btnScan(View view) {

        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        Frame frame = new Frame.Builder()
                .setBitmap(mbitmap)
                .build();

        SparseArray<Barcode> barcodeSparseArray = detector.detect(frame);

        if (barcodeSparseArray.size() > 0) {
            Barcode result = barcodeSparseArray.valueAt(0);
            String serviceOTP = scanTxt(result.rawValue);
            Log.w("ehserviceotp",serviceOTP);

        } else {
            Toast.makeText(this, "Select a QR code Or QR Code is Not Clear", Toast.LENGTH_SHORT).show();
        }
    }

    public void SelectImg(View view) {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Qr Code"), PICK_IMAGE);
        txtresult.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            uri = data.getData();
            try {
                mbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                img.setImageBitmap(mbitmap);
                imglogo.setVisibility(View.GONE);
                nameser.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                wnames.setVisibility(View.GONE);
                ServiceEdit.setVisibility(View.GONE);
                Or.setVisibility(View.GONE);


                scan.setVisibility(View.VISIBLE);
            } catch (IOException e) {

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 101, 0, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 101:
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(this, LoginScreenActivity.class));
                break;
        }
        return true;
    }


    void showDialogForNewPrintCountOTP(String rawtxt) {

        String [] SplitRawCodeValues = rawtxt.split(";");

        dialogForNewPrintCountOTP = new Dialog(MainActivity.this);
        dialogForNewPrintCountOTP.setContentView(R.layout.new_printcount_dialogbox);
        dialogForNewPrintCountOTP.setTitle("New Print Count OTP");
        dialogForNewPrintCountOTP.setCancelable(true);
        EnterManualPrintId = dialogForNewPrintCountOTP.findViewById(R.id.enter_print_id);
        RawDialogCode = dialogForNewPrintCountOTP.findViewById(R.id.txtarcode);


        if(rawtxt.isEmpty())
        {
            EnterManualPrintId.setVisibility(View.VISIBLE);

        }

        else {
            RawDialogCode.setVisibility(View.VISIBLE);
            RawDialogCode.setText(SplitRawCodeValues[0]);

        }




        cardViewButton = dialogForNewPrintCountOTP.findViewById(R.id.generate);
        cardViewButton1 = dialogForNewPrintCountOTP.findViewById(R.id.cancel);
        A3PrintCountEditTxt = dialogForNewPrintCountOTP.findViewById(R.id.a3prtcount);
        A4PrintCountEditTxt = dialogForNewPrintCountOTP.findViewById(R.id.a4prtcount);
        A5PrintCountEditTxt = dialogForNewPrintCountOTP.findViewById(R.id.a5prtcount);
        textViewName = dialogForNewPrintCountOTP.findViewById(R.id.textName);
        RawDialogVersion = dialogForNewPrintCountOTP.findViewById(R.id.EDTextVersion);




        if(SplitRawCodeValues.length>1)
        {
            textViewName.setText(SplitRawCodeValues[1]);


        }


        if(SplitRawCodeValues.length>2)
        {
            RawDialogVersion.setText(SplitRawCodeValues[2]);


        }
        Log.w("lengthwalig", ""+SplitRawCodeValues.length);
        if(SplitRawCodeValues.length > 3)
        {
            newPrintCountDealerName = SplitRawCodeValues[3];
            Log.w("ddm", newPrintCountDealerName);
        }

        if(SplitRawCodeValues.length>4)
        {
            newPrintCountDealerId = Integer.parseInt(SplitRawCodeValues[4]);
            Log.w("ddmid",""+ newPrintCountDealerId);

        }




        cardViewButton.setOnClickListener(view -> {

            Toast.makeText(this, ""+RawDialogCode.getText().toString(), Toast.LENGTH_SHORT).show();

            int A3PrintCountValue = 0;
            int A4PrintCountValue = 0;
            int A5PrintCountValue = 0;


            if (A3PrintCountEditTxt.getText().toString().isEmpty() && A4PrintCountEditTxt.getText().toString().isEmpty() && A5PrintCountEditTxt.getText().toString().isEmpty()){
                A3PrintCountEditTxt.setError("Enter the Value of Print Count");
                A3PrintCountEditTxt.requestFocus();

            }
            if(!A3PrintCountEditTxt.getText().toString().isEmpty())
            {
                A3PrintCountValue = Integer.parseInt(A3PrintCountEditTxt.getText().toString());


            }

            if(!A4PrintCountEditTxt.getText().toString().isEmpty())
            {
                A4PrintCountValue = Integer.parseInt(A4PrintCountEditTxt.getText().toString());


            }

            if(!A5PrintCountEditTxt.getText().toString().isEmpty())
            {
                A5PrintCountValue = Integer.parseInt(A5PrintCountEditTxt.getText().toString());


            }

            if (A3PrintCountValue <= 0 || A4PrintCountValue<=0 || A5PrintCountValue<=0) {


            }


            int[] printcountvaluesarray
                    = {A3PrintCountValue, A4PrintCountValue, A5PrintCountValue};

            Log.w("array elements", Arrays.toString(printcountvaluesarray));


            if (ConnectionCheck.isConnected(connectivityManager, networkInfo, MainActivity.this)) {
                //  if (isVarified()) {
                Log.w("562 wali if","Working");
                //  printCount = Integer.parseInt(editText.getText().toString());
                TotalCount = A3PrintCountValue + A4PrintCountValue + A5PrintCountValue;
                Log.w("Totalcount572",""+TotalCount);
                Log.w("RmnCount",""+ remainingCounts);
                if (remainingCounts > TotalCount || remainingCounts == TotalCount) {
                    Log.w("ifisfk","done");
                    remainingCounts = remainingCounts -  TotalCount;
                    clientName = textViewName.getText().toString();
                    //   uploadDetails();
                    newPrintCountOTPLogic(rawtxt, printcountvaluesarray);
                    uploadDetailsForNewPrintCount();
                } else {
                    Toast.makeText(MainActivity.this, "Unable to Generate Print Count OTP", Toast.LENGTH_LONG).show();
                }
                // }
            } else {
                Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            }



        });

        cardViewButton1.setOnClickListener(view -> dialogForNewPrintCountOTP.dismiss());


        dialogForNewPrintCountOTP.show();

    }

    private void newPrintCountOTPLogic(String rawPrintCountOtp, int[] printcountvaluesarray) {


        List<Integer> arrayIndex = new ArrayList<>();
        int compiledPrintCount0= 0, compiledPrintCount1= 0, compiledPrintCount2= 0;
        String FinalGroup1 = null, FinalGroup2 = null,FinalGroup3 = null;;



        for(int i=0; i<printcountvaluesarray.length; i++)
        {
            Log.w("prctval",""+printcountvaluesarray[i]);
            if(i==0)
            {
                compiledPrintCount0 = printcountvaluesarray[0]/50;

                Log.w("printcountvaluesarray", String.valueOf(compiledPrintCount0));
                Log.w("indexatrayda",""+i);


            }

            if(i==1)
            {

                compiledPrintCount1 = printcountvaluesarray[1]/50;

                Log.w("oneif", String.valueOf(compiledPrintCount1));
                Log.w("oneifindexatrayda",""+i);



            }

            if(i==2)
            {
                compiledPrintCount2 = printcountvaluesarray[2]/50;

                Log.w("oneif", String.valueOf(compiledPrintCount2));
                Log.w("oneifindexatrayda",""+i);


            }

            arrayIndex.add(i);



        }


        Log.w("rawcode",rawPrintCountOtp.replaceAll("\\s", ""));



//         Step 1 add count to last three digits

        // A3

        if(arrayIndex.contains(0))
        {


            int Group3 = Integer.parseInt(rawPrintCountOtp.replaceAll("\\s", "").substring(9,12));
            Log.w("gg3",""+Group3);


            int Group3Total = Group3+compiledPrintCount0;
            Log.w("gg3ttl",""+Group3Total);
            if(rawPrintCountOtp.charAt(9) == '0')

            {

                FinalGroup3 = rawPrintCountOtp.charAt(8) + "0" + Group3Total;

            }

            else {
                FinalGroup3 = ""+rawPrintCountOtp.charAt(8) + ""+Group3Total;

            }
            Log.w("FinalGroup3",FinalGroup3);

        }

        // A4

        if(arrayIndex.contains(1))
        {

            int Group2 = Integer.parseInt(rawPrintCountOtp.replaceAll("\\s", "").substring(5,8));
            Log.w("gg2",""+Group2);


            int Group2Total = Group2+compiledPrintCount1;
            Log.w("Group2Total",""+Group2Total);
            Log.w("compiledPrintCount",""+compiledPrintCount1);

            Log.w("charAt(5)",""+rawPrintCountOtp.charAt(5));
            if(rawPrintCountOtp.charAt(5) == '0')

            {

                FinalGroup2 = rawPrintCountOtp.charAt(4) + "0" + Group2Total;
                Log.w("ifrawPrintCountOtp.charAt(4)",""+rawPrintCountOtp.charAt(4));
                Log.w("ifGroup2Total",""+Group2Total);
                Log.w("ifFinalGroup2",FinalGroup2);

            }

            else {
                FinalGroup2 = ""+rawPrintCountOtp.charAt(4) + ""+Group2Total;

            }
            Log.w("FinalGroup2",FinalGroup2);

        }
        // A5

        if(arrayIndex.contains(2))
        {

            int Group1 = Integer.parseInt(rawPrintCountOtp.substring(1,4));
            Log.w("Group1",""+Group1);


            int Group1Total = Group1+compiledPrintCount2;
            Log.w("Group1Total",""+Group1Total);
            if(rawPrintCountOtp.charAt(1) == '0')
            {

                FinalGroup1 = rawPrintCountOtp.charAt(0) + "0" + Group1Total;

            }

            else {
                FinalGroup1 = ""+rawPrintCountOtp.charAt(0) + ""+Group1Total;

            }
            Log.w("FinalGroup1",FinalGroup1);
        }

//         Step 1 add count to last three digits

        char G1_1;
        int G1_2 , G1_3 ,G1_4;
        G1_1 = FinalGroup1 != null ? FinalGroup1.charAt(0) : 0;
        Log.w("bgh1",""+G1_1);
        if(G1_1=='P')
        {
            G1_1 = '8';

        } else if (G1_1=='A') {
            G1_1 = '3';

        } else if (G1_1=='C') {
            G1_1 = '9';
        } else if (G1_1=='M') {
            G1_1 = '2';
        }
        Log.w("gh1",""+G1_1);

//  Separate Second digit of Group1 and add 4 to it
        G1_2 =  Character.getNumericValue(FinalGroup1 != null ? FinalGroup1.charAt(1) : 0) + 4;
        if(G1_2>9)
        {
            G1_2 = G1_2 - 10;
        }

        Log.w("gh2",""+G1_2);

        //  Separate Third digit of Group1 and add 3 to it

        G1_3 = Character.getNumericValue(FinalGroup1 != null ? FinalGroup1.charAt(2) : 0) + 3;

        Log.w("bgh3",""+G1_3);


        if(G1_3>9)
        {

            G1_3 = G1_3 - 10;
        }

        Log.w("gh3",""+G1_3);

        // Separate Fourth digit of Group1 and add 2 to it

        G1_4 = Character.getNumericValue(FinalGroup1 != null ? FinalGroup1.charAt(3) : 0) + 2;
        if(G1_4>9)
        {
            G1_4 = G1_4 - 10;

        }

        Log.w("gh4",""+G1_4);

        // Again form Group 1 by concatenating all 4 digits.

        FinalGroup1 = ""+G1_1 + ""+G1_2 + ""+G1_3 + ""+G1_4;
        Log.w("Lastfinalgoup", FinalGroup1);


// Group 1 ends

        // Group 2

        char G2_1;
        int G2_2 , G2_3 ,G2_4;
        G2_1 = FinalGroup2 != null ? FinalGroup2.charAt(0) : 0;
        Log.w("bgh1",""+G2_1);
        if(G2_1=='E')
        {
            G2_1 = '1';

        } else if (G2_1=='D') {
            G2_1 = '4';

        } else if (G2_1=='H') {
            G2_1 = '6';
        } else if (G2_1=='W') {
            G2_1 = '7';
        }
        Log.w("gh12",""+G2_1);

//  Separate Second digit of Group1 and add 4 to it
        G2_2 =  Character.getNumericValue(FinalGroup2 != null ? FinalGroup2.charAt(1) : 0) + 8;
        if(G2_2>9)
        {
            G2_2 = G2_2 - 10;
        }

        Log.w("gh22",""+G2_2);

        //  Separate Third digit of Group1 and add 3 to it

        G2_3 = Character.getNumericValue(FinalGroup2 != null ? FinalGroup2.charAt(2) : 0) + 7;

        Log.w("bgh3",""+G2_3);


        if(G2_3>9)
        {

            G2_3 = G2_3 - 10;
        }

        Log.w("gh33",""+G2_3);

        // Separate Fourth digit of Group1 and add 2 to it
        G2_4 = Character.getNumericValue(FinalGroup2.charAt(3));
        Log.w("bbcg24",""+G2_4);

        G2_4 = Character.getNumericValue(FinalGroup2 != null ? FinalGroup2.charAt(3) : 0) + 6;
        Log.w("G2_44",""+G2_4);
        if(G2_4>9)

        {
            G2_4 = G2_4 - 10;

        }

        Log.w("gh44",""+G2_4);

        // Again form Group 1 by concatenating all 4 digits.

        FinalGroup2 = ""+G2_1 + ""+G2_2 + ""+G2_3 + ""+G2_4;
        Log.w("Lastfinalgroup2", FinalGroup2);

        // Group 2 ends

        // group 3

        char G3_1;
        int G3_2 , G3_3 ,G3_4;
        G3_1 = FinalGroup3 != null ? FinalGroup3.charAt(0) : 0;
        Log.w("bgh3",""+G3_1);
        if(G3_1=='B')
        {
            G3_1 = '4';

        } else if (G3_1=='U') {
            G3_1 = '5';

        } else if (G3_1=='F') {
            G3_1 = '6';
        } else if (G3_1=='T') {
            G3_1 = '7';
        }
        Log.w("gh12",""+G2_1);

//  Separate Second digit of Group1 and add 4 to it
        G3_2 =  Character.getNumericValue(FinalGroup3 != null ? FinalGroup3.charAt(1) : 0) + 2;
        if(G3_2>9)
        {
            G3_2 = G3_2 - 10;
        }

        Log.w("gh22",""+G2_2);

        //  Separate Third digit of Group1 and add 3 to it

        G3_3 = Character.getNumericValue(FinalGroup3 != null ? FinalGroup3.charAt(2) : 0) + 1;

        Log.w("bgh3",""+G2_3);


        if(G3_3>9)
        {

            G3_3 = G3_3 - 10;
        }

        Log.w("gh33",""+G2_3);

        // Separate Fourth digit of Group1 and add 2 to it
        //  G3_4 = Character.getNumericValue(FinalGroup3.charAt(3));
        //Log.w("bbcg24",""+G2_4);

        G3_4 = Character.getNumericValue(FinalGroup3 != null ? FinalGroup3.charAt(3) : 0);
        Log.w("G2_44",""+G2_4);
        if(G3_4>9)

        {
            G3_4 = G3_4 - 10;

        }

        Log.w("gh44",""+G2_4);

        // Again form Group 1 by concatenating all 4 digits.

        FinalGroup3 = ""+G3_1 + ""+G3_2 + ""+G3_3 + ""+G3_4;
        Log.w("Lastfinalgroup3", FinalGroup3);


        String FinalPintCountOtp = FinalGroup1 + " " +FinalGroup2 + " " +FinalGroup3;
        finish();
        shareWhatsappprint(FinalPintCountOtp);
    }


    String scanTxt(String raw) {
        if (isNewPrintCountOTP(raw)) {
            showDialogForNewPrintCountOTP(raw);
        } else if (isOldPrintCountOTP(raw)) {
           // showDialogForOldPrintCountOTP(raw);
            printCountDialogHelper.showDialogForOldPrintCountOTP(raw, remainingCounts);
            Log.w("rawdatamain",raw);
            Log.w("rawremain",""+remainingCounts);

        } else if (isNewServiceOTP(raw)) {

           String newGeneratedServiceOTP = newServiceOTPGenerator.newServiceOTPLogic(raw);
             shareServiceOTP(newGeneratedServiceOTP);

            //  String serviceOTP = reconstructServiceOTP(raw);
          //  shareServiceOTP(serviceOTP);
          //  return serviceOTP;




        } else if (hasLicenseRights(raw)) {
            NewLicensePeriodDialogHelper.showDialog(MainActivity.this, raw);

        } else {
            showNoRightsMessage();
        }
        return raw;
    }



    private boolean isNewPrintCountOTP(String raw) {
        return (
                Character.isLetter(raw.charAt(0)) &&
                        Character.isDigit(raw.charAt(1))  &&
                        Character.isDigit(raw.charAt(2))&&
                        Character.isDigit(raw.charAt(3)) &&

                        Character.isLetter(raw.charAt(4)) &&
                        Character.isDigit(raw.charAt(5))  &&
                        Character.isDigit(raw.charAt(6))&&
                        Character.isDigit(raw.charAt(7)) &&

                        Character.isLetter(raw.charAt(8)) &&
                        Character.isDigit(raw.charAt(9))  &&
                        Character.isDigit(raw.charAt(10))&&
                        Character.isDigit(raw.charAt(11)) &&

                        Rights.contains("C"));

    }

    private boolean isOldPrintCountOTP(String raw) {
        char charAtIndex8 = raw.charAt(8);
        return (charAtIndex8 == '%' || charAtIndex8 == '&' || charAtIndex8 == '@' || charAtIndex8 == '#');
    }

    private boolean isNewServiceOTP(String raw) {
        return (Character.isLetter(raw.charAt(1)) & Rights.contains("S"));
    }





    private boolean hasLicenseRights(String raw) {
        return raw.length() >= 4 && Character.isLetter(raw.charAt(3)) && Rights.contains("L");
    }


    private void showNoRightsMessage() {
        Toast.makeText(this, "No Rights For this QR Code", Toast.LENGTH_LONG).show();
    }

    private void shareServiceOTP(String serviceOTP) {
        Log.w("ServiceOtpis", serviceOTP);
        shareWhatsapp(serviceOTP);
    }


    void shareWhatsapp(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Service OTP");
        intent.putExtra(Intent.EXTRA_TEXT, "This is Service OTP " + text);
        startActivity(Intent.createChooser(intent, "This is Service OTP " + text));
    }

    void shareWhatsappprint(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Service OTP");
        intent.putExtra(Intent.EXTRA_TEXT, "This is Print Count OTP " + text);
        startActivity(Intent.createChooser(intent, "This is Print Count OTP " + text));
    }

    public void shareWhatsappLicense(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "License OTP");
        intent.putExtra(Intent.EXTRA_TEXT, "This is License OTP " + text);
        startActivity(Intent.createChooser(intent, "This is License OTP " + text));
    }




    boolean isVarified() {
        boolean isValidate = true;
        String i = editText.getText().toString();


        try {

            int printint = Integer.parseInt(i);
            if (printint == 00) {
                editText.setError("Enter correct value");
                editText.requestFocus();
                isValidate = false;
            }


        } catch (Exception e) {

        }


        if (Integer.parseInt(i) % 100 != 0) {
            isValidate = false;
            editText.setError("Not Multiple of 100");
        }
        if (Integer.parseInt(i) > 9999) {
            isValidate = false;
            editText.setError("Greater Than 9999");
        }


        return isValidate;
    }




    public void uploadDetailsForOldPrintCount(String clientName, int remainingCounts, String OTPCODE, int requestedCounts, String oldPrintCountVersion, String oldPrintCountDealerName, int oldPrintCountDealerId) {
        Log.w("colname",clientName);
        Log.w("colrmg",""+remainingCounts);

        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.updateAndInsertForOldPrintCount, response -> {
            try {
                JSONObject object = new JSONObject(response);
                String message = object.getString("message");
                if (message.contains("Sucessfully")) {
                    pd.dismiss();
                   String oldGeneratedPrintCount = oldPrintCountGenerator.oldPrintCountOTPLogic(OTPCODE, requestedCounts);
                   shareWhatsappprint(oldGeneratedPrintCount);

                    if(pdialog !=null)
                    {
                        pdialog.dismiss();

                    }
                    if(dialofForOldPrintCountOTP !=null)
                    {
                        dialofForOldPrintCountOTP.dismiss();

                    }



                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                pd.dismiss();
            }
        }, error -> pd.dismiss()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                if(clientName != " ")
                {

                    map.put("ClientName", clientName);
                    Log.w("ccname",clientName);
                }
                if(clientName == " ")
                {
                    map.put("ClientName", oldPrintCountClientName);
                    Log.w("ccname", oldPrintCountClientName);

                }

                map.put("Version", oldPrintCountVersion);
                Log.w("cversion", oldPrintCountVersion);

                map.put("DoneByUserName", currentLoggedInUserName);
                Log.w("cdoneby", currentLoggedInUserName);

                map.put("RequestedCount", String.valueOf(requestedCounts));
                Log.w("cPrintcnt",String.valueOf(requestedCounts));
                map.put("DoneByUserId", String.valueOf(currentLoggedInUserId));
                Log.w("cUserId",String.valueOf(currentLoggedInUserId));
                map.put("RemainingCount", String.valueOf(remainingCounts));
                Log.w("cncount",String.valueOf(remainingCounts));

                map.put("DealerName",oldPrintCountDealerName);
                Log.w("cdealernn",oldPrintCountDealerName);
                map.put("DealerId", ""+oldPrintCountDealerId);
                Log.w("cdealernnid",""+oldPrintCountDealerId);
                return map;
            }
        };
        requestQueue.add(request);
    }

    void uploadDetailsForNewPrintCount() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.updateAndInsert, response -> {
            try {
                JSONObject object = new JSONObject(response);
                String message = object.getString("message");
                Log.w("jsonmsg",message.toString());
                if (message.contains("Sucessfully")) {
                    pd.dismiss();
                    // String hi = compileOTP(OTPCODE);
                    //shareWhatsappprint(hi);
                    if(pdialog !=null)
                    {
                        pdialog.dismiss();


                    }
                    if(dialofForOldPrintCountOTP !=null)
                    {
                        dialofForOldPrintCountOTP.dismiss();

                    }



                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                pd.dismiss();
            }
        }, error -> pd.dismiss()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if(clientName != " ")
                {


                    map.put("Client", clientName);
                }
                if(clientName == " ")
                {
                    map.put("Client", oldPrintCountClientName);


                }

                map.put("Version", RawDialogVersion.getText().toString());
                map.put("Doneby", currentLoggedInUserName);
                map.put("DealerName", newPrintCountDealerName);
                Log.w("dealernn", newPrintCountDealerName);
                map.put("Did", ""+ newPrintCountDealerId);
                Log.w("dealernnid",""+ newPrintCountDealerId);

                map.put("Printcnt", ""+TotalCount);
                Log.w("cccft",""+TotalCount);
                map.put("User_ID", String.valueOf(currentLoggedInUserId));
                map.put("Count", String.valueOf(remainingCounts));
                Log.w("ccft",String.valueOf(remainingCounts));
                Log.w("count di val", String.valueOf(remainingCounts));
                map.put("A5Value",A5PrintCountEditTxt.getText().toString());
                Log.w("A5dival", A5PrintCountEditTxt.getText().toString());
                map.put("A4Value",A4PrintCountEditTxt.getText().toString());
                Log.w("A4vlaue",A4PrintCountEditTxt.getText().toString());
                map.put("A3Value",A3PrintCountEditTxt.getText().toString());
                Log.w("A3dival",A3PrintCountEditTxt.getText().toString());

                return map;
            }
        };
        requestQueue.add(request);
    }



    @Override
    protected void onResume() {
        super.onResume();
        RemainingCount.setText(String.valueOf("Remaining Print Counts " + remainingCounts));
        nameser.setText("Welcome ");
        wnames.setText(wname);
    }

    public void generate(View view) {


        // Thix is the raw qrcode that i have entered in edittext
        String serviceOTP = scanTxt(ServiceEdit.getText().toString());
        Log.w("TextEntered", serviceOTP);
        ServiceEdit.setText("");
        shareWhatsapp(serviceOTP);
    }

    public void GeneratePrint(View view) {


        // ShowDialog();

        showDialogForNewPrintCountOTP("");



    }


    void getUserRights(){

        StringRequest request=new StringRequest(Request.Method.POST, Util.GetUserRights, response -> {
            Log.w("ass",""+response);
            try {
                JSONObject object=new JSONObject(response);
                JSONArray array=object.getJSONArray("students");
                String message=object.getString("message");
                if (message.contains("Login Sucessful")){
                    for (int i=0;i<array.length();i++){
                        JSONObject object1=array.getJSONObject(i);
                        Rights = object1.getString("Rights");
                        Log.w("Rights",Rights);

                    }

                }else{
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"Login Fail "+e, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(MainActivity.this,"Login Fail" +error, Toast.LENGTH_LONG).show();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map=new HashMap<>();
                map.put("Phone",phone);
                return map;
            }
        };
        requestQueue.add(request);
    }

}
