/**
 * wesoft.com Inc.
 * Copyright (c) 2005-2016 All Rights Reserved.
 */
package blue.toolbox.tester;

import blue.toolbox.communication.NettyDataSender;

import com.wesoft.yatp.ConvertUtils;
import com.wesoft.yatp.type.impl.MacConvertor;

/**
 * @author <a href="mailto:chencq@hzwesoft.com">chencq</a>
 * @version $Id: SendLipsumData.java, 2016/11/30 15:39 $
 */
public class SendLipsumData {

    public static void main(String[] args) {
        MacConvertor macConvertor = new MacConvertor();

        byte[] version = "TC01".getBytes();
        byte[] heaerLength = { 8 };
        byte[] header = join(heaerLength, macConvertor.convertToBytes("FF:FF:FF:FF:FF:FF"));

        byte[] type1 = { 6 };
        byte[] value1 = "330110000001".getBytes();
        byte[] length1 = { (byte) value1.length };
        byte[] content1 = join(join(type1, length1), value1);
        byte[] type2 = { 31 };
        byte[] value2 = "孙悟空".getBytes();
        byte[] length2 = { (byte) value1.length };
        byte[] encode2 = { 1 };
        byte[] content2 = join(join(join(type2, length2), encode2), value2);
        byte[] dataLength = ConvertUtils.toShortBytes((short) (content1.length + content2.length));
        byte[] data = join(join(dataLength, content1), content2);
        byte[] length = ConvertUtils.toShortBytes((short) (version.length + 2 + header.length + data.length));

        byte[] result = join(version, join(length, data));
        while (true) {
            try {
                System.out.println(1);
                NettyDataSender.send("127.0.0.1", 10001, result);
                Thread.sleep(100000);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }

    }

    private static byte[] join(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
