import javax.swing.*;
import java.awt.*;

public class CommonButton2 {

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Button");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(null);

        // 버튼 생성 (텍스트를 "로그인"으로 설정)
        CustomButton2 button = new CustomButton2("메뉴바");
        button.setBounds(100, 50, 200, 50); // 버튼 크기와 위치 설정
        frame.add(button);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// 커스텀 버튼 클래스
class CustomButton2 extends JButton {
    public CustomButton2(String text) {
        super(text); // 전달된 텍스트를 설정
        setFocusPainted(false); // 포커스 테두리 제거
        setContentAreaFilled(true); // 버튼 배경 활성화
        setBorderPainted(false); // 기본 테두리 제거
        setBackground(Color.WHITE); // 버튼 배경색 흰색
        setFont(new Font("나눔고딕", Font.BOLD, 16)); // 폰트 설정
        setForeground(Color.BLACK); // 텍스트 색상
    }
}

