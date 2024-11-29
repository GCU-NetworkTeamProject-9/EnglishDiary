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
        
        // 기본 일기 추가
        List<Diary> defaultDiaries=List.of(
        		new Diary("admin","First snow day",LocalDate.of(2024, 11, 28),"Today I see snow. So I make a snowman"),
        		new Diary("admin","Freezing",LocalDate.of(2024, 11, 29),"I'm freezing. I become a snowman"),
        		new Diary("admin","Hi",LocalDate.of(2024, 11, 30),"Hi my name is China My name is Q")
        );

     // 사용자별 기본 챌린지와 일기 초기화
        for (String user : userCredentials.keySet()) {
            userChallenges.put(user, new ArrayList<>(defaultChallenges));

            // 사용자별 기본 일기 초기화
            Map<Integer, List<String>> defaultUserDiaryMap = new HashMap<>();
            int day = 1; // 기본적으로 첫 번째 일기를 첫 번째 날로 저장
            for (Diary diary : defaultDiaries) {
                defaultUserDiaryMap.putIfAbsent(day, new ArrayList<>());
                defaultUserDiaryMap.get(day).add("[" + diary.getTitle() + "] " + diary.getContent());
                day++;
            }
            userDiaries.put(user, defaultUserDiaryMap);
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
            } else if (input.equals("challenges")) {
                sendChallenges(clientName);
            } else if (input.startsWith("createChallenge")) {
                handleCreateChallenge(input);
            } else if (input.equals("diaries")) {
                sendDiaries(clientName);
            }else if (input.startsWith("writeDiary")) {
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
        
        private void sendDiaries(String clientName) {
            try {
                Map<Integer, List<String>> diaryMap = userDiaries.getOrDefault(clientName, new HashMap<>());

                // Diary 객체를 생성하여 클라이언트로 전송
                for (Map.Entry<Integer, List<String>> entry : diaryMap.entrySet()) {
                    int day = entry.getKey();
                    for (String diaryEntry : entry.getValue()) {
                        try {
                            // 문자열을 파싱하여 Diary 객체로 변환
                            String[] parts = diaryEntry.split("] ", 2);
                            if (parts.length == 2) {
                                String title = parts[0].substring(1); // 제목 추출
                                String content = parts[1]; // 내용 추출
                                LocalDate date = LocalDate.now().minusDays(7 - day); // 날짜 계산

                                // 사용자명, 제목, 날짜, 내용을 포함하여 클라이언트로 전송
                                out.println(clientName + "," + title + "," + date + "," + content);
                            }
                        } catch (Exception e) {
                            System.err.println("Invalid diary entry: " + diaryEntry);
                            e.printStackTrace();
                        }
                    }
                }

                // 데이터 전송 완료 표시
                out.println("end");
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

//일기 데이터 클래스
class Diary {
private String username;     // 작성자명
private String title;        // 일기 제목
private LocalDate date;      // 작성 날짜
private String content;      // 일기 내용

// 생성자
public Diary(String username, String title, LocalDate date, String content) {
   this.username = username;
   this.title = title;
   this.date = date;
   this.content = content;
}

// Getter 메서드
public String getUsername() {
   return username;
}

public String getTitle() {
   return title;
}

public LocalDate getDate() {
   return date;
}

public String getContent() {
   return content;
}

@Override
public String toString() {
   return "Diary{" +
           "username='" + username + '\'' +
           ", title='" + title + '\'' +
           ", date=" + date +
           ", content='" + content + '\'' +
           '}';
}
}

