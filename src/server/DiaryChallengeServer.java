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
        userCredentials.put("user1", "1234");
        userCredentials.put("user2", "1234");
        userCredentials.put("user3", "1234");
        userCredentials.put("user4", "1234");
        userCredentials.put("user5", "1234");
        userCredentials.put("user6", "1234");
        userCredentials.put("user7", "1234");
        userCredentials.put("user8", "1234");
        userCredentials.put("user9", "1234");
        userCredentials.put("user10", "1234");
        userCredentials.put("user11", "1234");

        // 기본 챌린지 추가
        List<Challenge> defaultChallenges = List.of(
                new Challenge("Daily English Diary for Beginners", "Write a daily English diary.", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30), 12),
                new Challenge("Daily Reading Challenge", "Read 10 pages of a book daily.", LocalDate.of(2024, 10, 15), LocalDate.of(2024, 12, 15), 20),
                new Challenge("Exercise Challenge", "Exercise 30 minutes daily.", LocalDate.of(2024, 11, 5), LocalDate.of(2024, 12, 5), 15)
        );

        for (String user : userCredentials.keySet()) {
            userChallenges.put(user, new ArrayList<>(defaultChallenges));
            userDiaries.put(user, new HashMap<>()); // 초기화
        }

        for (String user : userCredentials.keySet()) {
            userChallenges.put(user, new ArrayList<>(defaultChallenges));
            userDiaries.put(user, new HashMap<>()); // 초기화
            successDaysMap.put(user, 0); // 성공 일수 초기화
        }

//        // 더미 다이어리 데이터 추가
//        Map<Integer, List<String>> user1Diaries = new HashMap<>();
//        user1Diaries.put(1, List.of("[Day 1] Studied English vocabulary.", "[Day 1] Practiced speaking."));
//        user1Diaries.put(2, List.of("[Day 2] Watched an English movie.", "[Day 2] Took notes on phrases."));
//        userDiaries.put("user1", user1Diaries);
//
//        Map<Integer, List<String>> user2Diaries = new HashMap<>();
//        user2Diaries.put(1, List.of("[Day 1] Read 10 pages of a novel.", "[Day 1] Summarized the story."));
//        user2Diaries.put(3, List.of("[Day 3] Exercised for 30 minutes.", "[Day 3] Did yoga."));
//        userDiaries.put("user2", user2Diaries);

        // 성공 횟수에 따른 일기 데이터 추가
        userDiaries.put("user1", Map.of(
                1, List.of("[Day 1] Studied English vocabulary.", "[Day 1] Practiced speaking."),
                2, List.of("[Day 2] Watched an English movie.", "[Day 2] Took notes on phrases."),
                3, List.of("[Day 3] Learned new grammar rules.", "[Day 3] Wrote example sentences."),
                4, List.of("[Day 4] Spoke with a friend in English.", "[Day 4] Practiced pronunciation."),
                5, List.of("[Day 5] Completed a vocabulary quiz.", "[Day 5] Reviewed mistakes.")
        ));

        userDiaries.put("user2", Map.of(
                1, List.of("[Day 1] Read 10 pages of a novel.", "[Day 1] Summarized the story."),
                2, List.of("[Day 2] Explored a new genre.", "[Day 2] Wrote a book review."),
                3, List.of("[Day 3] Exercised for 30 minutes.", "[Day 3] Did yoga.")
        ));

        userDiaries.put("user3", new HashMap<>()); // 성공 횟수 없음, 데이터 비어 있음

        userDiaries.put("user4", Map.of(
                1, List.of("[Day 1] Worked on project documentation.", "[Day 1] Collaborated with teammates."),
                2, List.of("[Day 2] Planned new milestones.", "[Day 2] Completed coding tasks."),
                3, List.of("[Day 3] Reviewed code for quality.", "[Day 3] Fixed bugs."),
                4, List.of("[Day 4] Held a team meeting.", "[Day 4] Set goals for the week."),
                5, List.of("[Day 5] Learned new programming concepts.", "[Day 5] Applied them to the project."),
                6, List.of("[Day 6] Completed a technical article.", "[Day 6] Shared knowledge with peers."),
                7, List.of("[Day 7] Improved debugging skills.", "[Day 7] Resolved a major issue."),
                8, List.of("[Day 8] Enhanced system performance.", "[Day 8] Documented results."),
                9, List.of("[Day 9] Practiced algorithm problems.", "[Day 9] Participated in a hackathon."),
                10, List.of("[Day 10] Successfully deployed the app.", "[Day 10] Received user feedback.")
        ));

        userDiaries.put("user5", Map.of(
                1, List.of("[Day 1] Attended a motivational seminar.", "[Day 1] Took detailed notes.")
        ));

        userDiaries.put("user6", Map.of(
                1, List.of("[Day 1] Practiced meditation for 15 minutes.", "[Day 1] Recorded reflections."),
                2, List.of("[Day 2] Explored mindfulness techniques.", "[Day 2] Applied them in daily life.")
        ));

        userDiaries.put("user7", Map.of(
                1, List.of("[Day 1] Explored a new hiking trail.", "[Day 1] Enjoyed nature."),
                2, List.of("[Day 2] Cooked a healthy meal.", "[Day 2] Experimented with new recipes."),
                3, List.of("[Day 3] Researched investment strategies.", "[Day 3] Created a financial plan."),
                4, List.of("[Day 4] Helped a friend with a task.", "[Day 4] Strengthened relationships."),
                5, List.of("[Day 5] Learned about sustainable living.", "[Day 5] Implemented changes."),
                6, List.of("[Day 6] Organized personal workspace.", "[Day 6] Boosted productivity."),
                7, List.of("[Day 7] Completed a workout challenge.", "[Day 7] Achieved personal goals.")
        ));

        userDiaries.put("user8", Map.of(
                1, List.of("[Day 1] Explored advanced coding topics.", "[Day 1] Built a small application."),
                2, List.of("[Day 2] Researched design patterns.", "[Day 2] Implemented a singleton pattern."),
                3, List.of("[Day 3] Practiced competitive programming.", "[Day 3] Solved 5 algorithm problems."),
                4, List.of("[Day 4] Joined a community workshop.", "[Day 4] Contributed to open-source."),
                5, List.of("[Day 5] Learned about cloud services.", "[Day 5] Deployed a simple web app."),
                6, List.of("[Day 6] Explored API development.", "[Day 6] Created RESTful APIs."),
                7, List.of("[Day 7] Reviewed Java multithreading.", "[Day 7] Wrote efficient concurrent code."),
                8, List.of("[Day 8] Learned new front-end frameworks.", "[Day 8] Built a responsive webpage."),
                9, List.of("[Day 9] Optimized database queries.", "[Day 9] Reduced response times.")
        ));


        // 성공 일수 데이터 초기화
        successDaysMap.put("user1", 5); // User1은 5일간 다이어리 성공
        successDaysMap.put("user2", 3); // User2는 3일간 다이어리 성공
        successDaysMap.put("user3", 0); // User3은 아직 성공 일수가 없음
        successDaysMap.put("user4",10);
        successDaysMap.put("user5",1);
        successDaysMap.put("user6",2);
        successDaysMap.put("user7",7);
        successDaysMap.put("user8",9);

        // 사용자 별 추가 챌린지
        userChallenges.get("user3").add(new Challenge(
                "Healthy Eating Challenge", "Eat healthy meals daily.", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1), 8
        ));

        System.out.println("Server initialized with dummy data!");

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
                System.out.println("Client disconnected.");
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
            } else if (input.equals("diaries")) {
                handleDiaries(clientName);
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
                out.println("Unknown command. Use: login, userinfo, diaries, challenges, createChallenge, writeDiary, status, rank, exit");
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

        private void handleDiaries(String clientName) {
            Map<Integer, List<String>> diaryMap = userDiaries.getOrDefault(clientName, new HashMap<>());
            for (Map.Entry<Integer, List<String>> entry : diaryMap.entrySet()) {
                int day = entry.getKey();
                for (String diary : entry.getValue()) {
                    out.println(clientName + ",Day " + day + "," + LocalDate.now() + "," + diary);
                }
            }
            out.println("end");
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

            out.println("startRanking");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : ranking) {
                out.println(rank + "," + entry.getKey() + "," + entry.getValue());
                rank++;
            }
            out.println("endRanking");
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
