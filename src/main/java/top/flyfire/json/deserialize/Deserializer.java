package top.flyfire.json.deserialize;

import top.flyfire.json.Parser;
import top.flyfire.json.Peeker;
import top.flyfire.json.Structure;
import top.flyfire.json.deserialize.data.*;
import top.flyfire.json.deserialize.value.PrimitiveModel;

/**
 * Created by shyy_work on 2016/6/21.
 */
@SuppressWarnings("all")
public class Deserializer implements Peeker {

    private String source;

    private int cursor,cursorBound;

    private DataFactory dataFactory;

    private Data data;

    private Parser STRUCTEDPARSER;

    private Parser INDEXEDPARSER;

    private Parser PRIMITIVEPARSER;

    public Deserializer(String source, DataFactory dataFactory) {
        super();
        this.source = source;
        this.cursor = 0;
        this.cursorBound = source.length();
        this.dataFactory = dataFactory;
        STRUCTEDPARSER = new ObjectParser();
        INDEXEDPARSER = new ArrayParser();
        PRIMITIVEPARSER = new PrimitiveParser();
    }

    public Deserializer(String source, DataFactory dataFactory,Data data){

    }

    public Object deserialize() {
        peek().parse();
        return data.flush();
    }


    @Override
    public Parser peek() {
        char dest = fetch();
        if(Tokenizer.isArrayStart(dest)){
            if(!roll()) throw new RuntimeException(this.source);
            data = dataFactory.buildAsIndexed(data);
            return INDEXEDPARSER;
        }else if(Tokenizer.isObjectStart(dest)){
            if(!roll()) throw new RuntimeException(this.source);
            data =dataFactory.buildAsStructed(data);
            return STRUCTEDPARSER;
        }else{
            data = dataFactory.buildAsPrimitive(data);
            return PRIMITIVEPARSER;
        }
    }

    private char fetch(){
        return source.charAt(cursor);
    }

    private boolean roll(){
        return ++cursor<cursorBound;
    }

    private void dataFlush(){
        data = data.flush2Parent();
    }

    private class ObjectParser implements Parser ,Structure,Peeker {

        private Parser PRIMITIVEPARSER;

        public ObjectParser() {
            super();
            PRIMITIVEPARSER = new StructedPrimitiveParser();
        }

        @Override
        public void parse() {
            if(validateAndStart()){
                do{

                    key : {
                        int start = cursor;
                        int end = -1;
                        while(roll()&&end==-1){
                            if(Tokenizer.isPrp2Val(fetch())){
                                end = cursor;
                            }
                        }
                        String property = source.substring(start,end);
                        data.setExtra(property);
                    }

                    value : {
                        peek().parse();
                        dataFlush();
                    }

                }while(hasNext());
            }
            validateAndEnd();
        }

        @Override
        public void validateAndEnd() {

        }

        @Override
        public boolean hasNext() {
            char dest = fetch();
            roll();
            if(Tokenizer.isObjectEnd(dest)){
                return false;
            }else if(Tokenizer.isNext(dest)){
                return true;
            }else{
                throw new RuntimeException(source.substring(0,cursor));
            }
        }

        @Override
        public boolean validateAndStart() {
            char dest = fetch();
            if(Tokenizer.isObjectEnd(dest)){
                roll();
                return false;
            }else{
                return true;
            }
        }

        @Override
        public Parser peek() {
            char dest;
            if(Tokenizer.isArrayStart(dest = fetch())){
                if(!roll()) throw new RuntimeException(source);
                data = dataFactory.buildAsIndexed(data);;
                return INDEXEDPARSER;
            }else if(Tokenizer.isObjectStart(dest)){
                if(!roll()) throw new RuntimeException(source);
                data = dataFactory.buildAsStructed(data);
                return STRUCTEDPARSER;
            }else{
                data = dataFactory.buildAsPrimitive(data);
                return PRIMITIVEPARSER;
            }
        }
    }

    private class ArrayParser implements Parser ,Structure , Peeker {

        private Parser PRIMITIVEPARSER;

        public ArrayParser() {
            super();
            PRIMITIVEPARSER = new IndexedPrimitiveParser();
        }

        @Override
        public void parse() {
            if(validateAndStart()){
                int i = 0;
                do{
                    index : {
                        data.setExtra(i++);
                    }
                    value : {
                        peek().parse();
                        dataFlush();
                    }
                }while(hasNext());
            }
            validateAndEnd();
        }


        @Override
        public void validateAndEnd() {

        }

        @Override
        public boolean hasNext() {
            char dest = fetch();
            roll();
            if (Tokenizer.isArrayEnd(dest)){
                return false;
            }else if(Tokenizer.isNext(dest)){
                return true;
            }else{
                throw new RuntimeException(source.substring(0,cursor));
            }
        }

        @Override
        public boolean validateAndStart() {
            char dest;
            if(Tokenizer.isArrayEnd(dest = fetch())){
                roll();
                return false;
            }else{
                return true;
            }
        }

        @Override
        public Parser peek() {
            char dest ;
            if(Tokenizer.isArrayStart(dest = fetch())){
                if(!roll()) throw new RuntimeException(source);
                data = dataFactory.buildAsIndexed(data);;
                return INDEXEDPARSER;
            }else if(Tokenizer.isObjectStart(dest)){
                if(!roll()) throw new RuntimeException(source);
                data = dataFactory.buildAsStructed(data);
                return STRUCTEDPARSER;
            }else{
                data = dataFactory.buildAsPrimitive(data);
                return PRIMITIVEPARSER;
            }
        }
    }

    private class PrimitiveParser implements Parser {
        @Override
        public void parse() {
            data.setExtra(Object.class);
            data.set(new PrimitiveModel(source).val());
        }
    }

    private class StructedPrimitiveParser implements Parser {
        @Override
        public void parse() {
            int start = cursor, end = -1;char dest,skip = ((skip = fetch())=='\"'||skip=='\'')?skip:'\0';
            while(roll()&&end==-1){
                if(((skip=((dest = fetch())==skip?'\0':skip))=='\0')&&Tokenizer.isNext(dest)
                        ||Tokenizer.isObjectEnd(dest)){
                    end = cursor;
                    break;
                }
            }
            data.setExtra(Object.class);
            data.set(new PrimitiveModel(source.substring(start, end)).val());
        }
    }

    private class IndexedPrimitiveParser implements Parser {
        @Override
        public void parse() {
            int start = cursor,end = -1;char dest ,skip = ((skip =fetch())=='\"'||skip=='\'')?skip:'\0';
            while(roll()&&end==-1){
                if(((skip=((dest = fetch())==skip?'\0':skip))=='\0')&&(Tokenizer.isNext(dest)||Tokenizer.isArrayEnd(dest))){
                    end = cursor;
                    break;
                }
            }
            data.setExtra(Object.class);
            data.set(new PrimitiveModel(source.substring(start, end)).val());
        }
    }

}
