import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
public class AdminDashboard extends JFrame {
    private JTable carTable, customerTable, userTable, saleTable;
    private DefaultTableModel carModel, customerModel, userModel, saleModel;
    private JLabel revenueLabel, totalCarsLabel, totalCustomersLabel, totalSalesLabel;
    private List<Car> cars;
    private List<Customer> customers;
    private List<Sale> sales;
    private List<User> users;
    private JPanel mainContent;
    private CardLayout cardLayout;
    private JButton currentSelectedBtn = null;
    private Image backgroundImage;
    private String loggedInUser = "admin";

    // Colors
    private static final Color DARK_BG = new Color(18, 25, 35);
    private static final Color SIDEBAR_BG = new Color(15, 20, 30);
    private static final Color HEADER_BG = new Color(10, 15, 25);
    private static final Color CARD_BG = new Color(25, 35, 50);
    private static final Color ACCENT_BLUE = new Color(0, 180, 255);
    private static final Color ACCENT_GREEN = new Color(0, 255, 150);
    private static final Color ACCENT_RED = new Color(255, 70, 70);
    private static final Color ACCENT_GOLD = new Color(255, 200, 0);
    private static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    private static final Color ACCENT_ORANGE = new Color(255, 120, 50);
    private static final Color ACCENT_CYAN = new Color(0, 200, 200);
    private static final Color TEXT_LIGHT = new Color(220, 230, 240);
    private static final Color TEXT_DARK = new Color(150, 165, 180);
    private static final Color TABLE_HEADER_BG = new Color(30, 40, 55);
    private static final Color TABLE_SELECTION_BG = new Color(0, 100, 150);
    private static final Color BORDER_COLOR = new Color(45, 55, 70);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);

    static class Car
    {
        int id; String brand, model, year, color, fuelType, transmission; double price; boolean available; String imageUrl;
        Car(int id, String brand, String model, String year, double price, String color, String fuel, String trans, boolean avail, String imageUrl) {
            this.id = id; this.brand = brand; this.model = model; this.year = year;
            this.price = price; this.color = color; this.fuelType = fuel; this.transmission = trans;
            this.available = avail; this.imageUrl = imageUrl;
        }
    }

    static class Customer {
        int id; String name, phone, email, address, cnic;
        Customer(int id, String name, String phone, String email, String address, String cnic) {
            this.id = id; this.name = name; this.phone = phone; this.email = email; this.address = address; this.cnic = cnic;
        }
    }

    static class Sale {
        int id, carId, customerId; double amount; String date, payment;
        Sale(int id, int carId, int customerId, double amount, String payment, String date) {
            this.id = id; this.carId = carId; this.customerId = customerId; this.amount = amount; this.payment = payment; this.date = date;
        }
    }

    static class User
    {
        String username, password, name, email, phone, role, status;
        User(String username, String password, String name, String email, String phone, String role, String status) {
            this.username = username; this.password = password; this.name = name; this.email = email; this.phone = phone; this.role = role; this.status = status;
        }
    }

    public AdminDashboard() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {}
        }

        loadBackgroundImage();
        initData();
        setupUI();
        loadTables();
        updateStats();
    }

    private void loadBackgroundImage()
    {
        try {
            String imageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=1920";
            URL url = new URL(imageUrl);
            backgroundImage = new ImageIcon(url).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }
    }

    private void initData()
    {
        cars = new ArrayList<>();
        cars.add(new Car(1, "Toyota", "Corolla", "2024", 4500000, "Pearl White", "Petrol", "Automatic", true,
                "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=200"));
        cars.add(new Car(2, "Honda", "Civic", "2024", 5200000, "Black Pearl", "Petrol", "Automatic", true,
                "https://images.unsplash.com/photo-1606016159991-dfe4f2746ad5?w=200"));
        cars.add(new Car(3, "Suzuki", "Swift", "2023", 2800000, "Racing Red", "Petrol", "Manual", true,
                "https://images.unsplash.com/photo-1618842676031-5e2c5e4c2d5d?w=200"));
        cars.add(new Car(4, "Kia", "Sportage", "2024", 6800000, "Ocean Blue", "Diesel", "Automatic", true,
                "https://images.unsplash.com/photo-1631295868223-63228b40f9e4?w=200"));

        customers = new ArrayList<>();
        customers.add(new Customer(1, "Ali Khan", "+92 300 1234567", "ali.khan@email.com", "DHA Phase 8, Karachi", "42101-1234567-8"));
        customers.add(new Customer(2, "Sara Ahmed", "+92 311 9876543", "sara.ahmed@email.com", "Gulberg III, Lahore", "42101-8765432-1"));

        sales = new ArrayList<>();
        sales.add(new Sale(1, 1, 1, 4500000, "Bank Transfer", "2024-01-15"));
        sales.add(new Sale(2, 2, 2, 5200000, "Credit Card", "2024-02-20"));

        users = new ArrayList<>();
        users.add(new User("admin", "admin123", "Administrator", "admin@alimotors.com", "+92 300 0000000", "Super Admin", "Active"));
        users.add(new User("manager", "manager123", "John Doe", "manager@alimotors.com", "+92 301 1122333", "Manager", "Active"));
    }

    private void setupUI()
    {
        setTitle("ALI MOTORS PREMIUM - Executive Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    GradientPaint overlay = new GradientPaint(0, 0, new Color(0, 0, 0, 200),
                            getWidth(), getHeight(), new Color(0, 0, 0, 220));
                    g2d.setPaint(overlay);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(18, 25, 35),
                            getWidth(), getHeight(), new Color(8, 12, 20));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainContainer.setLayout(new BorderLayout());

        mainContainer.add(createHeader(), BorderLayout.NORTH);
        mainContainer.add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainContent.add(createDashboard(), "dashboard");
        mainContent.add(createCarManagement(), "cars");
        mainContent.add(createCustomerManagement(), "customers");
        mainContent.add(createUserManagement(), "users");
        mainContent.add(createSalesReport(), "sales");
        mainContent.add(createInventoryReport(), "inventory");
        mainContent.add(createLoanCalculator(), "loan");
        mainContent.add(createSellCar(), "sell");
        mainContent.add(createSettings(), "settings");

        mainContainer.add(mainContent, BorderLayout.CENTER);
        mainContainer.add(createFooter(), BorderLayout.SOUTH);

        add(mainContainer);
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBackground(new Color(10, 15, 25, 200));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🚗");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JLabel title = new JLabel("ALI MOTORS PREMIUM - Executive Dashboard");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        title.setForeground(ACCENT_GOLD);

        JLabel subtitle = new JLabel(" | Enterprise Edition v3.0");
        subtitle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_DARK);

        leftPanel.add(iconLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        leftPanel.add(title);
        leftPanel.add(subtitle);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        timeLabel.setForeground(ACCENT_BLUE);

        new javax.swing.Timer(1000, e -> {
            timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a | EEE, MMM d, yyyy")));
        }).start();

        JLabel adminBadge = new JLabel("👑 ADMINISTRATOR");
        adminBadge.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        adminBadge.setForeground(ACCENT_GREEN);
        adminBadge.setBackground(new Color(0, 100, 150, 80));
        adminBadge.setOpaque(true);
        adminBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        JLabel notificationIcon = new JLabel("🔔");
        notificationIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        notificationIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(notificationIcon);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rightPanel.add(timeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rightPanel.add(adminBadge);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setOpaque(false);
        sidebarPanel.setBackground(new Color(15, 20, 30, 230));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        JLabel logoLabel = new JLabel("🏎️");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 55));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandLabel = new JLabel("ALI MOTORS");
        brandLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        brandLabel.setForeground(ACCENT_GOLD);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel versionLabel = new JLabel("PREMIUM EDITION");
        versionLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        versionLabel.setForeground(ACCENT_BLUE);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(brandLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        logoPanel.add(versionLabel);
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        String[][] items = {
                {"📊 Dashboard", "dashboard"},
                {"🚗 Car Management", "cars"},
                {"👥 Customer Management", "customers"},
                {"👤 User Management", "users"},
                {"💰 Sales Report", "sales"},
                {"📦 Inventory Report", "inventory"},
                {"💵 Loan Calculator", "loan"},
                {"🛒 Sell Car", "sell"},
                {"⚙️ Settings", "settings"}
        };

        for (String[] item : items) {
            JButton btn = createSidebarButton(item[0]);
            String card = item[1];
            btn.addActionListener(e -> {
                cardLayout.show(mainContent, card);
                setSelectedButton(btn);
            });
            sidebarPanel.add(btn);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebarPanel.add(Box.createVerticalGlue());
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setOpaque(false);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel userAvatar = new JLabel("👤");
        userAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        userAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userName = new JLabel("Administrator");
        userName.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        userName.setForeground(TEXT_LIGHT);
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);

        userInfoPanel.add(userAvatar);
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userInfoPanel.add(userName);
        sidebarPanel.add(userInfoPanel);
        JButton logoutBtn = createSidebarButton("🚪 Logout");
        logoutBtn.setBackground(new Color(255, 70, 70, 30));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(AdminDashboard.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage();
            }
        });
        sidebarPanel.add(logoutBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane scrollPane = new JScrollPane(sidebarPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(16);
        verticalBar.setBackground(new Color(30, 40, 55));
        verticalBar.setForeground(ACCENT_BLUE);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setForeground(TEXT_LIGHT);
        btn.setOpaque(false);
        btn.setBackground(new Color(15, 20, 30, 230));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != currentSelectedBtn) {
                    btn.setForeground(ACCENT_BLUE);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != currentSelectedBtn) {
                    btn.setForeground(TEXT_LIGHT);
                }
            }
        });

        return btn;
    }

    private void setSelectedButton(JButton selected) {
        if (currentSelectedBtn != null) {
            currentSelectedBtn.setBackground(new Color(15, 20, 30, 230));
            currentSelectedBtn.setForeground(TEXT_LIGHT);
        }
        currentSelectedBtn = selected;
        selected.setBackground(new Color(0, 100, 150, 150));
        selected.setForeground(Color.WHITE);
    }

    private JPanel createDashboard() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        totalCarsLabel = createStatCard(panel, gbc, 0, 0, "Total Inventory", "🚗", "0", ACCENT_BLUE);
        totalCustomersLabel = createStatCard(panel, gbc, 0, 1, "Active Customers", "👥", "0", ACCENT_GREEN);
        totalSalesLabel = createStatCard(panel, gbc, 1, 0, "Total Transactions", "💰", "0", ACCENT_GOLD);
        revenueLabel = createStatCard(panel, gbc, 1, 1, "Total Revenue", "💵", "Rs. 0", ACCENT_PURPLE);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        JLabel welcomeLabel = new JLabel("Welcome back, Administrator!");
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        welcomeLabel.setForeground(ACCENT_GOLD);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel quoteLabel = new JLabel("\"Drive your success with Ali Motors Premium\"");
        quoteLabel.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));
        quoteLabel.setForeground(TEXT_DARK);
        quoteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(quoteLabel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(welcomePanel, gbc);

        return panel;
    }

    private JLabel createStatCard(JPanel panel, GridBagConstraints gbc, int row, int col, String title, String icon, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
            }
        };
        card.setBackground(new Color(25, 35, 50, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setPreferredSize(new Dimension(320, 140));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        JPanel textPanel = new JPanel(new BorderLayout(0, 5));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_DARK);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);

        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor, 2),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor, 1),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
        });

        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(card, gbc);

        return valueLabel;
    }

    private JPanel createLoanCalculator() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("💰 Vehicle Loan Calculator");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(ACCENT_GOLD);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel carLabel = new JLabel("Select Vehicle:");
        carLabel.setForeground(TEXT_LIGHT);
        panel.add(carLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> carCombo = new JComboBox<>();
        carCombo.setBackground(new Color(35, 45, 60));
        carCombo.setForeground(TEXT_LIGHT);
        for (Car car : cars) {
            carCombo.addItem(car.brand + " " + car.model + " - Rs." + String.format("%,.0f", car.price));
        }
        panel.add(carCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel downLabel = new JLabel("Down Payment (Rs):");
        downLabel.setForeground(TEXT_LIGHT);
        panel.add(downLabel, gbc);

        gbc.gridx = 1;
        JTextField downPaymentField = createStyledTextField();
        downPaymentField.setText("500000");
        panel.add(downPaymentField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel rateLabel = new JLabel("Interest Rate (%):");
        rateLabel.setForeground(TEXT_LIGHT);
        panel.add(rateLabel, gbc);

        gbc.gridx = 1;
        JTextField rateField = createStyledTextField();
        rateField.setText("12");
        panel.add(rateField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel tenureLabel = new JLabel("Loan Tenure (Years):");
        tenureLabel.setForeground(TEXT_LIGHT);
        panel.add(tenureLabel, gbc);

        gbc.gridx = 1;
        JComboBox<Integer> tenureCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        tenureCombo.setBackground(new Color(35, 45, 60));
        tenureCombo.setForeground(TEXT_LIGHT);
        panel.add(tenureCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JButton calculateBtn = create3DButton("📊 Calculate EMI", ACCENT_BLUE);
        panel.add(calculateBtn, gbc);

        gbc.gridy = 6;
        JPanel resultPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        resultPanel.setBackground(new Color(25, 35, 50, 230));
        resultPanel.setBorder(BorderFactory.createLineBorder(ACCENT_GREEN));

        JLabel loanAmountLabel = new JLabel("Loan Amount: Rs. 0");
        JLabel emiLabel = new JLabel("Monthly EMI: Rs. 0");
        JLabel totalInterestLabel = new JLabel("Total Interest: Rs. 0");
        JLabel totalPaymentLabel = new JLabel("Total Payment: Rs. 0");

        resultPanel.add(loanAmountLabel);
        resultPanel.add(emiLabel);
        resultPanel.add(totalInterestLabel);
        resultPanel.add(totalPaymentLabel);
        panel.add(resultPanel, gbc);

        calculateBtn.addActionListener(e -> {
            try {
                Car selectedCar = cars.get(carCombo.getSelectedIndex());
                double carPrice = selectedCar.price;
                double downPayment = Double.parseDouble(downPaymentField.getText());
                double annualRate = Double.parseDouble(rateField.getText());
                int years = (Integer) tenureCombo.getSelectedItem();

                double loanAmount = carPrice - downPayment;
                double monthlyRate = annualRate / 12 / 100;
                int months = years * 12;
                double emi = loanAmount * monthlyRate * Math.pow(1 + monthlyRate, months) / (Math.pow(1 + monthlyRate, months) - 1);
                double totalPayment = emi * months;
                double totalInterest = totalPayment - loanAmount;

                loanAmountLabel.setText("Loan Amount: Rs. " + String.format("%,.0f", loanAmount));
                emiLabel.setText("Monthly EMI: Rs. " + String.format("%,.0f", emi));
                totalInterestLabel.setText("Total Interest: Rs. " + String.format("%,.0f", totalInterest));
                totalPaymentLabel.setText("Total Payment: Rs. " + String.format("%,.0f", totalPayment));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid numbers!");
            }
        });

        return panel;
    }

    private JPanel createSellCar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;  // Important: Give weight to columns

        JLabel title = new JLabel("🛒 Sell Car");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(ACCENT_GOLD);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel carLabel = new JLabel("Select Vehicle:");
        carLabel.setForeground(TEXT_LIGHT);
        carLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        panel.add(carLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> carCombo = new JComboBox<>();
        carCombo.setBackground(new Color(35, 45, 60));
        carCombo.setForeground(TEXT_LIGHT);
        carCombo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        for (Car car : cars) {
            if (car.available) {
                carCombo.addItem(car.id + " - " + car.brand + " " + car.model);
            }
        }
        panel.add(carCombo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel customerLabel = new JLabel("Select Customer:");
        customerLabel.setForeground(TEXT_LIGHT);
        customerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        panel.add(customerLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> customerCombo = new JComboBox<>();
        customerCombo.setBackground(new Color(35, 45, 60));
        customerCombo.setForeground(TEXT_LIGHT);
        customerCombo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        for (Customer customer : customers) {
            customerCombo.addItem(customer.id + " - " + customer.name);
        }
        panel.add(customerCombo, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentLabel.setForeground(TEXT_LIGHT);
        paymentLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        panel.add(paymentLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"💵 Cash", "🏦 Bank Transfer", "💳 Credit Card"});
        paymentCombo.setBackground(new Color(35, 45, 60));
        paymentCombo.setForeground(TEXT_LIGHT);
        paymentCombo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        panel.add(paymentCombo, gbc);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton sellBtn = new JButton("💰 SELL VEHICLE");
        sellBtn.setBackground(ACCENT_GREEN);
        sellBtn.setForeground(Color.BLACK);
        sellBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        sellBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sellBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        sellBtn.addActionListener(e -> {
            if (carCombo.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(panel, "Please select a vehicle!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (customerCombo.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(panel, "Please select a customer!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Car selectedCar = cars.get(carCombo.getSelectedIndex());
            Customer selectedCustomer = customers.get(customerCombo.getSelectedIndex());
            String payment = (String) paymentCombo.getSelectedItem();

            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Confirm Sale:\n\n" +
                            "Vehicle: " + selectedCar.brand + " " + selectedCar.model + "\n" +
                            "Customer: " + selectedCustomer.name + "\n" +
                            "Price: Rs. " + String.format("%,.0f", selectedCar.price) + "\n" +
                            "Payment: " + payment,
                    "Confirm Sale", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                selectedCar.available = false;
                JOptionPane.showMessageDialog(panel,
                        "✅ Vehicle sold successfully to " + selectedCustomer.name + "!\n" +
                                "Amount: Rs. " + String.format("%,.0f", selectedCar.price),
                        "Sale Complete", JOptionPane.INFORMATION_MESSAGE);
                carCombo.removeAllItems();
                for (Car car : cars) {
                    if (car.available) {
                        carCombo.addItem(car.id + " - " + car.brand + " " + car.model);
                    }
                }
            }
        });
        panel.add(sellBtn, gbc);

        return panel;
    }

    private JPanel createCarManagement() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setOpaque(false);

        JButton addBtn = create3DButton(" Add Car", ACCENT_GREEN);
        JButton editBtn = create3DButton("Edit", ACCENT_BLUE);
        JButton deleteBtn = create3DButton(" Delete", ACCENT_RED);
        JButton refreshBtn = create3DButton(" Refresh", ACCENT_ORANGE);

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);

        String[] cols = {"ID", "Image", "Brand", "Model", "Year", "Price", "Color", "Fuel", "Trans", "Status"};
        carModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) return ImageIcon.class;
                return Object.class;
            }
        };
        carTable = createStyledTable(carModel);
        carTable.setRowHeight(70);

        addBtn.addActionListener(e -> showAddCarDialog());
        editBtn.addActionListener(e -> editCar());
        deleteBtn.addActionListener(e -> deleteCar());
        refreshBtn.addActionListener(e -> loadCarTable());

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(carTable), BorderLayout.CENTER);

        return panel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(25, 35, 50, 230) : new Color(30, 40, 55, 230));
                }
                c.setForeground(TEXT_LIGHT);
                return c;
            }
        };
        table.setForeground(TEXT_LIGHT);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(5, 5));

        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(ACCENT_BLUE);
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        return table;
    }

    private JButton create3DButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, bgColor.brighter(), 0, getHeight(), bgColor.darker());
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(SHADOW_COLOR);
                g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(ACCENT_GOLD);
            }
            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    private void showAddCarDialog() {
        JDialog dialog = new JDialog(this, "Add New Car", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(DARK_BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(DARK_BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField brandField = createStyledTextField();
        JTextField modelField = createStyledTextField();
        JTextField yearField = createStyledTextField();
        JTextField priceField = createStyledTextField();
        JTextField colorField = createStyledTextField();
        JTextField imageUrlField = createStyledTextField();
        imageUrlField.setText("https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=200");

        JComboBox<String> fuelCombo = createStyledComboBox(new String[]{"Petrol", "Diesel", "Electric"});
        JComboBox<String> transCombo = createStyledComboBox(new String[]{"Manual", "Automatic"});

        addFormField(form, gbc, "Brand:", brandField, 0);
        addFormField(form, gbc, "Model:", modelField, 1);
        addFormField(form, gbc, "Year:", yearField, 2);
        addFormField(form, gbc, "Price:", priceField, 3);
        addFormField(form, gbc, "Color:", colorField, 4);
        addFormField(form, gbc, "Fuel:", fuelCombo, 5);
        addFormField(form, gbc, "Transmission:", transCombo, 6);
        addFormField(form, gbc, "Image URL:", imageUrlField, 7);

        JButton saveBtn = create3DButton(" Save", ACCENT_GREEN);
        saveBtn.addActionListener(e -> {
            try {
                int newId = cars.size() + 1;
                Car car = new Car(newId, brandField.getText(), modelField.getText(), yearField.getText(),
                        Double.parseDouble(priceField.getText()), colorField.getText(),
                        (String) fuelCombo.getSelectedItem(), (String) transCombo.getSelectedItem(),
                        true, imageUrlField.getText());
                cars.add(car);
                loadCarTable();
                updateStats();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "✅ Car added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        form.add(saveBtn, gbc);

        dialog.add(form);
        dialog.setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBackground(new Color(35, 45, 60));
        field.setForeground(TEXT_LIGHT);
        field.setCaretColor(ACCENT_BLUE);
        field.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(35, 45, 60));
        combo.setForeground(TEXT_LIGHT);
        return combo;
    }

    private void editCar() {
        int row = carTable.getSelectedRow();
        if (row >= 0) {
            JOptionPane.showMessageDialog(this, "Edit feature coming soon!");
        } else {
            JOptionPane.showMessageDialog(this, "Select a car to edit!");
        }
    }

    private void deleteCar() {
        int row = carTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this car?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cars.remove(row);
                loadCarTable();
                updateStats();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a car to delete!");
        }
    }

    private JPanel createCustomerManagement() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setOpaque(false);

        JButton addBtn = create3DButton("➕ Add Customer", ACCENT_GREEN);
        JButton deleteBtn = create3DButton("🗑️ Delete", ACCENT_RED);

        toolbar.add(addBtn);
        toolbar.add(deleteBtn);

        String[] cols = {"ID", "Name", "Phone", "Email", "Address", "CNIC"};
        customerModel = new DefaultTableModel(cols, 0);
        customerTable = createStyledTable(customerModel);

        addBtn.addActionListener(e -> showAddCustomerDialog());
        deleteBtn.addActionListener(e -> deleteCustomer());

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        return panel;
    }

    private void showAddCustomerDialog() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField cnicField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(DARK_BG);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("CNIC:")); panel.add(cnicField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int newId = customers.size() + 1;
            customers.add(new Customer(newId, nameField.getText(), phoneField.getText(),
                    emailField.getText(), addressField.getText(), cnicField.getText()));
            loadCustomerTable();
            updateStats();
        }
    }

    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            customers.remove(row);
            loadCustomerTable();
            updateStats();
        }
    }

    private JPanel createUserManagement() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setOpaque(false);

        JButton addBtn = create3DButton("➕ Add User", ACCENT_GREEN);

        toolbar.add(addBtn);

        String[] cols = {"Username", "Name", "Email", "Phone", "Role", "Status"};
        userModel = new DefaultTableModel(cols, 0);
        userTable = createStyledTable(userModel);

        addBtn.addActionListener(e -> {
            JTextField usernameField = new JTextField();
            JPasswordField passField = new JPasswordField();
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{"User", "Manager", "Admin"});

            JPanel userPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            userPanel.setBackground(DARK_BG);
            userPanel.add(new JLabel("Username:")); userPanel.add(usernameField);
            userPanel.add(new JLabel("Password:")); userPanel.add(passField);
            userPanel.add(new JLabel("Name:")); userPanel.add(nameField);
            userPanel.add(new JLabel("Email:")); userPanel.add(emailField);
            userPanel.add(new JLabel("Phone:")); userPanel.add(phoneField);
            userPanel.add(new JLabel("Role:")); userPanel.add(roleCombo);

            int result = JOptionPane.showConfirmDialog(this, userPanel, "Add User", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                users.add(new User(usernameField.getText(), new String(passField.getPassword()),
                        nameField.getText(), emailField.getText(), phoneField.getText(),
                        (String) roleCombo.getSelectedItem(), "Active"));
                loadUserTable();
            }
        });

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSalesReport() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel(" Sales Reports", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        title.setForeground(ACCENT_GOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Car", "Customer", "Amount", "Payment", "Date"};
        saleModel = new DefaultTableModel(cols, 0);
        saleTable = createStyledTable(saleModel);
        loadSalesTable();
        panel.add(new JScrollPane(saleTable), BorderLayout.CENTER);

        JButton reportBtn = create3DButton("Generate Report", ACCENT_BLUE);
        reportBtn.addActionListener(e -> generateSalesReport());
        panel.add(reportBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void loadSalesTable() {
        saleModel.setRowCount(0);
        for (Sale sale : sales) {
            String carName = cars.stream().filter(c -> c.id == sale.carId).findFirst().map(c -> c.brand + " " + c.model).orElse("Unknown");
            String customerName = customers.stream().filter(c -> c.id == sale.customerId).findFirst().map(c -> c.name).orElse("Unknown");
            saleModel.addRow(new Object[]{sale.id, carName, customerName,
                    "Rs. " + String.format("%,.0f", sale.amount), sale.payment, sale.date});
        }
    }

    private void generateSalesReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALI MOTORS SALES REPORT ===\n\n");
        double total = 0;
        for (Sale sale : sales) {
            String carName = cars.stream().filter(c -> c.id == sale.carId).findFirst().map(c -> c.brand + " " + c.model).orElse("Unknown");
            sb.append("Sale #").append(sale.id).append(": ").append(carName)
                    .append(" - Rs.").append(String.format("%,.0f", sale.amount)).append("\n");
            total += sale.amount;
        }
        sb.append("\nTotal Sales: Rs.").append(String.format("%,.0f", total));
        JTextArea ta = new JTextArea(sb.toString());
        ta.setBackground(DARK_BG);
        ta.setForeground(ACCENT_GREEN);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Sales Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createInventoryReport() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("📦 Inventory Report", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        title.setForeground(ACCENT_GOLD);
        panel.add(title, BorderLayout.NORTH);

        JTextArea reportArea = new JTextArea();
        reportArea.setBackground(new Color(25, 35, 50, 230));
        reportArea.setForeground(TEXT_LIGHT);
        reportArea.setEditable(false);

        JButton generateBtn = create3DButton(" Generate", ACCENT_BLUE);
        generateBtn.addActionListener(e -> {
            StringBuilder report = new StringBuilder();
            report.append("INVENTORY REPORT\n");
            report.append("=".repeat(50)).append("\n");
            for (Car car : cars) {
                report.append(String.format("%d. %s %s (%s) - Rs.%,.0f [%s]\n",
                        car.id, car.brand, car.model, car.year, car.price,
                        car.available ? "Available" : "Sold"));
            }
            report.append("\nTotal Cars: ").append(cars.size());
            reportArea.setText(report.toString());
        });

        panel.add(generateBtn, BorderLayout.CENTER);
        panel.add(new JScrollPane(reportArea), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSettings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel(" Settings");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(ACCENT_GOLD);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        JButton backupBtn = create3DButton(" Backup Data", ACCENT_GREEN);
        JButton restoreBtn = create3DButton(" Restore Data", ACCENT_ORANGE);
        JButton clearBtn = create3DButton(" Clear Cache", ACCENT_RED);

        backupBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "✅ Backup created!"));
        restoreBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "⚠️ Restore feature coming soon"));
        clearBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "🧹 Cache cleared!"));

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(backupBtn, gbc);
        gbc.gridx = 1;
        panel.add(restoreBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(clearBtn, gbc);

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBackground(new Color(10, 15, 25, 200));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel status = new JLabel("✅ System Ready | Admin Mode");
        status.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        status.setForeground(TEXT_DARK);
        footer.add(status, BorderLayout.WEST);

        return footer;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_LIGHT);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadCarTable() {
        carModel.setRowCount(0);
        for (Car car : cars) {
            ImageIcon carImage = null;
            try {
                carImage = new ImageIcon(new URL(car.imageUrl));
                Image img = carImage.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                carImage = new ImageIcon(img);
            } catch (Exception e) {
                carImage = new ImageIcon();
            }
            carModel.addRow(new Object[]{car.id, carImage, car.brand, car.model, car.year,
                    "Rs." + String.format("%,.0f", car.price), car.color, car.fuelType, car.transmission,
                    car.available ? "✅ Available" : "❌ Sold"});
        }
    }

    private void loadCustomerTable() {
        customerModel.setRowCount(0);
        for (Customer c : customers) {
            customerModel.addRow(new Object[]{c.id, c.name, c.phone, c.email, c.address, c.cnic});
        }
    }

    private void loadUserTable() {
        userModel.setRowCount(0);
        for (User u : users) {
            userModel.addRow(new Object[]{u.username, u.name, u.email, u.phone, u.role, u.status});
        }
    }

    private void loadTables() {
        loadCarTable();
        loadCustomerTable();
        loadUserTable();
        loadSalesTable();
    }

    private void updateStats() {
        totalCarsLabel.setText(String.valueOf(cars.size()));
        totalCustomersLabel.setText(String.valueOf(customers.size()));
        totalSalesLabel.setText(String.valueOf(sales.size()));
        double revenue = sales.stream().mapToDouble(s -> s.amount).sum();
        revenueLabel.setText("Rs. " + String.format("%,.0f", revenue));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}