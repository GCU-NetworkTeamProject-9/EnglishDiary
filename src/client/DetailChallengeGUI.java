package client;

import javax.swing.*;
import java.awt.*;

public class DetailChallengeGUI extends JFrame {

    public DetailChallengeGUI(Challenge challenge) {
        setTitle("챌린지 상세보기");
        setSize(540,960);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 제목 레이블
        JLabel titleLabel = new JLabel(challenge.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // 상세 정보 패널
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBackground(new Color(224, 255, 255));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 상세 정보 추가
        detailPanel.add(new JLabel("내용: " + challenge.getDescription()));
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(new JLabel("시작일: " + challenge.getStartDate()));
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(new JLabel("종료일: " + challenge.getEndDate()));
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(new JLabel("참여 인원: " + challenge.getParticipants() + "명"));

        add(detailPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}

