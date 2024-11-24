import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserManager {

    private static final String FILE_PATH = "src/data/user.txt";

    public static void addChallengeToUserDiary(String userNickname, String challengeName, Map<String, String> dailyDiaries) {

        // 기존 데이터를 저장할 구조체
        Map<String, Map<String, List<String>>> userData = new LinkedHashMap<>();

        // Step 1: 파일 읽기 및 데이터 로드
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            String currentUser = null;
            String currentChallenge = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("User:")) {
                    currentUser = line.split(": ")[1];
                    userData.putIfAbsent(currentUser, new LinkedHashMap<>());
                } else if (line.startsWith("Challenge:") && currentUser != null) {
                    currentChallenge = line.split(": ")[1];
                    userData.get(currentUser).putIfAbsent(currentChallenge, new ArrayList<>());
                } else if (line.startsWith("Day") && currentUser != null && currentChallenge != null) {
                    userData.get(currentUser).get(currentChallenge).add(line);
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 새로 생성
            System.out.println("File not found, creating a new one.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: 데이터 수정 또는 추가
        userData.putIfAbsent(userNickname, new LinkedHashMap<>()); // userNickname 존재 x -> key : userNickname, value는 하위 챌린지 생성을 위한 LinkedHashMap으로
        Map<String, List<String>> userChallenges = userData.get(userNickname); //userNickname의 Challenge 정보를 가져옴
        userChallenges.putIfAbsent(challengeName, new ArrayList<>()); //만약 해당 챌린지에 참가하지 않았다면 새 챌린지를 추가함

        // 기존 Challenge에 일일 일기 추가
        for (Map.Entry<String, String> entry : dailyDiaries.entrySet()) {
            String day = entry.getKey();
            String diary = entry.getValue();
            userChallenges.get(challengeName).add("        " + day + ": " + diary);
        }

        // Step 3: 파일 쓰기
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, Map<String, List<String>>> userEntry : userData.entrySet()) {
                writer.write("User: " + userEntry.getKey());
                writer.newLine();
                for (Map.Entry<String, List<String>> challengeEntry : userEntry.getValue().entrySet()) {
                    writer.write("    Challenge: " + challengeEntry.getKey());
                    writer.newLine();
                    for (String diaryEntry : challengeEntry.getValue()) {
                        writer.write(diaryEntry);
                        writer.newLine();
                    }
                }
                writer.newLine(); // 사용자 간 간격
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 테스트를 위한 main 메서드
    public static void main(String[] args) {
        // 새로운 사용자와 챌린지 데이터
        String userNickname = "user5";
        String challengeName = "Challenge 3";
        Map<String, String> dailyDiaries = new LinkedHashMap<>();
        dailyDiaries.put("Day 1", "Completed the challenge with a morning walk.");
        dailyDiaries.put("Day 2", "Drank 2L of water after meals.");
        dailyDiaries.put("Day 3", "Missed the goal due to a busy schedule.");

        // 데이터 추가
        addChallengeToUserDiary(userNickname, challengeName, dailyDiaries);
    }
}
