package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cs14055 on 2016/02/02.
 */
public class BMMatching {
    private static String pattern;
    private static int[] shift;
    private static int patlen;

    private static void init(){
        shift = new int[65536];

        for(int i = 0;i < shift.length;i++){
            shift[i] = patlen;
        }
        for(int i = 0;i < patlen - 1;i++){
            Log.d("BMMathcing Init","Shift : " + (int)pattern.charAt(i));
            shift[(int)pattern.charAt(i)] = patlen - i - 1;
        }
    }
    private static int compare(char s,char p){
        return s - p;
    }
    private static int shift(int i){
        int j;
        j = shift[i];
        return j;
    }
    public static int bmMatching(String text, String pat, int index){
        int i,j;
        Log.d("BMMatching", "MATCH " + text + " AND " + pat);
        Log.d("BMMatching", "LENGTH (TEXT)" + text.length() + " (PATTERN)" + pat.length());

        pattern = new String(pat);
        patlen = pattern.length();
        if(patlen == 0){
            return -2;
        }

        i = patlen - 1 + index;

        init();

        while(i < text.length()){
            j = patlen - 1;
            while(compare(text.charAt(i),pattern.charAt(j))==0){
                if(j == 0){
                    return i;
                }
                i--;
                j--;
            }
            i = i + Math.max(shift((int)text.charAt(i)),patlen-j);
        }
        return -1;
    }

}
