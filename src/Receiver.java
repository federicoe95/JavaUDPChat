import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Receiver implements Runnable {

    private static final int PORT = 50000;

    private final UUID myClientId;
    private final DatagramSocket socket;

    public Receiver(UUID myClientId) throws Exception {
        this.myClientId = myClientId;
        this.socket = new DatagramSocket(PORT);
        this.socket.setBroadcast(true);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];

        System.out.println("In ascolto sulla porta " + PORT);

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String raw = new String(
                        packet.getData(),
                        0,
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                String[] parts = raw.split("\\|", 3);
                if (parts.length < 3) continue;

                UUID senderId = UUID.fromString(parts[0]);
                String nickname = parts[1];
                String message = parts[2];

                //Ignora i messaggi inviati da sè stesso
                if (senderId.equals(myClientId)) {
                    continue;
                }

                String senderIp = packet.getAddress().getHostAddress();

                System.out.println("[" + nickname + " (" + senderIp + ")] " + message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}