package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DetailChallengeGUI extends JFrame {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public DetailChallengeGUI(Challenge challenge, Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        setTitle("챌린지 상세보기");
        setSize(540, 960);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 제목 레이블
        JLabel titleLabel = new JLabel(challenge.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK); // 글씨 검정
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(224, 255, 255));
        add(titleLabel, BorderLayout.NORTH);

        // 상세 정보 패널
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBackground(new Color(224, 255, 255));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 상세 정보 추가
        JLabel descriptionLabel = new JLabel("내용: " + challenge.getDescription());
        descriptionLabel.setForeground(Color.BLACK);
        detailPanel.add(descriptionLabel);
        detailPanel.add(Box.createVerticalStrut(10));

        JLabel startDateLabel = new JLabel("시작일: " + challenge.getStartDate());
        startDateLabel.setForeground(Color.BLACK);
        detailPanel.add(startDateLabel);
        detailPanel.add(Box.createVerticalStrut(10));

        JLabel endDateLabel = new JLabel("종료일: " + challenge.getEndDate());
        endDateLabel.setForeground(Color.BLACK);
        detailPanel.add(endDateLabel);
        detailPanel.add(Box.createVerticalStrut(10));

        JLabel participantsLabel = new JLabel("참여 인원: " + challenge.getParticipants() + "명");
        participantsLabel.setForeground(Color.BLACK);
        detailPanel.add(participantsLabel);

        add(detailPanel, BorderLayout.NORTH);

        // 일기 데이터 패널
        JPanel diaryPanel = new JPanel();
        diaryPanel.setLayout(new BoxLayout(diaryPanel, BoxLayout.Y_AXIS));
        diaryPanel.setBackground(new Color(224, 255, 255));

        JScrollPane scrollPane = new JScrollPane(diaryPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("챌린지 관련 일기 데이터"));
        add(scrollPane, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(224, 255, 255));
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 일기 데이터 자동 로드
        fetchDiariesForChallenge(challenge.getTitle(), diaryPanel);

        setVisible(true);
    }

    private void fetchDiariesForChallenge(String challengeTitle, JPanel diaryPanel) {
        try {
            out.println("getDiariesByChallenge," + challengeTitle);
            diaryPanel.removeAll();

            String line;
            List<String> diaryEntries = new ArrayList<>();
            while (!(line = in.readLine()).equals("end")) {
                diaryEntries.add(line);
            }

            if (diaryEntries.isEmpty()) {
                // 데이터가 없을 경우 메시지 표시
                JLabel noDataLabel = new JLabel("데이터가 없습니다.");
                noDataLabel.setFont(new Font("나눔고딕", Font.BOLD, 16));
                diaryPanel.setBackground(Color.WHITE);
                noDataLabel.setForeground(Color.BLACK);
                noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                diaryPanel.add(noDataLabel);
            } else {

                for (String diaryEntry : diaryEntries) {
                    if (!diaryEntry.contains(",") || diaryEntry.split(",").length < 3) {
                        // 형식이 올바르지 않은 경우
                        diaryPanel.removeAll();
                        JLabel noDataLabel = new JLabel("데이터가 없습니다.");
                        noDataLabel.setFont(new Font("나눔고딕", Font.BOLD, 16));
                        diaryPanel.setBackground(Color.WHITE);
                        noDataLabel.setForeground(Color.BLACK);
                        noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        diaryPanel.add(noDataLabel);
                        break;
                    }

                    // 정상적인 데이터 처리
                    JPanel entryPanel = new JPanel();
                    entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
                    entryPanel.setBackground(Color.WHITE); // 배경 흰색
                    entryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JLabel titleLabel = new JLabel(diaryEntry.split(",")[0]);
                    titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 16));
                    titleLabel.setForeground(Color.BLACK); // 글씨 검정
                    entryPanel.add(titleLabel);

                    JLabel dateLabel = new JLabel("작성 날짜: " + diaryEntry.split(",")[1]);
                    dateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 14));
                    dateLabel.setForeground(Color.BLACK); // 글씨 검정
                    entryPanel.add(dateLabel);

                    JLabel contentLabel = new JLabel("내용: " + diaryEntry.split(",")[2]);
                    contentLabel.setFont(new Font("나눔고딕", Font.PLAIN, 14));
                    contentLabel.setForeground(Color.BLACK); // 글씨 검정
                    entryPanel.add(contentLabel);

                    entryPanel.add(Box.createVerticalStrut(10)); // 간격 추가

                    diaryPanel.add(entryPanel);
                    diaryPanel.add(Box.createVerticalStrut(5)); // 일기 사이 간격
                }
            }

// 마지막으로 패널 업데이트
                diaryPanel.revalidate();
                diaryPanel.repaint();

            } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "서버와의 통신 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
