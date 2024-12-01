package client;

import javax.swing.*;
import java.awt.*;

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setOpaque(false); // 배경을 투명하게 설정
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 부드럽게 그리기
        g2.setColor(getBackground()); // 패널의 배경색 가져오기
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius); // 둥근 사각형 그리기
    }
}