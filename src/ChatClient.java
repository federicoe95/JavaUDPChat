import java.util.Scanner;
import java.util.UUID;

public class ChatClient {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        UUID clientId = UUID.randomUUID();

        System.out.print("Inserisci un nickname: ");
        String nickname = scanner.nextLine().trim();

        System.out.println(nickname + " Client ID: " + clientId);

        String broadcastIp = "192.168.1.255";
        Sender sender = new Sender(broadcastIp, clientId, nickname);
        Receiver receiver = new Receiver(clientId);

        new Thread(receiver).start();

        System.out.println("Chat UDP avviata con successo!");

        while (true) {
            String msg = scanner.nextLine();
            sender.send(msg);
        }
    }
}

