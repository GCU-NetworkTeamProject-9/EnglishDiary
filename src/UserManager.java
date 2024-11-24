import java.io.*;
import java.util.*;

public class UserManager {

    private static final String FILE_PATH = "src/data/user_diary.txt";
    private static final String USER_FILE_PATH = "src/data/user.txt";

    public static void addChallengeToUserDiary(String userNickname, String challengeName, Map<String, String> dailyDiary) throws IOException {

        // 기존 데이터를 저장할 구조체
        List<String> fileContent = new ArrayList<>();
        boolean userToHold = false;

        String holdingUser = null;
        String currentUser = null;
        String currentChallenge = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("User:")) {
                    currentUser = line.split(": ")[1].trim();
                    fileContent.add(line);

                    if (currentUser.equals(userNickname)) {
                        line = reader.readLine();

                        if(line == null) break;
                        if(line.startsWith("    Challenge:")){
                            currentChallenge = line.split(": ")[1].trim();
                            fileContent.add(line);

                            if (currentChallenge.equals(challengeName)) {
                                line = reader.readLine();
                                if(!line.startsWith("User:")){
                                    if (line.startsWith("        Day")) {
                                        fileContent.add(line);
                                    }
                                    for (Map.Entry<String, String> entry : dailyDiary.entrySet()) {
                                        fileContent.add("        " + entry.getKey() + ": " + entry.getValue());
                                    }
                                }else{
                                    userToHold = true;
                                    holdingUser = line;
                                    for (Map.Entry<String, String> entry : dailyDiary.entrySet()) {
                                        fileContent.add("        " + entry.getKey() + ": " + entry.getValue());
                                    }
                                }
                            }
                        }else{
                            userToHold = true;
                            holdingUser = line;
                            fileContent.add("    Challenge: " + challengeName);
                            line = reader.readLine();

                            while (line.startsWith("        Day")) {
                                fileContent.add(line);
                                line = reader.readLine();
                            }
                            for (Map.Entry<String, String> entry : dailyDiary.entrySet()) {
                                fileContent.add("        " + entry.getKey() + ": " + entry.getValue());
                            }
                        }
                        if(userToHold) {
                            fileContent.add(holdingUser);
                            userToHold = false;
                        };
                    }
                    else{ // 목적 user 가 아닌 애들 fileContent에 추가
                        line = reader.readLine();
                        if(line == null) break;
                        if(line.startsWith("    Challenge:")){
                            fileContent.add(line);
                            line = reader.readLine();
                            if(!line.startsWith("User:")){
                                while (line.startsWith("        Day")) {
                                    fileContent.add(line);
                                    line = reader.readLine();
                                }
                            }
                        }
                    }
                }else{
                    line = reader.readLine();
                    if(line == null) break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static Set<String> readUsers() throws IOException {
        Set<String> validUsers = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("User Nickname:")) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) { // 방어적 코드 추가
                        String nickname = parts[1].trim();
                        validUsers.add(nickname);
                        System.out.println("Nickname added: " + nickname);
                    } else {
                        System.out.println("Invalid line format: " + line);
                    }
                }
            }
        }
        return validUsers;
    }

    private static void initUsers() throws IOException {
        Set<String> validUsers = new HashSet<>();
        validUsers = readUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : validUsers) {
                writer.write("User: " + line);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // 새로운 사용자와 챌린지 데이터
        String userNickname = "user5";
        String challengeName = "Challenge 1";
        Map<String, String> dailyDiary = new HashMap<>();
        dailyDiary.put("Day 1", "Playing Soccer");

        // 데이터 추가
        addChallengeToUserDiary(userNickname, challengeName, dailyDiary);
    }
}
