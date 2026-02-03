import java.util.UUID;

public class Message {
    private UUID clientId;
    private String nickname;
    private String text;
    private long timestamp;

    public Message(UUID clientId, String nickname, String text) {
        this.clientId = clientId;
        this.nickname = nickname;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    // getter
    public UUID getClientId() { return clientId; }
    public String getNickname() { return nickname; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
}