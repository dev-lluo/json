package top.flyfire.json.component.defaults.highparse;

import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.wrapper.Wrapper;
import top.flyfire.common.reflect.wrapper.WrapperFactory;
import top.flyfire.json.component.JsonComponent;


/**
 * Created by devll on 2016/10/22.
 */
public class HighParseJavaObjectCpt implements JsonComponent {

    private MetaInfo metaInfo;

    private Wrapper wrapper;

    private HighValueData data;

    private int skipLevel;

    public HighParseJavaObjectCpt(MetaInfo metaInfo) {
        this.skipLevel = -1;
        this.metaInfo = metaInfo;
    }

    private boolean openCheck(int level){
        if(metaInfo==MetaInfo.NULL&&skipLevel == -1){
            skipLevel = level;
        }
        return skipLevel == -1;
    }

    private boolean indexingCheck(){
        return skipLevel == -1;
    }

    public void closeCheck(int level){
        if(level == skipLevel){
            skipLevel = -1;
        }
    }

    @Override
    public void openArray(int level) {
        if (openCheck(level)) {
            data = new HighStructValueData(wrapper = WrapperFactory.wrap(metaInfo), wrapper.instance(), (HighStructValueData) data);
        }
    }

    @Override
    public void closeArray(int level) {
        closeCheck(level);
        dataRender();
    }

    @Override
    public void openObject(int level) {
        if (openCheck(level)) {
            data = new HighStructValueData(wrapper = WrapperFactory.wrap(metaInfo), wrapper.instance(), (HighStructValueData) data);
        }
    }

    @Override
    public void closeObject(int level) {
        closeCheck(level);
        dataRender();
    }

    @Override
    public void indexing(Object index, int level) {
        if(indexingCheck()) {
            ((HighStructValueData) data).indexing(index);
            metaInfo = wrapper.getMetaInfo(index);
        }
    }

    @Override
    public void value(String value, int level) {
        if (openCheck(level)) {
            data = new HighValueData(wrapper = WrapperFactory.wrap(metaInfo), value, (HighStructValueData) data);
        }
        closeCheck(level);
        dataRender();
    }

    @Override
    public void toNext(int level) {

    }

    private void dataRender(){
        HighStructValueData temp = data.render();
        if(temp!=null){
            data = temp;
            wrapper = data.wrapper;
        }
    }

    public Object result(){
        return this.data.getValue();
    }
}
