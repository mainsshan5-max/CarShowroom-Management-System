import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class WelcomePage extends JFrame {

    private Image backgroundImage;

    public WelcomePage() {
        setTitle("Ali Motors - Welcome");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        loadBackground();

        add(createMainPanel());

        setVisible(true);
    }

    private void loadBackground()
    {
        try{
            String imageUrl = "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=900";
            backgroundImage = new ImageIcon(new URL(imageUrl)).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(10, 10, 20),
                            getWidth(), getHeight(), new Color(30, 30, 50)
                    );
                    g2d.setPaint(gp);
                }

                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("🏎️🔥");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("ALI MOTORS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 60));
        title.setForeground(new Color(0, 200, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator line = new JSeparator();
        line.setMaximumSize(new Dimension(300, 3));
        line.setForeground(Color.CYAN);
        line.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Premium Car Dealership");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(new Color(0, 255, 150));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Luxury • Performance • Excellence • Speed");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        tagline.setForeground(Color.LIGHT_GRAY);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        JButton start = createButton("GET STARTED", new Color(0, 180, 255));
        JButton exit = createButton("EXIT", new Color(255, 70, 70));

        start.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        exit.addActionListener(e -> System.exit(0));

        btnPanel.add(start);
        btnPanel.add(exit);

        center.add(Box.createVerticalGlue());
        center.add(icon);
        center.add(Box.createRigidArea(new Dimension(0, 15)));
        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(line);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(subtitle);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(tagline);
        center.add(Box.createRigidArea(new Dimension(0, 25)));
        center.add(btnPanel);
        center.add(Box.createVerticalGlue());

        mainPanel.add(center, BorderLayout.CENTER);

        return mainPanel;
    }
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }
}