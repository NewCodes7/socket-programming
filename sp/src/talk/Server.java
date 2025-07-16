package talk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;
    private static ServerSocket server;
    private static ExecutorService executor;
    private static ConcurrentHashMap<String, Client> clients;

    private static class Client {
        public Socket socket;
        public ObjectOutputStream out;
        public String nickname;

        public Client (Socket socket, ObjectOutputStream out, String nickname) {
            this.socket = socket;
            this.out = out;
            this.nickname = nickname;
        }
    }

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(PORT);
        executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        clients = new ConcurrentHashMap<>();

        while (true) { 
            Socket socket = listenAndAccept();
            executor.submit(() -> handleClinet(socket));
        }
    }

    public static void handleClinet(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            String sessionId = generateSessionId();
            Message welcome = new Message("auth", sessionId, "[welcome] 연결 완료!");
            out.writeObject(welcome);
            out.flush();

            Message message;
            while ((message = (Message) in.readObject()) != null) {
                if (message.getType().equalsIgnoreCase("nickname")) {
                    String nickname = message.getBody();
                    clients.put(sessionId, new Client(socket, out, nickname));
                    continue;
                }

                message.setBody(clients.get(sessionId).nickname + ": " + message.getBody());

                // 브로드캐스팅
                for (Map.Entry<String, Client> entry : clients.entrySet()) {
                    entry.getValue().out.writeObject(message);
                    entry.getValue().out.flush();
                }

                System.out.println("'" + message.getBody() + "'에 대한 브로드캐스팅 완료!");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("클라이언트 처리 오류: " + e.getMessage());
        }
    }

    public static Socket listenAndAccept() throws IOException {
        return server.accept();
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}