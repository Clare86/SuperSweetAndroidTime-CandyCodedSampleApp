package com.codeschool.candycoded;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)this.findViewById(R.id.text_view_title);
        textView.setText(R.string.products_title);

        final ArrayList<String> candy_list = new ArrayList<String>();

        candy_list.add("Tropical Wave");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_candy,
                R.id.text_view_candy,
                candy_list
        );

        ListView listView = (ListView)this.findViewById(R.id.list_view_candy);

        listView.setAdapter(adapter);

        Context context = this;
        String text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra("candy_name", candy_list.get(i));
                startActivity(detailIntent);
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://s3.amazonaws.com/courseware.codeschool.com/super_sweet_android_time/API/CandyCoded.json",
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        Log.e("AsyncHttpClient", "response = " + response);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        Log.d("AsyncHttpClient", "response = " + response);
                        Gson gson = new GsonBuilder().create();;
                        Candy[] candies = gson.fromJson(response, Candy[].class);
                        adapter.clear();
                        for(Candy candy : candies) {
                            adapter.add(candy.name);
                        }
                    }
                });
    }
}
