import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class RankingGUI {

	public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Ranking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540,960);

        // 챌린지 패널 생성 및 추가
        RankingPanel rankingPanel = new RankingPanel(frame);
        frame.add(rankingPanel);

        

        // 프레임 표시
        frame.setVisible(true);
    }
}

//로그인 화면 클래스
class RankingPanel extends JPanel {
 private JFrame parentFrame;
 public int order=33; // 내 랭킹 순위 받아오기
 public RankingPanel(JFrame parentFrame) {
     this.parentFrame = parentFrame;
     setLayout(null);
     setBackground(new Color(224, 255, 255)); // 하늘색 배경 설정

     // 랭킹 레이블
     JLabel titleLabel = new JLabel("랭킹");
     titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
     titleLabel.setBounds(65, 20, 400, 70);
     add(titleLabel);
     
     // 내 순위 레이블
     JLabel myRanking = new JLabel("내 순위는 "+order+"위 입니다.");
     myRanking.setFont(new Font("나눔고딕", Font.BOLD, 16));
     myRanking.setBounds(280, 25, 400, 70);
     add(myRanking);

     String[] columnNames2 = {"순위", "이름", "성공 횟수"};
     DefaultTableModel tableModel2 = new DefaultTableModel(columnNames2, 0);
     JTable rankingTable = new JTable(tableModel2);
     
     // 데이터 추가 (20개)
     for (int i = 1; i <= 100; i++) {
         tableModel2.addRow(new Object[]{i, "사용자 " + i, (int) (Math.random() * 10) + 1});
     }
     // 테이블 스타일 설정
     rankingTable.setBackground(Color.WHITE); // 테이블 배경 흰색
     rankingTable.setForeground(Color.BLACK); // 텍스트 색상 검정
     rankingTable.setFont(new Font("나눔고딕", Font.PLAIN, 14)); // 텍스트 크기 설정
     rankingTable.setRowHeight(30); // 행 높이 설정

     // 테이블 헤더 스타일 설정
     JTableHeader tableHeader = rankingTable.getTableHeader();
     tableHeader.setFont(new Font("나눔고딕", Font.BOLD, 16)); // 헤더 글씨 크기 설정
     tableHeader.setBackground(new Color(220, 220, 220)); // 헤더 배경색 설정
     tableHeader.setForeground(Color.BLACK); // 헤더 글씨 색상 설정

     // 스크롤 패널 생성 및 테이블 추가
     JScrollPane tableScrollPane2 = new JScrollPane(rankingTable);
     tableScrollPane2.setBounds(60, 100, 400, 700);
     add(tableScrollPane2);
     
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

     // 버튼 클릭 이벤트
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

//     mypageMenuButton.addActionListener(new ActionListener() {
//         @Override
//         public void actionPerformed(ActionEvent e) {
//             parentFrame.dispose(); // 현재 창 종료
//             MyPageGUI.main(new String[]{}); // MyPageGUI 실행
//         }
//     });
     
 }
}
