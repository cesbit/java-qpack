package transceptor.technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.junit.Test;
import static org.junit.Assert.*;
import static transceptor.technology.Types.*;

/**
 *
 * @author tristan
 */
public class QPackTest {

    private final Map<Object, Object> map = new HashMap<>();

    public QPackTest() {
//        map.put(true, new byte[]{QP_BOOL_TRUE});
//        map.put(false, new byte[]{QP_BOOL_FALSE});
//        map.put(null, new byte[]{QP_BOOL_NULL});
//        map.put(" Hi Qpack", new byte[]{(byte) 140, (byte) 239, (byte) 163, (byte) 159, 32, 72, 105, 32, 81, 112, 97, 99, 107});
//        map.put("Nicolaas Copernicus (19 februari 1473 - Frauenburg (Ermland, Koninklijk Pruisen), 24 mei 1543) was een Poolse, etnisch Duitse kanunnik en een belangrijk wiskundige, arts, jurist en sterrenkundige uit het Koninkrijk Polen.", new byte[]{(byte) 228, (byte) 221, 78, 105, 99, 111, 108, 97, 97, 115, 32, 67, 111, 112, 101, 114, 110, 105, 99, 117, 115, 32, 40, 49, 57, 32, 102, 101, 98, 114, 117, 97, 114, 105, 32, 49, 52, 55, 51, 32, 45, 32, 70, 114, 97, 117, 101, 110, 98, 117, 114, 103, 32, 40, 69, 114, 109, 108, 97, 110, 100, 44, 32, 75, 111, 110, 105, 110, 107, 108, 105, 106, 107, 32, 80, 114, 117, 105, 115, 101, 110, 41, 44, 32, 50, 52, 32, 109, 101, 105, 32, 49, 53, 52, 51, 41, 32, 119, 97, 115, 32, 101, 101, 110, 32, 80, 111, 111, 108, 115, 101, 44, 32, 101, 116, 110, 105, 115, 99, 104, 32, 68, 117, 105, 116, 115, 101, 32, 107, 97, 110, 117, 110, 110, 105, 107, 32, 101, 110, 32, 101, 101, 110, 32, 98, 101, 108, 97, 110, 103, 114, 105, 106, 107, 32, 119, 105, 115, 107, 117, 110, 100, 105, 103, 101, 44, 32, 97, 114, 116, 115, 44, 32, 106, 117, 114, 105, 115, 116, 32, 101, 110, 32, 115, 116, 101, 114, 114, 101, 110, 107, 117, 110, 100, 105, 103, 101, 32, 117, 105, 116, 32, 104, 101, 116, 32, 75, 111, 110, 105, 110, 107, 114, 105, 106, 107, 32, 80, 111, 108, 101, 110, 46});
//        map.put("Nicolaas Copernicus (19 februari 1473 - Frauenburg (Ermland, Koninklijk Pruisen), 24 mei 1543) was een Poolse, etnisch Duitse kanunnik en een belangrijk wiskundige, arts, jurist en sterrenkundige uit het Koninkrijk Polen. Dit is allemaal test data om een string te krijgen die langer is dan de lengte van de vorige.", new byte[]{(byte) 229, 59, 1, 78, 105, 99, 111, 108, 97, 97, 115, 32, 67, 111, 112, 101, 114, 110, 105, 99, 117, 115, 32, 40, 49, 57, 32, 102, 101, 98, 114, 117, 97, 114, 105, 32, 49, 52, 55, 51, 32, 45, 32, 70, 114, 97, 117, 101, 110, 98, 117, 114, 103, 32, 40, 69, 114, 109, 108, 97, 110, 100, 44, 32, 75, 111, 110, 105, 110, 107, 108, 105, 106, 107, 32, 80, 114, 117, 105, 115, 101, 110, 41, 44, 32, 50, 52, 32, 109, 101, 105, 32, 49, 53, 52, 51, 41, 32, 119, 97, 115, 32, 101, 101, 110, 32, 80, 111, 111, 108, 115, 101, 44, 32, 101, 116, 110, 105, 115, 99, 104, 32, 68, 117, 105, 116, 115, 101, 32, 107, 97, 110, 117, 110, 110, 105, 107, 32, 101, 110, 32, 101, 101, 110, 32, 98, 101, 108, 97, 110, 103, 114, 105, 106, 107, 32, 119, 105, 115, 107, 117, 110, 100, 105, 103, 101, 44, 32, 97, 114, 116, 115, 44, 32, 106, 117, 114, 105, 115, 116, 32, 101, 110, 32, 115, 116, 101, 114, 114, 101, 110, 107, 117, 110, 100, 105, 103, 101, 32, 117, 105, 116, 32, 104, 101, 116, 32, 75, 111, 110, 105, 110, 107, 114, 105, 106, 107, 32, 80, 111, 108, 101, 110, 46, 32, 68, 105, 116, 32, 105, 115, 32, 97, 108, 108, 101, 109, 97, 97, 108, 32, 116, 101, 115, 116, 32, 100, 97, 116, 97, 32, 111, 109, 32, 101, 101, 110, 32, 115, 116, 114, 105, 110, 103, 32, 116, 101, 32, 107, 114, 105, 106, 103, 101, 110, 32, 100, 105, 101, 32, 108, 97, 110, 103, 101, 114, 32, 105, 115, 32, 100, 97, 110, 32, 100, 101, 32, 108, 101, 110, 103, 116, 101, 32, 118, 97, 110, 32, 100, 101, 32, 118, 111, 114, 105, 103, 101, 46});
        map.put(4, new byte[]{4});
        map.put(-1, new byte[]{64});
        map.put(63, new byte[]{63});
        map.put(-48, new byte[]{111});
        map.put(-60, new byte[]{123});
//        map.put(64, new byte[]{(byte) 232, 64});
//        map.put(-114, new byte[]{(byte) 232, (byte) 142});
//        map.put(1293, new byte[]{(byte) 233, 13, 5});
//        map.put(-5379, new byte[]{(byte) 233, (byte) 253, (byte) 234});
//        map.put(2147483647, new byte[]{(byte) 234, (byte) 255, (byte) 255, (byte) 255, 127});
//        map.put(2147483648L, new byte[]{(byte) 235, 0, 0, 0, (byte) 128, 0, 0, 0, 0});
//        map.put(2147483648541L, new byte[]{(byte) 235, 29, 2, 0, 0, (byte) 244, 1, 0, 0});
//        map.put(-2147483648541L, new byte[]{(byte) 235, (byte) 227, (byte) 253, (byte) 255, (byte) 255, 11, (byte) 254, (byte) 255, (byte) 255});
//        map.put(9223372036854775807L, new byte[]{(byte) 235, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, 127});
//        map.put(-9223372036854775807L, new byte[]{(byte) 235, 1, 0, 0, 0, 0, 0, 0, (byte) 128});
//        map.put(1.0, new byte[]{QP_DOUBLE_1});
//        map.put(0.0, new byte[]{QP_DOUBLE_0});
//        map.put(-1.0, new byte[]{QP_DOUBLE_N1});
//        map.put(1.234, new byte[]{(byte) 236, 88, 57, (byte) 180, (byte) 200, 118, (byte) 190, (byte) 243, 63});
//        map.put(56516123123132.122345, new byte[]{(byte) 236, 16, (byte) 222, (byte) 174, (byte) 184, 87, (byte) 179, (byte) 201, 66});
//        map.put(-347685.89452, new byte[]{(byte) 236, 6, 13, (byte) 253, (byte) 147, (byte) 151, 56, 21, (byte) 193});
//        map.put(-120, new byte[]{(byte) 232, (byte) 136});
//        map.put(new byte[]{0x13, 0x00, 0x00, 0x00, 0x08, 0x00}, new byte[]{(byte) 134, 19, 0, 0, 0, 8, 0});
//        map.put(new int[]{2, 8}, new byte[]{(byte) 239, 2, 8});
//        map.put(new int[]{10, 20, 30, 40, 50, 60}, new byte[]{(byte) 252, 10, 20, 30, 40, 50, 60, (byte) 254});
//        map.put(new Object[]{new int[]{5, 7}, 3}, new byte[]{(byte) 239, (byte) 239, 5, 7, 3});
//
//        ArrayList<Integer> list = new ArrayList<Integer>() {{
//                add(4);
//                add(5);
//                add(6);
//        }};
//        ArrayList<Integer> list2 = new ArrayList<Integer>() {{
//                add(4);
//                add(5);
//                add(6);
//                add(7);
//                add(8);
//                add(9);
//        }};
//        Map<Integer, Integer> map2 = new HashMap<>();
//        map2.put(2, 7);
//        map2.put(5, 9);
//        Queue q = new LinkedList();
//        q.add(4);
//        q.add(6);
//        Stack s = new Stack();
//        s.add(1);
//        s.add(6);
//        s.add(4);
//        
//        map.put(list, new byte[]{(byte)240, 4, 5, 6});
//        map.put(list2, new byte[]{(byte)252, 4, 5, 6, 7, 8, 9, (byte)254});
//        map.put(map2, new byte[]{(byte)245, 2, 7, 5, 9});
//        map.put(q, new byte[]{(byte)239, 4, 6});
//        map.put(s, new byte[]{(byte)240, 1, 6, 4});
    }

    @Test
    public void testPack() {
        /*map.entrySet().forEach((entry) -> {
            QPack qpack = new QPack();
            byte[] result = qpack.pack(entry.getKey());
            System.out.println("Input:    " + entry.getKey());
            System.out.println("Expected: " + Arrays.toString((byte[]) entry.getValue()));
            System.out.println("Result:   " + Arrays.toString(result));
            System.out.println("================================================");
            assertArrayEquals((byte[]) entry.getValue(), result);
        });*/
    }

    @Test
    public void testUnpack() {
        map.entrySet().forEach((entry) -> {
            QPack qpack = new QPack();
            Object result = qpack.unpack(qpack.pack(entry.getKey()));
            System.out.println("Expected: " + entry.getKey());
            System.out.println("Result:   " + result);
            System.out.println("================================================");
            assertEquals(entry.getKey(), result);
        });
    }
}
