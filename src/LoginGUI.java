import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginGUI {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 12345;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540,960);

        // 로그인 화면 추가
        LoginPanel loginPanel = new LoginPanel(frame);
        frame.add(loginPanel);

        // 프레임 표시
        frame.setVisible(true);
    }
}

// 로그인 화면 클래스
class LoginPanel extends JPanel {
    private JFrame parentFrame;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null);


        // 배경 패널 생성
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(new Color(255, 255, 255)); // 하늘색 배경 설정
        backgroundPanel.setBounds(0, 0, 540,960);
        parentFrame.add(backgroundPanel);

        // ImageIcon으로 이미지 로드 및 크기 조정
        ImageIcon originalIcon = new ImageIcon("resources/images/diary_icon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // JLabel 생성 후 이미지 설정
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(220, 60, 80, 80); // 위치와 크기 설정
        backgroundPanel.add(imageLabel);

        // 제목 레이블
        JLabel titleLabel = new JLabel("English Diary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setBounds(65, 150, 400, 70);
        backgroundPanel.add(titleLabel);

        JLabel titleLabel2 = new JLabel("Mate");
        titleLabel2.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel2.setBounds(185, 210, 400, 70);
        backgroundPanel.add(titleLabel2);

        // 아이디 입력 필드
        HintTextField idField = new HintTextField("Enter your ID");
        idField.setBounds(20, 350, 480, 45);
        backgroundPanel.add(idField);

        // 비밀번호 입력 필드
        HintPasswordField passwordField = new HintPasswordField("Enter your Password");
        passwordField.setBounds(20, 430, 480, 45);
        backgroundPanel.add(passwordField);

        // 로그인 버튼
        CustomButton loginButton = new CustomButton("로그인");
        loginButton.setBounds(20, 530, 480, 45);
        backgroundPanel.add(loginButton);

        // 로그인 버튼 클릭 이벤트
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());

                System.out.println(id);
                System.out.println(password);

                try {
                    socket = new Socket(LoginGUI.SERVER_ADDRESS, LoginGUI.SERVER_PORT);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    // ID를 서버로 전송
                    out.println("Username : " + id);

                    // 서버로부터 환영 메시지 받기
                    String welcomeMessage = in.readLine();

                    System.out.println(welcomeMessage);

                    // 유효성 검사
                    if (welcomeMessage.startsWith("Welcome")) {
                        //JOptionPane.showMessageDialog(null, "Login Successful!");

                        // MainGUI 실행
                        ChallengeGUI.main(new String[]{}); // MainGUI의 main 메서드 실행
                        parentFrame.dispose(); // 현재 로그인 창 닫기
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
                        closeConnection();
                    }

                }catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Server connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
