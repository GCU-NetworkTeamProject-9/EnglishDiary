package client;

import javax.swing.*;
import java.awt.*;

public class CommonButton {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Button");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(null);

        // 버튼 생성 (텍스트를 "로그인"으로 설정)
        CustomButton button = new CustomButton("로그인");
        button.setBounds(100, 50, 200, 50); // 버튼 크기와 위치 설정
        frame.add(button);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// 커스텀 버튼 클래스
class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text); // 전달된 텍스트를 설정
        setFocusPainted(false); // 포커스 테두리 제거
        setContentAreaFilled(false); // 기본 버튼 배경 제거
        setBorderPainted(false); // 기본 테두리 제거
        setFont(new Font("나눔고딕", Font.BOLD, 16)); // 폰트 설정
        setForeground(Color.BLACK); // 텍스트 색상
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경색
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // 둥근 직사각형 (30, 30)

        // 텍스트 그리기
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 테두리 색상
        g2.setColor(new Color(30, 144, 255)); // 파란색
        g2.setStroke(new BasicStroke(2)); // 테두리 두께
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30); // 둥근 테두리 (30, 30)

        g2.dispose();
    }
}
