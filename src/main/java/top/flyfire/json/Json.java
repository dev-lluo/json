package top.flyfire.json;

import top.flyfire.json.component.JsonComponent;
import top.flyfire.json.component.defaults.format.FormatCpt;
import top.flyfire.json.deserialize.Deserializer;

/**
 * Created by devll on 2016/10/19.
 */
public class Json {

    public static String format(String json){
        FormatCpt formatCpt = new FormatCpt();
        new Deserializer(json,formatCpt).deserialize();
        return formatCpt.result();
    }

    public static void exec(String json, JsonComponent component){
        new Deserializer(json,component).deserialize();
    }

}
