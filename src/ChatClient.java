import java.util.Scanner;
import java.util.UUID;

public class ChatClient {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci nickname: ");
        String nickname = scanner.nextLine().trim();

        UUID clientId = UUID.randomUUID();
        String multicastIp = "230.0.0.1"; // IP multicast scelto
        String lanIpPrefix = "192.168.1";

        Sender sender = new Sender(multicastIp, clientId, nickname);
        Receiver receiver = new Receiver(clientId, multicastIp, lanIpPrefix);

        new Thread(receiver).start();

        System.out.println("Chat pronta. Scrivi un messaggio:");

        while (true) {
            String text = scanner.nextLine();
            sender.send(text);
        }
    }
}
