import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class RankingTable {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);
        frame.setLayout(null);

        // 테이블 데이터와 컬럼 정의
        String[] columnNames2 = {"순위", "이름", "성공 횟수"};
        DefaultTableModel tableModel2 = new DefaultTableModel(columnNames2, 0);
        JTable rankingTable = new JTable(tableModel2);

        // 데이터 추가
        tableModel2.addRow(new Object[]{"1", "홍길동", "5"});
        tableModel2.addRow(new Object[]{"2", "김철수", "4"});
        tableModel2.addRow(new Object[]{"3", "이영희", "3"});

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
        frame.add(tableScrollPane2);

        // 프레임 표시
        frame.setVisible(true);
    }
}
