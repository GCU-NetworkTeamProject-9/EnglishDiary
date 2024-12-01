package client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.table.DefaultTableModel;

public class RankingGUI {

    public static void main(String[] args) {
        System.out.println("client.RankingGUI");
        JFrame frame = new JFrame("Ranking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        JPanel mainPanel = new JPanel(new BorderLayout());

        RankingPanel rankingPanel = new RankingPanel(frame);

        JScrollPane scrollPane = new JScrollPane(rankingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setPreferredSize(new Dimension(540, 60));
        buttonPanel.setBackground(new Color(224, 255, 255));

        CustomButton2 challengeMenuButton = new CustomButton2("챌린지");
        CustomButton2 rankingMenuButton = new CustomButton2("랭킹");
        CustomButton2 mypageMenuButton = new CustomButton2("마이페이지");

        buttonPanel.add(challengeMenuButton);
        buttonPanel.add(rankingMenuButton);
        buttonPanel.add(mypageMenuButton);

        challengeMenuButton.addActionListener(e -> {
            frame.dispose();
            ChallengeGUI.main(new String[]{});
        });
        mypageMenuButton.addActionListener(e -> {
            frame.dispose();
            MyPageGUI.main(new String[]{}); // MyPageGUI 실행
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}

class RankingPanel extends JPanel {
    public RankingPanel(JFrame parentFrame) {
        setLayout(new BorderLayout());
        setBackground(new Color(224, 255, 255));

        JLabel titleLabel = new JLabel("랭킹");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"순위", "이름", "성공 횟수"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable rankingTable = new JTable(tableModel);

        try {
            // 서버에서 랭킹 데이터 가져오기
            PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(LoginGUI.socket.getInputStream()));

            out.println("rank"); // 랭킹 요청

            String line;
            boolean isRanking = false;
            while ((line = in.readLine()) != null) {
                if (line.equals("startRanking")) {
                    isRanking = true;
                    continue;
                }
                if (line.equals("endRanking")) {
                    break;
                }
                if (isRanking) {
                    String[] data = line.split(",");
                    if (data.length == 3) {
                        tableModel.addRow(new Object[]{data[0], data[1], data[2]});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "서버와 연결할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane tableScrollPane = new JScrollPane(rankingTable);
        add(tableScrollPane, BorderLayout.CENTER);
    }
}
