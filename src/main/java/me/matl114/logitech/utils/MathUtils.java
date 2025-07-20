package me.matl114.logitech.utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

public class MathUtils {
    static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    static final int[] mask ;
    static final int[] POW;
    static final String zeroString=toBinaryCode(0);
    static {
        mask=new int[33];
        POW=new int[33];
        for(int i=0; i<32; i++) {
            POW[i]=(1<<i);
            mask[i]=(1<<i) -1;
        }
        mask[32]=-1;

    }
    public static int getBitPos(int k){
        return POW[k];
    }
    /**
     * int型存储规则 采用小端存储
     */
    public static boolean getBits(int code,int bit){
        return ((code>>bit) &1)!=0;
    }
    public static boolean getBit(String codeStr,int bit){
        return codeStr.charAt(bit)=='1';
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


    public static String toBinaryCode(int num){
        StringBuilder sb= new StringBuilder();
        for (int i=0;i<32;++i){
            sb.append((num&1)==0?'0':'1');
            num=num>>1;
        }
        return sb.toString();
    }

    /**
     * make sure these codes
     * @param a
     * @param b
     * @return
     */
    public static String andStr(String a,String b){
        if(a.length()!=32||b.length()!=32){
            return zeroString;
        }else {
            StringBuilder sb= new StringBuilder();
            for(int i=0;i<32;++i){
                sb.append((a.charAt(i)==digits[1]&&b.charAt(i)==digits[1])?'1':'0');
            }
            return sb.toString();
        }
    }
    public static String orStr(String a,String b){
        if(a.length()!=32||b.length()!=32){
            return zeroString;
        }else {
            StringBuilder sb= new StringBuilder();
            for(int i=0;i<32;++i){
                sb.append((a.charAt(i)==digits[1]||b.charAt(i)==digits[1])?'1':'0');
            }
            return sb.toString();
        }
    }
    public static String xorStr(String a,String b){
        if(a.length()!=32||b.length()!=32){
            return zeroString;
        }else {
            StringBuilder sb= new StringBuilder();
            for(int i=0;i<32;++i){
                sb.append((a.charAt(i)==b.charAt(i))?'0':'1');
            }
            return sb.toString();
        }
    }
    public static String notStr(String a){
        if(a.length()!=32){
            return zeroString;
        }else {
            StringBuilder sb= new StringBuilder();
            for(int i=0;i<32;++i){
                sb.append((a.charAt(i)==digits[1])?'0':'1');
            }
            return sb.toString();
        }
    }
    public static String leftShiftStr(String a){
        if(a.length()!=32){
            return zeroString;
        }else {
            StringBuilder sb= new StringBuilder(a);
            sb.insert(0,digits[0]);
            return sb.substring(0,32);
        }
    }

    /**
     * 算数右移
     * @param a
     * @return
     */
    public static String rightShiftStr(String a){
        if(a.length()!=32){
            return zeroString;
        }else {
            StringBuilder sb= new StringBuilder(a);
            //sb.deleteCharAt(0);
            sb.append(a.charAt(a.length()-1));
            return sb.substring(1,a.length()+1);
        }
    }
    public static String toBinaryCodeForce(int num){
        int chars = 32;
        byte[] buf;
        buf = new byte[chars];
        formatUnsignedInt(num, 1, buf, chars);
        return new String(buf, StandardCharsets.UTF_8);
    }
    public static int fromBinaryCode(String code){
        /**
         * we assume that  code len 32
         */
        return 0;
    }
    public static int maskToN(int code ,int n){

        int maskN =mask[n];
        return code&maskN;
    }
    public static int getBit(int code,int pos){
        return (code&POW[pos])==0?0:1;
    }
    public static int bitCount(int code,int to){
        int n=maskToN(code,to);
        n = n - ((n >> 1) & 0x55555555);	// 1
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333);	// 2
        n = (n + (n >> 4)) & 0x0f0f0f0f;	// 3
        n = n + (n >> 8);	// 4
        n = n + (n >> 16);	// 5
        return n & 0x3f;	// 6
//        int tmp = code - ((code >>1) &0333_3333_3333) - ((code >>2) &011111111111);
//        return ((tmp + (tmp >>3)) &030707070707) %63;
    }
    public static int bitCountStupid(int code,int to){
        int n=maskToN(code,to);
        int count=0;
        for(int i=0;i<32;++i){
            count+=code&1;
            code=code>>1;
        }
        return count;
    }
    public static int fromLong(@Nonnull Long a){
        if(a>Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }else return a.intValue();
    }
    public static int safeAdd(int a,int b){
        int x=a+b;
        if ((x^a) < 0 && (x^b) < 0){
            return Integer.MAX_VALUE;
        }
        return x;

    }
    public static int safeDivide(int a,int b){
        if(b==0){
            return a==0?0:Integer.MAX_VALUE;
        }else {
            return a / b;
        }
    }
    public static int sha256(String data,int size){
        assert (size<=8);
        String output= DigestUtils.sha256Hex(data);
        String hashCode=output.substring(Math.max( output.length()-size,0));
        try{
            return Integer.parseInt(hashCode,16);
        }catch (Throwable e){
            e.printStackTrace();
            return -336;
        }
    }
    public static int sha512(String data,int size){
        assert (size<=8);
        String output= DigestUtils.sha512Hex(data);
        String hashCode=output.substring(Math.max( output.length()-size,0));
        try{
            return Integer.parseInt(hashCode,16);
        }catch (Throwable e){
            e.printStackTrace();
            return -336;
        }
    }
    public static int sha384(String data,int size){
        assert (size<=8);
        String output= DigestUtils.sha384Hex(data);
        String hashCode=output.substring(Math.max( output.length()-size,0));
        try{
            return Integer.parseInt(hashCode,16);
        }catch (Throwable e){
            e.printStackTrace();
            return -336;
        }
    }
}
