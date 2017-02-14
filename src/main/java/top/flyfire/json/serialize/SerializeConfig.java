package top.flyfire.json.serialize;

/**
 * Created by devll on 2017/2/14.
 */
public class SerializeConfig {

    public static final SerializeConfig DEFAULT = new SerializeConfig();

    private boolean ignoreCircularReference;

    private int readDeep;


    public SerializeConfig() {
        this(true,3);
    }

    public SerializeConfig(boolean ignoreCircularReference) {
        this(ignoreCircularReference,0);
    }

    public SerializeConfig(int readDeep) {
        this(true,readDeep);
    }

    public SerializeConfig(boolean ignoreCircularReference, int readDeep) {
        this.ignoreCircularReference = ignoreCircularReference;
        this.readDeep = readDeep;
    }

    public boolean isIgnoreCircularReference() {
        return ignoreCircularReference;
    }

    public int getReadDeep() {
        return readDeep;
    }
}
