import java.io.*;
import java.net.*;
import java.util.*;

public class DiaryChallengeServer {
    private static final int PORT = 12345; // 서버 포트 번호
    private static Map<String, Map<Integer, Integer>> userDiaryCount = new HashMap<>(); // 클라이언트별 날짜별 일기 개수
    private static Map<String, Integer> successCount = new HashMap<>(); // 클라이언트별 성공 횟수
    private static int challengeDays = 7; // 챌린지 기간 (일 단위)

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 클라이언트 이름 설정
                out.println("Enter your name:");
                clientName = in.readLine();

                // 클라이언트 데이터 초기화
                userDiaryCount.putIfAbsent(clientName, new HashMap<>());
                successCount.putIfAbsent(clientName, 0);

                out.println("Welcome to the Diary Challenge, " + clientName + "!");
                out.println("Commands: [write <diary entry>, status, evaluate, rank, exit]");

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.startsWith("write ")) {
                        String diaryEntry = input.substring(6);
                        writeDiary(clientName);
                        out.println("Diary entry saved for " + clientName + "!");
                    } else if (input.equals("status")) {
                        checkStatus(clientName);
                    } else if (input.equals("evaluate")) {
                        evaluateChallenge(clientName);
                    } else if (input.equals("rank")) {
                        showRanking();
                    } else if (input.equals("exit")) {
                        out.println("Goodbye!");
                        break;
                    } else {
                        out.println("Unknown command. Use: write, status, evaluate, rank, exit");
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + clientName);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void writeDiary(String clientName) {
            // 날짜 계산
            int currentDay = getCurrentDay();
            Map<Integer, Integer> diaryMap = userDiaryCount.get(clientName);

            // 현재 날짜의 일기 개수 업데이트
            diaryMap.put(currentDay, diaryMap.getOrDefault(currentDay, 0) + 1);
        }

        private void checkStatus(String clientName) {
            Map<Integer, Integer> diaryMap = userDiaryCount.get(clientName);
            out.println("=== Your Diary Status ===");
            for (int day = 1; day <= challengeDays; day++) {
                int count = diaryMap.getOrDefault(day, 0);
                out.println("Day " + day + ": " + count + " diary entries");
            }
        }

        private void evaluateChallenge(String clientName) {
            Map<Integer, Integer> diaryMap = userDiaryCount.get(clientName);
            int successDays = 0;

            for (int day = 1; day <= challengeDays; day++) {
                if (diaryMap.getOrDefault(day, 0) > 0) {
                    successDays++; // 하루라도 일기가 있으면 성공
                }
            }

            double completionRate = (successDays / (double) challengeDays) * 100;

            if (completionRate >= 50) {
                out.println("Challenge completed! Success Days: " + successDays);
                successCount.put(clientName, successCount.get(clientName) + 1);
            } else {
                out.println("Challenge failed. Success Days: " + successDays + ". Completion rate: " + completionRate + "%");
            }
        }

        private void showRanking() {
            List<Map.Entry<String, Integer>> ranking = new ArrayList<>(successCount.entrySet());
            ranking.sort((a, b) -> b.getValue() - a.getValue()); // 성공 횟수 기준 내림차순 정렬

            out.println("=== Challenge Ranking ===");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : ranking) {
                out.println(rank + ". " + entry.getKey() + " - Successes: " + entry.getValue());
                rank++;
            }
        }

        private int getCurrentDay() {
            // 현재 날짜를 1부터 challengeDays까지의 범위로 임의로 반환
            // 실제 환경에서는 LocalDate를 사용하여 적절히 설정 가능
            return (int) ((System.currentTimeMillis() / (1000 * 60 * 60 * 24)) % challengeDays) + 1;
        }
    }
}
