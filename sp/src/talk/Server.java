package talk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private static final int PORT = 8080;
    private static ServerSocket server;
    private static Socket client;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(PORT);
        listenAndAccept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

        writer.println("[welcome] 연결 완료!");

        String message;
        while ((message = reader.readLine()) != null) {
            writer.println("[echo]" + message);
        }
    }

    public static void listenAndAccept() throws IOException {
        client = server.accept();
    }
}