package talk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;
    private static ServerSocket server;
    private static Socket client;
    private static ExecutorService executor;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(PORT);
        executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        while (true) { 
            listenAndAccept();
            executor.submit(() -> handleClinet());
        }
    }

    public static void handleClinet() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

            writer.println("[welcome] 연결 완료!");

            String message;
            while ((message = reader.readLine()) != null) {
                writer.println("[echo]" + message);
            }
        } catch (IOException e) {
            System.err.println("클라이언트 처리 오류: " + e.getMessage());
        }
    }

    public static void listenAndAccept() throws IOException {
        client = server.accept();
    }
}