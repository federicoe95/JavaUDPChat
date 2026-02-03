import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Sender {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserisci il tuo nickname: ");
        String nickname = sc.nextLine();

        int porta = 12345; // stessa porta del receiver
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        //InetAddress broadcast = InetAddress.getByName("255.255.255.255");
        InetAddress broadcast = InetAddress.getByName("192.168.1.255");

        System.out.println("Scrivi i messaggi e premi invio per inviarli...");

        while (true) {
            String messaggio = sc.nextLine();
            String fullMessage = "[" + nickname + "]: " + messaggio;
            byte[] data = fullMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, broadcast, porta);
            socket.send(packet);
        }
    }
}