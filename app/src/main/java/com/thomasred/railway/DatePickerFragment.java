package com.thomasred.railway;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;


/**
 * Created by RED on 2016/12/28.
 */

public class DatePickerFragment extends DialogFragment {

    public String data = "";
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //得到Calendar類實例，用於獲取當前時間
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //返回DatePickerDialog對象
        //因為實現了OnDateSetListener接口，所以第二個參數直接傳入this
        return new DatePickerDialog(getActivity(), (MainActivity)getActivity(), year, month, day){
            // 重寫onStop
            @Override
            protected void onStop() {
            }
        };
    }
}
