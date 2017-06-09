package io.znz.jsite.visa.simulator;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Chaly on 2017/4/13.
 */
public class SocketClient {
//    public static void main(String args[]) {
//        try {
//            Socket socket = new Socket("192.168.2.175", 10008);
//            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
//            //由系统标准输入设备构造BufferedReader对象
//            PrintWriter os = new PrintWriter(socket.getOutputStream());
//            //由Socket对象得到输出流，并构造PrintWriter对象
//            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            //由Socket对象得到输入流，并构造相应的BufferedReader对象
//            String readline;
//            readline = sin.readLine(); //从系统标准输入读入一字符串
//            while (!readline.equals("bye")) {
//                //若从标准输入读入的字符串为 "bye"则停止循环
//                os.println(readline);
//                //将从系统标准输入读入的字符串输出到Server
//                os.flush();
//                //刷新输出流，使Server马上收到该字符串
//                System.out.println("Client:" + readline);
//                //在系统标准输出上打印读入的字符串
//                System.out.println("Server:" + is.readLine());
//                //从Server读入一字符串，并打印到标准输出上
//                readline = sin.readLine(); //从系统标准输入读入一字符串
//            } //继续循环
//            os.close(); //关闭Socket输出流
//            is.close(); //关闭Socket输入流
//            socket.close(); //关闭Socket
//        } catch (Exception e) {
//            System.out.println("Error" + e); //出错，则打印出错信息
//        }
//    }


    //    String  readline = is.readLine(); //从系统标准输入读入一字符串
//            while (!readline.equals("bye")) {
//        //若从标准输入读入的字符串为 "bye"则停止循环
//        writer.write("Client收到内容"+ readline);
//        writer.flush();
//        //刷新输出流，使Server马上收到该字符串
//        System.out.println("Client:" + readline);
//        //在系统标准输出上打印读入的字符串
//        System.out.println("Server:" + is.readLine());
//        //从Server读入一字符串，并打印到标准输出上
//        readline = is.readLine(); //从系统标准输入读入一字符串
//    } //继续循环
//    private static byte charToByte(char c) {
//        return (byte) "0123456789ABCDEF".indexOf(c);
//    }
//
//    public static byte[] hexStringToBytes(String hexString) {
//        if (hexString == null || hexString.equals("")) {
//            return null;
//        }
//        hexString = hexString.toUpperCase();
//        int length = hexString.length() / 2;
//        char[] hexChars = hexString.toCharArray();
//        byte[] d = new byte[length];
//        for (int i = 0; i < length; i++) {
//            int pos = i * 2;
//            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//        }
//        return d;
//    }
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public void client() throws IOException {
        Socket client = null;
        BufferedReader is = null;
        Writer writer = null;
        try {
//            client = new Socket("192.168.2.175", 10008);
            client = new Socket("124.193.165.18", 10008);

            //地网协议头
            OutputStream out = client.getOutputStream();
            out.write(intToBytes(0xFE));
            out.write(intToBytes(0xFE));
            out.flush();

//            writer = new OutputStreamWriter(client.getOutputStream());
//            writer.write("Hello Server.");
//            writer.write("eof\n");
//            writer.flush();

            is = new BufferedReader(new InputStreamReader(client.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String temp;
            int index;
            while ((temp = is.readLine()) != null) {
                if ((index = temp.indexOf("eof")) != -1) {
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
            // logger.info(sb.toString());
            System.out.println(sb.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Close the IO session error: ");
            }
        }

    }

    public static void main(String[] args) throws IOException {
        SocketClient sc = new SocketClient();
        sc.client();
    }
}
