package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

public class JoinChallengeGUI {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Join client.Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        // "Join client.Challenge" 화면 추가
        JoinChallengePanel joinChallengePanel = new JoinChallengePanel(frame);
        frame.add(joinChallengePanel);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// "Join client.Challenge" 화면 클래스
class JoinChallengePanel extends JPanel {
    private JFrame parentFrame;

    public JoinChallengePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null); // 절대 레이아웃 설정

        // 배경 패널 생성
        setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // 제목 레이블 추가
        JLabel titleLabel = new JLabel("일기 작성");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(150, 20, 300, 50); // 위치와 크기 설정
        add(titleLabel);

        // 일기 제목 입력 필드
        JLabel diaryTitleLabel = new JLabel("일기 제목:");
        diaryTitleLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        diaryTitleLabel.setBounds(50, 100, 150, 30);
        add(diaryTitleLabel);

        JTextField diaryTitleField = new JTextField();
        diaryTitleField.setBounds(200, 100, 250, 30);
        add(diaryTitleField);

        // 일기 내용 입력 필드
        JLabel diaryContentLabel = new JLabel("일기 내용:");
        diaryContentLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        diaryContentLabel.setBounds(50, 150, 150, 30);
        add(diaryContentLabel);

        JTextArea diaryContentArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(diaryContentArea);
        scrollPane.setBounds(50, 190, 400, 300);
        add(scrollPane);

        // 제출 버튼
        CustomButton submitButton = new CustomButton("제출하기");
        submitButton.setBounds(200, 520, 120, 40);
        add(submitButton);

        submitButton.addActionListener(e -> {
            String diaryTitle = diaryTitleField.getText();
            String diaryContent = diaryContentArea.getText();

            if (diaryTitle.isEmpty() || diaryContent.isEmpty()) {
                JOptionPane.showMessageDialog(this, "일기 제목과 내용을 모두 입력해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
                out.println("writeDiary," + diaryTitle + "," + diaryContent);

                JOptionPane.showMessageDialog(this, "일기가 성공적으로 저장되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("일기 전송: "+diaryTitle+", "+diaryContent);
                parentFrame.dispose(); // 현재 창 닫기
                ChallengeGUI.main(new String[]{}); // ChallengeGUI로 돌아가기
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버 연결에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
