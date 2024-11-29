package client;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;

public class MyPageGUI {
	
	private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
    	
        System.out.println("client.MyPageGUI");
        // 메인 프레임 생성
        JFrame frame = new JFrame("My Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        // "My Page" 화면 추가
        MyPagePanel myPagePanel = new MyPagePanel(frame);
        frame.add(myPagePanel);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// "My Page" 화면 클래스
class MyPagePanel extends JPanel {
    private JFrame parentFrame;

    public MyPagePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout()); // BorderLayout 설정

        // 메인 패널
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

        // 제목 레이블 추가
        JLabel titleLabel = new JLabel("My Page");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(220, 20, 300, 50);
        mainPanel.add(titleLabel);

        RoundedPanel cardPanel1 = new RoundedPanel(40);
        cardPanel1.setLayout(null);
        cardPanel1.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel1.setBounds(30, 100, 460, 200);
        mainPanel.add(cardPanel1);


        // 카드 패널 내부에 "내 정보"와 관련된 텍스트 추가
        JLabel myInfoLabel = new JLabel("내 정보");
        myInfoLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        myInfoLabel.setBounds(20, 10, 300, 30);
        cardPanel1.add(myInfoLabel);

        // 선 추가
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setBounds(20, 50, 420, 1);
        cardPanel1.add(separator1);

        JLabel myAccountCheckLabel = new JLabel("내 계정 확인");
        myAccountCheckLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        myAccountCheckLabel.setBounds(20, 60, 300, 30);
        cardPanel1.add(myAccountCheckLabel);

        // 선 추가
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setBounds(20, 90, 420, 1);
        cardPanel1.add(separator2);

        JLabel changeNameLabel = new JLabel("이름 변경");
        changeNameLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        changeNameLabel.setBounds(20, 100, 300, 30);
        cardPanel1.add(changeNameLabel);

        // 선 추가
        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        separator3.setBounds(20, 130, 420, 1);
        cardPanel1.add(separator3);

        JLabel changePasswordLabel = new JLabel("비밀번호 변경");
        changePasswordLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        changePasswordLabel.setBounds(20, 140, 300, 30);
        cardPanel1.add(changePasswordLabel);

        // 선 추가
        JSeparator separator4 = new JSeparator(SwingConstants.HORIZONTAL);
        separator4.setBounds(20, 170, 420, 1);
        cardPanel1.add(separator4);
        
        
        // 2번째 패널
        RoundedPanel cardPanel2 = new RoundedPanel(40);
        cardPanel2.setLayout(null);
        cardPanel2.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel2.setBounds(30, 340, 460, 200);
        mainPanel.add(cardPanel2);


     // 카드 패널 내부에 "내 정보"와 관련된 텍스트 추가
        JLabel myChallengeLabel = new JLabel("챌린지");
        myChallengeLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        myChallengeLabel.setBounds(20, 10, 300, 30);
        cardPanel2.add(myChallengeLabel);

        // 선 추가
        JSeparator separator5 = new JSeparator(SwingConstants.HORIZONTAL);
        separator5.setBounds(20, 50, 420, 1);
        cardPanel2.add(separator5);

        JLabel myJoinChallengeLabel = new JLabel("내가 참여한 챌린지");
        myJoinChallengeLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        myJoinChallengeLabel.setBounds(20, 60, 300, 30);
        cardPanel2.add( myJoinChallengeLabel);

        // 선 추가
        JSeparator separator6 = new JSeparator(SwingConstants.HORIZONTAL);
        separator6.setBounds(20, 90, 420, 1);
        cardPanel2.add(separator6);

        JLabel mySucessChallengeLabel = new JLabel("내가 성공한 챌린지");
        mySucessChallengeLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        mySucessChallengeLabel.setBounds(20, 100, 300, 30);
        cardPanel2.add(mySucessChallengeLabel);

        // 선 추가
        JSeparator separator7 = new JSeparator(SwingConstants.HORIZONTAL);
        separator7.setBounds(20, 130, 420, 1);
        cardPanel2.add(separator7);

        JLabel myWritingChallengeLabel = new JLabel("내가 작성한 챌린지");
        myWritingChallengeLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        myWritingChallengeLabel.setBounds(20, 140, 300, 30);
        cardPanel2.add(myWritingChallengeLabel);

        // 선 추가
        JSeparator separator8 = new JSeparator(SwingConstants.HORIZONTAL);
        separator8.setBounds(20, 170, 420, 1);
        cardPanel2.add(separator8);
        
        // 내가 참여한 챌린지 클릭 이벤트 추가
        myJoinChallengeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.dispose(); // 현재 화면 닫기
                MyWritingChallengeGUI.main(new String[]{}); // MyWritingChallengeGUI 실행
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            	 myJoinChallengeLabel.setForeground(Color.BLUE); // 마우스 올리면 글씨 색 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	 myJoinChallengeLabel.setForeground(Color.BLACK); // 마우스 떼면 원래 색으로 변경
            }
        });
        
        // 내가 성공한 챌린지 클릭 이벤트 추가
        mySucessChallengeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.dispose(); // 현재 화면 닫기
                MyWritingChallengeGUI.main(new String[]{}); // MyWritingChallengeGUI 실행
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            	mySucessChallengeLabel.setForeground(Color.BLUE); // 마우스 올리면 글씨 색 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	mySucessChallengeLabel.setForeground(Color.BLACK); // 마우스 떼면 원래 색으로 변경
            }
        });
        // 내가 작성한 챌린지 클릭 이벤트 추가
        myWritingChallengeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.dispose(); // 현재 화면 닫기
                MyWritingChallengeGUI.main(new String[]{}); // MyWritingChallengeGUI 실행
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                myWritingChallengeLabel.setForeground(Color.BLUE); // 마우스 올리면 글씨 색 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                myWritingChallengeLabel.setForeground(Color.BLACK); // 마우스 떼면 원래 색으로 변경
            }
        });
        // 3번째 패널 추가
        RoundedPanel cardPanel3 = new RoundedPanel(40);
        cardPanel3.setLayout(null);
        cardPanel3.setBackground(Color.WHITE); // 카드 배경 흰색
        cardPanel3.setBounds(30, 580, 460, 200);
        mainPanel.add(cardPanel3); // cardPanel3을 올바르게 추가



        // 카드 패널 내부에 "기타"와 관련된 텍스트 추가
        JLabel othersLabel = new JLabel("기타");
        othersLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        othersLabel.setBounds(20, 10, 300, 30);
        cardPanel3.add(othersLabel);

        // 선 추가
        JSeparator separator9 = new JSeparator(SwingConstants.HORIZONTAL);
        separator9.setBounds(20, 50, 420, 1);
        cardPanel3.add(separator9);

        JLabel announcementLabel = new JLabel("공지사항");
        announcementLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        announcementLabel.setBounds(20, 60, 300, 30);
        cardPanel3.add( announcementLabel);

        // 선 추가
        JSeparator separator10 = new JSeparator(SwingConstants.HORIZONTAL);
        separator10.setBounds(20, 90, 420, 1);
        cardPanel3.add(separator10);

        JLabel customServiceCenterLabel = new JLabel("고객센터");
        customServiceCenterLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        customServiceCenterLabel.setBounds(20, 100, 300, 30);
        cardPanel3.add(customServiceCenterLabel);

        // 선 추가
        JSeparator separator11 = new JSeparator(SwingConstants.HORIZONTAL);
        separator11.setBounds(20, 130, 420, 1);
        cardPanel3.add(separator11);

        JLabel operatingPolicyLabel = new JLabel("운영정책");
        operatingPolicyLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        operatingPolicyLabel.setBounds(20, 140, 300, 30);
        cardPanel3.add(operatingPolicyLabel);

        // 선 추가
        JSeparator separator12 = new JSeparator(SwingConstants.HORIZONTAL);
        separator12.setBounds(20, 170, 420, 1);
        cardPanel3.add(separator12);
        
        
        

        
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

        // 메인 패널과 하단 탭을 BorderLayout에 추가
        add(mainPanel, BorderLayout.CENTER);
        add(bottomTabPanel, BorderLayout.SOUTH);
    }
}