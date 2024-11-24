import java.io.*;
import java.net.*;
import java.util.*;

public class MultiThreadedServer {
    private static final int PORT = 12345; // 서버 포트 번호
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); //기다리다가 새 클라이언트가 요청시 accept
                System.out.println("New client connected: " + clientSocket.getInetAddress()); //New client 연결됨 표시
                ClientHandler clientHandler = new ClientHandler(clientSocket); //새 클라이언트의 클라이언트 핸들러 추가
                clientHandlers.add(clientHandler); //클라이언트 추가 --
                new Thread(clientHandler).start(); // 각 클라이언트별 스레드 생성 및 run() 메서드 실행

//                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
//
//                // 사용자 입력을 서버로 전송
//                String userMessage;
//                while ((userMessage = userInput.readLine()) != null) {
//                    clientHandler.sendMessage(userMessage);
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 클라이언트 핸들러 클래스
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() { //스레드 호출 시 자동으로 실행됨
            try {
                String message;
                out.println("Welcome to the server!");

                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message); // 다른 클라이언트들에게 메시지 전송
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket.getInetAddress());
            } finally {
                try {
                    socket.close();
                    clientHandlers.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 클라이언트에게 메시지 보내기
        private void sendMessage(String message) {
            out.println(message);
        }

        // 모든 클라이언트에게 메시지 브로드캐스트
        private static void broadcast(String message) {
            synchronized (clientHandlers) {
                for (ClientHandler handler : clientHandlers) {
                    handler.sendMessage(message);
                }
            }
        }
    }
}
