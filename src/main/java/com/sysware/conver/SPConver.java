package com.sysware.conver;


import com.google.gson.JsonObject;
import net.sf.json.JSONObject;

/**
 * SharePoint 返回参数转换
 *
 */
public class SPConver {


    /**
     * 根据返回json对象，取出用户id
     * 可以用来设置权限的id
     * @param jsonString SharePoint请求返回的json字符串
     * @return 用户id
     */
    public static String getUserId(String jsonString){
        String userId ="" ;
        JSONObject userInfo =  getUserInfo(jsonString);
        if(userInfo != null ){
            userId=   userInfo.getString("Id");
        }
        return userId;
    }

    /**
     * 获取用户信息
     * @param jsonString SharePoint请求返回的json字符串
     * @return 用户的JSONObject
     */
    public static JSONObject getUserInfo(String jsonString){
        JSONObject userInfo = getJSONObject(jsonString);
        return  userInfo;
    }

    /**
     * 获取返回数据的id
     * 如果源数据里有id 的话
     *
     * @param jsonString SharePoint请求返回的json字符串
     * @return 数据id
     */
    public static String  getId(String jsonString){
        JSONObject d = getJSONObject(jsonString);
        if(d != null ){
            return d.getString("Id");
        }
        return null;
    }

    /**
     * 获取jsonObject对象
     * @param jsonString SharePoint请求返回的json字符串
     * @return 数据id
     */
    public static JSONObject getJSONObject(String jsonString){
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        if(jsonObject !=null ){
            return jsonObject.getJSONObject("d");
        }
        return null;
    }

}
