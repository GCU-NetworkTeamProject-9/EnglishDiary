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
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start(); // 각 클라이언트별 스레드 생성
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
        public void run() {
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
