package com.suraj.accurateotp2023.oldPrintCountOTP;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.suraj.accurateotp2023.util.ConnectionCheck;
import com.suraj.accurateotp2023.MainActivity;
import com.suraj.accurateotp2023.R;

public class OldPrintCountDialogHelper {
    private final MainActivity mainActivity;
    private Dialog dialog;
    private TextView textViewName, RawVersion;
    private EditText editText;
    private CardView cardViewButton;
    private CardView cardViewButton1;

    String oldPrintCountClientName;

    ConnectivityManager connectivityManager;

    private Context context;
    private String OTPCODE;


    private String oldPrintCountDealerName;
    private int oldPrintCountDealerId;
    private int remainingCounts;
    private int requestedCounts;
    private String clientName;

    String oldPrintCountVersion;

    NetworkInfo networkInfo;

    public OldPrintCountDialogHelper(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;
        initializeDialog();
    }

    private void initializeDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.old_printcount_dialogbox);
        dialog.setTitle("Print Count OTP");
        dialog.setCancelable(false);

        cardViewButton = dialog.findViewById(R.id.generate);
        cardViewButton1 = dialog.findViewById(R.id.cancel);
        editText = dialog.findViewById(R.id.editText3);
        textViewName = dialog.findViewById(R.id.textName);
        RawVersion = dialog.findViewById(R.id.textVersion);
    }

    public void showDialogForOldPrintCountOTP(String rawtxt, int remainingCounts) {
        Log.w("rawdadata",rawtxt);
        Log.w("rawdivalues",""+remainingCounts);
        String[] SplitRawCodeValues = rawtxt.split(";");
        OTPCODE = SplitRawCodeValues[0];
        Log.w("ooop", "" + SplitRawCodeValues[0]);
        oldPrintCountClientName = SplitRawCodeValues[1];
        Log.w("ccdanem", oldPrintCountClientName);
        oldPrintCountVersion = SplitRawCodeValues[2];
        oldPrintCountDealerName = SplitRawCodeValues[3];
        oldPrintCountDealerId = Integer.parseInt(SplitRawCodeValues[4]);
        this.remainingCounts = remainingCounts;
        RawVersion.setText(oldPrintCountVersion);

        cardViewButton.setOnClickListener(view -> handleGenerateButtonClick());

        cardViewButton1.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void handleGenerateButtonClick() {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            editText.setError("Enter the Value of Print Count");
            editText.requestFocus();
            return;
        }

        if (textViewName.getText().toString().isEmpty()) {
            textViewName.setError("Enter the name");
            textViewName.requestFocus();
            return;
        }

        int requestedCounts = Integer.parseInt(input);
        if (requestedCounts <= 0) {
            editText.setError("Enter Correct Value");
            editText.requestFocus();
            return;
        }

        if (ConnectionCheck.isConnected(connectivityManager, networkInfo, context)) {
            if (isVerified()) {
                clientName = textViewName.getText().toString();
                if (remainingCounts >= requestedCounts) {
                    remainingCounts -= requestedCounts;
                    mainActivity.uploadDetailsForOldPrintCount(clientName, remainingCounts, OTPCODE, requestedCounts, oldPrintCountVersion, oldPrintCountDealerName,oldPrintCountDealerId);
                } else {
                    Toast.makeText(context, "Unable to Generate Print Count OTP", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(context, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isVerified() {
        // Implement your verification logic here
        // Return true if verified, false otherwise
        return true;
    }


}