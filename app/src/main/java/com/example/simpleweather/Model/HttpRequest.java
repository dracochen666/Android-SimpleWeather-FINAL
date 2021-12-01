package com.example.simpleweather.Model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequest {

    public static Weather weather = new Weather();;
    //拿到最终返回的数据
    public static String getResult(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static Weather processCityJSON(String cityJSON) {
        try {
            //解析请求的CityJSON
            JSONObject jsonObject = new JSONObject(cityJSON);
            JSONArray result = jsonObject.getJSONArray("location");
            JSONObject location = result.getJSONObject(0);
            //拿到cityID
            String cityID = location.getString("id");
            //通过cityID再去请求city的icon和temp;

            //拼接URL
            String weatherURL = Constant.qQWeatherWeatherPath + "?key=" + Constant.qQWeatherKey + "&loc" +
                    "ation=" + cityID;
            //拿到weatherJSON
            String weatherJSON = getResult(weatherURL);
            //处理weatherJSON
            processWeatherJSON(weatherJSON);

            weather.city = location.getString("name");

            //返回城市名
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //将resultInfo解析json
    public static String processWeatherJSON(String weatherJson){

        try {
            JSONObject jsonObject = new JSONObject(weatherJson);
            String result = jsonObject.getString("now");
            JSONObject jsonNow = new JSONObject(result);
            String icon = jsonNow.getString("icon");
            String temp = jsonNow.getString("temp");
//            System.out.println(icon);
//            System.out.println(temp);
            weather.temp = temp;
            weather.icon = icon;
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "解析JSON失败";
    }
    //计算文件的大小
    public static long getFileLen(String filePath){
        try{
            File file = new File(filePath);
            return file.length();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    //处理字符串
    public static String trimSpaceTag(String str) {
        String regEx_space = "\n";//定义空格回车换行符
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(str);
        str = m_space.replaceAll(""); // 过滤空格回车标签
        return str.trim(); // 返回文本字符串
    }


}
