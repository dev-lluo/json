package top.flyfire.json.deserialize.component.defaults.highparse;


import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.metainfo.FieldMetaInfo;
import top.flyfire.common.reflect.wrapper.BuildInWrapper;

/**
 * Created by devll on 2016/12/5.
 */
public class BuildInValueData extends HighStructValueData<String,BuildInWrapper,Object>  {

    protected FieldMetaInfo fieldMetaInfo;

    public BuildInValueData(BuildInWrapper wrapper, Object value, HighStructValueData owner) {
        super(wrapper, value, owner);
    }

    @Override
    public MetaInfo indexing(String index) {
        fieldMetaInfo = wrapper.getField(index);
        return fieldMetaInfo.getType();
    }

    @Override
    public HighStructValueData push(Object value) {
        this.fieldMetaInfo.setValueTo(this.value, value);
        return this;
    }
}
