package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class JoinChallengeGUI {

    public static void main(String[] args) {
        System.out.println("client.JoinChallengeGUI");
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
        setLayout(new BorderLayout()); // BorderLayout 설정

        // 메인 패널
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // 제목 레이블 추가
        JLabel titleLabel = new JLabel("일기 작성");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(150, 20, 300, 50);
        mainPanel.add(titleLabel);

        // 일기 제목 입력 필드
        JLabel diaryTitleLabel = new JLabel("일기 제목:");
        diaryTitleLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        diaryTitleLabel.setBounds(50, 100, 150, 30);
        mainPanel.add(diaryTitleLabel);

        JTextField diaryTitleField = new JTextField();
        diaryTitleField.setBounds(200, 100, 250, 30);
        mainPanel.add(diaryTitleField);

        // 일기 내용 입력 필드
        JLabel diaryContentLabel = new JLabel("일기 내용:");
        diaryContentLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        diaryContentLabel.setBounds(50, 150, 150, 30);
        mainPanel.add(diaryContentLabel);

        JTextArea diaryContentArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(diaryContentArea);
        scrollPane.setBounds(50, 190, 400, 300);
        mainPanel.add(scrollPane);

        // 제출 버튼
        CustomButton submitButton = new CustomButton("제출하기");
        submitButton.setBounds(200, 520, 120, 40);
        mainPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            String diaryTitle = diaryTitleField.getText();
            String diaryContent = diaryContentArea.getText();
            LocalDate today = LocalDate.now(); // 작성 날짜
            String username = LoginGUI.loggedInUsername; // 로그인한 사용자명

            if (diaryTitle.isEmpty() || diaryContent.isEmpty()) {
                JOptionPane.showMessageDialog(this, "일기 제목과 내용을 모두 입력해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
                // 서버로 일기 정보 전송
                out.println("writeDiary," + username + "," + diaryTitle + "," + today + "," + diaryContent);

                JOptionPane.showMessageDialog(this, "일기가 성공적으로 저장되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("일기 전송: " + username + ", " + diaryTitle + ", " + today + ", " + diaryContent);
                parentFrame.dispose(); // 현재 창 닫기
                ChallengeGUI.main(new String[]{}); // ChallengeGUI로 돌아가기
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버 연결에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 하단 탭 추가
        JPanel bottomTabPanel = new JPanel(new GridLayout(1, 3));
        bottomTabPanel.setPreferredSize(new Dimension(540, 60));
        bottomTabPanel.setBackground(new Color(224, 255, 255));

        CustomButton2 challengeMenuButton = new CustomButton2("챌린지");
        CustomButton2 rankingMenuButton = new CustomButton2("랭킹");
        CustomButton2 mypageMenuButton = new CustomButton2("마이페이지");

        bottomTabPanel.add(challengeMenuButton);
        bottomTabPanel.add(rankingMenuButton);
        bottomTabPanel.add(mypageMenuButton);

        // 탭 버튼 이벤트 리스너
        challengeMenuButton.addActionListener(e -> {
            parentFrame.dispose();
            ChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
        });

        rankingMenuButton.addActionListener(e -> {
            parentFrame.dispose();
            RankingGUI.main(new String[]{}); // RankingGUI 실행
        });

        mypageMenuButton.addActionListener(e -> {
            parentFrame.dispose();
            MyPageGUI.main(new String[]{}); // MyPageGUI 실행
        });

        // 메인 패널과 하단 탭을 BorderLayout에 추가
        add(mainPanel, BorderLayout.CENTER);
        add(bottomTabPanel, BorderLayout.SOUTH);
    }
}

