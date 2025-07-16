package talk;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private String sessionId;
    private String body;

    public Message (String type, String sessionId, String body) {
        this.type = type;
        this.sessionId = sessionId;
        this.body = body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "MyMessage{type='" + type + "', sessiondId='" + sessionId + "', body='" + body + "'}";
    }
}
