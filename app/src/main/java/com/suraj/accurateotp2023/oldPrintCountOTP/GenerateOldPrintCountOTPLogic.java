package com.suraj.accurateotp2023.oldPrintCountOTP;

import android.util.Log;

public class GenerateOldPrintCountOTPLogic {
    int c = 0;
    String LMG = "", MG = "", RMG = "";
    String PLMG = "", PMG = "", PRMG = "";
    String newString = "";
    String value1 = "", value0 = "";
    public String oldPrintCountOTPLogic(String raw, int requestedCounts) {
        Log.w("newrawdata",raw);
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

        int divide = requestedCounts / 100;
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
                switch (d) {
                    case 'A':
                        d = '@';
                        break;
                    case 'B':
                        d = '!';
                        break;
                    case 'J':
                        d = '%';
                        break;
                    case 'W':
                        d = '&';
                        break;
                    case 'X':
                        d = '*';
                        break;
                    default:
                        break;
                }
            }
            PD.append(d);
        }
        return PD.toString();
    }

    public char finalAlgo(char val1) {
        switch ((int) val1) {
            case 39:
                val1 = 'a';
                break;
            case 44:
                val1 = 'b';
                break;
            case 45:
                val1 = 'c';
                break;
            case 46:
                val1 = 'd';
                break;
            case 58:
                val1 = 'e';
                break;
            case 59:
                val1 = 'f';
                break;
            default:
                break;
        }
        return val1;
    }
}
