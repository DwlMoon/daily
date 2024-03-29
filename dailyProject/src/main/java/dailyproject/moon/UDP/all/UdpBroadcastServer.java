package dailyproject.moon.UDP.all;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpBroadcastServer {
    public boolean closed = false;
    public String ip = "255.255.255.255";//广播地址
    public int port = 6787;//指定广播接收数据端口
    public int MessageIndex = 0;//数据发送次数

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("UdpBroadcastServer start ");
                runServer();
            }
        }).start();
    }

    private void runServer(){
        try {
            while(!closed){
                Thread.sleep(2000);
                send();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("UdpTestServer run Exception: "+e.toString());
        }
    }

    public void send(){
        try{
            String sendMessage="hello ,message from server,"+MessageIndex++;
            InetAddress adds = InetAddress.getByName(ip);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(sendMessage.getBytes(),sendMessage.length(), adds, port);
            ds.send(dp);
            ds.close();
        }catch (Exception e) {
            System.out.println("UdpTestServer send Exception: "+e.toString());
        }

        if(MessageIndex>=50){
            closed = true;
            System.out.println("closed  ");
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        UdpBroadcastServer server = new UdpBroadcastServer();
        server.start();
    }

}