package me.matl114.logitech.Utils;

import java.util.function.Supplier;

public class MathUtils {
    protected static final boolean isCompacted=((Supplier<Boolean>)(()->{
         Boolean obj=   (Boolean)ReflectUtils.invokeGetRecursively("草",Settings.METHOD,"coder");
         if(obj==null) {
             return true;
         }
         return obj;
    })).get();
    static final int HI_BYTE_SHIFT=((Supplier<Integer>)(()->{
        Integer obj=   (Integer) ReflectUtils.invokeGetRecursively("草",Settings.FIELD,"HI_BYTE_SHIFT");
        if(obj==null) return 0;
        return obj;
    })).get();
    static final int LO_BYTE_SHIFT=((Supplier<Integer>)(()->{
        Integer obj=   (Integer) ReflectUtils.invokeGetRecursively("草",Settings.FIELD,"LO_BYTE_SHIFT");
        if(obj==null) return 8;
        return obj;
    })).get();

    static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    /**
     * int型存储规则 采用小端存储
     */
    public static boolean getBits(int code,int bit){
        return ((code>>bit) &1)!=0;
    }
    public static boolean getBit(String codeStr,int bit){
        return codeStr.charAt(bit)=='1';
    }
    static void putChar(byte[] val, int index, int c) {
        assert index >= 0 && index < val.length >> 1 : "Trusted caller missed bounds check";

        index <<= 1;
        val[index++] = (byte)(c >> HI_BYTE_SHIFT);
        val[index] = (byte)(c >> LO_BYTE_SHIFT);
    }
    private static void formatUnsignedInt(int val, int shift, byte[] buf, int len) {
        int charPos = 0;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[charPos] = (byte)digits[val & mask];
            val >>>= shift;
            ++charPos;

        } while(charPos <len);

    }

    private static void formatUnsignedIntUTF16(int val, int shift, byte[] buf, int len) {
        int charPos = 0;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            putChar(buf, charPos, digits[val & mask]);
            val >>>= shift;
            ++charPos;
        } while(charPos <len);
    }
    public static String toBinaryCode(int num){
        int chars = 32;
        byte[] buf;
        if(isCompacted){
            buf = new byte[chars];
            formatUnsignedInt(num, 1, buf, chars);
            return new String(buf, (byte)0);
        }
        else {
            buf = new byte[chars * 2];
            formatUnsignedIntUTF16(num, 1, buf, chars);
            return new String(buf, (byte)1);
        }

    }
    public static int fromBinaryCode(String code){
        /**
         * we assume that  code len 32
         */
        return 0;
    }
}