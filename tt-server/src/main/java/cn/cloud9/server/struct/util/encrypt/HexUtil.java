package cn.cloud9.server.struct.util.encrypt;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 10:07
 */
public class HexUtil {

    private static int hexUnit = 16;

    /**
     * 十六进制串转换为字节数组
     * @param hex
     * @return
     */
    public static byte[] hex2bytes(String hex) {
        final int byteSize = hex.length() / 2;
        final byte[] bytes = new byte[byteSize];
        for (int i = 0; i < byteSize; i++) {
            final int high4 = Integer.parseInt(hex.substring(i * 2, i * 2 + 1), hexUnit);
            final int low4 = Integer.parseInt(hex.substring(i * 2 + 1, i * 2 + 2), hexUnit);
            bytes[i] = (byte) (high4 * hexUnit + low4 );
        }
        return bytes;
    }

    /**
     * 将字节转换为16进制数组
     * @param encodeBytes
     * @return
     */
    public static String bytes2hex(byte[] encodeBytes) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : encodeBytes) {
            /* 获取当前字节补码的后八位 */
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) hex = "0" + hex;
            builder.append(hex);
        }
        return builder.toString();
    }

}
