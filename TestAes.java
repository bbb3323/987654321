import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

public class Test {

    public static void main(String[] args) {

        String imagePath = "imagePath";
        String data = "/Users/mac/Downloads/data.txt";
        String cc = "/Users/mac/Downloads/**.*";

        String key = read(imagePath);
        //------------------read 2 data

        String string = readFile(cc);
        String de = de(string, key);
        writerFile(data, de);

        //----------------- writer 2 cc

//        String readFile = readFile(data);
//        writerFile(cc,en(readFile,key));

    }

    private static String readFile(String file) {
        try {
            File file1 = new File(file);
            FileReader fileReader = new FileReader(file1);
            char[] c = new char[(int) file1.length()];
            fileReader.read(c);
            return new String(c).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void writerFile(String file, String data) {
        try {
            File file1 = new File(file);
            FileWriter writer = new FileWriter(file1);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String read(String file) {
        File image = new File(file);
        try {
            FileInputStream fr = new FileInputStream(image);
            byte[] bytes = fr.readAllBytes();
            byte[] copy = new byte[44];
            System.arraycopy(bytes, bytes.length - 44, copy, 0, 44);
            String string = new String(Base64.getDecoder().decode(copy));
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //en 1 de 2
    public static String de(String str, String key) {
        SecretKeySpec keySpec = k(key.substring(0, 16));
        IvParameterSpec ivParameterSpec = i(key.substring(16, 32));
        Cipher instance = null;
        try {
            instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, keySpec, ivParameterSpec);
            byte[] bytes = instance.doFinal(decodeHex(str.toCharArray()));
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String en(String str, String key) {
        SecretKeySpec keySpec = k(key.substring(0, 16));
        IvParameterSpec ivParameterSpec = i(key.substring(16, 32));
        Cipher instance = null;
        try {
            instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(1, keySpec, ivParameterSpec);
            byte[] bytes = instance.doFinal(str.getBytes("UTF-8"));
            return encodeHexString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decodeHex(final char[] data) {
        final byte[] out = new byte[data.length >> 1];
        final int len = data.length;
        final int outLen = len >> 1;
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    protected static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        return digit;
    }


    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String encodeHexString(byte[] data) {
        final int dataLength = data.length;
        final char[] out = new char[dataLength << 1];
        for (int i = 0, j = 0; i < 0 + dataLength; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }


    private static IvParameterSpec i(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        return new IvParameterSpec(bArr);
    }

    private static SecretKeySpec k(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuilder stringBuffer = new StringBuilder(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        return new SecretKeySpec(bArr, "AES");
    }
}
//        https://xin2025.s3.ap-southeast-1.amazonaws.com/log1.js
//        https://xin2025.s3.ap-southeast-1.amazonaws.com/log2.js
