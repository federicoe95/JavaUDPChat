import java.util.Scanner;
import java.util.UUID;

public class ChatClient {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci nickname: ");
        String nickname = scanner.nextLine().trim();

        UUID clientId = UUID.randomUUID();
        String broadcastIp = "192.168.1.255"; // Inserisci l'indirizzo di broadcast

        Sender sender = new Sender(broadcastIp, clientId, nickname);
        Receiver receiver = new Receiver(clientId);

        new Thread(receiver).start();

        System.out.println("Chat pronta. Scrivi un messaggio:");

        while (true) {
            String text = scanner.nextLine();
            sender.send(text);
        }
    }
}
