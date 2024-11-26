package client;

import java.io.*;
import java.net.*;

public class DiaryChallengeClient {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // 서버 주소
    private static final int SERVER_PORT = 12345; // 서버 포트 번호

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the Diary client.Challenge server!");

            // 서버에서 메시지 수신
            Thread readThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from the server.");
                }
            });
            readThread.start();

            // 사용자 입력을 서버로 전송
            String userMessage;
            while ((userMessage = userInput.readLine()) != null) {
                out.println(userMessage);
                if (userMessage.equals("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
