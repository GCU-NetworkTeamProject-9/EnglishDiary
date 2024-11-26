import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MakeChallengeGUI {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Make Challenge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 960);

        MakeChallengePanel makeChallengePanel = new MakeChallengePanel(frame);
        frame.add(makeChallengePanel);

        frame.setVisible(true);
    }
}

class MakeChallengePanel extends JPanel {
    private JFrame parentFrame;

    public MakeChallengePanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null);
        setBackground(new Color(224, 255, 255));

        JLabel titleLabel = new JLabel("새 챌린지 만들기");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 25));
        titleLabel.setBounds(150, 20, 300, 50);
        add(titleLabel);

        JLabel nameLabel = new JLabel("챌린지 제목:");
        nameLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        nameLabel.setBounds(50, 100, 150, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        add(nameField);

        JLabel startDateLabel = new JLabel("시작 날짜:");
        startDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        startDateLabel.setBounds(50, 150, 150, 30);
        add(startDateLabel);

        JTextField startDateField = new JTextField("YYYY-MM-DD");
        startDateField.setBounds(200, 150, 250, 30);
        add(startDateField);

        JLabel endDateLabel = new JLabel("종료 날짜:");
        endDateLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        endDateLabel.setBounds(50, 200, 150, 30);
        add(endDateLabel);

        JTextField endDateField = new JTextField("YYYY-MM-DD");
        endDateField.setBounds(200, 200, 250, 30);
        add(endDateField);

        JLabel participantsLabel = new JLabel("최대 참여자 수:");
        participantsLabel.setFont(new Font("나눔고딕", Font.PLAIN, 18));
        participantsLabel.setBounds(50, 250, 150, 30);
        add(participantsLabel);

        JTextField participantsField = new JTextField();
        participantsField.setBounds(200, 250, 250, 30);
        add(participantsField);

        CustomButton createButton = new CustomButton("생성하기");
        createButton.setBounds(200, 320, 120, 40);
        add(createButton);

        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();
            String participants = participantsField.getText().trim();

            // 입력값 검증
            if (name.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || participants.isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 날짜 검증
                LocalDate parsedStartDate = LocalDate.parse(startDate);
                LocalDate parsedEndDate = LocalDate.parse(endDate);
                if (parsedStartDate.isAfter(parsedEndDate)) {
                    JOptionPane.showMessageDialog(this, "시작 날짜는 종료 날짜보다 빨라야 합니다.", "날짜 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 참여자 수 검증
                int maxParticipants = Integer.parseInt(participants);
                if (maxParticipants <= 0) {
                    JOptionPane.showMessageDialog(this, "참여자 수는 1명 이상이어야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 서버에 챌린지 생성 요청
                PrintWriter out = new PrintWriter(LoginGUI.socket.getOutputStream(), true);
                out.println("createChallenge," + name + "," + startDate + "," + endDate + "," + maxParticipants);

                JOptionPane.showMessageDialog(this,
                        "챌린지 제목: " + name +
                                "\n시작 날짜: " + startDate +
                                "\n종료 날짜: " + endDate +
                                "\n최대 참여자 수: " + participants,
                        "챌린지 생성 성공", JOptionPane.INFORMATION_MESSAGE);

                parentFrame.dispose();
                ChallengeGUI.main(new String[]{});

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.", "날짜 오류", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "참여자 수는 숫자여야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버와의 연결 중 오류가 발생했습니다.", "서버 오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
