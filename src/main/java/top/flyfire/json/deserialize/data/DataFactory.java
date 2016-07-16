package top.flyfire.json.deserialize.data;

import top.flyfire.common.ValueProxy;
import top.flyfire.common.reflect.MetaInfo;

/**
 * Created by shyy_work on 2016/6/21.
 */
public abstract class DataFactory {

    public DataFactory(){}

    private static ValueProxy<DataFactory> DEF = new ValueProxy<>();

    public final static DataFactory getDefault(){
        if(DEF.isNull()){
            synchronized (DEF){
                if(DEF.isNull()){
                    DEF.set(new DataFactory() {
                        @Override
                        public Data buildAsIndexed(Data data) {
                            return new IndexedData(data);
                        }

                        @Override
                        public Data buildAsStructed(Data data) {
                            return new StructedData(data);
                        }

                        @Override
                        public Data buildAsPrimitive(Data data) {
                            return new PrimitiveData(data);
                        }
                    });
                }
            }
        }
        return DEF.get();
    }

    public final static DataFactory getMetaInfoFactory(){
        return new DataFactory() {

            @Override
            public Data buildAsIndexed(Data data) {
                return data == null ? null:null;
            }

            @Override
            public Data buildAsStructed(Data data) {
                return null;
            }

            @Override
            public Data buildAsPrimitive(Data data) {
                return null;
            }
        };
    }

    public abstract Data buildAsIndexed(Data data);

    public abstract Data buildAsStructed(Data data);

    public abstract Data buildAsPrimitive(Data data);
}
