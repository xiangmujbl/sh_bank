/*
 * JSON工具类 
 * 进行 字符串、JAVA对象、JSON对象之间的转换
 */
package com.mmec.util;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {
	private  static Gson gson = new Gson();
	
	//java对象转换成json字符串
	public static <T> String Obj2String(T obj)
	{
		//将java对象转换为json对象
		return gson.toJson(obj);
	}
	//java对象转换成json字符串
	public static  String list2String(List obj)
	{
		//将java对象转换为json对象
		return gson.toJson(obj);
	}

	public  static  <T> List<T> str2List(String str,T obj)
	{
		JSONArray jsonArray = JSONArray.fromObject(str);  
        List<T> lists = (List) JSONArray.toCollection(jsonArray,  obj.getClass());  
        return lists;
	}
	//java对象转换成json字符串
	public static  String map2String(Map obj)
	{
		//将java对象转换为json对象
		return gson.toJson(obj);
	}
	//JSON格式字符串转换成JSON对象
	public static  JSONObject string2JSON(String jsonStr)
	{
		JSONObject json = JSONObject.fromObject(jsonStr);
		return json;
	}
	
	//JSON字符串转换为java对象
	public static  <T> T string2Object(String objStr,Class<T> obj)
	{
		//将JSON对象转换为java对象
		return (T)JSONObject.toBean(string2JSON(objStr),obj);
	}
}

class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp>{  
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    public JsonElement serialize(Timestamp src, Type arg1, JsonSerializationContext arg2) {  
        String dateFormatAsString = format.format(new Date(src.getTime()));  
        return new JsonPrimitive(dateFormatAsString);  
    }  
  
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {  
        if (!(json instanceof JsonPrimitive)) {  
            throw new JsonParseException("The date should be a string value");  
        }  
  
        try {  
            Date date = format.parse(json.getAsString());  
            return new Timestamp(date.getTime());  
        } catch (ParseException e) {  
            throw new JsonParseException(e);  
        }  
    }  
  
}  