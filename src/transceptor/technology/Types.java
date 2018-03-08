package transceptor.technology;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tristan Nottelman
 */
public class Types {

    public static final byte QP_HOOK = (byte) 0x7c;

    public static final byte QP_DOUBLE_N1 = (byte) 0x7d; // #125
    public static final byte QP_DOUBLE_0 = (byte) 0x7e;
    public static final byte QP_DOUBLE_1 = (byte) 0x7f;

    public static final byte QP_RAW8 = (byte) 0xe4; // #228
    public static final byte QP_RAW16 = (byte) 0xe5;
    public static final byte QP_RAW32 = (byte) 0xe6;
    public static final byte QP_RAW64 = (byte) 0xe7;

    public static final byte QP_INT8 = (byte) 0xe8; // #232
    public static final byte QP_INT16 = (byte) 0xe9;
    public static final byte QP_INT32 = (byte) 0xea;
    public static final byte QP_INT64 = (byte) 0xeb;

    public static final byte QP_DOUBLE = (byte) 0xec; // #236 (this is one 8 bytes, reserve for 4 bytes)

    public static final byte START_ARR = (byte) 237;
    public static final byte QP_ARRAY0 = (byte) 0xed;  //# 237
    public static final byte QP_ARRAY1 = (byte) 0xee;
    public static final byte QP_ARRAY2 = (byte) 0xef;
    public static final byte QP_ARRAY3 = (byte) 0xf0;
    public static final byte QP_ARRAY4 = (byte) 0xf1;
    public static final byte QP_ARRAY5 = (byte) 0xf2;

    public static final byte START_MAP = (byte) 243;
    public static final byte QP_MAP0 = (byte) 0xf3; // # 243
    public static final byte QP_MAP1 = (byte) 0xf4;
    public static final byte QP_MAP2 = (byte) 0xf5;
    public static final byte QP_MAP3 = (byte) 0xf6;
    public static final byte QP_MAP4 = (byte) 0xf7;
    public static final byte QP_MAP5 = (byte) 0xf8;

    public static final byte QP_BOOL_TRUE = (byte) 0xf9; // #249
    public static final byte QP_BOOL_FALSE = (byte) 0xfa;
    public static final byte QP_BOOL_NULL = (byte) 0xfb;

    public static final byte QP_OPEN_ARRAY = (byte) 0xfc; // #252
    public static final byte QP_OPEN_MAP = (byte) 0xfd;
    public static final byte QP_CLOSE_ARRAY = (byte) 0xfe;
    public static final byte QP_CLOSE_MAP = (byte) 0xff;

    public static final Map<Integer, Integer> RAW_MAP;
    public static final Map<Integer, Integer> NUMBER_MAP;
    public static final Map<Integer, Boolean> SIMPLE_MAP;

    static {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(QP_RAW8 & 0xff, 1);
        map.put(QP_RAW16 & 0xff, 2);
        map.put(QP_RAW32 & 0xff, 4);
        map.put(QP_RAW64 & 0xff, 8);
        RAW_MAP = Collections.unmodifiableMap(map);

        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(QP_INT8 & 0xff, 1);
        map2.put(QP_INT16 & 0xff, 2);
        map2.put(QP_INT32 & 0xff, 4);
        map2.put(QP_INT64 & 0xff, 8);
        map2.put(QP_DOUBLE & 0xff, 8);
        NUMBER_MAP = Collections.unmodifiableMap(map2);

        Map<Integer, Boolean> map3 = new HashMap<>();
        map3.put(QP_BOOL_TRUE & 0xff, true);
        map3.put(QP_BOOL_FALSE & 0xff, false);
        map3.put(QP_BOOL_NULL & 0xff, null);
        SIMPLE_MAP = Collections.unmodifiableMap(map3);
    }
}
