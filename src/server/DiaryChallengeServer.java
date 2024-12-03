package server;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.*;

public class DiaryChallengeServer {
    private static final int PORT = 12345;
    private static Map<String, String> userCredentials = new HashMap<>();
    private static Map<String, Map<Challenge, Map<Integer, List<DiaryEntry>>>> userDiaries = new HashMap<>();
    private static Map<String, Integer> successDaysMap = new HashMap<>();
    private static Map<String, List<Challenge>> userChallenges = new HashMap<>();
    private static List<Challenge> allChallenges = new ArrayList<>();

    public static void main(String[] args) {
        // 기본 사용자 계정 추가
        userCredentials.put("admin", "1234");
        // 사용자 10명 추가
        for (int i = 1; i <= 10; i++) {
            userCredentials.put("user" + i, "1234");
        }

        allChallenges.add(new Challenge("Daily English Diary for Beginners", "Write a daily English diary.", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30), 3));
        allChallenges.add(new Challenge("Daily Reading Challenge", "Read 10 pages of a book daily.", LocalDate.of(2024, 10, 15), LocalDate.of(2024, 12, 15), 4));
        allChallenges.add(new Challenge("Exercise Challenge", "Exercise 30 minutes daily.", LocalDate.of(2024, 11, 5), LocalDate.of(2024, 12, 5), 3));


        // 기본 챌린지 추가
        List<Challenge> defaultChallenges = List.of(
                new Challenge("Daily English Diary for Beginners", "Write a daily English diary.", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30), 3),
                new Challenge("Daily Reading Challenge", "Read 10 pages of a book daily.", LocalDate.of(2024, 10, 15), LocalDate.of(2024, 12, 15), 4),
                new Challenge("Exercise Challenge", "Exercise 30 minutes daily.", LocalDate.of(2024, 11, 5), LocalDate.of(2024, 12, 5), 3)
        );

        // 사용자 초기화
        for (String user : userCredentials.keySet()) {
            userDiaries.put(user, new HashMap<>());
            userChallenges.put(user, new ArrayList<>(allChallenges)); // 사용자별 챌린지 리스트 생성
            successDaysMap.put(user, 0);
        }

        // 더미 데이터 생성
//        Random random = new Random();
//        for (String user : userCredentials.keySet()) {
//            Map<Challenge, Map<Integer, List<DiaryEntry>>> diaries = new HashMap<>();
//            for (Challenge challenge : allChallenges) {
//                Map<Integer, List<DiaryEntry>> dailyEntries = new HashMap<>();
//                int totalDays = random.nextInt(5) + 1; // 1~5일 랜덤 작성
//                for (int day = 1; day <= totalDays; day++) {
//                    List<DiaryEntry> entries = new ArrayList<>();
//                    int entriesPerDay = random.nextInt(3) + 1; // 하루 1~3개 랜덤 작성
//                    for (int i = 1; i <= entriesPerDay; i++) {
//                        entries.add(new DiaryEntry("Day " + day + " Entry " + i, "This is a dummy entry for " + challenge.getTitle() + "."));
//                    }
//                    dailyEntries.put(day, entries);
//                }
//                diaries.put(challenge, dailyEntries);
//            }
//            userDiaries.put(user, diaries);
//            successDaysMap.put(user, random.nextInt(10) + 1); // 1~10일 랜덤 성공
//        }
//        generateDummyData();

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

    private static void generateDummyData() {
        Random random = new Random();

        for (Challenge challenge : allChallenges) {
            // 해당 챌린지의 참가자 수만큼 사용자 선택
            List<String> participants = new ArrayList<>(userCredentials.keySet());
            Collections.shuffle(participants);
            participants = participants.subList(0, challenge.getParticipants());

            for (String user : participants) {
                Map<Challenge, Map<Integer, List<DiaryEntry>>> diaries = userDiaries.get(user);
                Map<Integer, List<DiaryEntry>> dailyEntries = new HashMap<>();

                int totalDays = random.nextInt(5) + 1; // 1~5일 랜덤 작성
                for (int day = 1; day <= totalDays; day++) {
                    List<DiaryEntry> entries = new ArrayList<>();
                    int entriesPerDay = random.nextInt(3) + 1; // 하루 1~3개 랜덤 작성
                    for (int i = 1; i <= entriesPerDay; i++) {
                        LocalDate writtenDate = LocalDate.now().minusDays(random.nextInt(30)); // 랜덤 작성 날짜
                        entries.add(new DiaryEntry("Day " + day + " Entry " + i, "This is a dummy entry for " + challenge.getTitle() + ".", writtenDate));
                    }
                    dailyEntries.put(day, entries);
                }
                diaries.put(challenge, dailyEntries);
                successDaysMap.put(user, successDaysMap.get(user) + totalDays); // 성공 일수 갱신
            }
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
            }
//            else if (input.startsWith("getDiariesByChallenge")) {
//                String[] parts = input.split(",", 2);
//                String challengeTitle = parts[1];
//                sendDiariesByChallenge(clientName, challengeTitle);
//            }
            else if (input.startsWith("getDiariesByChallenge")) {
                String[] parts = input.split(",", 2);
                String challengeTitle = parts[1];
                sendDiariesByChallenge(challengeTitle);
            }
//            else if (input.equals("challenges")) {
//                System.out.println("challenges");
//                sendChallenges(clientName);
//            }
            else if (input.equals("challenges")) {
                System.out.println("get challenges");
                sendAllChallenges();
            }
            else if (input.startsWith("createChallenge")) {
                System.out.println("createChallenge");
                handleCreateChallenge(input);
            }
            else if (input.equals("diaries")) {
                System.out.println("getUser's diaries");
                handleDiaries(clientName);
            }
            else if (input.startsWith("writeDiary")) {
                System.out.println("writeDiary");
                String[] parts = input.split(",", 4);
                String diaryTitle = parts[1];
                String diaryContent = parts[2];
                String challengeTitle = parts[3];
                writeDiary(clientName, diaryTitle, diaryContent, challengeTitle);
            } else if (input.equals("rank")) {
                System.out.println("show ranking");
                showRanking();
            } else if (input.equals("exit")) {
                out.println("Goodbye!");
            } else {
                out.println("Unknown command.");
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
                    out.println("success");
                } else {
                    out.println("fail");
                }
            } else {
                out.println("fail");
            }
        }

//        private void sendDiariesByChallenge(String clientName, String challengeTitle) {
//            Map<Challenge, Map<Integer, List<DiaryEntry>>> diaryMap = userDiaries.getOrDefault(clientName, new HashMap<>());
//            boolean found = false;
//
//            for (Map.Entry<Challenge, Map<Integer, List<DiaryEntry>>> entry : diaryMap.entrySet()) {
//                Challenge challenge = entry.getKey();
//                if (challenge.getTitle().equals(challengeTitle)) {
//                    for (Map.Entry<Integer, List<DiaryEntry>> dayEntry : entry.getValue().entrySet()) {
//                        int day = dayEntry.getKey();
//                        for (DiaryEntry diary : dayEntry.getValue()) {
//                            out.println("Day " + day + ": " + diary);
//                        }
//                    }
//                    found = true;
//                }
//            }
//            if (!found) {
//                out.println("No diaries found for challenge: " + challengeTitle);
//            }
//            out.println("end");
//        }
private void sendDiariesByChallenge(String challengeTitle) {
    boolean found = false;

    for (Map.Entry<String, Map<Challenge, Map<Integer, List<DiaryEntry>>>> userEntry : userDiaries.entrySet()) {
        String user = userEntry.getKey();
        for (Map.Entry<Challenge, Map<Integer, List<DiaryEntry>>> challengeEntry : userEntry.getValue().entrySet()) {
            Challenge challenge = challengeEntry.getKey();
            if (challenge.getTitle().equals(challengeTitle)) {
                for (Map.Entry<Integer, List<DiaryEntry>> dayEntry : challengeEntry.getValue().entrySet()) {
                    int day = dayEntry.getKey();
                    for (DiaryEntry diary : dayEntry.getValue()) {
                        out.println("User: " + user + ", Day " + diary.writtenDate + ", " + diary.content);
                    }
                }
                found = true;
            }
        }
    }

    if (!found) {
        out.println("No diaries found for challenge: " + challengeTitle);
    }
    out.println("end");
}


//        private void sendChallenges(String clientName) {
//            List<Challenge> challenges = userChallenges.getOrDefault(clientName, new ArrayList<>());
//            for (Challenge challenge : challenges) {
//                out.println(challenge.getTitle() + "," + challenge.getDescription() + "," +
//                        challenge.getStartDate() + "," + challenge.getEndDate() + "," + challenge.getParticipants());
//            }
//            out.println("end");
//        }

        private void sendAllChallenges() {
            for (Challenge challenge : allChallenges) {
                out.println(challenge.getTitle() + "," + challenge.getDescription() + "," +
                        challenge.getStartDate() + "," + challenge.getEndDate() + "," + challenge.getParticipants());
            }
            out.println("end");
        }

        private void handleCreateChallenge(String input) {
            try {
                String[] parts = input.split(",", 5);
                String title = parts[1];
                String startDate = parts[2];
                String endDate = parts[3];
                int participants = Integer.parseInt(parts[4]);

                // 새로운 챌린지 생성
                Challenge newChallenge = new Challenge(title, "User-created challenge.", LocalDate.parse(startDate), LocalDate.parse(endDate), participants);

                // 현재 사용자의 챌린지에 추가
                userChallenges.get(clientName).add(newChallenge);

                // 전체 챌린지 목록 업데이트
                allChallenges.add(newChallenge);

                System.out.println("New challenge added for user " + clientName + ": " + newChallenge.getTitle());
                out.println("Challenge created successfully: " + title);
            } catch (Exception e) {
                e.printStackTrace();
                out.println("Failed to create challenge.");
            }
        }


        private void writeDiary(String clientName, String diaryTitle, String diaryContent, String challengeTitle) {
            Challenge selectedChallenge = null;
            for (Challenge challenge : userChallenges.get(clientName)) {
                if (challenge.getTitle().equals(challengeTitle)) {
                    selectedChallenge = challenge;
                    break;
                }
            }
            if (selectedChallenge == null) {
                out.println("Challenge not found: " + challengeTitle);
                return;
            }

            int currentDay = getCurrentDay();
            LocalDate today = LocalDate.now(); // 작성 날짜
            Map<Challenge, Map<Integer, List<DiaryEntry>>> diaryMap = userDiaries.get(clientName);
            diaryMap.putIfAbsent(selectedChallenge, new HashMap<>());
            Map<Integer, List<DiaryEntry>> challengeDiaries = diaryMap.get(selectedChallenge);
            challengeDiaries.putIfAbsent(currentDay, new ArrayList<>());
            challengeDiaries.get(currentDay).add(new DiaryEntry(diaryTitle, diaryContent, today)); // 작성 날짜 추가

            successDaysMap.put(clientName, successDaysMap.get(clientName) + 1);
            System.out.println("New diary added for user " + clientName + " under challenge " + challengeTitle);
        }

        private void handleDiaries(String clientName) {
            Map<Challenge, Map<Integer, List<DiaryEntry>>> diaryMap = userDiaries.getOrDefault(clientName, new HashMap<>());
            boolean hasDiaries = false;

            for (Map.Entry<Challenge, Map<Integer, List<DiaryEntry>>> challengeEntry : diaryMap.entrySet()) {
                Challenge challenge = challengeEntry.getKey();
                for (Map.Entry<Integer, List<DiaryEntry>> dayEntry : challengeEntry.getValue().entrySet()) {
                    int day = dayEntry.getKey();
                    for (DiaryEntry diary : dayEntry.getValue()) {
                        out.println("챌린지: " + challenge.getTitle() + ", 작성 날짜: " + diary.getWrittenDate() + ", " + diary);
                        hasDiaries = true;
                    }
                }
            }

            if (!hasDiaries) {
                out.println("No diaries found for user: " + clientName);
            }
            out.println("end");
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

    static class DiaryEntry {
        private String title;
        private String content;
        private LocalDate writtenDate; // 작성 날짜 추가

        public DiaryEntry(String title, String content, LocalDate writtenDate) {
            this.title = title;
            this.content = content;
            this.writtenDate = writtenDate;
        }

        public LocalDate getWrittenDate() {
            return writtenDate;
        }

        @Override
        public String toString() {
            return "작성 날짜: " + writtenDate + ", " + title + ": " + content;
        }
    }
}
