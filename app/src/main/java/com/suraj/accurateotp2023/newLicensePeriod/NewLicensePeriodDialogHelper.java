package com.suraj.accurateotp2023.newLicensePeriod;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.suraj.accurateotp2023.MainActivity;
import com.suraj.accurateotp2023.R;

public class NewLicensePeriodDialogHelper {

    public static void showDialog(Context context, String rawtxt) {
        String[] SplitRawCodeValues = rawtxt.split(";");

        Dialog dialogForNewLicensePeriod = new Dialog(context);
        dialogForNewLicensePeriod.setContentView(R.layout.new_licenseperiod_dialogbox);
        dialogForNewLicensePeriod.setTitle("License Period");
        dialogForNewLicensePeriod.setCancelable(true);

        CardView cardViewButton = dialogForNewLicensePeriod.findViewById(R.id.generate);
        CardView cardViewButton1 = dialogForNewLicensePeriod.findViewById(R.id.cancel);
        TextView RawDialogCode = dialogForNewLicensePeriod.findViewById(R.id.txtarcode);
        EditText EditTxtDays = dialogForNewLicensePeriod.findViewById(R.id.textdays);

        EditTxtDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EditTxtDays.getText().toString().length() > 3) {
                    cardViewButton.setVisibility(View.VISIBLE);
                }
                if (EditTxtDays.getText().toString().length() < 4) {
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

            String FinalNewLicensePeriod = FinalGroup1 + " " + FinalGroup2 + " " + FinalGroup3;
            ((MainActivity) context).finish();
            // Assuming you have a method shareWhatsappLicense in MainActivity
            ((MainActivity) context).shareWhatsappLicense(FinalNewLicensePeriod);
        });

        cardViewButton1.setOnClickListener(view -> dialogForNewLicensePeriod.dismiss());

        dialogForNewLicensePeriod.show();
    }
}