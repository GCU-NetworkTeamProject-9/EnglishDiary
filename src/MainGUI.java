import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class MainGUI {

	static String name="홍길동";
	static int rank=33;
	static int challengeNumber=3;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 메인 프레임 생성
        JFrame frame = new JFrame("English Diary Mate");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(null);
        
        // 제목 레이블
        JLabel titleLabel = new JLabel("English Diary Mate");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(300, 10, 400, 40);
        frame.add(titleLabel);
        
        // ImageIcon으로 이미지 로드 및 크기 조정
        ImageIcon originalIcon = new ImageIcon("resources/images/diary.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // JLabel 생성 후 이미지 설정
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(20, 60, 200, 200); // 위치와 크기 설정
        frame.add(imageLabel);

        // 이름 레이블
        JLabel NameLabel = new JLabel("이름: " + name);
        NameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        NameLabel.setBounds(40, 280, 200, 30); // 위치와 크기 설정
        frame.add(NameLabel);
        
        // 랭킹 레이블
        JLabel RankingLabel = new JLabel("랭킹: " + rank+"위");
        RankingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        RankingLabel.setBounds(40, 305, 200, 30); // 위치와 크기 설정
        frame.add(RankingLabel);
        
        // 도전 개수 레이블
        JLabel ChallengeNumberlabel = new JLabel("참여 중인 도전: " + challengeNumber+"개");
        ChallengeNumberlabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        ChallengeNumberlabel.setBounds(40, 330, 200, 30); // 위치와 크기 설정
        frame.add(ChallengeNumberlabel);
        
        
        // 참여 하고 있는 도전 표시 테이블
        JLabel searchLabel = new JLabel("검색 :");
        searchLabel.setBounds(240, 60, 200, 25);
        frame.add(searchLabel);

        JTextField searchField = new JTextField();
        searchField.setBounds(380, 60, 150, 25);
        frame.add(searchField);

        String[] columnNames = {"이름", "내용", "시작일", "종료일", "참여 인원 수"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(240, 100, 300, 160);
        frame.add(tableScrollPane);
        
        JLabel inputLabel = new JLabel("오늘의 영어 일기 : ");
        JTextArea inputArea = new JTextArea(8, 20); // 5줄 높이로 설정
        JScrollPane inputScrollPane = new JScrollPane(inputArea);

        inputLabel.setBounds(240, 270, 200, 30); // 위치와 크기 설정
        frame.add(inputLabel);
        inputArea.setBounds(240, 310, 300, 200); // 위치와 크기 설정
        frame.add(inputArea);
        //inputScrollPane.setBounds(540, 310, 200, 30); // 위치와 크기 설정
        //frame.add(inputScrollPane);
        
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(340, 520, 100, 30); // 위치와 크기 설정
        frame.add(sendButton);
        
        // 참여 하고 있는 도전 표시 테이블
        JLabel rankingTitleLabel = new JLabel("랭킹");
        rankingTitleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        rankingTitleLabel.setBounds(700, 60, 200, 25);
        frame.add(rankingTitleLabel);


        String[] columnNames2 = {"순위", "이름", "성공 횟수"};
        DefaultTableModel tableModel2 = new DefaultTableModel(columnNames2, 0);
        JTable rankingTable = new JTable(tableModel2);
        JScrollPane tableScrollPane2 = new JScrollPane(rankingTable);
        tableScrollPane2.setBounds(560, 100, 300, 400);
        frame.add(tableScrollPane2);
        
        // 프레임 표시
        frame.setVisible(true);



	}

}
