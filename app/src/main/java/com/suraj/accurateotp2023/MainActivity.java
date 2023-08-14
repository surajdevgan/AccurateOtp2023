package com.suraj.accurateotp2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.CaseMap;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
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
    String sex = " ";
    EditText geditText = null;
    String newString = "";
    String value1 = "", value0 = "";
    String LMG = "", MG = "", RMG = "";
    String PLMG = "", PMG = "", PRMG = "";
    int c = 0;
    String name = "";

    String version = "";
    String OTPCODE = "";

    String DealerName = "";
    int Did = 0;
    int printCount = 0;
    String gname = "";
    String gversion = "";
    String gOTPCODE = "", Rights = "";
    TextView textViewVersion, RemainingCount;
    EditText textViewName, ServiceEdit, gtextViewName, PrintID,gtextViewVersion, EditTxtDays, EnterManualPrintId;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    int count = 0, id = 0;
    String userName = "";
    String wname;
    Dialog dialog, pdialog, printcountdialog;
    ProgressDialog pd;
    EditText A3PrintCountEditTxt, A4PrintCountEditTxt, A5PrintCountEditTxt;
    TextView RawDialogCode;
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
        count = Integer.parseInt(preferences.getString(Util.count, ""));
        wname = preferences.getString(Util.Name, "");

        id = preferences.getInt(Util.id, 0);
        userName = preferences.getString(Util.Name, "");
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
            if (serviceOTP.contains("#") || serviceOTP.contains("@") || serviceOTP.contains("%") || serviceOTP.contains("&")) {
                String[] otp = serviceOTP.split(";");

                Log.w("ooop",""+otp[0]);
                name = otp[1];
                Log.w("ccdanem",name);
                version = otp[2];
                OTPCODE = otp[0];


              //  showDialouge();

            }
            if(Character.isLetter(serviceOTP.charAt(0))) {
                //  Toast.makeText(this,"This is PrintCount otp", Toast.LENGTH_SHORT).show();

              //  showDialogForPrintCount(serviceOTP);


            }
            else {
               // shareWhatsapp(serviceOTP);
            }
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
                startActivity(new Intent(this, login.class));
                break;
        }
        return true;
    }


    void showDialouge()  // this is the dialog for old print count
    {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.print_count_dialouge);
        dialog.setTitle("Print Count OTP");
        dialog.setCancelable(false);
        cardViewButton = dialog.findViewById(R.id.generate);
        cardViewButton1 = dialog.findViewById(R.id.cancel);
        editText = dialog.findViewById(R.id.editText3);
        textViewName = dialog.findViewById(R.id.textName);


        cardViewButton.setOnClickListener(view -> {

            String a = editText.getText().toString();
            if (a.isEmpty()) {
                editText.setError("Enter the Value of Print Count");
                editText.requestFocus();
                return;
            }

            if(textViewName.getText().toString().isEmpty())
            {
                textViewName.setError("Enter the name");
                textViewName.requestFocus();
                return;
            }

            if (a.startsWith(String.valueOf(0))) {
                editText.setError("Enter Correct Value");
                editText.requestFocus();
                return;

            }


            if (ConnectionCheck.isConnected(connectivityManager, networkInfo, MainActivity.this)) {
              //  if (isVarified()) {
                Log.w("1532 wali if","Working");
                printCount = Integer.parseInt(editText.getText().toString());
                    TotalCount = Integer.parseInt(A3PrintCountEditTxt.getText().toString()) + Integer.parseInt(A4PrintCountEditTxt.getText().toString()) + Integer.parseInt(A5PrintCountEditTxt.getText().toString());
                    Log.w("Totalcount",""+TotalCount);
                    if (count > printCount || count == printCount) {
                        count = count - printCount;
                        sex = textViewName.getText().toString();
                     //   uploadDetails();
                        uploadNewDetailsLogic();
                    } else {
                        Toast.makeText(MainActivity.this, "297 Unable to Generate Print Count OTP", Toast.LENGTH_LONG).show();
                    }
               // }
            } else {
                Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            }
        });
        cardViewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    void showDialogForLicensePeriod(String rawtxt) {




        String [] SplitRawCodeValues = rawtxt.split(";");

        printcountdialog = new Dialog(MainActivity.this);
        printcountdialog.setContentView(R.layout.license_period);
        printcountdialog.setTitle("License Period");
        printcountdialog.setCancelable(true);

        cardViewButton = printcountdialog.findViewById(R.id.generate);
        cardViewButton1 = printcountdialog.findViewById(R.id.cancel);
        RawDialogCode = printcountdialog.findViewById(R.id.txtarcode);
        EditTxtDays = printcountdialog.findViewById(R.id.textdays);

        EditTxtDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(EditTxtDays.getText().toString().length()>3)
                {
                    cardViewButton.setVisibility(View.VISIBLE);

                }

                if(EditTxtDays.getText().toString().length()<4)
                {
                    cardViewButton.setVisibility(View.GONE);


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        RawDialogCode.setText(SplitRawCodeValues[0]);



        cardViewButton.setOnClickListener(view -> {


 String NumberOfDays = EditTxtDays.getText().toString();
 String FinalGroup1 = null, FinalGroup2 = null, FinalGroup3 = null;


// int NumberOfDayslength = Integer.parseInt(String.valueOf(EditTxtDays.getText().toString().length()));
  //          Toast.makeText(this, ""+NumberOfDayslength, Toast.LENGTH_SHORT).show();


 //   Group1

    int G1_1  = Character.getNumericValue(NumberOfDays.charAt(0));


    int G1_2  = Character.getNumericValue(rawtxt.charAt(1)) + 2;

            if (G1_2 > 9)
            {

                G1_2 = G1_2 - 10;
            }

            int G1_3 = Character.getNumericValue(rawtxt.charAt(2)) + 3;
            if (G1_3 > 9)
            {

                G1_3 = G1_3 - 10;
            }

          int  G1_4 = Character.getNumericValue(NumberOfDays.charAt(1));
            G1_4 = G1_4 + 4;
            if( G1_4 > 9)
            {
                G1_4 = G1_4 - 10;

            }


            FinalGroup1 = ""+G1_1 + G1_2 + G1_3 +G1_4;
            Log.w("FinalGroup1",FinalGroup1);



            //   Group2

            int G2_1 = Character.getNumericValue(rawtxt.charAt(4)) + 5;
            Log.w("G2_1",""+G2_1);
            if (G2_1 > 9){
                G2_1 = G2_1 - 10;

            }

            int G2_2 = Character.getNumericValue(rawtxt.charAt(5)) + 6;
            if (G2_2 > 9){
                G2_2 = G2_2 - 10;

            }


            int G2_3 = Character.getNumericValue(rawtxt.charAt(6)) + 7;
            if (G2_3 > 9){
                G2_3 = G2_3 - 10;

            }

            int  G2_4 = Character.getNumericValue(NumberOfDays.charAt(2)) + 8;

            if (G2_4 > 9){
                G2_4 = G2_4 - 10;

            }

            FinalGroup2 = ""+G2_1 + G2_2 + G2_3 + G2_4;
            Log.w("FinalGroup2",FinalGroup2);


            //   Group3

            int G3_1 = Character.getNumericValue(rawtxt.charAt(8)) + 9;
            Log.w("G3_1", ""+G3_1);
            if (G3_1 > 9){
                G3_1 = G3_1 - 10;

            }

            int G3_2 = Character.getNumericValue(rawtxt.charAt(9));



            int G3_3 = Character.getNumericValue(rawtxt.charAt(10)) + 1;
            if (G3_3 > 9){
                G3_3 = G3_3 - 10;

            }

            int  G3_4 = Character.getNumericValue(NumberOfDays.charAt(3)) + 2;

            if (G3_4 > 9){
                G3_4 = G3_4 - 10;

            }

            FinalGroup3 = ""+G3_1 + G3_2 + G3_3 + G3_4;
            Log.w("FinalGroup3",FinalGroup3);


            String FinalLicensePeriod = FinalGroup1 + " " +FinalGroup2 + " " +FinalGroup3;
            finish();
            shareWhatsappLicense(FinalLicensePeriod);

        });

        cardViewButton1.setOnClickListener(view -> printcountdialog.dismiss());


        printcountdialog.show();

    }
    void showDialogForPrintCount(String rawtxt) {

        String [] SplitRawCodeValues = rawtxt.split(";");

        printcountdialog = new Dialog(MainActivity.this);
        printcountdialog.setContentView(R.layout.print_count_otp_dialog_box);
        printcountdialog.setTitle("Print Count OTP");
        printcountdialog.setCancelable(true);
        EnterManualPrintId = printcountdialog.findViewById(R.id.enter_print_id);
        RawDialogCode = printcountdialog.findViewById(R.id.txtarcode);


        if(rawtxt.isEmpty())
        {
            EnterManualPrintId.setVisibility(View.VISIBLE);

        }

        else {
            RawDialogCode.setVisibility(View.VISIBLE);
            RawDialogCode.setText(SplitRawCodeValues[0]);

        }




        cardViewButton = printcountdialog.findViewById(R.id.generate);
        cardViewButton1 = printcountdialog.findViewById(R.id.cancel);
        A3PrintCountEditTxt = printcountdialog.findViewById(R.id.a3prtcount);
        A4PrintCountEditTxt = printcountdialog.findViewById(R.id.a4prtcount);
        A5PrintCountEditTxt = printcountdialog.findViewById(R.id.a5prtcount);
        textViewName = printcountdialog.findViewById(R.id.textName);
        RawDialogVersion = printcountdialog.findViewById(R.id.textVersion);




        if(SplitRawCodeValues.length>1)
        {
            textViewName.setText(SplitRawCodeValues[1]);


        }


        if(SplitRawCodeValues.length>2)
        {
            RawDialogVersion.setText(SplitRawCodeValues[2]);


        }
        Log.w("length", ""+SplitRawCodeValues.length);
        if(SplitRawCodeValues.length>3)
        {
            DealerName = SplitRawCodeValues[3];
            Log.w("ddm",DealerName);
        }

        if(SplitRawCodeValues.length>4)
        {
            Did = Integer.parseInt(SplitRawCodeValues[4]);
            Log.w("ddmid",""+Did);

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
                Log.w("RmnCount",""+count);
                if (count > TotalCount || count == TotalCount) {
                    Log.w("ifisfk","done");
                    count = count -  TotalCount;
                    sex = textViewName.getText().toString();
                    //   uploadDetails();
                    PrintCountLogic(rawtxt, printcountvaluesarray);
                    uploadNewDetailsLogic();
                } else {
                    Toast.makeText(MainActivity.this, "Unable to Generate Print Count OTP", Toast.LENGTH_LONG).show();
                }
                // }
            } else {
                Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            }



        });

        cardViewButton1.setOnClickListener(view -> printcountdialog.dismiss());


        printcountdialog.show();

    }

    private void PrintCountLogic(String rawPrintCountOtp, int[] printcountvaluesarray) {


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
        StringBuilder Id = new StringBuilder();

        if(Character.isLetter(raw.charAt(0)) & Rights.contains("C")) {
          //  Toast.makeText(this,"This is PrintCount otp", Toast.LENGTH_SHORT).show();

            showDialogForPrintCount(raw);

        }


        // This if is to check if the code is service otp
       else if (Character.isLetter(raw.charAt(1)) & Rights.contains("S")) {
          //  Toast.makeText(this,"This is service otp", Toast.LENGTH_SHORT).show();
            // Group 1 Logic
            int G1_1  = Character.getNumericValue(raw.charAt(0)) + 1;

        if(G1_1>9)
        {

            G1_1 = G1_1 - 10;
        }

        char G1_2 = raw.charAt(1);

            if (G1_2 == 'Z') {
                G1_2 = '8';
            } else if (G1_2 == 'C') {
                G1_2 = '5';
            }
            else if (G1_2 == 'R') {
                G1_2 = '4';
            } else if (G1_2 == 'F') {
                G1_2 = '0';
            }


            int G1_3  = Character.getNumericValue(raw.charAt(2)) + 3;


            if (G1_3 > 9)
            {

                G1_3 = G1_3 - 10;
            }
            int G1_4  = Character.getNumericValue(raw.charAt(3)) + 4;

            if (G1_4 > 9)
            {

                G1_4 = G1_4 - 10;
            }

            String ReconstructedGroup1 = String.valueOf(G1_1).concat(String.valueOf(G1_2)).concat(String.valueOf(G1_3)).concat(String.valueOf(G1_4));

         // Group 2 Logic
            int G2_1  = Character.getNumericValue(raw.charAt(4)) + 5;

            if(G2_1>9)
            {

                G2_1 = G2_1 - 10;
            }

            char G2_2 = raw.charAt(5);
            if (G2_2 == 'B') {
                G2_2 = '1';
            } else if (G2_2 == 'P') {
                G2_2 = '7';
            }
            else if (G2_2 == 'E') {
                G2_2 = '9';
            } else if (G2_2 == 'A') {
                G2_2 = '2';
            }


            int G2_3  = Character.getNumericValue(raw.charAt(6)) + 7;


            if (G2_3 > 9)
            {

                G2_3 = G2_3 - 10;
            }
            int G2_4  = Character.getNumericValue(raw.charAt(7)) + 8;

            if (G2_4 > 9)
            {

                G2_4 = G2_4 - 10;
            }

            String ReconstructedGroup2 = String.valueOf(G2_1).concat(String.valueOf(G2_2)).concat(String.valueOf(G2_3)).concat(String.valueOf(G2_4));


            // Group 3 Logic

            int G3_1  = Character.getNumericValue(raw.charAt(8)) + 9;

            if(G3_1>9)
            {

                G3_1 = G3_1 - 10;
            }

            char G3_2 = raw.charAt(9);
            if (G3_2 == 'H') {
                G3_2 = '3';
            } else if (G3_2 == 'K') {
                G3_2 = '6';
            }
            else if (G3_2 == 'N') {
                G3_2 = '4';
            } else if (G3_2 == 'G') {
                G3_2 = '2';
            }


            int G3_3  = Character.getNumericValue(raw.charAt(10)) + 1;


            if (G3_3 > 9)
            {

                G3_3 = G3_3 - 10;
            }
            int G3_4  = Character.getNumericValue(raw.charAt(11)) + 2;

            if (G3_4 > 9)
            {

                G3_4 = G3_4 - 10;
            }

            String ReconstructedGroup3 = String.valueOf(G3_1).concat(String.valueOf(G3_2)).concat(String.valueOf(G3_3)).concat(String.valueOf(G3_4));
            String ServiceOtpis = ReconstructedGroup1 + " " + ReconstructedGroup2 + " " + ReconstructedGroup3;
            Log.w("ServiceOtpis",ServiceOtpis);

            shareWhatsapp(ServiceOtpis);

return  ServiceOtpis;

        } else if (Character.isLetter(raw.charAt(3)) & Rights.contains("L")) {
            showDialogForLicensePeriod(raw);

        }

       else {

            Toast.makeText(this, "No Rights For this Qr Code", Toast.LENGTH_LONG).show();
        }

        return raw;
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

    void shareWhatsappLicense(String text) {
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

    boolean isgVarified() {
        boolean isgValidate = true;
        String i = geditText.getText().toString();


        try {

            int printint = Integer.parseInt(i);
            if (printint == 00) {
                geditText.setError("Enter correct value");
                geditText.requestFocus();
                isgValidate = false;
            }


        } catch (Exception e) {

        }


        if (Integer.parseInt(i) % 100 != 0) {
            isgValidate = false;
            geditText.setError("Not Multiple of 100");
        }
        if (Integer.parseInt(i) > 9999) {
            isgValidate = false;
            geditText.setError("Greater Than 9999");
        }


        return isgValidate;
    }

    public String compileOTP(String raw) {
        raw = raw + " ";
        String[] val = new String[raw.length()];
        for (int i = 0; i <= raw.length() - 1; i++) {
            if (i % 4 == 0) {
                val[i] = raw.substring(c, i);
                c = i;
            }
        }

        LMG = val[4];
        MG = val[8];
        RMG = val[12];
        Log.w("LMG di val", LMG);

//		                        Left Most Group Processing

        String LMD1 = LMG.substring(0, 1);
        String RD1 = LMG.substring(1, 4);
        String PDG1 = firstAlgo(RD1);

//                              Middle Group Processing

        String LMD2 = MG.substring(0, 1);
        String SLMD2 = MG.substring(1, 2);
        String RD2 = MG.substring(2, 4);
        String PDG2 = firstAlgo(RD2);

//                              Right Most Group Processing

        String LMD3 = RMG.substring(0, 1);
        String RD3 = RMG.substring(1, 4);
        String PDG3 = firstAlgo(RD3);

        String TPS = PDG1 + PDG2 + PDG3;
        Log.w("ttt",TPS);

        for (int i = 0; i <= TPS.length() - 1; i++) {
            char d = TPS.charAt(i);
            if (Character.isLetter(d)) {
                int ASCII = d;
                ASCII = ASCII + i;
                if (ASCII == 91) {
                    d = 'o';
                } else if (ASCII == 92) {
                    d = 'p';
                } else if (ASCII == 93) {
                    d = 'q';
                } else if (ASCII == 94) {
                    d = 'r';
                } else if (ASCII == 95) {
                    d = 's';
                } else if (ASCII == 96) {
                    d = 't';
                } else {
                    d = (char) ASCII;
                }
                newString = newString + d;
            } else {
                newString = newString + d;
            }
        }

        PLMG = newString.substring(0, 3);
        PMG = newString.substring(3, 5);
        PRMG = newString.substring(5, 8);

        //                             Print Count Otp

        int divide = printCount / 100;
        if (String.valueOf(divide).length() == 1) {
            value1 = "0";
            value0 = String.valueOf(divide).substring(0, 1);
        } else {
            value1 = String.valueOf(divide).substring(0, 1);
            value0 = String.valueOf(divide).substring(1, 2);
        }

        char a = LMD1.charAt(0);

        char b = LMD2.charAt(0);
        char e = SLMD2.charAt(0);

        char d = LMD3.charAt(0);

        a = (char) ((int) a + Integer.parseInt(value1));            //LMD1

        d = (char) ((int) d + Integer.parseInt(value0));  //LMD3

        b = (char) ((int) b + Integer.parseInt(value1));      //LMD2

        e = (char) ((int) e + Integer.parseInt(value0));    //SLMD2

        String NLMG = finalAlgo(a) + PLMG;
        String NRMG = finalAlgo(d) + PRMG;
        String NMG = finalAlgo(b) + "" + finalAlgo(e) + "" + PMG;

        return NLMG + " " + NMG + " " + NRMG;
    }

    public String firstAlgo(String val) {
        StringBuilder PD = new StringBuilder();
        for (int i = 0; i <= val.length() - 1; i++) {
            char d = val.charAt(i);
            if (Character.isLetter(d)) {
                if (d == 'A') {
                    d = '@';
                } else if (d == 'B') {
                    d = '!';
                } else if (d == 'J') {
                    d = '%';
                } else if (d == 'W') {
                    d = '&';
                } else if (d == 'X') {
                    d = '*';
                }
            }
            PD.append(d);
        }
        return String.valueOf(PD);
    }

    public char finalAlgo(char val1) {
        if ((int) val1 == 39) {
            val1 = 'a';
        } else if ((int) val1 == 44) {
            val1 = 'b';
        } else if ((int) val1 == 45) {
            val1 = 'c';
        } else if ((int) val1 == 46) {
            val1 = 'd';
        } else if ((int) val1 == 58) {
            val1 = 'e';
        } else if ((int) val1 == 59) {
            val1 = 'f';
        }
        return val1;
    }

    void uploadDetails() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.updateAndInsert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String message = object.getString("message");
                    if (message.contains("Sucessfully")) {
                        pd.dismiss();
                        String hi = compileOTP(OTPCODE);
                        shareWhatsappprint(hi);
                        if(pdialog !=null)
                        {
                            pdialog.dismiss();


                        }
                        if(dialog!=null)
                        {
                            dialog.dismiss();

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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if(sex != " ")
                {


                    map.put("Client", sex);
                }
                if(sex  == " ")
                {
                    map.put("Client", name);

                }

                map.put("Version", version);
                map.put("Doneby", userName);


                map.put("Printcnt", String.valueOf(printCount));
                map.put("User_ID", String.valueOf(id));
                map.put("Count", String.valueOf(count));
                map.put("A5Value",A5PrintCountEditTxt.getText().toString());
                map.put("A4Value",A4PrintCountEditTxt.getText().toString());
                map.put("A3Value",A3PrintCountEditTxt.getText().toString());

                return map;
            }
        };
        requestQueue.add(request);
    }

    void uploadNewDetailsLogic() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.updateAndInsert, response -> {
            try {
                JSONObject object = new JSONObject(response);
                String message = object.getString("message");
                if (message.contains("Sucessfully")) {
                    pd.dismiss();
                    String hi = compileOTP(OTPCODE);
                    shareWhatsappprint(hi);
                    if(pdialog !=null)
                    {
                        pdialog.dismiss();


                    }
                    if(dialog!=null)
                    {
                        dialog.dismiss();

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
                if(sex != " ")
                {


                    map.put("Client", sex);
                }
                if(sex  == " ")
                {
                    map.put("Client", name);


                }

                map.put("Version", RawDialogVersion.getText().toString());
                map.put("Doneby", userName);
                map.put("DealerName",DealerName);
                Log.w("dealernn",DealerName);
                map.put("Did", ""+Did);
                Log.w("dealernnid",""+Did);

              //  map.put("Printcnt", "0");
                map.put("Printcnt", ""+TotalCount);
                Log.w("cccft",""+TotalCount);
                map.put("User_ID", String.valueOf(id));
                map.put("Count", String.valueOf(count));
                Log.w("ccft",String.valueOf(count));
                Log.w("count di val", String.valueOf(count));
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
        RemainingCount.setText(String.valueOf("Remaining Print Counts " + count));
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

        showDialogForPrintCount("");



    }

    void ShowDialog() {

        Toast.makeText(this, "1528", Toast.LENGTH_SHORT).show();

        pdialog = new Dialog(this);
        pdialog.setContentView(R.layout.generate_print_dialog);
        pdialog.setTitle("Print Count OTP");
        pdialog.setCancelable(false);
        PrintID = pdialog.findViewById(R.id.printid);
        gcardViewButton = pdialog.findViewById(R.id.ggenerate);
        gcardViewButton1 = pdialog.findViewById(R.id.gcancel);
       // geditText = pdialog.findViewById(R.id.geditText3);
        gtextViewName = pdialog.findViewById(R.id.gtextName);
        gtextViewVersion = pdialog.findViewById(R.id.gtextVersion);

        gcardViewButton.setOnClickListener(v -> {
            String a = geditText.getText().toString();
            String b = PrintID.getText().toString();

            if(gtextViewName.getText().toString().isEmpty())
            {
                gtextViewName.setError("Enter the name");
                gtextViewName.requestFocus();
                return;
            }
            if(b.length()<12)
            {
                PrintID.setError("Incorrect value");
                PrintID.requestFocus();
                return;

            }
            if (b.isEmpty()) {
                PrintID.setError("Print Id is require");
                PrintID.requestFocus();
                return;
            }
            if (a.isEmpty()) {
                geditText.setError("Enter the Value of Print Count");
                geditText.requestFocus();
                return;
            }

            if (a.startsWith(String.valueOf(0))) {
                geditText.setError("Enter Correct Value");
                geditText.requestFocus();
                return;

            }
            if (ConnectionCheck.isConnected(connectivityManager, networkInfo, MainActivity.this)) {
                Log.w("1532 wali if","Working");
               // if (isgVarified()) {
                    name = gtextViewName.getText().toString();
                    OTPCODE = PrintID.getText().toString();
                    version = gtextViewVersion.getText().toString();
                    printCount = Integer.parseInt(geditText.getText().toString());
                    TotalCount = Integer.parseInt(A3PrintCountEditTxt.getText().toString()) + Integer.parseInt(A4PrintCountEditTxt.getText().toString()) + Integer.parseInt(A5PrintCountEditTxt.getText().toString());
                    Log.w("Totalcount",""+TotalCount);
                    if (count > TotalCount || count == TotalCount) {
                        count = count - TotalCount;
                      //  uploadDetails();
                        uploadNewDetailsLogic();
                    }

                    else {
                        Toast.makeText(MainActivity.this, "1561 Unable to Generate Print Count OTP", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }






        });
        gcardViewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();

            }
        });



        pdialog.show();

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
