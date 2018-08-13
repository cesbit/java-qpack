package technology.transceptor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static technology.transceptor.Types.*;

/**
 *
 * @author Tristan Nottelman
 */
public class QPack {

    private final ByteArrayOutputStream bytesContainer;
    private final DataOutputStream container;
    private int position;

    public QPack() {
        this.bytesContainer = new ByteArrayOutputStream();
        this.container = new DataOutputStream(bytesContainer);
        this.position = 0;
    }

    /**
     * Converts a number into array of bytes
     *
     * @param number
     * @param size
     * @return
     */
    private byte[] toBytes(long number, int size) {
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte) (number >> (i * 8));
        }
        return result;
    }

    /**
     * Converts array of bytes to number
     *
     * @param b byte array
     * @param tp double or integer
     * @return
     */
    private Number convertByteToNumber(byte[] b, int tp) {
        ByteBuffer wrapped = ByteBuffer.wrap(b);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        if (tp == (QP_DOUBLE & 0xff)) {
            return wrapped.getDouble();
        } else {
            switch (b.length) {
                case 1:
                    return (int) wrapped.get(0);
                case 2:
                    return (int) wrapped.getShort();
                case 4:
                    return wrapped.getInt();
                case 8:
                    return wrapped.getLong();
            }
            return 0;
        }
    }

    /**
     * Packs the data into bytes
     *
     * @param data
     * @return array of bytes
     * @throws IOException
     */
    private byte[] packRecursive(Object data) throws IOException {
        // empty
        if (data == null) {
            container.writeByte(QP_BOOL_NULL);
            return bytesContainer.toByteArray();
        }

        // boolean
        if (data instanceof Boolean) {
            if ((boolean) data) {
                container.writeByte(QP_BOOL_TRUE);
            } else {
                container.writeByte(QP_BOOL_FALSE);
            }
            return bytesContainer.toByteArray();
        }

        // numbers
        if (data instanceof Number) {

            // double
            if (data instanceof Double) {
                if ((double) data == 0.0) {
                    container.write(QP_DOUBLE_0);
                } else if ((double) data == 1.0) {
                    container.write(QP_DOUBLE_1);
                } else if ((double) data == -1.0) {
                    container.write(QP_DOUBLE_N1);
                } else {
                    ByteBuffer b = ByteBuffer.allocate(8);
                    b.order(ByteOrder.LITTLE_ENDIAN);
                    b.putDouble((double) data);
                    container.write(QP_DOUBLE);
                    container.write(b.array());
                }
                return bytesContainer.toByteArray();
            }

            // int
            long length = 0;

            if (data instanceof Long) {
                length = (long) data;
            } else if (data instanceof Integer) {
                length = (int) data;
            } else if (data instanceof Short) {
                length = (short) data;
            } else if (data instanceof Byte) {
                length = (byte) data;
            }

            if (length < 64 && length >= 0) {
                container.write((int) length);
                return bytesContainer.toByteArray();
            }

            if (length < 0 && length >= -63) {
                container.write(63 - (int) length);
                return bytesContainer.toByteArray();
            }

            if (length <= Byte.MAX_VALUE && length >= Byte.MIN_VALUE) {
                container.writeByte(QP_INT8);
                container.write(toBytes(length, 1));
            } else if (length <= Short.MAX_VALUE && length >= Short.MIN_VALUE) {
                container.writeByte(QP_INT16);
                container.write(toBytes(length, 2));
            } else if (length <= Integer.MAX_VALUE && length >= Integer.MIN_VALUE) {
                container.writeByte(QP_INT32);
                container.write(toBytes(length, 4));
            } else if (length <= Long.MAX_VALUE && length >= Long.MIN_VALUE) {
                container.writeByte(QP_INT64);
                container.write(toBytes(length, 8));
            } else {
                throw new IllegalArgumentException("qpack allows up to 64bit signed integers, got bit length: " + length);
            }
            return bytesContainer.toByteArray();
        }

        // string & byte[]
        if (data instanceof String || data instanceof byte[]) {
            byte[] bytes;
            if (data instanceof String) {
                bytes = ((String) data).getBytes();
            } else {
                bytes = (byte[]) data;
            }
            long length = bytes.length;

            if (length < 100) {
                container.writeByte(128 + (int) length);
            } else if (length < 256) {
                ByteBuffer bb = ByteBuffer.allocate(1);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.put((byte) length);
                container.writeByte(QP_RAW8);
                container.write(bb.array());
            } else if (length < 65536) {
                ByteBuffer bb = ByteBuffer.allocate(2);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short) length);
                container.writeByte(QP_RAW16);
                container.write(bb.array());
            } else if (length < 4294967296L) {
                ByteBuffer bb = ByteBuffer.allocate(4);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putInt((int) length);
                container.writeByte(QP_RAW32);
                container.write(bb.array());
            } else if (length < Long.MAX_VALUE) {
                ByteBuffer bb = ByteBuffer.allocate(8);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putLong((long) length);
                container.writeByte(QP_RAW64);
                container.write(bb.array());
            } else {
                throw new IllegalArgumentException("raw string length too large to fit in qpack: " + length);
            }
            container.write(bytes);
            return bytesContainer.toByteArray();
        }

        // arrays
        if (data.getClass().isArray()) {
            int l = Array.getLength(data);
            if (l < 6) {
                container.writeByte(START_ARR + l);
                for (int i = 0; i < l; i++) {
                    packRecursive(Array.get(data, i));
                }
            } else {
                container.writeByte(QP_OPEN_ARRAY);
                for (int i = 0; i < l; i++) {
                    packRecursive(Array.get(data, i));
                }
                container.writeByte(QP_CLOSE_ARRAY);
            }
            return bytesContainer.toByteArray();
        }

        // collection
        if (data instanceof Collection<?>) {
            int l = ((Collection<?>) data).size();
            if (l < 6) {
                container.writeByte(START_ARR + l);
                for (Object o : ((Collection<?>) data)) {
                    packRecursive(o);
                }
            } else {
                container.writeByte(QP_OPEN_ARRAY);
                for (Object o : ((Collection<?>) data)) {
                    packRecursive(o);
                }
                container.writeByte(QP_CLOSE_ARRAY);
            }
            return bytesContainer.toByteArray();
        }

        // map
        if (data instanceof Map<?, ?>) {
            int l = ((Map<?, ?>) data).size();
            if (l < 6) {
                container.writeByte(START_MAP + l);
                for (Map.Entry<?, ?> o : ((Map<?, ?>) data).entrySet()) {
                    packRecursive(o.getKey());
                    packRecursive(o.getValue());
                }
            } else {
                container.writeByte(QP_OPEN_MAP);
                for (Map.Entry<?, ?> o : ((Map<?, ?>) data).entrySet()) {
                    packRecursive(o.getKey());
                    packRecursive(o.getValue());
                }
                container.writeByte(QP_CLOSE_MAP);
            }
            return bytesContainer.toByteArray();
        }

        throw new IllegalArgumentException("packing type " + data.getClass().getName() + " is not supported with qpack");
    }

    /**
     *
     * @param data
     * @return
     */
    private Object unpackRecursive(byte[] data, int pos, int end, String decoder) {
        int tp = (data[position] & 0xff);
        position = ++pos;
        // fixed integer
        if (tp < 64) {
            return tp;
        }
        // fixed negative integer
        if (tp < 124) {
            return 63 - tp;
        }
        // reserved for an object hook
        if (tp == QP_HOOK) {
            return 0;
        }
        // fixed doubles
        if (tp < 0x80) {
            return ((double) tp - 126);
        }
        // fixed string length
        if (tp < 0xe4) {
            int endPos = position + (tp - 128);
            int p = position;
            position = endPos;
            if (decoder == null) {
                return Arrays.copyOfRange(data, p, endPos);
            } else {
                try {
                    return new String(Arrays.copyOfRange(data, p, endPos), decoder);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(QPack.class.getName()).log(Level.SEVERE, "The Character Encoding " + decoder + " is not supported", ex);
                }
            }
        }
        // string
        if (tp < 0xe8) {
            int qpType = RAW_MAP.get(tp);
            int endPos = position + qpType + data.length;
            int p = position + qpType;
            position = endPos;
            if (decoder == null) {
                return Arrays.copyOfRange(data, p, data.length);
            } else {
                try {
                    return new String(Arrays.copyOfRange(data, p, data.length), decoder);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(QPack.class.getName()).log(Level.SEVERE, "The Character Encoding " + decoder + " is not supported", ex);
                }
            }
        }
        // integer (double included)
        if (tp < 0xed) {
            int qpType = NUMBER_MAP.get(tp);
            int p = position;
            position += qpType;
            return convertByteToNumber(Arrays.copyOfRange(data, p, p + qpType), tp);
        }
        // fixed array
        if (tp < 0xf3) {
            Object[] array = new Object[tp - 0xed];
            for (int i = 0; i < (tp - 0xed); i++) {
                Object value = unpackRecursive(data, position, end, decoder);
                array[i] = value;
            }
            return array;
        }
        // fixed map
        if (tp < 0xf9) {
            Map map = new HashMap<>();
            for (int i = 0; i < (tp - 0xf3); i++) {
                Object key = unpackRecursive(data, position, end, decoder);
                Object value = unpackRecursive(data, position, end, decoder);
                map.put(key, value);
            }
            return map;
        }
        // boolean
        if (tp < 0xfc) {
            return SIMPLE_MAP.get(tp);
        }
        // open array
        if (tp == (QP_OPEN_ARRAY & 0xff)) {
            List list = new ArrayList();
            while (position < end && data[position] != QP_CLOSE_ARRAY) {
                Object value = unpackRecursive(data, position, end, decoder);
                list.add(value);
            }
            position++;
            return list.toArray();
        }
        // open map
        if (tp == (QP_OPEN_MAP & 0xff)) {
            Map map = new HashMap<>();
            while (position < end && data[position] != QP_CLOSE_MAP) {
                Object key = unpackRecursive(data, position, end, decoder);
                Object value = unpackRecursive(data, position, end, decoder);
                map.put(key, value);
            }
            position++;
            return map;
        }

        throw new IllegalArgumentException("Error in qpack at position " + position);
    }

    /**
     * Packs data into array of bytes, this method makes use of _pack for
     * recursion
     *
     * @param data
     * @return
     */
    public byte[] pack(Object data) {
        try {
            container.close();
        } catch (IOException ex) {
            Logger.getLogger(QPack.class.getName()).log(Level.SEVERE, null, ex);
        }
        bytesContainer.reset();
        byte[] output = null;
        try {
            output = packRecursive(data);
        } catch (IOException ex) {
            Logger.getLogger(QPack.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     *
     * @param data
     * @return
     */
    public Object unpack(byte[] data) {
        position = 0;
        return unpackRecursive(data, 0, data.length, null);
    }

    /**
     *
     * @param data
     * @param decoder
     * @return
     */
    public Object unpack(byte[] data, String decoder) {
        position = 0;
        return unpackRecursive(data, 0, data.length, decoder);
    }

}
