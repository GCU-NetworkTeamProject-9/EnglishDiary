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

public class ChallengeGUI {

    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        System.out.println("ChallengeGUI");
        try {
            // 서버 연결
            Socket socket = LoginGUI.socket; // 로그인 GUI에서 이미 연결된 소켓을 사용
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // 챌린지 데이터 요청
            out.println("challenges");
            List<Challenge> challenges = fetchChallenges();

            // GUI 생성
            JFrame frame = new JFrame("Challenge");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(540, 960);

            ChallengePanel challengePanel = new ChallengePanel(challenges);
            JScrollPane scrollPane = new JScrollPane(challengePanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            frame.add(scrollPane);
            frame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static List<Challenge> fetchChallenges() throws IOException {
        List<Challenge> challenges = new ArrayList<>();
        String line;
        while (!(line = in.readLine()).equals("end")) {
            String[] data = line.split(",");
            if (data.length == 5) { // 데이터 필드 검증
                try {
                    challenges.add(new Challenge(
                            data[0], // Title
                            data[1], // Description
                            LocalDate.parse(data[2]), // Start Date
                            LocalDate.parse(data[3]), // End Date
                            Integer.parseInt(data[4]) // Participants
                    ));
                } catch (Exception e) {
                    System.err.println("Invalid data format: " + line);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid data received: " + line);
            }
        }
        return challenges;
    }
}

// 챌린지 화면 클래스
class ChallengePanel extends JPanel {

    public ChallengePanel(List<Challenge> challenges) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // "나의 챌린지" 레이블
        JLabel titleLabel = new JLabel("나의 챌린지");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(20)); // 여백 추가

        // 챌린지 목록 추가
        for (Challenge challenge : challenges) {
            JPanel challengeCard = createChallengeCard(challenge);
            challengeCard.setAlignmentX(CENTER_ALIGNMENT);
            add(challengeCard);
            add(Box.createVerticalStrut(10)); // 카드 간 여백 추가
        }

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(224, 255, 255));

        CustomButton joinChallengeButton = new CustomButton("챌린지 참여");
        CustomButton makeChallengeButton = new CustomButton("새 챌린지 만들기");
        buttonPanel.add(joinChallengeButton);
        buttonPanel.add(makeChallengeButton);

        add(buttonPanel);

        // 버튼 이벤트
        joinChallengeButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (parentFrame != null) {
                parentFrame.dispose();
            }
            JoinChallengeGUI.main(new String[]{}); // JoinChallengeGUI 실행
        });

        makeChallengeButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (parentFrame != null) {
                parentFrame.dispose();
            }
            MakeChallengeGUI.main(new String[]{}); // MakeChallengeGUI 실행
        });
    }

    // 개별 챌린지 카드를 생성하는 메서드
    private JPanel createChallengeCard(Challenge challenge) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // 테두리
        cardPanel.setPreferredSize(new Dimension(480, 150));

        // 제목
        JLabel titleLabel = new JLabel(challenge.getTitle());
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 14));
        titleLabel.setBounds(10, 10, 400, 20);
        cardPanel.add(titleLabel);

        // 내용
        JLabel descriptionLabel = new JLabel("<html>내용: " + challenge.getDescription() + "</html>");
        descriptionLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        descriptionLabel.setBounds(10, 40, 400, 40);
        cardPanel.add(descriptionLabel);

        // 시작일
        JLabel startDateLabel = new JLabel("시작일: " + challenge.getStartDate());
        startDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        startDateLabel.setBounds(10, 90, 200, 20);
        cardPanel.add(startDateLabel);

        // 종료일
        JLabel endDateLabel = new JLabel("종료일: " + challenge.getEndDate());
        endDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        endDateLabel.setBounds(220, 90, 200, 20);
        cardPanel.add(endDateLabel);

        // 참여 인원
        JLabel participantsLabel = new JLabel("참여 인원: " + challenge.getParticipants() + "명");
        participantsLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        participantsLabel.setBounds(10, 120, 200, 20);
        cardPanel.add(participantsLabel);

        return cardPanel;
    }
}

// Challenge 클래스
class Challenge {
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
