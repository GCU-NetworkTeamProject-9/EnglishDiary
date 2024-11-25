import java.io.*;
import java.net.*;
import java.util.*;

public class DiaryChallengeServer {
    private static final int PORT = 12345; // 서버 포트 번호
    private static Map<String, Map<Integer, List<String>>> userDiaries = new HashMap<>(); // 클라이언트별 날짜별 일기 저장
    private static Map<String, Integer> successDaysMap = new HashMap<>(); // 클라이언트별 성공한 날짜의 총합
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
                userDiaries.putIfAbsent(clientName, new HashMap<>());
                successDaysMap.putIfAbsent(clientName, 0);

                out.println("Welcome to the Diary Challenge, " + clientName + "!");
                out.println("Commands: [write <diary entry>, status, rank, view, exit]");

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.startsWith("write ")) {
                        String diaryEntry = input.substring(6);
                        writeDiary(clientName, diaryEntry);
                        out.println("Diary entry saved for " + clientName + "!");
                    } else if (input.equals("status")) {
                        checkStatus(clientName);
                    } else if (input.equals("rank")) {
                        showRanking();
                    } else if (input.equals("view")) {
                        viewDiaries(clientName);
                    } else if (input.equals("exit")) {
                        out.println("Goodbye!");
                        break;
                    } else {
                        out.println("Unknown command. Use: write, status, rank, view, exit");
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

        private void writeDiary(String clientName, String diaryEntry) {
            // 날짜 계산
            int currentDay = getCurrentDay();
            Map<Integer, List<String>> diaryMap = userDiaries.get(clientName);

            // 현재 날짜의 일기 저장
            diaryMap.putIfAbsent(currentDay, new ArrayList<>());
            diaryMap.get(currentDay).add(diaryEntry);

            // 성공한 날짜 확인 및 업데이트
            if (diaryMap.get(currentDay).size() == 1) { // 첫 번째 일기를 작성하면 성공으로 간주
                successDaysMap.put(clientName, successDaysMap.get(clientName) + 1);
            }
        }

        private void checkStatus(String clientName) {
            Map<Integer, List<String>> diaryMap = userDiaries.get(clientName);
            out.println("=== Your Diary Status ===");
            for (int day = 1; day <= challengeDays; day++) {
                int count = diaryMap.getOrDefault(day, new ArrayList<>()).size();
                out.println("Day " + day + ": " + count + " diary entries");
            }
            out.println("Total success days: " + successDaysMap.get(clientName));
        }

        private void viewDiaries(String clientName) {
            Map<Integer, List<String>> diaryMap = userDiaries.get(clientName);
            out.println("=== Your Diaries ===");
            for (int day = 1; day <= challengeDays; day++) {
                List<String> entries = diaryMap.getOrDefault(day, new ArrayList<>());
                if (!entries.isEmpty()) {
                    out.println("Day " + day + ":");
                    for (String entry : entries) {
                        out.println(" - " + entry);
                    }
                }
            }
        }

        private void showRanking() {
            List<Map.Entry<String, Integer>> ranking = new ArrayList<>(successDaysMap.entrySet());
            ranking.sort((a, b) -> b.getValue() - a.getValue()); // 성공 날짜 총합 기준 내림차순 정렬

            out.println("=== Challenge Ranking ===");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : ranking) {
                out.println(rank + ". " + entry.getKey() + " - Total Success Days: " + entry.getValue());
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
