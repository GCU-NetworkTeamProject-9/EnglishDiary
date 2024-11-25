import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChallengeGUI {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 12345;

    private static JFrame mainFrame;
    private static ChallengePanel mainPanel;

    static List<MultiThreadedServer.ChallengeInfo> challengeList1 = null;


    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            MultiThreadedServer.addObserver(challenges -> {
                challengeList1 = challenges; // 옵저버에서 최신 데이터 동기화
                System.out.println("Observer triggered in GUI. List size: " + challengeList1.size());
                SwingUtilities.invokeLater(ChallengeGUI::refreshGUI);
            });


            // 메인 프레임 생성
            mainFrame = new JFrame("Challenge");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(540, 960);

            // 초기 챌린지 패널 생성
            updateChallengePanel();
            mainFrame.setVisible(true);

//        challenges.add(new Challenge("Daily English Diary for Beginners", "Solve one coding problem daily.",
//                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30), 12));
//        challenges.add(new Challenge("Daily English Book Reading", "Exercise for 30 minutes daily.",
//                LocalDate.of(2024, 10, 15), LocalDate.of(2024, 12, 15), 20));
//        challenges.add(new Challenge("Daily English Diary for General", "Read 10 pages of a book daily.",
//                LocalDate.of(2024, 11, 5), LocalDate.of(2024, 12, 5), 15));

            // 초기 챌린지 패널 생성
            mainPanel = new ChallengePanel(mainFrame, challengeList1);
            mainFrame.add(mainPanel);
            mainFrame.setVisible(true);
            // 프레임 표시
            mainFrame.setVisible(true);
        });
    }
    private static void updateChallengePanel(){

        try {2
            socket = new Socket(LoginGUI.SERVER_ADDRESS, LoginGUI.SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 서버에 챌린지 리스트 요청
            out.println("list_challenges");
            // 챌린지 리스트 요청
            out.println("list_challenges");

            // 챌린지 리스트 수신 및 파싱
            List<MultiThreadedServer.ChallengeInfo> receivedChallenges = new ArrayList<>();
            String response;
            while ((response = in.readLine()) != null) {
                if (response.equals("CHALLENGE_LIST_START")) {
                    continue;
                } else if (response.equals("CHALLENGE_LIST_END")) {
                    break;
                } else if (response.startsWith("CHALLENGE|")) {
                    String[] parts = response.split("\\|");
                    // parts 배열을 사용하여 ChallengeInfo 객체 생성
                    String id = parts[1];
                    String title = parts[2];
                    String description = parts[3];
                    String startDate = parts[4];
                    String endDate = parts[5];
                    String creator = parts[6];
                    // 참여자 수 등 추가 정보가 있다면 처리
                    MultiThreadedServer.ChallengeInfo challenge = new MultiThreadedServer.ChallengeInfo(id, title, description, startDate, endDate, creator);
                    receivedChallenges.add(challenge);
                }
            }

            // challengeList1 업데이트
            challengeList1 = receivedChallenges;


        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server connection failed", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void refreshGUI() {
        if (mainFrame != null) {
            System.out.println("Refreshing GUI...");
            System.out.println("Local challenge list size: " + challengeList1.size());
            System.out.println("Server challenge list size: " + MultiThreadedServer.getChallengeList().size());

            mainFrame.getContentPane().removeAll();
            mainPanel = new ChallengePanel(mainFrame, challengeList1);
            mainFrame.add(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }

}

// 챌린지 화면 클래스
class ChallengePanel extends JPanel {


    public ChallengePanel(JFrame parentFrame, List<MultiThreadedServer.ChallengeInfo> challenges) {
        System.out.println("ChallengePanel initialized with size: " + challenges.size());
        for (MultiThreadedServer.ChallengeInfo challenge : challenges) {
            System.out.println("Challenge in panel: " + challenge.getTitle());
        }

        setLayout(null);
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // "나의 챌린지" 레이블
        JLabel titleLabel = new JLabel("나의 챌린지");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(65, 20, 400, 70);
        add(titleLabel);

        // 챌린지 목록 추가
        int yOffset = 100; // 첫 번째 패널의 Y축 위치
        for (MultiThreadedServer.ChallengeInfo challenge : challenges) {
            System.out.println("yes : " + challenges.size());
            JPanel challengeCard = createChallengeCard(challenge);
            challengeCard.setBounds(40, yOffset, 440, 150); // 위치와 크기 설정
            add(challengeCard);
            yOffset += 170; // 다음 패널 간격
        }

        // 챌린지 참여 버튼
        CustomButton joinChallengeButton = new CustomButton("챌린지 참여");
        joinChallengeButton.setBounds(45, 650, 200, 45);
        add(joinChallengeButton);

        // 챌린지 생성 버튼
        CustomButton makeChallengeButton = new CustomButton("새 챌린지 만들기");
        makeChallengeButton.setBounds(275, 650, 200, 45);
        add(makeChallengeButton);

        // 하단 메뉴
        CustomButton2 challengeMenuButton = new CustomButton2("챌린지");
        challengeMenuButton.setBounds(0, 860, 180, 60);
        add(challengeMenuButton);

        CustomButton2 rankingMenuButton = new CustomButton2("랭킹");
        rankingMenuButton.setBounds(180, 860, 180, 60);
        add(rankingMenuButton);

        CustomButton2 mypageMenuButton = new CustomButton2("마이페이지");
        mypageMenuButton.setBounds(360, 860, 180, 60);
        add(mypageMenuButton);

        // 버튼 클릭 이벤트
        joinChallengeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.dispose(); // 현재 창 종료
                JoinChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
            }
        });

        makeChallengeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.dispose(); // 현재 창 종료
                MakeChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
                System.out.println("ended");
            }
        });


        challengeMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.dispose(); // 현재 창 종료
                ChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
            }
        });

        rankingMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.dispose(); // 현재 창 종료
                RankingGUI.main(new String[]{}); // RankingGUI 실행
            }
        });

//        mypageMenuButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                parentFrame.dispose(); // 현재 창 종료
//                MyPageGUI.main(new String[]{}); // MyPageGUI 실행
//            }
//        });


    }
    // 개별 챌린지 카드를 생성하는 메서드
    private JPanel createChallengeCard(MultiThreadedServer.ChallengeInfo challenge) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // 테두리

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
//class Challenge {
//    private String title;
//    private String description;
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private int participants;
//
//    public Challenge(String title, String description, LocalDate startDate, LocalDate endDate, int participants) {
//        this.title = title;
//        this.description = description;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.participants = participants;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public int getParticipants() {
//        return participants;
//    }
//}
