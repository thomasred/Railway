package com.thomasred.railway;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by RED on 2016/12/28.
 */


//實現OnTimeSetListener接口
public class TimePickerFragment extends DialogFragment {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //新建日曆類用於獲取當前時間
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //返回TimePickerDialog對象
        //因為實現了OnTimeSetListener接口，所以第二個參數直接傳入this
        return new TimePickerDialog(getActivity(), (MainActivity)getActivity(), hour, minute, false){
            // 重寫onStop
            @Override
            protected void onStop() {
            }
        };
    }
}
