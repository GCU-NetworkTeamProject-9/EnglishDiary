import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeChallengeGUI {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Make Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        // "Make Challenge" 화면 추가
        MakeChallengePanel makeChallengePanel = new MakeChallengePanel(frame);
        frame.add(makeChallengePanel);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// "Make Challenge" 화면 클래스
class MakeChallengePanel extends JPanel {
    private JFrame parentFrame;

    public MakeChallengePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null); // 절대 레이아웃 설정

        // 배경 패널 생성
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // 제목 레이블 추가
        JLabel titleLabel = new JLabel("새 챌린지 만들기");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(150, 20, 300, 50); // 위치와 크기 설정
        add(titleLabel);

        // 챌린지 제목 입력 필드
        JLabel nameLabel = new JLabel("챌린지 제목:");
        nameLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        nameLabel.setBounds(50, 100, 150, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        add(nameField);

        // 시작 날짜 입력 필드
        JLabel startDateLabel = new JLabel("시작 날짜:");
        startDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        startDateLabel.setBounds(50, 150, 150, 30);
        add(startDateLabel);

        JTextField startDateField = new JTextField("YYYY-MM-DD");
        startDateField.setBounds(200, 150, 250, 30);
        add(startDateField);

        // 종료 날짜 입력 필드
        JLabel endDateLabel = new JLabel("종료 날짜:");
        endDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        endDateLabel.setBounds(50, 200, 150, 30);
        add(endDateLabel);

        JTextField endDateField = new JTextField("YYYY-MM-DD");
        endDateField.setBounds(200, 200, 250, 30);
        add(endDateField);

        // 참여자 수 입력 필드
        JLabel participantsLabel = new JLabel("최대 참여자 수:");
        participantsLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        participantsLabel.setBounds(50, 250, 150, 30);
        add(participantsLabel);

        JTextField participantsField = new JTextField();
        participantsField.setBounds(200, 250, 250, 30);
        add(participantsField);

        // 생성 버튼
        CustomButton createButton = new CustomButton("생성하기");
        createButton.setBounds(200, 320, 120, 40);
        add(createButton);
        
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

        // 챌린지 생성 버튼 이벤트 추가
        createButton.addActionListener(e -> {
            String name = nameField.getText();
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            String participants = participantsField.getText();

            // 데이터 확인 및 출력
            JOptionPane.showMessageDialog(this, 
                "챌린지 제목: " + name +
                "\n시작 날짜: " + startDate +
                "\n종료 날짜: " + endDate +
                "\n최대 참여자 수: " + participants,
                "챌린지 생성 성공", JOptionPane.INFORMATION_MESSAGE);
            
            parentFrame.dispose(); // 현재 창 종료
            ChallengeGUI.main(new String[]{}); // RankingGUI 실행
        });
        
        // 메뉴 버튼 클릭 이벤트
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
}
