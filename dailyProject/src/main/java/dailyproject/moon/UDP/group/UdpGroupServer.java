package dailyproject.moon.UDP.group;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

@RestController
public class UdpGroupServer {
    public boolean closed = false;
    public String ip = "224.0.0.1";//组播虚拟地址
    public int port = 6789;//组播端口
    public int MessageIndex = 0;

    @PostConstruct
    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("UdpTestServer start ");
                runServer();
            }
        }).start();
    }

    private void runServer(){
        try {
            InetAddress	group = InetAddress.getByName(ip);
            MulticastSocket socket = new MulticastSocket(port);
            byte[] arb = new byte[1024];
            socket.joinGroup(group);//加入该组
            socket.setLoopbackMode(true);
            DatagramPacket datagramPacket =new DatagramPacket(arb,arb.length);
            while(!closed){
                socket.receive(datagramPacket);
                System.out.println("Server received ----> " + datagramPacket.getAddress().getHostAddress() + " : " + datagramPacket.getPort());
                Thread.sleep(2000);
                //读数据
                readMessage(datagramPacket);

                send();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("UdpTestServer run Exception: "+e.toString());
        }
    }


    public void readMessage(DatagramPacket receivePacket){
        String msg = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        System.out.println("received ====> " + msg);
    }




    @GetMapping("udpSendMessage")
    public void send(){
        String sendMessage="服务端消息 :  ,"+MessageIndex++;
        byte[] message = sendMessage.getBytes(); //发送信息

        try{
            InetAddress inetAddress = InetAddress.getByName(ip); //指定组播地址
            DatagramPacket datagramPacket = new DatagramPacket(message, message.length, inetAddress, port); //发送数据包囊
            MulticastSocket multicastSocket = new MulticastSocket();//创建组播socket
            multicastSocket.send(datagramPacket);
        }catch (Exception e) {
            System.out.println("UdpTestServer send Exception: "+e.toString());
        }


    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        UdpGroupServer server = new UdpGroupServer();
        server.start();
    }

}
