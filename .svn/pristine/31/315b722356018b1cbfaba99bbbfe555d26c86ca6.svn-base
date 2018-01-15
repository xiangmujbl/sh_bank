package com.mmec.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonToOther {
	private static Logger  log = Logger.getLogger(JsonToOther.class);
	public static List<Map<String, Object>> parseJSON2List(String jsonStr){  
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);  
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
       Iterator<JSONObject> it = jsonArr.iterator();  
        while(it.hasNext()){  
            JSONObject json2 = it.next();  
            list.add(parseJSON2Map(json2.toString()));  
        }  
        return list;  
    }  
      
    public static Map<String, Object> parseJSON2Map(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        //最外层解析  
        JSONObject json = JSONObject.fromObject(jsonStr);  
        for(Object k : json.keySet()){  
            Object v = json.get(k);   
            //如果内层还是数组的话，继续解析  
            if(v instanceof JSONArray){  
            List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
                Iterator<JSONObject> it = ((JSONArray)v).iterator();  
                while(it.hasNext()){  
                   JSONObject json2 = it.next();  
                    list.add(parseJSON2Map(json2.toString()));  
                }  
                map.put(k.toString(), list);  
            } else {  
                map.put(k.toString(), v);  
           }  
     }  
        return map;  
   }
    
    //json数组
    public static void jsonArray(String jsonArrayData)
    {
    	//JSONArray
		// jsonArrayData="[{\"a1\":\"12\",\"b1\":\"112\",\"c1\":\"132\",\"d1\":\"134\"},{\"a2\":\"12\",\"b2\":\"112\",\"c2\":\"132\",\"d2\":\"134\"},{\"a3\":\"12\",\"b3\":\"112\",\"c3\":\"132\",\"d3\":\"134\"}]";
		JSONArray jsonArray = JSONArray.fromObject(jsonArrayData);

		List<Map<String,Object>> mapListJson = (List)jsonArray;
		for (int i = 0; i < mapListJson.size(); i++) {
			Map<String,Object> obj=mapListJson.get(i);
			
			for(Entry<String,Object> entry : obj.entrySet()){
	            String strkey1 = entry.getKey();
	            Object strval1 = entry.getValue();
	            log.info("KEY:"+strkey1+"  -->  Value:"+strval1+"\n");
	        }
		}
    }
}
