package com.by_syk.aqi.util;

import android.graphics.Color;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by By_syk on 2016-07-12.
 */
public class AqiHelper {
    // city=xxx
    public static final String URL_AQI = "http://apis.by-syk.com/aqi";

    /**
     * Like:
     * {
     *   "city": "成都",
     *   "aqi": "48",
     *   "grade": "2",
     *   "quality": "良",
     *   "color": "#ffff00",
     *   "advice": "极少数异常敏感人群应减少户外活动",
     *   "time": "23:12"
     * }
     *
     * @return [city, aqi, quality, color, advice]
     */
    public static Aqi getAqiData(String city) {
        Aqi aqi = new Aqi();

        String jsonData;
        if (TextUtils.isEmpty(city)) {
            jsonData = ExtraUtil.downloadHtml(URL_AQI, "UTF-8");
        } else {
            jsonData = ExtraUtil.downloadHtml(URL_AQI + "?city=" + city, "UTF-8");
        }
        if (TextUtils.isEmpty(jsonData)) {
            return aqi;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            aqi.setCity(jsonObject.getString("city"));
            aqi.setAqi(jsonObject.getInt("aqi"));
            aqi.setAqiGrade(jsonObject.getInt("grade"));
            aqi.setQuality(jsonObject.getString("quality"));
            try {
                aqi.setColor(Color.parseColor(jsonObject.getString("color")));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            aqi.setAdvice(jsonObject.getString("advice"));
            aqi.setTime(jsonObject.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return aqi;
    }

    /**
     * 参考：
     * HJ 633-2012《环境空气质量指数（AQI）技术规定（试行）》
     */
    public static String getAqiAndAqiRange(Aqi aqi) {
        if (aqi == null) {
            return "";
        }

        String range = null;
        switch (aqi.getAqiGrade()) {
            case 1:
                range = "(0 - 50)";
                break;
            case 2:
                range = "(51 - 100)";
                break;
            case 3:
                range = "(101 - 150)";
                break;
            case 4:
                range = "(151 - 200)";
                break;
            case 5:
                range = "(201 - 300)";
                break;
            case 6:
                range = "(> 300)";
        }

        return String.format("[%1$s]: %2$s %3$s", aqi.getTime(), aqi.getAqi(), range);
    }
}
