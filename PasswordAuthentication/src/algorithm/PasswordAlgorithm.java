package algorithm;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordAlgorithm {
    //生成口令信息s=Agent(Dpw,Dsalt)
    public static String Agent(String Dpw, String Dsalt) throws Exception {
        //撒盐结果做密钥
        String K = Asalt(Dsalt, Dpw);
        //用一个64位的全0的二进制位串构造一个数据块Dp
        String Dp = "00000000";
        String Dc = null;

        //如果i<25 ，则回到对数据块加密的环节,des
        for (int i = 0; i < 25; i++) {
            Dc = Acrypt(Dp, K);
            Dp = null;
            Dp = Dc;
            Dc = null;
        }

        String s = binary(Dp.getBytes());
        Dc = s.substring(0, 64);
        //把数据块转换成字符串s=Atrans(Dc)
        s = Atrans(Dc);
        return s;
    }

    //给口令撒盐
    public static String Asalt(String dpw, String dsalt) {
        String Dtemp = dpw + dsalt;
        String Dhash = MD5(Dtemp);
        return Dhash;
    }

    //生成一个盐值：Dsalt=Arandom()
    public static String Arandom() {
        Random rand = new Random();
        int r = rand.nextInt(2088);
        String Dsalt = String.valueOf(r);
        return Dsalt;
    }

    //MD5算法实现Hash值
    private static String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
            return new String(str);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //数据块转字符
    private static String Atrans(String dc) {
        char[] str = new char[]{'.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        dc = dc + "00";
        String s = "";
        for (int i = 0; i < 11; i++) {
            String b = dc.substring(i * 6, 6 * (i + 1));
            int a = Integer.parseInt(b, 2);
            s = s + str[a];
        }
        return s;
    }

    //一个DES算法
    private static String Acrypt(String src, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec ks = new DESKeySpec(key.getBytes("gbk"));
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(ks);
        Cipher cip = Cipher.getInstance("DES");
        cip.init(1, sk, sr);
        String dest = new String(cip.doFinal(src.getBytes("gbk")));
        return dest;
    }

    private static String binary(byte[] bytes) {
        return (new BigInteger(1, bytes)).toString(2);
    }
}
