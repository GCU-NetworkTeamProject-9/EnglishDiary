import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinChallengeGUI {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Join Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        // "Join Challenge" 화면 추가
        JoinChallengePanel joinChallengePanel = new JoinChallengePanel(frame);
        frame.add(joinChallengePanel);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// "Join Challenge" 화면 클래스
class JoinChallengePanel extends JPanel {
    private JFrame parentFrame;

    public JoinChallengePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null); // 절대 레이아웃 설정

        // 배경 색상 설정
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // "챌린지 참여하기" 제목 텍스트
        JLabel titleLabel = new JLabel("Today's Diary");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(180, 20, 400, 70);
        add(titleLabel);

        // 참여 챌린지 텍스트 레이블
        JLabel challengeTitleLabel = new JLabel("참여 챌린지");
        challengeTitleLabel.setFont(new Font("나눔고딕", Font.PLAIN, 15));
        challengeTitleLabel.setBounds(30, 100, 100, 30);
        add(challengeTitleLabel);

        // 내가 참여한 챌린지 선택 콤보박스
        JComboBox<String> challengeComboBox = new JComboBox<>(new String[]{
            "Daily English Diary",
            "30-Minute Reading Challenge",
            "5K Steps Daily Walk",
            "Code 1 Problem a Day"
        });
        
        challengeComboBox.setBounds(140, 100, 360, 30);
        add(challengeComboBox);
        // 제목 입력 필드
        JTextField titleField = new JTextField("제목을 입력하세요");
        titleField.setBounds(20, 140, 480, 45);
        titleField.setForeground(Color.GRAY); // 힌트 텍스트 색상
        titleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (titleField.getText().equals("제목을 입력하세요")) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK); // 사용자가 입력할 때 기본 색상으로 변경
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (titleField.getText().isEmpty()) {
                    titleField.setText("제목을 입력하세요");
                    titleField.setForeground(Color.GRAY);
                }
            }
        });
        add(titleField);


        // 내용 입력 필드
        JTextArea contentField = new JTextArea("내용을 입력하세요");
        contentField.setBounds(20, 200, 480, 350);
        contentField.setLineWrap(true);
        contentField.setWrapStyleWord(true);
        contentField.setForeground(Color.GRAY); // 힌트 텍스트 색상
        contentField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (contentField.getText().equals("내용을 입력하세요")) {
                    contentField.setText("");
                    contentField.setForeground(Color.BLACK); // 사용자가 입력할 때 기본 색상으로 변경
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (contentField.getText().isEmpty()) {
                    contentField.setText("내용을 입력하세요");
                    contentField.setForeground(Color.GRAY);
                }
            }
        });
        add(contentField);

        // 참여 버튼
        CustomButton joinButton = new CustomButton("제출");
        joinButton.setBounds(200, 600, 120, 40);
        add(joinButton);

        // 참여 버튼 클릭 이벤트 처리
        joinButton.addActionListener(e -> {
            String title = titleField.getText();
            String content = contentField.getText();

            if (title.isEmpty() || title.equals("제목을 입력하세요") || content.isEmpty() || content.equals("내용을 입력하세요")) {
                JOptionPane.showMessageDialog(this, "제목과 내용을 입력해주세요!", "입력 오류", JOptionPane.ERROR_MESSAGE);
            } else {
                //JOptionPane.showMessageDialog(this, "챌린지 제목: " + title + "\n내용: " + content, "참여 성공", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(this,"오늘 챌린지 참여에 성공하였습니다");

            	//제출 로직 추가
            	
                parentFrame.dispose(); // 현재 창 종료
                ChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
            }
        });
        
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
