import com.google.gson.Gson;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Receiver implements Runnable {

    private static final int PORT = 50000;
    private final UUID myClientId;
    private final DatagramSocket socket;
    private final Gson gson = new Gson();

    public Receiver(UUID myClientId) throws Exception {
        this.myClientId = myClientId;
        this.socket = new DatagramSocket(PORT);
        this.socket.setBroadcast(true);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[4096]; // dimensioni buffer aumentato per sicurezza

        System.out.println("In ascolto sulla porta " + PORT);

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                Message msg = gson.fromJson(json, Message.class);

                // scarta i messaggi propri
                if (msg.getClientId().equals(myClientId)) continue;

                String senderIp = packet.getAddress().getHostAddress();
                System.out.println("[" + msg.getNickname() + " (" + senderIp + ")] " + msg.getText());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
