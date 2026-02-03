import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Sender implements Runnable {

    private static final int PORT = 50000;

    private final UUID clientId;
    private final String nickname;
    private final InetAddress broadcastAddress;
    private final DatagramSocket socket;

    public Sender(String broadcastIp, UUID clientId, String nickname) throws Exception {
        this.clientId = clientId;
        this.nickname = nickname;
        this.broadcastAddress = InetAddress.getByName(broadcastIp);
        this.socket = new DatagramSocket();
        this.socket.setBroadcast(true);
    }

    public void send(String message) throws Exception {
        String payload = clientId + "|" + nickname + "|" + message;

        byte[] buffer = payload.getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(
                buffer,
                buffer.length,
                broadcastAddress,
                PORT
        );
        socket.send(packet);
    }

    @Override
    public void run() {
        // opzionale se vuoi input continuo
    }
}
