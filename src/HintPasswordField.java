import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class HintPasswordField extends JPasswordField implements FocusListener {
    private final String hint;
    private boolean showingHint;

    public HintPasswordField(String hint) {
        this.hint = hint;
        this.showingHint = true;
        super.setEchoChar((char) 0); // 힌트가 보이도록 암호화 문자 제거
        super.setText(hint); // 초기 힌트 설정
        super.setForeground(Color.GRAY); // 힌트 색상 설정
        super.addFocusListener(this); // 포커스 리스너 추가
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (showingHint) {
            super.setText(""); // 포커스를 얻었을 때 힌트 제거
            super.setEchoChar('•'); // 암호화 문자 설정
            super.setForeground(Color.BLACK); // 입력 텍스트 색상
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (super.getPassword().length == 0) {
            super.setText(hint); // 포커스를 잃었을 때 힌트 설정
            super.setEchoChar((char) 0); // 힌트가 보이도록 암호화 문자 제거
            super.setForeground(Color.GRAY); // 힌트 색상
            showingHint = true;
        }
    }

    @Override
    public char[] getPassword() {
        return showingHint ? new char[0] : super.getPassword(); // 힌트 상태에서는 빈 배열 반환
    }
}

