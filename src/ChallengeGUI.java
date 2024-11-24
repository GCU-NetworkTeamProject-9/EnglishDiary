import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ChallengeGUI {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        // 챌린지 목록 생성
        List<Challenge> challenges = new ArrayList<>();
        challenges.add(new Challenge("Daily English Diary for Beginners", "Solve one coding problem daily.",
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30), 12));
        challenges.add(new Challenge("Daily English Book Reading", "Exercise for 30 minutes daily.",
                LocalDate.of(2024, 10, 15), LocalDate.of(2024, 12, 15), 20));
        challenges.add(new Challenge("Daily English Diary for General", "Read 10 pages of a book daily.",
                LocalDate.of(2024, 11, 5), LocalDate.of(2024, 12, 5), 15));

        // 챌린지 패널 생성 및 추가
        ChallengePanel challengePanel = new ChallengePanel(challenges);
        frame.add(challengePanel);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// 챌린지 화면 클래스
class ChallengePanel extends JPanel {
    public ChallengePanel(List<Challenge> challenges) {
        setLayout(null);
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // "나의 챌린지" 레이블
        JLabel titleLabel = new JLabel("나의 챌린지");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(65, 20, 400, 70);
        add(titleLabel);

        // 챌린지 목록 추가
        int yOffset = 100; // 첫 번째 패널의 Y축 위치
        for (Challenge challenge : challenges) {
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
        
        //하단 메뉴
        CustomButton2 challengeMenuButton=new CustomButton2("챌린지");
        challengeMenuButton.setBounds(0, 860, 180, 60);
        add(challengeMenuButton);
        
        CustomButton2 rankingMenuButton=new CustomButton2("랭킹");
        rankingMenuButton.setBounds(180, 860, 180, 60);
        add(rankingMenuButton);
        
        CustomButton2 mypageMenuButton=new CustomButton2("마이페이지");
        mypageMenuButton.setBounds(360, 860, 180, 60);
        add(mypageMenuButton);
    }

    	// 개별 챌린지 카드를 생성하는 메서드
    	private JPanel createChallengeCard(Challenge challenge) {
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
