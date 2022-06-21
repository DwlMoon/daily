package dailyproject.moon.IO.netty;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CheckUtil {


    /**
     * 二进制--》byte数组
     * @param bitString
     * @return
     */
    public static byte[] bitString2ByteArray2(String bitString) {
        int size = bitString.length() / 8;
        byte[] bytes = new byte[size];

        for (int i = 0, total = 0; i < size; i++) {
            byte b = 0x00;
            for (int j = 0; j < 8; j++, total++) {
                b |= (byte) ((bitString.charAt(total) - 48) << (7 - j));
            }
            bytes[i] = b;
        }

        return bytes;
    }


    /**
     * byte  -> 16进制
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }



    /**
     * 二进制转16进制
     * @param bit
     * @return
     */
    public static String bitTo16(String bit){
        return bytesToHexString(bitString2ByteArray2(bit)).toUpperCase();
    }


    /**
     * 16进制字符串转字节数组
     * @param src
     * @return
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }



    /**
     * 异或和校检
     * @param data
     * @return
     */
    public static String XorCheack(String data) {
        Integer[] list = Arrays.stream(IntStream.range(0, data.length() / 2)
                .mapToObj(i -> data.substring(2 * i, 2 * i + 2))
                .toArray(String[]::new))
                .map(String::toLowerCase)
                .map(s -> ((((s.charAt(0) > '9') ? ((s.charAt(0) - 'a') + 10) : (s.charAt(0) - '0')) << 4) + ((s.charAt(1) > '9') ? ((s.charAt(1) - 'a') + 10) : (s.charAt(1) - '0'))))
                .toArray(Integer[]::new);

        Integer[] list1 = IntStream.range(0, list.length / 2).mapToObj(i -> list[i * 2]).toArray(Integer[]::new);
        Integer[] list2 = IntStream.range(0, list.length / 2).mapToObj(i -> list[i * 2 + 1]).toArray(Integer[]::new);
        for (int i = list1.length - 2; i >= 0; i--) {
            list1[i] ^= list1[i + 1];
        }
        for (int i = list2.length - 2; i >= 0; i--) {
            list2[i] ^= list2[i + 1];
        }

        return String.format("%02X%02X", list1[0],list2[0]);
    }


    public static void main(String[] args) {
//        String data2="EB9020891837001000C000002AC00099010000191675D300981000C0000017D020660101200000000140050B09D7FFFFFFFFFFFFFFFF887325F0";
        String data2="EB902010";
        System.out.println(XorCheack(data2));
    }


}
