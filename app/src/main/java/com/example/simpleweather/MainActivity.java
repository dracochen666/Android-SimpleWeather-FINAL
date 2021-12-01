package com.example.simpleweather;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.example.simpleweather.Model.Constant;
import com.example.simpleweather.Model.HttpRequest;
import com.example.simpleweather.Model.Weather;

import java.io.IOException;
import java.net.HttpRetryException;

public class MainActivity extends AppCompatActivity {


    Runnable runnableCity = null;
    Runnable InquireRunnable;
    TextView text = null;
    String longitude;
    String latitude;
    //
    ImageView iv_search,iv_weather;
    TextView tv_degree,tv_location;
    String InquireLocation;
//    Handler handler;

    //界面布局
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_weathermain);

        iv_search = findViewById(R.id.iv_search);
        iv_weather = findViewById(R.id.iv_weather);
        tv_degree = findViewById(R.id.tv_degree);
        tv_location = findViewById(R.id.tv_location);

        init();

        runnableCity = new Runnable() {
            @Override
            public void run() {

                try {
                    String cityJSON = null;
                    //拼接字符串 URL?key=你的key&location=longitude,latitude
                    String nowURL = Constant.qQWeatherCityPath + "?key=" + Constant.qQWeatherKey + "&loca" +
                            "tion=" + longitude + "," + latitude;
                    System.out.println(nowURL);
                    //请求city返回JSON
                    cityJSON = HttpRequest.getResult(nowURL);
                    //处理city的JSON
                    Weather weather = HttpRequest.processCityJSON(cityJSON);
                    Message msg = handler.obtainMessage();//创建消息对象
                    //
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("weather",weather);

                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    handler.sendMessage(msg);

//                    System.out.println(weather);
////                    System.out.println(weather.temp);
//
//                    String iconNum = "w"+weather.icon;
//
//                    System.out.println(iconNum);
//                    int picId = getResources().getIdentifier(iconNum,"drawable","com.example.myavaudiorecorder");
//                    tv_location.setText(weather.city);
//                    iv_weather.setImageResource(picId);
////                    iv_weather.setBackgroundResource(picId);
//                    System.out.println(picId);
//                    tv_degree.setText(weather.temp + "°C");
                } catch (HttpRetryException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        InquireRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    String cityJSON = null;
                    //拼接字符串 URL?key=你的key&location=location
                    String nowURL = Constant.qQWeatherCityPath + "?key=" + Constant.qQWeatherKey + "&loca" +
                            "tion=" + InquireLocation;
                    System.out.println(nowURL);
                    //请求city返回JSON
                    cityJSON = HttpRequest.getResult(nowURL);
                    //处理city的JSON
                    Weather InquireWeather = HttpRequest.processCityJSON(cityJSON);

//                    System.out.println(InquireWeather);
//                    String iconNum = "w"+InquireWeather.icon;
//                    String cityName = InquireWeather.city;
//                    System.out.println(iconNum);
//                    System.out.println(InquireWeather.city);
//                    int picId2 = getResources().getIdentifier(iconNum,"drawable","com.example.myavaudiorecorder");
//                    iv_weather.setImageResource(picId2);
//                    tv_location.setText(InquireWeather.city.toString());
////                    iv_weather.setBackgroundResource(picId);
//                    tv_degree.setText(InquireWeather.temp + "°C");
                    Message msg = handler.obtainMessage();//创建消息对象
                    //
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("weather",InquireWeather);

                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    handler.sendMessage(msg);

                } catch (HttpRetryException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

//        System.out.println(latitude);
//        System.out.println(longitude);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_send = new Intent(MainActivity.this, SearchAcitivity.class);
                startActivityForResult(intent_send,1);

            }
        });
//        startTheard();



    }
//    @Override
//     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                 super.onActivityResult(requestCode, resultCode, data);
//                 InquireLocation = data.getDataString().toString();
//                //当otherActivity中返回数据的时候，会响应此方法
//                 //requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
//                 if(requestCode==1&&resultCode==2)
//                    {
//                        InquireLocation = data.getDataString();
//                    }
//             }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 2) {
            //校验结果码是否正确
            return;
        }
        if (requestCode == 1 && resultCode == 2) {
            //校验请求码是否正确
            if (data.getStringExtra("city") == null) {
                //判断返回结果是否为空
                return;
            }
            InquireLocation = data.getStringExtra("city");//获取返回结果值
            searchTheard();
        }
    }

    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏显示

        text = (TextView) findViewById(R.id.tv_location);  //获取显示Location信息的TextView组件
        //获取系统的LocationManager对象
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //添加权限检查
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //设置每一秒获取一次location信息
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,      //GPS定位提供者
                1000,       //更新数据时间为1秒
                1,      //位置间隔为1米
                //位置监听器
                new LocationListener() {  //GPS定位信息发生改变时触发，用于更新位置信息

                    @Override
                    public void onLocationChanged(Location location) {
                        //GPS信息发生改变时，更新位置
                        locationUpdates(location);
                    }

                    @Override
                    //位置状态发生改变时触发
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    //定位提供者启动时触发
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    //定位提供者关闭时触发
                    public void onProviderDisabled(String provider) {
                    }
                });
        //从GPS获取最新的定位信息
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);    //将最新的定位信息传递给创建的locationUpdates()方法中
    }
    public void locationUpdates(Location location) {  //获取指定的查询信息
        //如果location不为空时
        if (location != null) {
            StringBuilder stringBuilder = new StringBuilder();        //使用StringBuilder保存数据
            //获取经度、纬度、等属性值
            stringBuilder.append("Longitude：");
            longitude = String.valueOf(location.getLongitude());
            stringBuilder.append(location.getLongitude());
            stringBuilder.append("\nLatitude：");
            latitude = String.valueOf(location.getLatitude());
            stringBuilder.append(location.getLatitude());

            startTheard();
            text.setText(stringBuilder);            //显示获取的信息
        } else {
            //否则输出空信息
            text.setText("没有获取到GPS信息");
        }
    }


    private void startTheard() {
        //
        Thread thread = new Thread(runnableCity);
        thread.start();
    }
    private void searchTheard(){
        Thread InquireThread = new Thread(InquireRunnable);
        InquireThread.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            Weather weatherTemp = (Weather) bundle.getSerializable("weather");
                    System.out.println(weatherTemp);
//                    System.out.println(weather.temp);

                    String iconNum = "w"+weatherTemp.icon;

                    System.out.println(iconNum);
                    int picId = getResources().getIdentifier(iconNum,"drawable","com.example.myavaudiorecorder");
                    tv_location.setText(weatherTemp.city);
                    iv_weather.setImageResource(picId);
                    System.out.println(picId);
                    tv_degree.setText(weatherTemp.temp + "°C");
        }
    };


}


