import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver {
    public static void main(String[] args) throws Exception {
        int porta = 12345; // Porta condivisa
        DatagramSocket socket = new DatagramSocket(porta);
        System.out.println("In ascolto sulla porta " + porta + "...");

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println(msg);
        }
    }
}