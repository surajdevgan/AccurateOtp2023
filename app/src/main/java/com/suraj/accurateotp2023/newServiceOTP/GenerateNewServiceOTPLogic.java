package com.suraj.accurateotp2023.newServiceOTP;

import android.util.Log;

public class GenerateNewServiceOTPLogic {

  public String newServiceOTPLogic(String raw) {
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

       // shareWhatsapp(ServiceOtpis);

        return  ServiceOtpis;
    }

}
