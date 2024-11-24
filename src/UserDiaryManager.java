import java.io.*;
import java.util.*;

public class UserDiaryManager {

    private static final String FILE_PATH = "src/data/user_diary.txt";

    public static void addChallengeToUserDiary(String userNickname, String challengeName, Map<String, String> dailyDiaries) {
        List<String> fileContent = new ArrayList<>();
        boolean userFound = false;
        boolean challengeFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            String currentUser = null;
            String currentChallenge = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("User:")) {
                    currentUser = line.split(": ")[1].trim();
                    fileContent.add(line);

                    if (currentUser.equals(userNickname)) {
                        userFound = true;
                    }else{
                        userFound = false;
                    }
                } else if (line.startsWith("    Challenge:") && currentUser != null) {
                    currentChallenge = line.split(": ")[1].trim();
                    fileContent.add(line);

                    if (userFound && currentChallenge.equals(challengeName)) {
                        challengeFound = true;
                    }
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: Append new data if user or challenge was not found
        if (!userFound) {
            fileContent.add("User: " + userNickname);
        }

        if (!challengeFound) {
            fileContent.add("    Challenge: " + challengeName);
        }

        // Add new daily diaries
        for (Map.Entry<String, String> entry : dailyDiaries.entrySet()) {
            fileContent.add("        " + entry.getKey() + ": " + entry.getValue());
        }

        // Step 3: Write back the updated file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 새로운 사용자와 챌린지 데이터
        String userNickname = "user3";
        String challengeName = "Challenge 3";
        Map<String, String> dailyDiaries = new LinkedHashMap<>();
        dailyDiaries.put("Day 1", "Completed the challenge with a morning walk.");
        dailyDiaries.put("Day 2", "Drank 2L of water after meals.");
        dailyDiaries.put("Day 3", "Missed the goal due to a busy schedule.");

        // 데이터 추가
        addChallengeToUserDiary(userNickname, challengeName, dailyDiaries);
    }
}
