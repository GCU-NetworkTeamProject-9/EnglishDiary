package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MakeChallengeGUI {

    public static void main(String[] args) {
        System.out.println("client.MakeChallengeGUI");
        JFrame frame = new JFrame("Make Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        MakeChallengePanel makeChallengePanel = new MakeChallengePanel(frame);
        frame.add(makeChallengePanel);

        frame.setVisible(true);
    }
}

class MakeChallengePanel extends JPanel {
    private JFrame parentFrame;

    public MakeChallengePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout()); // BorderLayout 설정

        // 메인 패널
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(224, 255, 255));

        JLabel titleLabel = new JLabel("새 챌린지 만들기");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(150, 20, 300, 50);
        mainPanel.add(titleLabel);

        JLabel nameLabel = new JLabel("챌린지 제목:");
        nameLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        nameLabel.setBounds(50, 100, 150, 30);
        mainPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        mainPanel.add(nameField);

        JLabel startDateLabel = new JLabel("시작 날짜:");
        startDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        startDateLabel.setBounds(50, 150, 150, 30);
        mainPanel.add(startDateLabel);

        JTextField startDateField = new JTextField("YYYY-MM-DD");
        startDateField.setBounds(200, 150, 250, 30);
        mainPanel.add(startDateField);

        JLabel endDateLabel = new JLabel("종료 날짜:");
        endDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        endDateLabel.setBounds(50, 200, 150, 30);
        mainPanel.add(endDateLabel);

        JTextField endDateField = new JTextField("YYYY-MM-DD");
        endDateField.setBounds(200, 200, 250, 30);
        mainPanel.add(endDateField);

        JLabel participantsLabel = new JLabel("최대 참여자 수:");
        participantsLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        participantsLabel.setBounds(50, 250, 150, 30);
        mainPanel.add(participantsLabel);

        JTextField participantsField = new JTextField();
        participantsField.setBounds(200, 250, 250, 30);
        mainPanel.add(participantsField);

        CustomButton createButton = new CustomButton("생성하기");
        createButton.setBounds(200, 320, 120, 40);
        mainPanel.add(createButton);

        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();
            String participants = participantsField.getText().trim();

            // 입력값 검증
            if (name.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || participants.isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate parsedStartDate = LocalDate.parse(startDate);
                LocalDate parsedEndDate = LocalDate.parse(endDate);
                if (parsedStartDate.isAfter(parsedEndDate)) {
                    JOptionPane.showMessageDialog(this, "시작 날짜는 종료 날짜보다 빨라야 합니다.", "날짜 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int maxParticipants = Integer.parseInt(participants);
                if (maxParticipants <= 0) {
                    JOptionPane.showMessageDialog(this, "참여자 수는 1명 이상이어야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
                out.println("createChallenge," + name + "," + startDate + "," + endDate + "," + maxParticipants);

                JOptionPane.showMessageDialog(this, "챌린지가 성공적으로 생성되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.dispose();
                ChallengeGUI.main(new String[]{});
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.", "날짜 오류", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "참여자 수는 숫자여야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버와의 연결 중 오류가 발생했습니다.", "서버 오류", JOptionPane.ERROR_MESSAGE);
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

        challengeMenuButton.addActionListener(e -> {
            parentFrame.dispose();
            ChallengeGUI.main(new String[]{}); // ChallengeGUI 실행
        });

        rankingMenuButton.addActionListener(e -> {
            parentFrame.dispose();
            RankingGUI.main(new String[]{}); // RankingGUI 실행
        });

        // 메인 패널과 하단 탭 추가
        add(mainPanel, BorderLayout.CENTER);
        add(bottomTabPanel, BorderLayout.SOUTH);
    }
}
