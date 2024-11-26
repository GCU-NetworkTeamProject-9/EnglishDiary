package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class HintTextField extends JTextField implements FocusListener {
    private final String hint;
    private boolean showingHint;

    public HintTextField(String hint) {
        this.hint = hint;
        this.showingHint = true;
        super.setText(hint); // 초기 힌트 텍스트 설정
        super.setForeground(Color.GRAY); // 힌트 색상 설정
        super.addFocusListener(this); // 포커스 리스너 추가
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (showingHint) {
            super.setText(""); // 포커스를 얻었을 때 힌트 제거
            super.setForeground(Color.BLACK); // 텍스트 색상 변경
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (super.getText().isEmpty()) {
            super.setText(hint); // 포커스를 잃었을 때 힌트 설정
            super.setForeground(Color.GRAY); // 힌트 색상 변경
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText(); // 힌트 상태에서는 빈 문자열 반환
    }
}
