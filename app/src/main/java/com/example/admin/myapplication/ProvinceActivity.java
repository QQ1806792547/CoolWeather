package com.example.admin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProvinceActivity extends AppCompatActivity {
    public static final String CITY = "city";
    public static final String COUNTY = "county";
    public static final String PROVINCE = "province";
    private TextView textView;
    private Button button;
    private ListView listView;
    private List<String> areaNameList =new ArrayList<>();
    private List<Integer> areaIdList =new ArrayList<>();
    private List<String> weatherIdList =new ArrayList<>();
    private String currentlevel= PROVINCE;
    private int pid = 0;
    private int cid = 0;
    private int wid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procince);
        this.textView=(TextView) findViewById(R.id.abcd);
//        this.button = (Button)findViewById(R.id.button1);
        this.listView = (ListView) findViewById(R.id.list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProvinceActivity.this,android.R.layout.simple_list_item_1, areaNameList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("点击哪一个",""+position+":"+ProvinceActivity.this.areaIdList.get(position)+":"+ProvinceActivity.this.areaNameList.get(position));
                if(currentlevel== PROVINCE){
                    currentlevel= CITY;
                    pid=ProvinceActivity.this.areaIdList.get(position);
                }
                else if(currentlevel==CITY){
                    currentlevel= COUNTY;
                    cid=ProvinceActivity.this.areaIdList.get(position);
                }
                else if(currentlevel == COUNTY){
                    String wid=ProvinceActivity.this.weatherIdList.get(position);

                    Intent intent = new Intent(ProvinceActivity.this,WeatherActivity.class);
                    intent.putExtra("wid",wid);
                    startActivity(intent);
                }
                getData(adapter);
            }
        });
        getData(adapter);
    }

    private void getData(final ArrayAdapter<String> adapter){
        String weatherUrl = currentlevel==PROVINCE?"http://guolin.tech/api/china/":(currentlevel==CITY?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china/"+pid+"/"+cid);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                String[] result=parseJSONObject(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        textView.setText(responseText);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private String[] parseJSONObject(String responseText)  {
        this.areaNameList.clear();
        this.areaIdList.clear();
        this.weatherIdList.clear();
        try {
            JSONArray jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                this.areaNameList.add(jsonObject.getString("name"));
                this.areaIdList.add(jsonObject.getInt("id"));
                if (jsonObject.has("weather_id")){
                    this.weatherIdList.add(jsonObject.getString("weather_id"));
                }

            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
