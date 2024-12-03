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

public class ChallengeGUI {

    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        System.out.println("client.ChallengeGUI");
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

            // 메인 패널 생성 (전체 컨테이너)
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(new Color(224, 255, 255));

            // 챌린지 목록 패널
            ChallengePanel challengePanel = new ChallengePanel(challenges);

            // 챌린지 목록을 스크롤 가능하게 설정
            JScrollPane scrollPane = new JScrollPane(challengePanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // 버튼 패널 (챌린지 생성 및 참여 버튼)
            JPanel actionButtonPanel = new JPanel();
            actionButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
            actionButtonPanel.setPreferredSize(new Dimension(540, 80));
            actionButtonPanel.setBackground(new Color(224, 255, 255));

            CustomButton joinChallengeButton = new CustomButton("챌린지 참여");
            CustomButton makeChallengeButton = new CustomButton("새 챌린지 만들기");
            actionButtonPanel.add(joinChallengeButton);
            actionButtonPanel.add(makeChallengeButton);

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

            // 메인 패널에 구성 요소 추가
            mainPanel.add(scrollPane, BorderLayout.CENTER); // 챌린지 목록
            mainPanel.add(actionButtonPanel, BorderLayout.SOUTH); // 챌린지 생성/참여 버튼 및 하단 탭

            // 버튼 이벤트 리스너
            joinChallengeButton.addActionListener(e -> {
                frame.dispose();
                JoinChallengePanel.main(new String[]{}); // JoinChallengeGUI 실행
            });

            makeChallengeButton.addActionListener(e -> {
                frame.dispose();
                MakeChallengeGUI.main(new String[]{}); // MakeChallengeGUI 실행
            });

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
                MyPageGUI.main(new String[]{}); //MyPageGUI 실행
            });

            // 프레임에 메인 패널 추가
            frame.add(mainPanel);
            frame.add(bottomTabPanel, BorderLayout.PAGE_END); // 하단 탭
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

        JLabel titleLabel = new JLabel("챌린지 목록");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(20)); // 여백 추가

        for (Challenge challenge : challenges) {
            JPanel challengeCard = createChallengeCard(challenge);
            challengeCard.setAlignmentX(CENTER_ALIGNMENT);
            add(challengeCard);
            add(Box.createVerticalStrut(10)); // 카드 간 여백 추가
        }
    }

    private JPanel createChallengeCard(Challenge challenge) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        cardPanel.setPreferredSize(new Dimension(480, 150));

        JLabel titleLabel = new JLabel(challenge.getTitle());
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 14));
        titleLabel.setBounds(10, 10, 400, 20);
        cardPanel.add(titleLabel);

        JLabel descriptionLabel = new JLabel("<html>내용: " + challenge.getDescription() + "</html>");
        descriptionLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        descriptionLabel.setBounds(10, 40, 400, 40);
        cardPanel.add(descriptionLabel);

        JLabel startDateLabel = new JLabel("시작일: " + challenge.getStartDate());
        startDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        startDateLabel.setBounds(10, 90, 200, 20);
        cardPanel.add(startDateLabel);

        JLabel endDateLabel = new JLabel("종료일: " + challenge.getEndDate());
        endDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        endDateLabel.setBounds(220, 90, 200, 20);
        cardPanel.add(endDateLabel);

        JLabel participantsLabel = new JLabel("참여 인원: " + challenge.getParticipants() + "명");
        participantsLabel.setFont(new Font("나눔고딕", Font.PLAIN, 12));
        participantsLabel.setBounds(10, 120, 200, 20);
        cardPanel.add(participantsLabel);

        // 클릭 이벤트 추가
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 클릭된 챌린지의 데이터를 DetailChallengeGUI로 전달
                try {
                    new DetailChallengeGUI(challenge,LoginGUI.socket);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return cardPanel;
    }
}

// 챌린지 데이터 클래스
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
