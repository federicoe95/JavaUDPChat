import com.google.gson.Gson;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Sender implements Runnable {

    private static final int PORT = 50000;
    private final UUID clientId;
    private final String nickname;
    private final InetAddress groupAddress;
    private final MulticastSocket socket;
    private final Gson gson = new Gson();

    public Sender(String multicastIp, UUID clientId, String nickname) throws Exception {
        this.clientId = clientId;
        this.nickname = nickname;
        this.groupAddress = InetAddress.getByName(multicastIp);
        this.socket = new MulticastSocket();
    }

    public void send(String text) throws Exception {
        Message msg = new Message(clientId, nickname, text);
        String json = gson.toJson(msg);
        byte[] buffer = json.getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, PORT);
        socket.send(packet);
    }

@Override
    public void run() {
        // opzionale se vuoi input continuo
    }
}
