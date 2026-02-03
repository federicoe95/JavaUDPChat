import com.google.gson.Gson;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.UUID;

public class Receiver implements Runnable {

    private static final int PORT = 50000;
    private final UUID myClientId;
    private final MulticastSocket socket;
    private final InetAddress groupAddress;
    private final Gson gson = new Gson();

    public Receiver(UUID myClientId, String multicastIp, String lanIpPrefix) throws Exception {
        this.myClientId = myClientId;
        this.groupAddress = InetAddress.getByName(multicastIp);

        this.socket = new MulticastSocket(PORT);

        // Trova la prima interfaccia di rete attiva dato un prefisso e non loopback
        NetworkInterface networkInterface = findIPv4NetworkInterfaceByPrefix(lanIpPrefix);
        if (networkInterface == null) {
            throw new RuntimeException("Nessuna interfaccia di rete attiva trovata!");
        }

        // Join del gruppo usando il nuovo metodo
        SocketAddress group = new InetSocketAddress(groupAddress, PORT);
        socket.joinGroup(group, networkInterface);

        System.out.println("In ascolto sul gruppo multicast " + multicastIp + " tramite interfaccia " + networkInterface.getName());
    }

    private NetworkInterface findIPv4NetworkInterfaceByPrefix(String prefix) throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;

            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    String ip = addr.getHostAddress();
                    if (ip.startsWith(prefix)) {
                        System.out.println("Trovata NIC: " + ni.getName() + " -> " + ip);
                        return ni;
                    }
                }
            }
        }

        System.out.println("Nessuna NIC con prefisso: " + prefix + " trovata...");
        // fallback: loopback (utile per test sulla stessa macchina)
        return NetworkInterface.getByInetAddress(InetAddress.getByName("127.0.0.1"));
    }

    @Override
    public void run() {
        byte[] buffer = new byte[2048];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                Message msg = gson.fromJson(json, Message.class);

                if (msg.getClientId().equals(myClientId)) continue;

                String senderIp = packet.getAddress().getHostAddress();
                System.out.println("[" + msg.getNickname() + " (" + senderIp + ")] " + msg.getText());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
