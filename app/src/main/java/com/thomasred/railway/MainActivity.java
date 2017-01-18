package com.thomasred.railway;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import static com.thomasred.railway.R.id.date;

public class MainActivity extends FragmentActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener
{

    private Spinner _s_area, _s_station;//第一組下拉選單
    private Spinner _e_area, _e_station;//第二組下拉選單
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinutes;
    private Button _date, _time1, _time2;
    private ImageButton _send;
    private int s_spinner, e_spinner;
    private String nowDate =""; // 儲存當前日期，用來確認是否在此日期之前
    private String URL = "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=0";
    private String searchdate ="&searchdate=";
    private String fromstation ="&fromstation=1008";
    private String tostation ="&tostation=1238";
    private String trainclass ="&trainclass=2";
    private String fromtime ="&fromtime=0000";
    private String totime ="&totime=2359";
    private int timeData = 0;
    int[] all_area = {R.array.taipei_area, R.array.taoyuan_area,
            R.array.hsinchu_area,R.array.miaoli_area,
            R.array.taichung_area, R.array.changhua_area,
            R.array.nantou_area, R.array.yunlin_area,
            R.array.chiayi_area,R.array.tainan_area,
            R.array.kaohsiung_area,R.array.pingtung_area,
            R.array.taitung_area, R.array.hualien_area,
            R.array.ilan_area, R.array.pinghsi_shenao_area,
            R.array.neiwan_liujia_area, R.array.jiji_area,
            R.array.shalun_area};
    int[] def_station = {18, 0, 2, 12, 13, 0, 3, 2, 3,
            11, 7, 1, 7, 17, 12, 1, 13, 4, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _s_area = (Spinner) findViewById(R.id.s_area);
        _s_station = (Spinner) findViewById(R.id.s_station);
        init_sp(R.array.area_name, _s_area);
        _s_area.setSelection(0,true); // default 台北

        _e_area = (Spinner) findViewById(R.id.e_area);
        _e_station = (Spinner) findViewById(R.id.e_station);
        init_sp(R.array.area_name, _e_area);
        _e_area.setSelection(10,true); // default 高雄

        RadioGroup _RG = (RadioGroup) findViewById(R.id.RG);
        _RG.setOnCheckedChangeListener(this);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR);
        mMinutes = calendar.get(Calendar.MINUTE);

        _date = (Button)findViewById(date);
        _date.setOnClickListener(this);
        _time1 = (Button)findViewById(R.id.time1);
        _time1.setOnClickListener(this);
        _time2 = (Button)findViewById(R.id.time2);
        _time2.setOnClickListener(this);

        // 設定預設的搜索日期為當天
        //nowDate = setDateFormat(mYear, mMonth, mDay);
        nowDate = mYear + "/" +
                String.format(Locale.CHINESE,"%02d", (mMonth+1)) + "/" +
                String.format(Locale.CHINESE,"%02d", mDay);
        searchdate = "&searchdate=" + nowDate;
        _date.setText(nowDate);

        _send = (ImageButton)findViewById(R.id.send);
        _send.setOnClickListener(this);

    }

    /**
     * spinner initial
     */
    void init_sp(int area, Spinner sp){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                area, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
    }

    /**
     * spinner 物件選擇觸發
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        // TODO Auto-generated method stub
        switch(parent.getId()){
            case R.id.s_area :
                // Toast.makeText(MainActivity.this,
                //		"位置!!!"+  Integer.toString(position), Toast.LENGTH_LONG).show();
                s_spinner = all_area[position];
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                        all_area[position],
                        android.R.layout.simple_spinner_dropdown_item);
                _s_station.setAdapter(adapter2);
                _s_station.setOnItemSelectedListener(this);
                _s_station.setSelection(def_station[position],true); // default station
                break;
            case R.id.e_area :
                // Toast.makeText(MainActivity.this,
                //		"位置!!!"+  Integer.toString(position), Toast.LENGTH_LONG).show();
                e_spinner = all_area[position];
                ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                        all_area[position],
                        android.R.layout.simple_spinner_dropdown_item);
                _e_station.setAdapter(adapter4);
                _e_station.setOnItemSelectedListener(this);
                _e_station.setSelection(def_station[position],true); // default station
                break;
            case R.id.s_station :
                String item =this.getResources().getStringArray(s_spinner+1)[position];
                fromstation ="&fromstation="+item;
                break;
            case R.id.e_station :
                String item1 =this.getResources().getStringArray(e_spinner+1)[position];
                tostation ="&tostation="+item1;
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * RadioGroup 選擇觸發
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch(checkedId){
            case R.id.rb1 :
                trainclass ="&trainclass=2";
                break;
            case R.id.rb2 :
                trainclass ="&trainclass='1100','1101','1102','1107','1110','1120'";
                break;
            case R.id.rb3 :
                trainclass ="&trainclass='1131','1132','1140'";
                break;
        }
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String Month = String.format(Locale.CHINESE,"%02d", (month+1));
        String Day = String.format(Locale.CHINESE,"%02d", day);
        String date = year + "/" + Month + "/" + Day;
        _date.setText(date);
        searchdate = "&searchdate=" + date;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.format(Locale.CHINESE,"%02d", hourOfDay);
        String min = String.format(Locale.CHINESE,"%02d", minute);
        if(timeData == R.id.time1) {
            _time1.setText(hour + ":" + min);
            fromtime = "&fromtime=" + hour + min;
        } else {
            _time2.setText(hour + ":" + min);
            totime = "&totime=" + hour + min;
        }
    }

    /**
     * Button 選擇觸發
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        TimePickerFragment timePicker = new TimePickerFragment();
        switch(v.getId()){
            case date :
                //showDialog(0);
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date_picker");
                break;
            case R.id.time1 :
                //showDialog(1);
                timeData = R.id.time1;
                timePicker.show(getSupportFragmentManager(), "time_picker");
                break;
            case R.id.time2 :
                //showDialog(2);
                timeData = R.id.time2;
                timePicker.show(getSupportFragmentManager(), "time_picker");
                break;
            case R.id.send :
                // 取得網路連線的狀態
                NetworkInfo mNetworkInfo = ((ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();

                // 如果未連線的話，mNetworkInfo會等於null
                if(mNetworkInfo == null) {
                    Toast.makeText(MainActivity.this, "請開啟網路連線!", Toast.LENGTH_LONG).show();
                    break;
                }

                if(("&searchdate="+nowDate).compareTo(searchdate) > 0){
                    Toast.makeText(MainActivity.this,
                            "無法查詢"+nowDate+"以前的時刻表\n請重新選擇日期!"
                            , Toast.LENGTH_LONG).show();
                    break;
                }
                if(totime.substring(totime.length()-4).compareTo
                        (fromtime.substring(fromtime.length()-4)) < 0){
                    Toast.makeText(MainActivity.this,
                            "請重新選擇查詢時段!"
                            , Toast.LENGTH_LONG).show();
                    break;
                }

                URL = URL + searchdate + fromstation + tostation +
                        trainclass + fromtime + totime;

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Html.class);
                Bundle bundle = new Bundle();
                bundle.putString("Date", searchdate);
                /*Toast.makeText(MainActivity.this,
                        URL, Toast.LENGTH_LONG).show();*/
                bundle.putString("URL", URL);
                intent.putExtras(bundle);
                startActivity(intent);

                URL = "http://twtraffic.tra.gov.tw/twrail/SearchResult.aspx?searchtype=0";
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
}
