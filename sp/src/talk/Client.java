package talk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Client {
    private static final int PORT = 8080;
    private static Socket client;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        client = new Socket();
        client.connect(new InetSocketAddress("localhost", PORT));

        // 초기화
        out = new ObjectOutputStream(client.getOutputStream()); // out 먼저해야함!! 그 이유는..!
        in = new ObjectInputStream(client.getInputStream());
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 세션 및 닉네임 설정
        Message welcome = (Message) in.readObject();
        String sessionId = welcome.getSessionId();

        System.out.println(welcome.toString());
        System.out.print("닉네임 입력: ");
        
        String nickname = scanner.nextLine();
        Message nicknameReq = new Message("nickname", sessionId, nickname);
        out.writeObject(nicknameReq);
        out.flush();

        // 서버의 응답 메시지 대기
        executor.submit(() -> handleServerMessage(sessionId));

        // 사용자 입력 받아서 보내기 
        String input;
        while (true) {
            System.out.print(nickname + ": ");
            input = scanner.nextLine();

            Message message = new Message("message", sessionId, input);
            out.writeObject(message);
        }
    }

    private static void handleServerMessage(String sessionId) {
        try {
            Message message;
            while((message = (Message) in.readObject()) != null) {
                if (!message.getSessionId().equals(sessionId)) {
                    System.out.println(message.getBody());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            //TODO
        }
    }
}