import javax.swing.*;
import java.awt.*;
import java.net.URL;
public class RegisterPage extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField locationField;  // ✅ NEW - Location field
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private Image backgroundImage;
    private static final Color PRIMARY_DARK = new Color(5, 10, 20);
    private static final Color CARD_DARK = new Color(20, 30, 55, 245);
    private static final Color ACCENT_BLUE = new Color(0, 180, 255);
    private static final Color ACCENT_GREEN = new Color(0, 255, 150);
    private static final Color ACCENT_GOLD = new Color(255, 200, 0);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_GRAY = new Color(180, 190, 210);
    private static final Color INPUT_BG = new Color(15, 25, 45);
    private static final Color BORDER_BLUE = new Color(0, 150, 220);
    private static final Color BORDER_COLOR = new Color(50, 50, 70);
    private static final Color TEXT_LIGHT = new Color(220, 220, 230);
    private static final Color TEXT_DARK = new Color(150, 150, 170);
    private static final int FIELD_WIDTH = 400;
    private static final int FIELD_HEIGHT = 48;
    private static final int CONTAINER_HEIGHT = 75;

    public RegisterPage() {
        setTitle("Ali Motors - Create Account");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        loadBackgroundImage();

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 210));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(5, 15, 30),
                            getWidth(), getHeight(), new Color(15, 30, 50));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        JPanel registerCard = createRegisterCard();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(registerCard, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void loadBackgroundImage() {
        try {
            String imageUrl = "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=1920";
            URL url = new URL(imageUrl);
            backgroundImage = new ImageIcon(url).getImage();
            System.out.println("✅ Background image loaded for RegisterPage!");
        } catch (Exception e) {
            System.out.println("❌ Background image load failed: " + e.getMessage());
            backgroundImage = null;
        }
    }

    private JPanel createRegisterCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_DARK);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        card.setPreferredSize(new Dimension(520, 800));  // Increased height for location field
        card.setMaximumSize(new Dimension(520, 800));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel("🚗✨");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        title.setForeground(ACCENT_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Join Ali Motors Premium Community");
        subtitle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitle);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(FIELD_WIDTH, 1));
        separator.setForeground(BORDER_BLUE);

        // Full Name Field
        nameField = new JTextField();
        styleTextField(nameField);
        JPanel nameContainer = createFieldContainer("👤 Full Name", nameField);
        emailField = new JTextField();
        styleTextField(emailField);
        JPanel emailContainer = createFieldContainer("📧 Email Address", emailField);
        phoneField = new JTextField();
        styleTextField(phoneField);
        JPanel phoneContainer = createFieldContainer("📞 Phone Number", phoneField);

        locationField = new JTextField();
        styleTextField(locationField);
        JPanel locationContainer = createFieldContainer("📍 Location / City", locationField);

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        JPanel passwordContainer = createFieldContainer("🔒 Password", passwordField);

        confirmPasswordField = new JPasswordField();
        styleTextField(confirmPasswordField);
        JPanel confirmContainer = createFieldContainer("🔒 Confirm Password", confirmPasswordField);

        JButton registerBtn = new JButton("✨ REGISTER NOW");
        styleButton(registerBtn, ACCENT_GREEN);
        registerBtn.setMaximumSize(new Dimension(FIELD_WIDTH, 55));
        registerBtn.setPreferredSize(new Dimension(FIELD_WIDTH, 55));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        loginLabel.setForeground(TEXT_GRAY);

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        loginBtn.setForeground(ACCENT_GOLD);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setForeground(new Color(255, 220, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setForeground(ACCENT_GOLD);
            }
        });

        loginPanel.add(loginLabel);
        loginPanel.add(loginBtn);

        JLabel passwordHint = new JLabel("Password must be at least 4 characters");
        passwordHint.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 10));
        passwordHint.setForeground(TEXT_GRAY);
        passwordHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(separator);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(nameContainer);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(emailContainer);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(phoneContainer);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(locationContainer);  // ✅ NEW - Location field added
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(passwordContainer);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(confirmContainer);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(passwordHint);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(registerBtn);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(loginPanel);
        card.add(Box.createVerticalGlue());

        registerBtn.addActionListener(e -> handleRegister());
        loginBtn.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        return card;
    }

    private JPanel createFieldContainer(String labelText, JComponent field) {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.setMaximumSize(new Dimension(FIELD_WIDTH, CONTAINER_HEIGHT));
        container.setPreferredSize(new Dimension(FIELD_WIDTH, CONTAINER_HEIGHT));
        container.setMinimumSize(new Dimension(FIELD_WIDTH, CONTAINER_HEIGHT));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        label.setForeground(TEXT_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));

        container.add(label);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(field);

        return container;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(ACCENT_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_BLUE),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String location = locationField.getText().trim();  // ✅ NEW - Get location
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || location.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!\nAll fields are required.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address!\nExample: user@example.com",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phone.length() < 10) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid phone number!\nMinimum 10 digits required.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (location.length() < 3) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid location!\nExample: Karachi, Lahore, Islamabad",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!\nPlease re-enter your password.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 4 characters long!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        LoginPage.refreshUsersFromFile();
        try {
            LoginPage.registerUser(name, email, password, phone, location);
            JOptionPane.showMessageDialog(this,
                    "✅ Registration Successful!\n\n" +
                            "Welcome " + name + " to Ali Motors!\n" +
                            "📍 Location: " + location + "\n" +
                            "📞 Phone: " + phone + "\n" +
                            "You can now login with your credentials.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            new LoginPage();
            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Registration Failed!\n\n" +
                            "User with this email already exists.\n" +
                            "Please use a different email address or try logging in.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Registration Failed!\n\n" +
                            "An error occurred: " + e.getMessage() + "\n" +
                            "Please try again later.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}