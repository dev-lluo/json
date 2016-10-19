package top.flyfire.json.deserialize;

import top.flyfire.json.Parser;
import top.flyfire.json.Peeker;
import top.flyfire.json.Structure;
import top.flyfire.json.component.JsonComponent;

/**
 * Created by shyy_work on 2016/6/21.
 */
@SuppressWarnings("all")
public class Deserializer implements Peeker {

    private String source;

    private JsonComponent component;

    private int cursor,cursorBound,level;

    private Parser STRUCTEDPARSER;

    private Parser INDEXEDPARSER;

    private Parser PRIMITIVEPARSER;

    public Deserializer(String source, JsonComponent component) {
        super();
        this.source = source;
        this.cursor = this.level = 0;
        this.cursorBound = source.length();
        this.component = component;
        STRUCTEDPARSER = new ObjectParser();
        INDEXEDPARSER = new ArrayParser();
        PRIMITIVEPARSER = new PrimitiveParser();
    }

    public void deserialize() {
        peek().parse();
    }


    @Override
    public Parser peek() {
        char dest = fetch();
        if(Tokenizer.isArrayStart(dest)){
            if(!roll()) throw new RuntimeException(this.source);
            component.openStruct(0,level++);
            return INDEXEDPARSER;
        }else if(Tokenizer.isObjectStart(dest)){
            if(!roll()) throw new RuntimeException(this.source);
            component.openStruct(1,level++);
            return STRUCTEDPARSER;
        }else{
            return PRIMITIVEPARSER;
        }
    }

    private char fetch(){
        return source.charAt(cursor);
    }

    private boolean roll(){
        return ++cursor<cursorBound;
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
                        component.indexing(property,level);
                    }

                    value : {
                        peek().parse();
                    }

                }while(hasNext());
            }
            validateAndEnd();
        }

        @Override
        public void validateAndEnd() {
            component.closeStruct(1,--level);
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
                component.openStruct(0,level++);
                return INDEXEDPARSER;
            }else if(Tokenizer.isObjectStart(dest)){
                if(!roll()) throw new RuntimeException(source);
                component.openStruct(1,level++);
                return STRUCTEDPARSER;
            }else{
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
                        component.indexing(i++,level);
                    }
                    value : {
                        peek().parse();
                    }
                }while(hasNext());
            }
            validateAndEnd();
        }


        @Override
        public void validateAndEnd() {
            component.closeStruct(0,--level);
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
                component.openStruct(0,level++);
                return INDEXEDPARSER;
            }else if(Tokenizer.isObjectStart(dest)){
                if(!roll()) throw new RuntimeException(source);
                component.openStruct(1,level++);
                return STRUCTEDPARSER;
            }else{
                return PRIMITIVEPARSER;
            }
        }
    }

    private class PrimitiveParser implements Parser {
        @Override
        public void parse() {
            component.value(source,level);
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
            component.value(source.substring(start, end),level);
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
            component.value(source.substring(start, end),level);
        }
    }

}
