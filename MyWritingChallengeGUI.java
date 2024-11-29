package client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyWritingChallengeGUI {

    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        System.out.println("client.MyWritingChallengeGUI");
        try {
            // 서버 연결
            Socket socket = LoginGUI.socket; // 로그인 GUI에서 이미 연결된 소켓을 사용
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // 일기 데이터 요청
            out.println("diaries");
            List<Diary> diaries = fetchDiaries();

            // GUI 생성
            JFrame frame = new JFrame("MyWritingChallenge");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(540, 960);

            // 메인 패널 생성 (전체 컨테이너)
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(new Color(224, 255, 255));

            // 일기 목록 패널
            MyWritingChallengePanel diaryPanel = new MyWritingChallengePanel(diaries);

            // 일기 목록을 스크롤 가능하게 설정
            JScrollPane scrollPane = new JScrollPane(diaryPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // 하단 탭
            JPanel bottomTabPanel = new JPanel(new GridLayout(1, 3));
            bottomTabPanel.setPreferredSize(new Dimension(540, 60));
            bottomTabPanel.setBackground(new Color(224, 255, 255));

            CustomButton2 challengeMenuButton = new CustomButton2("챌린지");
            CustomButton2 rankingMenuButton = new CustomButton2("랭킹");
            CustomButton2 mypageMenuButton = new CustomButton2("마이페이지");

            bottomTabPanel.add(challengeMenuButton);
            bottomTabPanel.add(rankingMenuButton);
            bottomTabPanel.add(mypageMenuButton);

            // 버튼 이벤트 리스너
            rankingMenuButton.addActionListener(e -> {
                frame.dispose();
                RankingGUI.main(new String[]{}); // RankingGUI 실행
            });

            challengeMenuButton.addActionListener(e -> {
                frame.dispose();
                ChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
            });

            mypageMenuButton.addActionListener(e -> {
                frame.dispose();
                MyPageGUI.main(new String[]{}); // MyPageGUI 실행
            });

            // 메인 패널에 구성 요소 추가
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(bottomTabPanel, BorderLayout.PAGE_END); // 하단 탭

            // 프레임에 메인 패널 추가
            frame.add(mainPanel);
            frame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static List<Diary> fetchDiaries() throws IOException {
        List<Diary> diaries = new ArrayList<>();
        String line;
        while (!(line = in.readLine()).equals("end")) {
            String[] data = line.split(",", 4); // username, title, date, content
            if (data.length == 4) { // 데이터 필드 검증
                try {
                    diaries.add(new Diary(
                            data[0], // username
                            data[1], // title
                            LocalDate.parse(data[2]), // 작성 날짜
                            data[3]  // 내용
                    ));
                } catch (Exception e) {
                    System.err.println("Invalid data format: " + line);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid data received: " + line);
            }
        }
        return diaries;
    }

}

// 내가 작성한 일기 화면 클래스
class MyWritingChallengePanel extends JPanel {

    public MyWritingChallengePanel(List<Diary> diaries) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        JLabel titleLabel = new JLabel("내가 작성한 일기");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(20)); // 여백 추가

        for (Diary diary : diaries) {
            JPanel diaryCard = createDiaryCard(diary);
            diaryCard.setAlignmentX(CENTER_ALIGNMENT);
            add(diaryCard);
            add(Box.createVerticalStrut(10)); // 카드 간 여백 추가
        }
    }

    private JPanel createDiaryCard(Diary diary) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        cardPanel.setPreferredSize(new Dimension(480, 150));

        JLabel titleLabel = new JLabel(diary.getTitle());
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 14));
        titleLabel.setBounds(10, 10, 400, 20);
        cardPanel.add(titleLabel);

        JLabel dateLabel = new JLabel("작성 날짜: " + diary.getDate());
        dateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        dateLabel.setBounds(10, 40, 400, 20);
        cardPanel.add(dateLabel);

        JLabel contentLabel = new JLabel("<html>내용: " + diary.getContent() + "</html>");
        contentLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        contentLabel.setBounds(10, 70, 460, 60);
        cardPanel.add(contentLabel);

        // 클릭 이벤트 추가 (추가 작업 필요 시 Detail 화면 이동 등 구현 가능)
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(cardPanel, 
                    "일기 제목: " + diary.getTitle() + "\n" +
                    "작성 날짜: " + diary.getDate() + "\n" +
                    "내용: " + diary.getContent(), 
                    "일기 상세 보기", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return cardPanel;
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

