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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityActivity extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private ListView listView;
    private String[] data=new String[100];
//    private String[] data={"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private int[] cids=new int[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Intent intent = getIntent();
        final int pid = intent.getIntExtra("pid",0);
        Log.i("我们接收到了id",""+pid);
        this.textView=(TextView) findViewById(R.id.abc);
//        this.button = (Button)findViewById(R.id.button);
        this.listView = (ListView) findViewById(R.id.list_view1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CityActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView = (ListView) findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("点击哪一个",""+position+":"+CityActivity.this.cids[position]+":"+CityActivity.this.data[position]);
                Intent intent = new Intent(CityActivity.this,CountyActivity.class);
                intent.putExtra("cid",CityActivity.this.cids[position]);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
//        this.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(CityActivity.this,ProvinceActivity.class));
//            }
//        });


//        String weatherId="CN101010200";
        String weatherUrl =  "http://guolin.tech/api/china/"+pid;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                String[] result=parseJSONObject(responseText);
                CityActivity.this.data = result;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(responseText);
//                    }
//                });
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
    private String[] parseJSONObject(String responseText)  {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                this.data[i] = jsonObject.getString("name");
                this.cids[i] = jsonObject.getInt("id");
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
