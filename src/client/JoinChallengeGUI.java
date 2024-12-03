package client;

import client.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class JoinChallengePanel extends JPanel {
    private JFrame parentFrame;
    private JComboBox<String> challengeComboBox;

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

    public JoinChallengePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout()); // BorderLayout 설정

        // 메인 패널
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // "챌린지 참여하기" 제목 텍스트
        JLabel titleLabel = new JLabel("Today's Diary");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(180, 20, 400, 70);
        mainPanel.add(titleLabel);

        // 참여 챌린지 텍스트 레이블
        JLabel challengeTitleLabel = new JLabel("참여 챌린지:");
        challengeTitleLabel.setFont(new Font("나눔고딕", Font.PLAIN, 15));
        challengeTitleLabel.setBounds(30, 100, 100, 30);
        mainPanel.add(challengeTitleLabel);

        // 내가 참여한 챌린지 선택 콤보박스
        challengeComboBox = new JComboBox<>();
        challengeComboBox.setBounds(140, 100, 360, 30);
        mainPanel.add(challengeComboBox);

        // 서버에서 챌린지 데이터를 불러옵니다.
        loadChallengesFromServer();

        // 일기 제목 레이블
        JLabel diaryTitleLabel = new JLabel("일기 제목:");
        diaryTitleLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        diaryTitleLabel.setBounds(30, 150, 150, 30); // 콤보박스 아래로 위치 조정
        mainPanel.add(diaryTitleLabel);

        // 일기 제목 입력 필드
        JTextField diaryTitleField = new JTextField();
        diaryTitleField.setBounds(140, 150, 360, 30); // 콤보박스 아래로 위치 조정
        mainPanel.add(diaryTitleField);

        // 일기 내용 레이블
        JLabel diaryContentLabel = new JLabel("일기 내용:");
        diaryContentLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        diaryContentLabel.setBounds(30, 200, 150, 30); // 제목 필드 아래로 위치 조정
        mainPanel.add(diaryContentLabel);

        // 일기 내용 입력 필드
        JTextArea diaryContentArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(diaryContentArea);
        scrollPane.setBounds(30, 240, 470, 300); // 위치 조정
        mainPanel.add(scrollPane);

        // 제출 버튼
        CustomButton submitButton = new CustomButton("제출하기");
        submitButton.setBounds(200, 560, 120, 40); // 위치 조정
        mainPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            String selectedChallenge = (String) challengeComboBox.getSelectedItem();
            String diaryTitle = diaryTitleField.getText();
            String diaryContent = diaryContentArea.getText();

            if (selectedChallenge == null || diaryTitle.isEmpty() || diaryContent.isEmpty()) {
                JOptionPane.showMessageDialog(this, "챌린지, 일기 제목, 내용을 모두 입력해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
                out.println("writeDiary," + diaryTitle + "," + diaryContent + "," + selectedChallenge);

                JOptionPane.showMessageDialog(this, "일기가 성공적으로 저장되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
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
            MyPageGUI.main(new String[]{}); //MyPageGUI 실행
        });

        // 메인 패널과 하단 탭을 BorderLayout에 추가
        add(mainPanel, BorderLayout.CENTER);
        add(bottomTabPanel, BorderLayout.SOUTH);
    }

    private void loadChallengesFromServer() {
        try {
            PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(LoginGUI.socket.getInputStream()));

            out.println("challenges");

            String line;
            while (!(line = in.readLine()).equals("end")) {
                String[] parts = line.split(",");
                String challengeTitle = parts[0]; // 챌린지 제목
                challengeComboBox.addItem(challengeTitle); // 콤보박스에 추가
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "챌린지 데이터를 불러오는데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
