package top.flyfire.json;

import top.flyfire.json.component.JsonComponent;
import top.flyfire.json.component.defaults.FormatCpt;
import top.flyfire.json.deserialize.Deserializer;

/**
 * Created by devll on 2016/10/19.
 */
public class Json {

    public static String format(String json){
       return (String)new Deserializer(json,new FormatCpt()).deserialize();
    }

    public static <T> T exec(String json, JsonComponent<T> component){
        return (T)new Deserializer(json,component).deserialize();
    }

}
