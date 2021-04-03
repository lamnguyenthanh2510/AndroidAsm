package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLanguage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    //Kết nối biến từ file Layout
    EditText editSearch;
    Button bntSearch, bntChangeActivity;
    TextView txtName, txtCountry, txtStatus, txtCloud, txtWind,txtDay,txtHumidity,txtTemp;
    ImageView imgIcon;
    String City="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        GetCurrentWeatherData("HaNoi");

        bntSearch.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view ){
                String city = editSearch.getText().toString();
                if (city.equals("")){
                    City = "HaNoi";
                    GetCurrentWeatherData(City);
                }else {
                    City = city;
                    GetCurrentWeatherData(City);
                }
                GetCurrentWeatherData(city);
            }
        });

        bntChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            //Chuyển màn hình
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua","Dư Lieu truyen qua : " + city);


    }

//    private void Get7daysData(String data){
//        String url = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/353412?apikey=tbFOLXfZmAxAexEYOmXhcxnbZBDjQBSh&" + data ;
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        String stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("ketqua","Json: " + response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        );
//        RequestQueue.add(stringRequest);
//    }

    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&appid=e92c1f50af48a9a8121d1f2eec8cdf8c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>(){
                    public void onResponse(String response){
                        Log.d("Ketqua", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name  = jsonObject.getString("name");
                            txtName.setText("Tên Thành Phố: " + name);

                            long l  = Long.valueOf(day);
                            Date date  = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String Day = simpleDateFormat.format(date);

                            txtDay.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            //Lấy Hình ảnh qua Theme Picasso ( tìm hiểu qua Khoa Phạm)
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/wn/" + icon+"png").into(imgIcon);
                            txtStatus.setText(status);

                            //Lấy dự liệu đổ ẩm
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            //Lấy dữ liệu nhiệt độ
                            Double a = Double.valueOf(nhietdo);
                            String NhietDo = String.valueOf(a.intValue());
                            txtTemp.setText(NhietDo+"C");
                            txtHumidity.setText(doam+"%");

                            //Lấy dữ liệu của gió
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+"m/s");


                            //Lấy giữ Liệu Mây
                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("cloud");
                            String may = jsonObjectCloud.getString("all");
                            txtCloud.setText(may + "%");

                            //Lấy dữ Liệu Quốc Gia
                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtCountry.setText("Quốc Gia: " + country );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    public void onErrorResponse(VolleyError error){

                    }
                }
        );
        requestQueue.add(stringRequest);
    }
// Kết nối biến từ File Layout
    private void Anhxa(){
        editSearch = (EditText) findViewById(R.id.editTextSeach);
        bntSearch = (Button) findViewById(R.id.buttonSeach);
//        bntChangeActivity = (Button) findViewById(R.id.buttonChangeActiviti);
        txtName = (TextView) findViewById(R.id.textViewName);
        txtCloud = (TextView) findViewById(R.id.textViewCloud);
        txtCountry = (TextView) findViewById(R.id.textViewCountry);
        txtDay = (TextView) findViewById(R.id.textViewDay);
        txtStatus = (TextView) findViewById(R.id.textViewStatus);
        txtWind = (TextView) findViewById(R.id.textViewWind);
        txtHumidity = (TextView) findViewById(R.id.textViewHumidity);
        imgIcon = (ImageView) findViewById(R.id.imageViewIcon);
        txtTemp = (TextView) findViewById(R.id.textViewTemp);
    }


}