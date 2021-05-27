/**
 * Copyright(c) 2010-2013 by XiangShang Inc.
 * All Rights Reserved
 */

package dailyproject.moon.common.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

/**
 * json util
 *
 * @author jinkai
 */
public class MoonJsonUtil {
	
	
	public Object obj = null;
	
	/**
	 * bean to json
	 */
	public static String beanToJson(Object obj, boolean serializeNullValue) {
		
		if (obj == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(obj);
	}
	
	public static String beanToJson(Object obj) {
		if (obj == null) {
			return null;
		}
		return gson(true).toJson(obj);
	}
	
	/**
	 * beantoMap
	 *
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object obj) {
		
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					
					map.put(key, value);
				}
				
			}
		}
		catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}
		
		return map;
		
	}
	
	/**
	 * bean to json 适用于毫秒
	 */
	public static String beanWithDateToJson(Object obj, boolean serializeNullValue) {
		
		if (obj == null) {
			return null;
		}
		
		return gsonYYYYMMDDHHMMSS(serializeNullValue).toJson(obj);
	}
	
	/**
	 * json to bean
	 */
	public static <T> T jsonToBean(String json, Class<T> clazz) {
		
		if (json == null || "".equals(json.trim())) {
			return null;
		}
		StringReader strReader = new StringReader(json);
		JsonReader jsonReader = new JsonReader(strReader);
		return (T) jsonToBean(jsonReader, clazz);
	}
	
	private static <T> T jsonToBean(JsonReader json, Class<T> clazz) {
		
		if (json == null) {
			return null;
		}
		T bean = gson(false).fromJson(json, clazz);
		return (T) bean;
	}
	
	/**
	 * json to bean 适用于毫秒
	 */
	public static <T> T jsonWithDateToBean(String json, Class<T> clazz) {
		
		if (json == null || "".equals(json.trim())) {
			return null;
		}
		StringReader strReader = new StringReader(json);
		JsonReader jsonReader = new JsonReader(strReader);
		return jsonWithDateToBean(jsonReader, clazz);
	}
	
	private static <T> T jsonWithDateToBean(JsonReader json, Class<T> clazz) {
		
		if (json == null) {
			return null;
		}
		
		T b = gsonYYYYMMDDHHMMSS(false).fromJson(json, clazz);
		return b;
	}
	
	/**
	 * list to json
	 */
	public static <T> String listToJson(List<T> list, boolean serializeNullValue) {
		
		if (list == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(list);
	}


	/**
	 * list to json
	 */
	public static <T> String BloetolistToJson(List<T> list, boolean serializeNullValue) {

		if (list == null) {
			return null;
		}
		return gsonBloeto(serializeNullValue).toJson(list);
	}



	
	/**
	 * list to json
	 *
	 * @seee with date type
	 */
	@Deprecated
	public static <T> String listWithDateToJson(List<T> list, boolean serializeNullValue) {
		
		if (list == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(list);
	}
	
	/**
	 * json to list
	 */
	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		
		if (json == null || "".equals(json.trim())) {
			return null;
		}
		// json -> List
		StringReader strReader = new StringReader(json);
		List<T> list = null;
		try {
			list = readForList(strReader, false, clazz);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * json to list
	 */
	@Deprecated
	public static <T> List<T> jsonWithDateToList(String json, Class<T> clazz) {
		
		if (json == null || "".equals(json.trim())) {
			return null;
		}
		
		// json -> List
		StringReader strReader = new StringReader(json);
		List<T> list = null;
		try {
			list = readForList(strReader, true, clazz);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static <T> List<T> readForList(Reader reader, boolean hasDate, Class<T> clazz) throws IOException {
		
		JsonReader jsonReader = new JsonReader(reader);
		List<T> objs = new ArrayList<T>();
		jsonReader.beginArray();
		while (jsonReader.hasNext()) {
			T obj = null;
			if (hasDate) {
				obj = jsonWithDateToBean(jsonReader, clazz);
			}
			else {
				obj = jsonToBean(jsonReader, clazz);
			}
			if (obj != null)
				objs.add(obj);
		}
		jsonReader.endArray();
		jsonReader.close();
		return objs;
	}
	
	/**
	 * set to json
	 */
	public static <T> String setToJson(Set<T> set, boolean serializeNullValue) {
		
		if (set == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(set);
	}
	
	/**
	 * set to json
	 *
	 * @seee with date type
	 */
	@Deprecated
	public static <T> String setWithDateToJson(Set<T> set, boolean serializeNullValue) {
		
		if (set == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(set);
	}
	
	/**
	 * json to set
	 */
	public static <T> Set<T> jsonToSet(String json, Class<T> clazz) {
		
		if (json == null || "".equals(json.trim())) {
			return null;
		}
		// json -> set
		StringReader strReader = new StringReader(json);
		Set<T> set = null;
		try {
			set = readForSet(strReader, false, clazz);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	/**
	 * json to set
	 */
	@Deprecated
	public static <T> Set<T> jsonWithDateToSet(String json, Class<T> clazz) {
		
		if (json == null || "".equals(json.trim())) {
			return null;
		}
		
		// json -> set
		StringReader strReader = new StringReader(json);
		Set<T> set = null;
		try {
			set = readForSet(strReader, true, clazz);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	private static <T> Set<T> readForSet(Reader reader, boolean hasDate, Class<T> clazz) throws IOException {
		
		JsonReader jsonReader = new JsonReader(reader);
		Set<T> objs = new HashSet<T>();
		jsonReader.beginArray();
		while (jsonReader.hasNext()) {
			T obj = null;
			if (hasDate) {
				obj = jsonWithDateToBean(jsonReader, clazz);
			}
			else {
				obj = jsonToBean(jsonReader, clazz);
			}
			if (obj != null)
				objs.add(obj);
		}
		jsonReader.endArray();
		jsonReader.close();
		return objs;
	}
	
	/**
	 * map to json
	 */
	public static <T> String mapToJson(Map<String, T> map, boolean serializeNullValue) {
		
		if (map == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(map);
	}
	
	/**
	 * map to json
	 */
	@Deprecated
	public static <T> String mapWithDateToJson(Map<String, T> map, boolean serializeNullValue) {
		
		if (map == null) {
			return null;
		}
		return gson(serializeNullValue).toJson(map);
	}
	
	public static Gson gson(boolean serializeNullValue) {
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateSerializerUtils()).registerTypeAdapter(Instant.class, new InstantSerializerUtils())
				.registerTypeAdapter(Instant.class, new InstantDeSerializerUtils()).setDateFormat(DateFormat.LONG).registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

					public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
						return new Date(json.getAsJsonPrimitive().getAsLong());
					}
				});

		if (serializeNullValue) {
			return gsonBuilder.serializeNulls().create();
		}
		else {
			return gsonBuilder.create();
		}

	}

	public static Gson gsonBloeto(boolean serializeNullValue) {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Instant.class, new InstantDeSerializerUtils()).setDateFormat("MM/dd HH:mm");

		if (serializeNullValue) {
			return gsonBuilder.serializeNulls().create();
		}
		else {
			return gsonBuilder.create();
		}

	}

	public static Gson gsonYYYYMMDDHHMMSS(boolean serializeNullValue) {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateSerializerUtils()).registerTypeAdapter(Instant.class, new InstantSerializerUtils())
				.registerTypeAdapter(Instant.class, new InstantDeSerializerUtils()).setDateFormat(DateFormat.LONG).registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getAsString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}).registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) ->
				new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)));

		if (serializeNullValue) {
			return gsonBuilder.serializeNulls().create();
		} else {
			return gsonBuilder.create();
		}

	}

	/**
	 * 日期解序列实用工具类
	 */
	static class DateSerializerUtils implements JsonSerializer<Date> {


		@Override
		public JsonElement serialize(Date date, Type type, JsonSerializationContext content) {

			return new JsonPrimitive(date.getTime());
		}

	}

	static class InstantSerializerUtils implements JsonSerializer<Instant> {


		@Override
		public JsonElement serialize(Instant value, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(LocalDateTime.ofInstant(value, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}
	}

	static class InstantDeSerializerUtils implements JsonDeserializer<Instant> {


		@Override
		public Instant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			return LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(jsonElement.getAsString())).atOffset(ZoneOffset.ofHours(8)).toInstant();
			//			return Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(jsonElement.getAsString()));
		}
	}

	/**
	 * 日期序列化实用工具类
	 */
	static class DateDeserializerUtils implements JsonDeserializer<Date> {


		@Override
		public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

			return new Date(json.getAsJsonPrimitive().getAsLong());
		}
	}
	
	/**
	 * 根据json字符串返回Map对象
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String json, Class<?> clas, String... values) {
		
		return MoonJsonUtil.toMap(MoonJsonUtil.toJsonObject(json, null), clas, values);
	}
	
	/**
	 * 将JSONObjec对象转换成Map-List集合
	 *
	 * @param json
	 * @return
	 */
	private static Map<String, Object> toMap(JsonObject json, Class<?> clas, String... values) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Set<Entry<String, JsonElement>> entrySet = json.entrySet();
		for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext();) {
			Entry<String, JsonElement> entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof JsonArray)
				map.put((String) key, toList((JsonArray) entry.getValue().getAsJsonArray(), clas));
			else if (value instanceof JsonObject)
				map.put((String) key, jsonToBean(entry.getValue().getAsString(), clas));
			else {
				if (value.equals("null") || value == null || value == "") {
					map.put((String) key, null);
				}
				else {
					map.put((String) key, entry.getValue().getAsString());
				}
			}
		}
		return map;
	}
	
	/**
	 * 将JSONArray对象转换成List集合
	 *
	 * @param json
	 * @return
	 */
	public static List<?> toList(JsonArray json, Class<?> clas) {
		
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < json.size(); i++) {
			Object value = json.get(i);
			if (value instanceof JsonObject) {
				list.add(jsonToBean(value.toString(), clas));
			}
			else {
				list.add(value);
			}
		}
		return list;
	}
	
	public static Gson createGson() {
		
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		Gson gson = builder.create();
		return gson;
	}
	
	/**
	 * 获取JsonObject
	 *
	 * @param json
	 * @return
	 */
	public static JsonObject toJsonObject(String json, String jsonObject) {
		
		JsonParser parser = new JsonParser();
		if (StringUtils.hasText(jsonObject)) {
			if (parser.parse(json).getAsJsonObject().get(jsonObject) instanceof JsonNull) {
				return null;
			}
			else {
				JsonObject jsonObj = (JsonObject) parser.parse(json).getAsJsonObject().get(jsonObject);
				return jsonObj;
			}
		}
		else {
			JsonObject jsonObj = parser.parse(json).getAsJsonObject();
			return jsonObj;
		}
	}
	
	/**
	 * 获取json中参数的值
	 * @param json
	 * @param param
	 * @return
	 */
	public Object analysisJson(Object json, String param) {
		//如果obj为json数组  
		if (json instanceof JsonArray) {
			JsonArray objArray = (JsonArray) json;
			for (int i = 0; i < objArray.size(); i++) {
				analysisJson(objArray.get(i), param);
			}
		}
		//如果为json对象  
		else if (json instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject) json;
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				Object object = jsonObject.get(entry.getKey());
				if (param.equals(entry.getKey())) {
					//如果得到的是数组  
					if (jsonObject.get(entry.getKey()) instanceof JsonArray) {
						obj = jsonObject.get(entry.getKey()).toString();
					}
					//如果key中是一个json对象  
					else if (jsonObject.get(entry.getKey()) instanceof JsonObject) {
						obj = jsonObject.get(entry.getKey()).toString();
					}
					else {
						obj = jsonObject.get(entry.getKey()).getAsString();
					}
					break;
				}
				//如果得到的是数组  
				if (object instanceof JsonArray) {
					JsonArray objArray = (JsonArray) object;
					analysisJson(objArray, param);
				}
				//如果key中是一个json对象  
				else if (object instanceof JsonObject) {
					analysisJson((JsonObject) object, param);
				}
			}
		}
		return obj;
	}
	
	/**
	 * 获取JsonArray
	 * 
	 * @param json
	 * @return
	 */
	public static JsonArray toJsonArray(String json) {
		
		JsonParser parser = new JsonParser();
		JsonArray jsonArr = parser.parse(json).getAsJsonArray();
		return jsonArr;
	}
	
	/**
	 * beantoMap
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> beantoMap(Object obj) {
		
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					
					map.put(key, value);
				}
				
			}
		}
		catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}
		
		return map;
		
	}
	
	public static void main(String[] args) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Type mtype = new TypeToken<List<Map<String, String>>>() {}.getType();
		System.out.println(MoonJsonUtil.listToJson(list, true));
		List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
		lis = new Gson().fromJson(MoonJsonUtil.listToJson(list, true), mtype);
		System.out.println(lis.size());
		for (Map<String, String> ma : lis) {
			System.out.println(MoonJsonUtil.mapToJson(ma, true));
		}
	}
}
