package top.flyfire.json.serialize.component.defaults;

import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.common.reflect.metainfo.ClassMetaInfo;
import top.flyfire.common.reflect.metainfo.FieldMetaInfo;

import java.util.Enumeration;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonObject extends JsonObjectStruct<Object> {

    Enumeration<FieldMetaInfo> fieldMetaInfoEnumeration;

    public JsonObject(Object value, JsonValue parent) {
        super(value, parent);
        ClassMetaInfo metaInfo = (ClassMetaInfo) ReflectUtils.getMetaInfo(cached.getClass());
        fieldMetaInfoEnumeration = metaInfo.fieldEnum();
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        return fieldMetaInfoEnumeration.hasMoreElements();
    }

    @Override
    public Entry peeking() {
        FieldMetaInfo fieldMetaInfo = fieldMetaInfoEnumeration.nextElement();
        return new EntryImpl(fieldMetaInfo.getFieldName(),fieldMetaInfo.invokeGetter(cached));
    }

    @Override
    public boolean hasNext() {
        return fieldMetaInfoEnumeration.hasMoreElements();
    }
}
