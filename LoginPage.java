import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class LoginPage extends JFrame
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginBtn, registerBtn;
    private JLabel statusLabel;
    private Image backgroundImage;
    private Image leftCarImage;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "users.txt";
    private static final String CARS_FILE = "cars.txt";
    private static final String PURCHASES_FILE = "purchases.txt";
    private static final Color PRIMARY_DARK = new Color(5, 10, 20);
    private static final Color CARD_DARK = new Color(20, 30, 55, 245);
    private static final Color ACCENT_BLUE = new Color(0, 180, 255);
    private static final Color ACCENT_GOLD = new Color(255, 200, 0);
    private static final Color ACCENT_GREEN = new Color(0, 255, 100);
    private static final Color ACCENT_RED = new Color(255, 80, 80);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_GRAY = new Color(180, 190, 210);
    private static final Color INPUT_BG = new Color(15, 25, 45);
    private static final Color BORDER_BLUE = new Color(0, 150, 220);
    private static final Color BUTTON_HOVER = new Color(0, 130, 200);
    private static final Color HEADER_BG = new Color(5, 8, 12, 240);
    static class User {
        String fullName;
        String email;
        String password;
        String phone;
        String location;
        String registerDate;
        User(String fullName, String email, String password) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = "";
            this.location = "";
            this.registerDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        User(String fullName, String email, String password, String phone, String location) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.location = location;
            this.registerDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    static {
        loadUsersFromFile();
        createDataFilesIfNotExist();

        if (!users.containsKey(ADMIN_USERNAME)) {
            users.put(ADMIN_USERNAME, new User("Administrator", ADMIN_USERNAME, ADMIN_PASSWORD));
            saveUserToFile(ADMIN_USERNAME, "Administrator", ADMIN_USERNAME, ADMIN_PASSWORD, "", "");
        }

        if (users.size() <= 1) {
            addDemoUser("John Doe", "user1", "pass123", "03001234567", "Karachi, Pakistan");
            addDemoUser("Ahmed Khan", "ahmed", "ahmed123", "03007654321", "Lahore, Pakistan");
            addDemoUser("Sarah Ali", "sarah", "sarah123", "03009876543", "Islamabad, Pakistan");
        }
    }
    private static void addDemoUser(String fullName, String email, String password, String phone, String location) {
        if (!users.containsKey(email)) {
            users.put(email, new User(fullName, email, password, phone, location));
            saveUserToFile(email, fullName, email, password, phone, location);
        }
    }
    private static void createDataFilesIfNotExist() {
        try {
            File userFile = new File(FILE_NAME);
            if (!userFile.exists()) userFile.createNewFile();
            File carsFile = new File(CARS_FILE);
            if (!carsFile.exists()) carsFile.createNewFile();
            File purchasesFile = new File(PURCHASES_FILE);
            if (!purchasesFile.exists()) purchasesFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating data files: " + e.getMessage());
        }
    }

    public static void registerUser(String fullName, String email, String password, String phone, String location) {
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("User already exists!");
        }
        User newUser = new User(fullName, email, password, phone, location);
        users.put(email, newUser);
        saveUserToFile(email, fullName, email, password, phone, location);
    }

    private static void saveUserToFile(String email, String fullName, String username, String password, String phone, String location) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String line = String.format("%s,%s,%s,%s,%s,%s%n",
                    fullName, username, password, phone, location,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            bw.write(line);
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public static void loadUsersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String fullName = data[0];
                    String email = data[1];
                    String password = data[2];
                    String phone = data.length > 3 ? data[3] : "";
                    String location = data.length > 4 ? data[4] : "";
                    users.put(email, new User(fullName, email, password, phone, location));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public static void refreshUsersFromFile() {
        loadUsersFromFile();
    }

    public LoginPage() {
        setTitle("Ali Motors - Premium Login Portal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadBackgroundImages();
        setupUI();
        setVisible(true);
    }

    private void loadBackgroundImages() {
        try {
            String imageUrl = "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=1920";
            backgroundImage = new ImageIcon(new URL(imageUrl)).getImage();
            String carImageUrl = "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?w=1200";
            leftCarImage = new ImageIcon(new URL(carImageUrl)).getImage();
        } catch (Exception e) {
            backgroundImage = null;
            leftCarImage = null;
        }
    }

    private void setupUI() {
        JPanel mainContainer = new JPanel(new BorderLayout()) {
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
                }
            }
        };
        mainContainer.setOpaque(false);
        mainContainer.setLayout(new BorderLayout());

        // Header with Logout Button (Top Right Corner)
        mainContainer.add(createHeader(), BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(1400, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        JPanel leftPanel = createPremiumLeftPanel();
        JPanel rightPanel = createModernRightPanel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.45;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.55;
        contentPanel.add(rightPanel, gbc);

        centerPanel.add(contentPanel);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(createFooter(), BorderLayout.SOUTH);

        add(mainContainer);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(45, 55, 70)),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel title = new JLabel("🏎️ ALI MOTORS");
        title.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 24));
        title.setForeground(ACCENT_GOLD);

        // Logout Button on Top Right Corner
        JButton logoutBtn = new JButton("🚪 LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(200, 60, 60));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(220, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(200, 60, 60));
            }
        });
        logoutBtn.addActionListener(e -> handleLogout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_GRAY);
        Timer timer = new Timer(1000, ev -> {
            timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        });
        timer.start();

        rightPanel.add(timeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        rightPanel.add(logoutBtn);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(HEADER_BG);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(45, 55, 70)),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        JLabel footerLabel = new JLabel("© 2024 Ali Motors - Premium Automotive Excellence");
        footerLabel.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_GRAY);
        footer.add(footerLabel, BorderLayout.CENTER);
        return footer;
    }

    private JPanel createPremiumLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (leftCarImage != null) {
                    g2d.drawImage(leftCarImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 180));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel brandLabel = new JLabel("ALI MOTORS");
        brandLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 48));
        brandLabel.setForeground(ACCENT_GOLD);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline1 = new JLabel("Premium Automotive Excellence");
        tagline1.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 20));
        tagline1.setForeground(ACCENT_BLUE);
        tagline1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline2 = new JLabel("Since 1995");
        tagline2.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 14));
        tagline2.setForeground(TEXT_GRAY);
        tagline2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Contact Information
        JLabel contactTitle = new JLabel("📞 CONTACT US");
        contactTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        contactTitle.setForeground(ACCENT_GREEN);
        contactTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel contactPanel = new JPanel();
        contactPanel.setOpaque(false);
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel phoneLabel = new JLabel("📱 +92 300 1234567");
        phoneLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        phoneLabel.setForeground(TEXT_WHITE);
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("✉️ info@alimotors.com");
        emailLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        emailLabel.setForeground(TEXT_WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel locationLabel = new JLabel("📍 Showroom: Karachi, Pakistan");
        locationLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        locationLabel.setForeground(TEXT_WHITE);
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contactPanel.add(phoneLabel);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactPanel.add(emailLabel);
        contactPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactPanel.add(locationLabel);

        JLabel featuresTitle = new JLabel("✦ PREMIUM FEATURES ✦");
        featuresTitle.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 16));
        featuresTitle.setForeground(ACCENT_GREEN);
        featuresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] features = {
                "• Luxury Vehicle Collection",
                "• Easy Financing Options",
                "• 24/7 Customer Support",
                "• Free Test Drive Booking",
                "• 5-Year Warranty Package"
        };

        panel.add(Box.createVerticalGlue());
        panel.add(brandLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(tagline1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(tagline2);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JSeparator sep = new JSeparator();
        sep.setForeground(ACCENT_GOLD);
        sep.setMaximumSize(new Dimension(300, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sep);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        panel.add(contactTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(contactPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(featuresTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 13));
            featureLabel.setForeground(TEXT_WHITE);
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(featureLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createModernRightPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_DARK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        panel.setPreferredSize(new Dimension(500, 600));

        JLabel title = new JLabel("WELCOME BACK");
        title.setForeground(ACCENT_GOLD);
        title.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please login to your account");
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userTypeLabel = new JLabel("Login As:");
        userTypeLabel.setForeground(TEXT_WHITE);
        userTypeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        userTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userTypeCombo = new JComboBox<>(new String[]{"👑 ADMINISTRATOR", "👤 CUSTOMER"});
        userTypeCombo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        userTypeCombo.setBackground(INPUT_BG);
        userTypeCombo.setForeground(ACCENT_BLUE);
        userTypeCombo.setMaximumSize(new Dimension(400, 40));
        userTypeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        userTypeCombo.addActionListener(e -> updateLoginButtonText());

        JLabel usernameLabel = new JLabel("Email ");
        usernameLabel.setForeground(TEXT_WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField = createStyledTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(TEXT_WHITE);
        passwordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = createStyledPasswordField();

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBtn = createStyledButton("LOGIN", ACCENT_BLUE);
        loginBtn.addActionListener(e -> handleLogin());

        registerBtn = createStyledButton("CREATE ACCOUNT", ACCENT_GREEN);
        registerBtn.addActionListener(e -> openRegisterPage());

        JLabel forgotLabel = new JLabel("Forgot Password?");
        forgotLabel.setForeground(ACCENT_GOLD);
        forgotLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        forgotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginPage.this,
                        "Please contact support at: support@alimotors.com\nor call: +92 300 1234567",
                        "Reset Password", JOptionPane.INFORMATION_MESSAGE);
            }
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setForeground(ACCENT_BLUE);
            }
            public void mouseExited(MouseEvent e) {
                forgotLabel.setForeground(ACCENT_GOLD);
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(userTypeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(userTypeCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(usernameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(passwordLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(statusLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(loginBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(forgotLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,

                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Show success message
            JOptionPane.showMessageDialog(this,
                    "✓ You have been successfully logged out!\n\n" +
                            "Thank you for using Ali Motors.\n" +
                            "We hope to see you again soon!",
                    "Logout Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            // Close current window
            dispose();

            // Open new welcome screen (LoginPage)
            SwingUtilities.invokeLater(() -> new  WelcomePage()); // or your actual welcome screen class);
        }
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(ACCENT_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_BLUE, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(400, 45));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(ACCENT_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_BLUE, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setFont(new Font("Segoe UI  Emoji", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(400, 45));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(400, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void updateLoginButtonText() {
        String selected = (String) userTypeCombo.getSelectedItem();
        if (selected != null && selected.contains("ADMINISTRATOR")) {
            loginBtn.setText("LOGIN AS ADMIN");
        } else {
            loginBtn.setText("LOGIN AS CUSTOMER");
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String selectedType = (String) userTypeCombo.getSelectedItem();
        boolean isAdmin = selectedType.contains("ADMINISTRATOR");

        if (username.isEmpty() || password.isEmpty()) {
            showStatusMessage("Please fill in all fields!", ACCENT_RED);
            return;
        }

        if (isAdmin) {
            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                showStatusMessage("✓ Admin login successful!", ACCENT_GREEN);
                JOptionPane.showMessageDialog(this,
                        "Welcome Administrator!\nYou have full access to the system.",
                        "Admin Login", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new AdminDashboard();
            } else {
                showStatusMessage("✗ Invalid admin credentials!", ACCENT_RED);
                passwordField.setText("");
            }
        } else {
            refreshUsersFromFile();
            User user = users.get(username);
            if (user != null && user.password.equals(password)) {
                showStatusMessage("✓ Welcome " + user.fullName + "!", ACCENT_GREEN);
                JOptionPane.showMessageDialog(this,
                        "Welcome " + user.fullName + "!\n" +
                                "📞 Phone: " + user.phone + "\n" +
                                "📍 Location: " + user.location + "\n" +
                                "📅 Member since: " + user.registerDate,
                        "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new UserDashboard(user.fullName);
            } else {
                showStatusMessage("✗ Invalid username or password!", ACCENT_RED);
                passwordField.setText("");
            }
        }
    }

    private void showStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        Timer timer = new Timer(3000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }

    private void openRegisterPage() {
        dispose();
        new RegisterPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginPage();
        });
    }
}