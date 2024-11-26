package server;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.*;

public class DiaryChallengeServer {
    private static final int PORT = 12345;
    private static Map<String, String> userCredentials = new HashMap<>();
    private static Map<String, Map<Integer, List<String>>> userDiaries = new HashMap<>();
    private static Map<String, Integer> successDaysMap = new HashMap<>();
    private static Map<String, List<Challenge>> userChallenges = new HashMap<>();

    public static void main(String[] args) {
        // 기본 사용자 계정 추가
        userCredentials.put("admin", "1234");
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");

        // 기본 챌린지 추가
        List<Challenge> defaultChallenges = List.of(
                new Challenge("Daily English Diary for Beginners", "Write a daily English diary.", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30), 12),
                new Challenge("Daily Reading Challenge", "Read 10 pages of a book daily.", LocalDate.of(2024, 10, 15), LocalDate.of(2024, 12, 15), 20),
                new Challenge("Exercise Challenge", "Exercise 30 minutes daily.", LocalDate.of(2024, 11, 5), LocalDate.of(2024, 12, 5), 15)
        );

        for (String user : userCredentials.keySet()) {
            userChallenges.put(user, new ArrayList<>(defaultChallenges));
        }

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

                String input;
                while ((input = in.readLine()) != null) {
                    handleClientRequest(input);
                }
            } catch (IOException e) {
                System.out.println("client.Client disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleClientRequest(String input) throws IOException {
            if (input.startsWith("login")) {
                handleLogin(input);
            } else if (input.equals("userinfo")) {
                handleUserInfo(clientName);
            } else if (input.equals("challenges")) {
                sendChallenges(clientName);
            } else if (input.startsWith("createChallenge")) {
                handleCreateChallenge(input);
            } else if (input.startsWith("writeDiary")) {
                String[] parts = input.split(",", 3);
                String diaryTitle = parts[1];
                String diaryContent = parts[2];
                writeDiary(clientName, "[" + diaryTitle + "] " + diaryContent);
            } else if (input.equals("status")) {
                checkStatus(clientName);
            } else if (input.equals("rank")) {
                showRanking();
            } else if (input.equals("exit")) {
                out.println("Goodbye!");
            } else {
                out.println("Unknown command. Use: login, userinfo, challenges, createChallenge, writeDiary, status, rank, exit");
            }
        }

        private void handleLogin(String input) {
            String[] parts = input.split(" ");
            if (parts.length == 3) {
                String id = parts[1];
                String password = parts[2];

                if (userCredentials.containsKey(id) && userCredentials.get(id).equals(password)) {
                    clientName = id;
                    userDiaries.putIfAbsent(clientName, new HashMap<>());
                    successDaysMap.putIfAbsent(clientName, 0);
                    out.println("success");
                } else {
                    out.println("fail");
                }
            } else {
                out.println("fail");
            }
        }

        private void handleUserInfo(String clientName) {
            try {
                out.println(clientName);
                out.println(getRank(clientName));
                out.println(userChallenges.get(clientName).size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendChallenges(String clientName) {
            try {
                List<Challenge> challenges = userChallenges.getOrDefault(clientName, new ArrayList<>());
                for (Challenge challenge : challenges) {
                    out.println(challenge.getTitle() + "," + challenge.getDescription() + "," +
                            challenge.getStartDate() + "," + challenge.getEndDate() + "," + challenge.getParticipants());
                }
                out.println("end");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handleCreateChallenge(String input) {
            try {
                String[] parts = input.split(",", 5);
                String title = parts[1];
                String startDate = parts[2];
                String endDate = parts[3];
                int participants = Integer.parseInt(parts[4]);

                Challenge newChallenge = new Challenge(title, "User-created challenge.", LocalDate.parse(startDate), LocalDate.parse(endDate), participants);
                userChallenges.get(clientName).add(newChallenge);
                System.out.println("New challenge added for user " + clientName + ": " + newChallenge.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void writeDiary(String clientName, String diaryEntry) {
            int currentDay = getCurrentDay();
            Map<Integer, List<String>> diaryMap = userDiaries.get(clientName);

            diaryMap.putIfAbsent(currentDay, new ArrayList<>());
            diaryMap.get(currentDay).add(diaryEntry);
            System.out.println("New diary added for user " + clientName + ": " + diaryEntry);
            if (diaryMap.get(currentDay).size() == 1) {
                successDaysMap.put(clientName, successDaysMap.get(clientName) + 1);
            }
        }

        private void checkStatus(String clientName) {
            Map<Integer, List<String>> diaryMap = userDiaries.get(clientName);
            out.println("=== Your Diary Status ===");
            for (int day = 1; day <= 7; day++) {
                int count = diaryMap.getOrDefault(day, new ArrayList<>()).size();
                out.println("Day " + day + ": " + count + " diary entries");
            }
            out.println("Total success days: " + successDaysMap.get(clientName));
        }

        private void showRanking() {
            List<Map.Entry<String, Integer>> ranking = new ArrayList<>(successDaysMap.entrySet());
            ranking.sort((a, b) -> b.getValue() - a.getValue());

            out.println("=== Challenge Ranking ===");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : ranking) {
                out.println(rank + ". " + entry.getKey() + " - Total Success Days: " + entry.getValue());
                rank++;
            }
            out.println("end");
        }

        private int getRank(String clientName) {
            List<Map.Entry<String, Integer>> ranking = new ArrayList<>(successDaysMap.entrySet());
            ranking.sort((a, b) -> b.getValue() - a.getValue());
            for (int i = 0; i < ranking.size(); i++) {
                if (ranking.get(i).getKey().equals(clientName)) {
                    return i + 1;
                }
            }
            return -1;
        }

        private int getCurrentDay() {
            return (int) ((System.currentTimeMillis() / (1000 * 60 * 60 * 24)) % 7) + 1;
        }
    }

    // Challenge 클래스
    static class Challenge {
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private int participants;

        public Challenge(String title, String description, LocalDate startDate, LocalDate endDate, int participants) {
            this.title = title;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.participants = participants;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public int getParticipants() {
            return participants;
        }
    }
}
