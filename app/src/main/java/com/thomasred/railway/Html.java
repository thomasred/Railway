package com.thomasred.railway;
/**
 * Created by RED on 2016/12/27.
 */


import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

//import android.widget.SimpleAdapter;

public class Html extends ListActivity {
    //private SimpleAdapter adapter;
    private MyAdapter adapter;
    private String url;
    private String date;
    private ArrayList<HashMap<String, Object>> mdata ;
    private final String Xpath_id="//a[@id='TrainCodeHyperLink']";
    private final String Xpath_train="//span[@id='classname']";
    private final String Xpath_cost="//span[@id='Label1']";
    private final String Xpath_comment="//span[@id='Comment']";
    static{
        if( Build.VERSION.SDK_INT > 8 ){  // SDK = 9 以上加入strick mode
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy
                    .Builder()
                    .detectNetwork()
                    .build());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString("URL");
        date = bundle.getString("Date");
        date = date.substring(12); // 去除 &searchdate=
        Log.d("tag", date);

        // 把 html 資料加入ArrayList中
        mdata = getHtmlContent();
        if(mdata.size()==0){
            Toast.makeText(Html.this,
                    "沒有任何車次，幫QQ", Toast.LENGTH_LONG).show();
        }else {

            // 新增SimpleAdapter
            adapter = new MyAdapter(this, date, mdata, R.layout.style,
                    new String[]{"train", "id", "fromtime", "totime",
                            "totaltime", "cost", "comment"},
                    new int[]{R.id.train, R.id.id, R.id.fromtime, R.id.totime,
                            R.id.totaltime, R.id.cost, R.id.comment});

            //ListActivity設定adapter
            setListAdapter(adapter);

            //啟用按鍵過濾功能，這兩行資料都會進行過濾
            getListView().setTextFilterEnabled(true);
        }

    }


    private ArrayList<HashMap<String, Object>> getHtmlContent() {
        // TODO Auto-generated method stub
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> item;
        HtmlCleaner htmlcleaner = new HtmlCleaner();
        try {
            TagNode root = htmlcleaner.clean(new URL(url));
            TagNode[] trTag = root.getElementsByAttValue("class",
                    "Grid_Row", true, true);
            TagNode[] tmp;
            for(int i = 1 ; i < trTag.length ; i ++){
                tmp = trTag[i].getElementsByName("td", true);
                item = new HashMap<String, Object>();
                for(int j = 0 ; j < tmp.length-2 ; j ++){
                    switch(j){
                        case 0:
                            item.put("train", ObToStr(tmp[j], Xpath_train));
                            break;
                        case 1:
                            item.put("id", ObToStr(tmp[j], Xpath_id));
                            break;
                        case 4:
                            item.put("fromtime", ObToStr(tmp[j],null));
                            break;
                        case 5:
                            item.put("totime", ObToStr(tmp[j],null));
                            break;
                        case 6:
                            item.put("totaltime", ObToStr(tmp[j],null));
                            break;
                        case 7:
                            item.put("comment", ObToStr(tmp[j],Xpath_comment));
                            break;
                        case 8:
                            item.put("cost", ObToStr(tmp[j],Xpath_cost));
                            break;
                    }
                }

                list.add(item);
            }

            return list;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    String ObToStr(TagNode tmp, String xpath){
        try {
            if(xpath == null)
                return tmp.getText().toString();
            return ((TagNode) tmp.evaluateXPath(xpath)[0])
                    .getText().toString();
        } catch (XPatherException e) {
            // TODO Auto-generated catch block
            Toast.makeText(Html.this,
                    "ERROR!~", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return"";
    }
}

