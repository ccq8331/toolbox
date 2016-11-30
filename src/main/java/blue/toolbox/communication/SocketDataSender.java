/**
 * wesoft.com Inc.
 * Copyright (c) 2005-2016 All Rights Reserved.
 */
package blue.toolbox.communication;

import java.io.*;
import java.net.Socket;

/**
 * @author <a href="mailto:chencq@hzwesoft.com">chencq</a>
 * @version $Id: SocketDataSender.java, 2016/11/28 14:30 $
 */
public class SocketDataSender {

    public static void send(String ip, int port) throws Exception {
        Socket socket = new Socket(ip, port);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String info = "测试数据包";
        pw.write(info);
        pw.flush();
        socket.shutdownOutput();
        String reply = null;
        while (!((reply = br.readLine()) == null)) {
            System.out.println("接收服务器的信息：" + reply);
        }
        br.close();
        is.close();
        pw.close();
        os.close();
        socket.close();

    }

}
