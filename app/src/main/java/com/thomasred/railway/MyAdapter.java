package com.thomasred.railway;

/**
 * Created by RED on 2016/12/27.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends BaseAdapter implements OnClickListener {
    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private String[] keyString;
    private String searchdate;
    private int[] ViewID;
    private int layoutID;
    private Context _context;

    public MyAdapter(Context context, String date, ArrayList<HashMap<String, Object>> appList,
                     int resource, String[] from, int[] to) {
        mAppList = appList;
        layoutID = resource;
        _context = context;
        searchdate = date;
        this.mInflater = LayoutInflater.from(context);
        keyString = new String[from.length];
        ViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, ViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutID, null);
            // 創建 ViewHodler 對象
            vHolder = new ViewHolder();
	        /*for(int i = 0 ; i < ViewID.length ; i ++)
	        	vHolder.v[i] = (TextView) convertView.findViewById(ViewID[i]);//*/
            vHolder.train = (TextView) convertView.findViewById(ViewID[0]);
            vHolder.id = (TextView) convertView.findViewById(ViewID[1]);
            vHolder.fromtime = (TextView) convertView.findViewById(ViewID[2]);
            vHolder.totime = (TextView) convertView.findViewById(ViewID[3]);
            vHolder.totaltime = (TextView) convertView.findViewById(ViewID[4]);
            vHolder.cost = (TextView) convertView.findViewById(ViewID[5]);
            vHolder.comment = (TextView) convertView.findViewById(ViewID[6]);
            // 設置 Tag
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        // 設置位圖
        TableLayout background = (TableLayout) convertView.findViewById(R.id.back);
        if("自強".compareTo(getData(position, "train")) == 0){
            background.setBackgroundColor(Color.parseColor("#11ff0000"));
            vHolder.train.setTextColor(Color.parseColor("#eeff0000"));
            vHolder.id.setTextColor(Color.parseColor("#eeff0000"));
        } else if("莒光".compareTo(getData(position, "train")) == 0){
            background.setBackgroundColor(Color.parseColor("#110000ff"));
            vHolder.train.setTextColor(Color.parseColor("#ee0000ff"));
            vHolder.id.setTextColor(Color.parseColor("#ee0000ff"));
        } else {
            background.setBackgroundColor(Color.parseColor("#11ff9900"));
            vHolder.train.setTextColor(Color.parseColor("#eeff9900"));
            vHolder.id.setTextColor(Color.parseColor("#eeff9900"));
        }

		/*for(int i = 0 ; i < keyString.length ; i ++)
			vHolder.v[i].setText(getData(position, keyString[i]));//*/
        vHolder.train.setText(getData(position, keyString[0]));
        vHolder.id.setText(getData(position, keyString[1]));
        vHolder.fromtime.setText(getData(position, keyString[2]));
        vHolder.totime.setText(getData(position, keyString[3]));
        vHolder.totaltime.setText(getData(position, keyString[4]));
        vHolder.cost.setText(getData(position, keyString[5]));
        vHolder.comment.setText(getData(position, keyString[6]));
        convertView.setOnClickListener(this);
        return convertView;
    }

    private String getData(int pos, String tag){
        return mAppList.get(pos).get(tag).toString();
    }

    static class ViewHolder {
        //ImageView pic;
        //TextView[] v = new TextView[7];
        TextView train;
        TextView id;
        TextView fromtime;
        TextView totime;
        TextView totaltime;
        TextView cost;
        TextView comment;//*/
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        ViewHolder vHolder = (ViewHolder) v.getTag();
        //Log.d("tag", vHolder.train.getText().toString());
        String data = vHolder.train.getText().toString() + " " +
                vHolder.fromtime.getText().toString() + " " +
                vHolder.cost.getText().toString()+"\n";
        NotificationManager mNotificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(_context);

        boolean isAboveLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        mBuilder.setContentTitle("記得取票!")//設置通知欄標題
                .setContentText(searchdate + " " + data)
                .setWhen(System.currentTimeMillis())//通知產生的時間，會在通知信息裡顯示，一般是系統獲取到的時間
                .setTicker("訂到票啦!!!! 超爽der!") //通知首次出現在通知欄，帶上升動畫效果的 5.0以上失效
                .setPriority(Notification.PRIORITY_DEFAULT) //設置該通知優先級
                .setLargeIcon(BitmapFactory.decodeResource(_context.getResources(),R.drawable.noti))
                .setSmallIcon(isAboveLollipop ? R.drawable.noti : R.drawable.ic_launcher)//設置通知小ICON
                .setDefaults(Notification.DEFAULT_ALL); //使用所有默認值，比如聲音，震動，閃屏等等

        Notification notification = mBuilder.build();
        mNotificationManager.notify(100, notification);


    }

}

