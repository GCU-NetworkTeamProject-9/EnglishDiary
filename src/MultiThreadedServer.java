import java.io.*;
import java.net.*;
import java.util.*;

public class MultiThreadedServer {
    private static final int PORT = 12345; // 서버 포트 번호
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    private static Map<String, DiaryChallengeServer> challenges = new HashMap<>(); // 챌린지 ID와 서버 매핑
    static List<ChallengeInfo> challengeList = Collections.synchronizedList(new ArrayList<>());

    // 챌린지 리스트를 반환하는 정적 메서드
    public static List<ChallengeInfo> getChallengeList() {
        System.out.println("getChallengeList called. Current size: " + challengeList.size());
        return challengeList; // 방어적 복사
    }

    public static class ChallengeInfo {
        String id;
        String title;
        String description;
        String creator;
        String startDate;
        String endDate;
        List<String> participants;

        public ChallengeInfo(String id, String title, String description, String startDate, String endDate, String creator) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.creator = creator;
            this.startDate = startDate;
            this.endDate = endDate;
            this.participants = new ArrayList<>();
            this.participants.add(creator);
        }

        public String getTitle() {
            return this.title;
        }
        public String getDescription() {
            return this.description;
        }
        public String getStartDate() {
            return this.startDate;
        }
        public String getEndDate() {
            return this.endDate;
        }
        public List<String> getParticipants() {
            return this.participants;
        }
    }

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

    // GUI 업데이트를 위한 옵저버 리스트
    private static final List<ChallengeListObserver> observers = Collections.synchronizedList(new ArrayList<>());

    // 옵저버 인터페이스
    public interface ChallengeListObserver {
        void onChallengeListUpdated(List<ChallengeInfo> challenges);
    }

    public static void addObserver(ChallengeListObserver observer) {
        observers.add(observer);
        System.out.println("Observer added. Total observers: " + observers.size());
        observer.onChallengeListUpdated(challengeList); // 초기 값 전달
    }

    // 챌린지 리스트 업데이트 시 호출할 메서드
    private static void notifyObservers() {
        System.out.println("Notifying observers. Current list size: " + challengeList.size());
        for (ChallengeListObserver observer : observers) {
            observer.onChallengeListUpdated(challengeList);
        }
    }

    // 클라이언트 핸들러 클래스
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

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
                message = in.readLine();
                processCommand(message);
                System.out.println("succeed!");

                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    processCommand(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket.getInetAddress());
            } finally {
                cleanup();
            }
        }

        private void processCommand(String message) {
            if (message.startsWith("create_challenge ")) {
                System.out.println("Create!");
                // 형식: create_challenge title|description|days
                String[] parts = message.substring(16).split("\\|");
                if (parts.length >= 4) {
                    createNewChallenge(parts[0], parts[1], parts[2], parts[3]);
                }
            } else if (message.equals("list_challenges")) {
                sendChallengeList();
            } else if (message.startsWith("join_challenge ")) {
                String challengeId = message.substring(14);
                joinChallenge(challengeId);
            } else if (message.startsWith("Username ")){
                System.out.println("Welcome " + username);
                out.println("Welcome " + username + "--!" + "Commands: [create_challenge, list_challenges, join_challenge, exit]");
            }
        }
        private void createNewChallenge(String title, String description, String startDate, String endDate) {
            String challengeId = UUID.randomUUID().toString();
            ChallengeInfo challengeInfo = new ChallengeInfo(challengeId, title, description, startDate, endDate, username);
            challengeList.add(challengeInfo);

            // 새로운 DiaryChallengeServer 인스턴스 생성
            DiaryChallengeServer newChallenge = new DiaryChallengeServer();
            challenges.put(challengeId, newChallenge);

            // 챌린지 생성 알림을 모든 클라이언트에게 브로드캐스트
            broadcast("NEW_CHALLENGE|" + challengeId + "|" + title + "|" + description + "|" + startDate + "|" + endDate + "|" + username);

//            System.out.println("Challenge created successfully! ID: " + challengeId);
            System.out.println(challengeList.size());
            for(ChallengeInfo challenge : getChallengeList()){
                System.out.println("--");
                System.out.println(challenge.title);
            }
            notifyObservers();
        }

        private void joinChallenge(String challengeId) {
            DiaryChallengeServer challenge = challenges.get(challengeId);
            if (challenge != null) {
                // 해당 챌린지의 참가자 목록에 추가
                challengeList.stream()
                        .filter(c -> c.id.equals(challengeId))
                        .findFirst()
                        .ifPresent(c -> {
                            if (!c.participants.contains(username)) {
                                c.participants.add(username);
                                broadcast("JOIN_CHALLENGE|" + challengeId + "|" + username);
                                out.println("Successfully joined the challenge!");
                            } else {
                                out.println("You are already participating in this challenge.");
                            }
                        });
            } else {
                out.println("Challenge not found.");
            }
        }
        private void sendChallengeList() {
            System.out.println("Sending to Socket");
            System.out.println(challengeList.size());
            out.println("CHALLENGE_LIST_START");
            for (ChallengeInfo challenge : challengeList) {
                out.println(String.format("CHALLENGE|%s|%s|%s|%s|%s|%d",
                        challenge.id,
                        challenge.title,
                        challenge.description,
                        challenge.startDate,
                        challenge.endDate,
                        challenge.creator,
                        challenge.participants.size()));
                System.out.println(String.format("CHALLENGE|%s|%s|%s|%s|%s|%d",
                        challenge.id,
                        challenge.title,
                        challenge.description,
                        challenge.startDate,
                        challenge.endDate,
                        challenge.creator,
                        challenge.participants.size()));
            }
            out.println("CHALLENGE_LIST_END");
            System.out.println("Ended sending");
        }

        private void cleanup() {
            clientHandlers.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
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
