package talk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

class Client {
    private static final int PORT = 8080;
    private static Socket client;

    public static void main(String[] args) throws IOException {
        client = new Socket();
        client.connect(new InetSocketAddress("localhost", PORT));

        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        String welcome = reader.readLine();
        System.out.println(welcome);

        String input;
        while (true) {
            input = scanner.nextLine();

            writer.println(input);

            String response = reader.readLine();
            System.out.println(response);
        }
    }
}