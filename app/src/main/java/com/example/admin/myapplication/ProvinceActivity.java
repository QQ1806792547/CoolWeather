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
    private TextView textView;
    private Button button;
    private ListView listView;
    private List<String> data=new ArrayList<>();
    private int[] pids=new int[100];
    private int[] cids=new int[100];
    private String currentlevel="ProvinceActivity";
    private int pid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procince);
        //TODO if(currentlevel=="city"){
        //     Intent intent = getIntent();
        //        final int pid = intent.getIntExtra("pid",0);
        // }
        this.textView=(TextView) findViewById(R.id.abcd);
//        this.button = (Button)findViewById(R.id.button1);
        this.listView = (ListView) findViewById(R.id.list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProvinceActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("点击哪一个",""+position+":"+ProvinceActivity.this.pids[position]+":"+ProvinceActivity.this.data.get(position));
                pid=ProvinceActivity.this.pids[position];
                currentlevel="city";
                getData(adapter);
//                Intent intent = new Intent(ProvinceActivity.this,CityActivity.class);
//                intent.putExtra("pid",ProvinceActivity.this.pids[position]);
//                if(currentlevel=="city"){
//                    intent.putExtra("cid",cids[position]);
//                }
//                startActivity(intent);
            }
        });
        getData(adapter);
    }

    private void getData(final ArrayAdapter<String> adapter){
        String weatherUrl = currentlevel=="city"?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china";
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
        JSONArray jsonArray = null;
        this.data.clear();
        try {
            jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                if(currentlevel=="city") {
                    this.cids[i] = jsonObject.getInt("id");
                }else {
                    this.pids[i] = jsonObject.getInt("id");
                }

            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
