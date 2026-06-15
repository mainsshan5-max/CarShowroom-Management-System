import javax.swing.*;
import javax.swing.table.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
public class UserDashboard extends JFrame
{
    private JTable carTable, myCarsTable;
    private DefaultTableModel carModel, myCarsModel;
    private JPanel mainContent;
    private CardLayout cardLayout;
    private String username;
    private List<Car> cars;
    private List<Car> purchasedCars;
    private JButton currentSelectedBtn = null;
    private Image backgroundImage;
    private Image leftSideImage;
    private static final Color DARK_BG = new Color(18, 25, 35);
    private static final Color ACCENT_BLUE = new Color(0, 180, 255);
    private static final Color ACCENT_GREEN = new Color(0, 255, 150);
    private static final Color ACCENT_GOLD = new Color(255, 200, 0);
    private static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    private static final Color ACCENT_ORANGE = new Color(255, 120, 50);
    private static final Color ACCENT_RED = new Color(255, 70, 70);
    private static final Color ACCENT_CYAN = new Color(0, 200, 200);
    private static final Color TEXT_LIGHT = new Color(220, 230, 240);
    private static final Color TEXT_DARK = new Color(150, 165, 180);
    private static final Color TABLE_HEADER_BG = new Color(30, 40, 55);
    private static final Color BORDER_COLOR = new Color(45, 55, 70);
    private static final Color BUTTON_DARK = new Color(20, 25, 35);
    private static final Color BUTTON_HOVER = new Color(35, 45, 60);
    private static final Color BUTTON_SELECTED = new Color(0, 100, 150);
    private static final Color LEFT_PANEL_BG = new Color(10, 15, 25, 230);
    static class Car
    {
        int id; String brand, model, year, color, fuelType, transmission; double price; boolean available; String imageUrl;
        Car(int id, String brand, String model, String year, double price, String color, String fuel, String trans, boolean avail, String imageUrl) {
            this.id = id; this.brand = brand; this.model = model; this.year = year;
            this.price = price; this.color = color; this.fuelType = fuel; this.transmission = trans;
            this.available = avail; this.imageUrl = imageUrl;
        }
    }
    public UserDashboard(String username){
        this.username = username;
        loadBackgroundImages();
        initData();
        setupUI();
        loadTables();
    }

    private void loadBackgroundImages(){
        try {
            String imageUrl = "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=1920";
            URL url = new URL(imageUrl);
            backgroundImage = new ImageIcon(url).getImage();
            String leftImageUrl = "https://images.unsplash.com/photo-1580273916550-e323be2ae537?w=500";
            URL leftUrl = new URL(leftImageUrl);
            leftSideImage = new ImageIcon(leftUrl).getImage();
        }
        catch (Exception e)
        {
            backgroundImage=null;
            leftSideImage=null;
        }
    }
    private void initData(){
        cars = new ArrayList<>();
        cars.add(new Car(1, "Toyota", "Corolla", "2024", 4500000, "Pearl White", "Petrol", "Automatic", true,
                "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=200"));
        cars.add(new Car(2, "Honda", "Civic", "2024", 5200000, "Black Pearl", "Petrol", "Automatic", true,
                "https://images.unsplash.com/photo-1606016159991-dfe4f2746ad5?w=200"));
        cars.add(new Car(3, "Suzuki", "Swift", "2023", 2800000, "Racing Red", "Petrol", "Manual", true,
                "https://images.unsplash.com/photo-1618842676031-5e2c5e4c2d5d?w=200"));
        cars.add(new Car(4, "Kia", "Sportage", "2024", 6800000, "Ocean Blue", "Diesel", "Automatic", true,
                "https://images.unsplash.com/photo-1631295868223-63228b40f9e4?w=200"));
        cars.add(new Car(5, "Hyundai", "Elantra", "2024", 7500000, "Silver", "Petrol", "Automatic", true,
                "https://images.unsplash.com/photo-1566473965997-3de9c817e938?w=200"));
        cars.add(new Car(6, "Tesla", "Model 3", "2024", 25000000, "Electric Blue", "Electric", "Automatic", true,
                "https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=200"));

        purchasedCars = new ArrayList<>();

        Car demo1 = cars.get(0);
        demo1.available = false;
        purchasedCars.add(demo1);

        Car demo2 = cars.get(2);
        demo2.available = false;
        purchasedCars.add(demo2);
    }

    private void setupUI() {
        setTitle("ALI MOTORS - Customer Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 220));
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
        mainContainer.setOpaque(false);

        mainContainer.add(createHeader(), BorderLayout.NORTH);
        JPanel leftPanel = createLeftFunctionPanel();
        mainContainer.add(leftPanel, BorderLayout.WEST);
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContent.add(createDashboard(), "dashboard");
        mainContent.add(createBrowseCars(), "browse");
        mainContent.add(createMyCars(), "mycars");
        mainContent.add(createProfile(), "profile");
        mainContent.add(createLoanCalculator(), "loan");
        mainContent.add(createCarComparison(), "compare");
        mainContent.add(createTestDrive(), "testdrive");

        mainContainer.add(mainContent, BorderLayout.CENTER);
        mainContainer.add(createFooter(), BorderLayout.SOUTH);

        add(mainContainer);
        setVisible(true);
    }

    private JPanel createLeftFunctionPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (leftSideImage != null) {
                    g2d.drawImage(leftSideImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 200));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(8, 12, 20),
                            0, getHeight(), new Color(5, 10, 18));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

                // Decorative border
                g2d.setColor(new Color(0, 180, 255, 60));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10);
            }
        };

        panel.setPreferredSize(new Dimension(280, 0));
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel logoIcon = new JLabel("🏎️");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandLabel = new JLabel("ALI MOTORS");
        brandLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        brandLabel.setForeground(ACCENT_GOLD);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel premiumLabel = new JLabel("PREMIUM");
        premiumLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        premiumLabel.setForeground(ACCENT_BLUE);
        premiumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(logoIcon);
        panel.add(Box.createVerticalStrut(5));
        panel.add(brandLabel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(premiumLabel);
        panel.add(Box.createVerticalStrut(20));
        JSeparator sep = new JSeparator();
        sep.setForeground(ACCENT_GOLD);
        sep.setMaximumSize(new Dimension(220, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sep);
        panel.add(Box.createVerticalStrut(20));
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userName = new JLabel(username);
        userName.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        userName.setForeground(ACCENT_GREEN);
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);

        userPanel.add(userIcon);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(userName);

        panel.add(userPanel);
        panel.add(Box.createVerticalStrut(20));
        String[][] menuItems = {
                {"📊 DASHBOARD", "dashboard"},
                {"🚗 BROWSE CARS", "browse"},
                {"🚙 MY CARS", "mycars"},
                {"👤 MY PROFILE", "profile"},
                {"💰 LOAN CALCULATOR", "loan"},
                {"📊 COMPARE CARS", "compare"},
                {"📅 TEST DRIVE", "testdrive"}
        };

        for (String[] item : menuItems) {
            JButton btn = createDarkNavButton(item[0]);
            String cardName = item[1];
            btn.addActionListener(e -> {
                cardLayout.show(mainContent, cardName);
                setSelectedButton(btn);
                if (cardName.equals("mycars")) loadTables();
            });
            panel.add(btn);
            panel.add(Box.createVerticalStrut(8));
        }

        panel.add(Box.createVerticalGlue());
        JButton logoutBtn = createDarkNavButton("🚪 LOGOUT");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(UserDashboard.this,
                    "Are you sure you want to logout?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage();
            }
        });
        panel.add(logoutBtn);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }
    private JButton createDarkNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(BUTTON_DARK);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 70, 85), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setMinimumSize(new Dimension(240, 45));
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != currentSelectedBtn) {
                    btn.setBackground(BUTTON_HOVER);
                    btn.setForeground(ACCENT_GOLD);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(ACCENT_GOLD, 1),
                            BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != currentSelectedBtn) {
                    btn.setBackground(BUTTON_DARK);
                    btn.setForeground(TEXT_LIGHT);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(60, 70, 85), 1),
                            BorderFactory.createEmptyBorder(12, 15, 12, 15)
                    ));
                }
            }
        });

        return btn;
    }

    private void setSelectedButton(JButton selected) {
        if (currentSelectedBtn != null) {
            currentSelectedBtn.setBackground(BUTTON_DARK);
            currentSelectedBtn.setForeground(TEXT_LIGHT);
            currentSelectedBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(60, 70, 85), 1),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
        }
        currentSelectedBtn = selected;
        if (selected != null) {
            selected.setBackground(BUTTON_SELECTED);
            selected.setForeground(ACCENT_GOLD);
            selected.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_GOLD, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(5, 8, 12, 240));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel title = new JLabel("🏎️ ALI MOTORS - Customer Dashboard");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        title.setForeground(ACCENT_GOLD);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("👤 Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        welcomeLabel.setForeground(ACCENT_GREEN);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_LIGHT);

        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        });
        timer.start();

        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        rightPanel.add(timeLabel);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createDashboard() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        JPanel card1 = createStatCard("Available Cars", String.valueOf(cars.stream().filter(c -> c.available).count()), "🚗", ACCENT_BLUE);
        JPanel card2 = createStatCard("My Cars", String.valueOf(purchasedCars.size()), "🚙", ACCENT_GREEN);
        JPanel card3 = createStatCard("Total Spent", "Rs. " + String.format("%,.0f", purchasedCars.stream().mapToDouble(c -> c.price).sum()), "💰", ACCENT_GOLD);
        JPanel card4 = createStatCard("Premium Member", "Active", "⭐", ACCENT_PURPLE);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(card1, gbc);
        gbc.gridx = 1;
        panel.add(card2, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(card3, gbc);
        gbc.gridx = 1;
        panel.add(card4, gbc);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome back, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        welcomeLabel.setForeground(ACCENT_GOLD);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel quoteLabel = new JLabel("\"Experience the luxury of driving with Ali Motors\"");
        quoteLabel.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));
        quoteLabel.setForeground(TEXT_DARK);
        quoteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statsLabel = new JLabel("You have purchased " + purchasedCars.size() + " vehicles from us!");
        statsLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        statsLabel.setForeground(ACCENT_GREEN);
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(quoteLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        welcomePanel.add(statsLabel);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(welcomePanel, gbc);

        return panel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(new Color(20, 28, 40, 240));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setPreferredSize(new Dimension(280, 120));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));

        JPanel textPanel = new JPanel(new BorderLayout(0, 5));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_DARK);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        valueLabel.setForeground(color);

        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBrowseCars() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("🔍 Browse Available Cars");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(ACCENT_GOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Image", "Brand", "Model", "Year", "Price", "Color", "Fuel", "Transmission", "Action"};
        carModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) return ImageIcon.class;
                return Object.class;
            }
        };

        carTable = new JTable(carModel);
        carTable.setRowHeight(70);
        carTable.setBackground(new Color(20, 28, 40));
        carTable.setForeground(TEXT_LIGHT);
        carTable.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        carTable.getTableHeader().setBackground(TABLE_HEADER_BG);
        carTable.getTableHeader().setForeground(ACCENT_BLUE);
        carTable.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        carTable.setSelectionBackground(new Color(0, 100, 150));
        carTable.setSelectionForeground(Color.WHITE);

        carTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        carTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        carTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        carTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        carTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        carTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        carTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        carTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        carTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        carTable.getColumnModel().getColumn(9).setPreferredWidth(100);

        for (Car car : cars) {
            if (car.available) {
                ImageIcon carImage = loadCarImage(car.imageUrl);
                carModel.addRow(new Object[]{car.id, carImage, car.brand, car.model, car.year,
                        "Rs." + String.format("%,.0f", car.price), car.color, car.fuelType, car.transmission,
                        "🛒 Buy Now"});
            }
        }

        carTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = carTable.getSelectedRow();
                int col = carTable.getSelectedColumn();
                if (row >= 0 && col == 9) {
                    int carId = (int) carModel.getValueAt(row, 0);
                    Car selectedCar = cars.stream().filter(c -> c.id == carId).findFirst().orElse(null);
                    if (selectedCar != null && selectedCar.available) {
                        showPurchaseDialog(selectedCar);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(carTable);
        scrollPane.getViewport().setBackground(new Color(20, 28, 40));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private ImageIcon loadCarImage(String urlString) {
        try {
            URL url = new URL(urlString);
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    private void showPurchaseDialog(Car car) {
        JDialog dialog = new JDialog(this, "Purchase Vehicle", true);
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(DARK_BG);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("🚗 Confirm Purchase");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_GOLD);
        panel.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        addDetailRow(panel, gbc, "Vehicle:", car.brand + " " + car.model);
        gbc.gridy = 2;
        addDetailRow(panel, gbc, "Price:", "Rs. " + String.format("%,.0f", car.price));
        gbc.gridy = 3;
        addDetailRow(panel, gbc, "Year:", car.year);
        gbc.gridy = 4;
        addDetailRow(panel, gbc, "Color:", car.color);

        gbc.gridy = 5;
        JLabel nameLbl = new JLabel("Full Name:");
        nameLbl.setForeground(TEXT_LIGHT);
        nameLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        panel.add(nameLbl, gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(username, 20);
        nameField.setBackground(new Color(35, 45, 60));
        nameField.setForeground(TEXT_LIGHT);
        nameField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        panel.add(nameField, gbc);
        gbc.gridx = 0;

        gbc.gridy = 6;
        JLabel phoneLbl = new JLabel("Phone Number:");
        phoneLbl.setForeground(TEXT_LIGHT);
        phoneLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        panel.add(phoneLbl, gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        phoneField.setBackground(new Color(35, 45, 60));
        phoneField.setForeground(TEXT_LIGHT);
        phoneField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        panel.add(phoneField, gbc);
        gbc.gridx = 0;

        gbc.gridy = 7;
        JLabel paymentLbl = new JLabel("Payment Method:");
        paymentLbl.setForeground(TEXT_LIGHT);
        paymentLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        panel.add(paymentLbl, gbc);
        gbc.gridx = 1;
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"💵 Cash", "🏦 Bank Transfer", "💳 Credit Card"});
        paymentCombo.setBackground(new Color(35, 45, 60));
        paymentCombo.setForeground(ACCENT_GOLD);
        paymentCombo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        panel.add(paymentCombo, gbc);
        gbc.gridx = 0;

        gbc.gridy = 8; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton confirmBtn = new JButton("✅ Confirm Purchase");
        confirmBtn.setBackground(ACCENT_GREEN);
        confirmBtn.setForeground(Color.BLACK);
        confirmBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelBtn = new JButton("❌ Cancel");
        cancelBtn.setBackground(ACCENT_RED);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmBtn.addActionListener(e -> {
            if (phoneField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter phone number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            car.available = false;
            purchasedCars.add(car);
            loadTables();
            dialog.dispose();
            JOptionPane.showMessageDialog(UserDashboard.this,
                    "✅ Car purchased successfully!\nThank you for choosing Ali Motors!",
                    "Purchase Confirmed", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainContent, "mycars");
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_LIGHT);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        JLabel val = new JLabel(value);
        val.setForeground(ACCENT_GREEN);
        val.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        panel.add(val, gbc);
        gbc.gridx = 0;
    }

    private JPanel createMyCars() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("🚗 My Purchased Cars");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(ACCENT_GOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Brand", "Model", "Year", "Price", "Color", "Fuel", "Transmission", "Status"};
        myCarsModel = new DefaultTableModel(cols, 0);
        myCarsTable = new JTable(myCarsModel);
        myCarsTable.setBackground(new Color(20, 28, 40));
        myCarsTable.setForeground(TEXT_LIGHT);
        myCarsTable.setRowHeight(45);
        myCarsTable.getTableHeader().setBackground(TABLE_HEADER_BG);
        myCarsTable.getTableHeader().setForeground(ACCENT_BLUE);
        myCarsTable.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        myCarsTable.setSelectionBackground(new Color(0, 100, 150));

        for (Car car : purchasedCars) {
            myCarsModel.addRow(new Object[]{car.id, car.brand, car.model, car.year,
                    "Rs." + String.format("%,.0f", car.price), car.color, car.fuelType,
                    car.transmission, "✅ Purchased"});
        }

        JScrollPane scrollPane = new JScrollPane(myCarsTable);
        scrollPane.getViewport().setBackground(new Color(20, 28, 40));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfile() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(20, 28, 40, 240));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GOLD, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(10, 10, 10, 10);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        cardGbc.gridx = 0; cardGbc.gridy = 0; cardGbc.gridwidth = 2;
        card.add(avatarLabel, cardGbc);

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        title.setForeground(ACCENT_GOLD);
        cardGbc.gridy = 1;
        card.add(title, cardGbc);

        cardGbc.gridwidth = 1; cardGbc.gridy = 2;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_LIGHT);
        card.add(userLabel, cardGbc);
        cardGbc.gridx = 1;
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setForeground(ACCENT_GREEN);
        card.add(usernameLabel, cardGbc);

        cardGbc.gridx = 0; cardGbc.gridy = 3;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(TEXT_LIGHT);
        card.add(nameLabel, cardGbc);
        cardGbc.gridx = 1;
        JTextField nameField = new JTextField(username);
        nameField.setBackground(new Color(35, 45, 60));
        nameField.setForeground(TEXT_LIGHT);
        nameField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        card.add(nameField, cardGbc);

        cardGbc.gridx = 0; cardGbc.gridy = 4;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_LIGHT);
        card.add(emailLabel, cardGbc);
        cardGbc.gridx = 1;
        JTextField emailField = new JTextField(username + "@example.com");
        emailField.setBackground(new Color(35, 45, 60));
        emailField.setForeground(TEXT_LIGHT);
        emailField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        card.add(emailField, cardGbc);

        cardGbc.gridx = 0; cardGbc.gridy = 5;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(TEXT_LIGHT);
        card.add(phoneLabel, cardGbc);
        cardGbc.gridx = 1;
        JTextField phoneField = new JTextField("+92 XXX XXXXXXX");
        phoneField.setBackground(new Color(35, 45, 60));
        phoneField.setForeground(TEXT_LIGHT);
        phoneField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        card.add(phoneField, cardGbc);

        cardGbc.gridx = 0; cardGbc.gridy = 6; cardGbc.gridwidth = 2;
        JButton updateBtn = new JButton("💾 Update Profile");
        updateBtn.setBackground(ACCENT_GREEN);
        updateBtn.setForeground(Color.BLACK);
        updateBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "✅ Profile updated successfully!\nName: " + nameField.getText(),
                "Profile Updated", JOptionPane.INFORMATION_MESSAGE));
        card.add(updateBtn, cardGbc);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(card, gbc);

        return panel;
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
            carCombo.addItem(car.id + " - " + car.brand + " " + car.model + " - Rs." + String.format("%,.0f", car.price));
        }
        panel.add(carCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel downLabel = new JLabel("Down Payment (Rs):");
        downLabel.setForeground(TEXT_LIGHT);
        panel.add(downLabel, gbc);

        gbc.gridx = 1;
        JTextField downPaymentField = new JTextField("500000");
        downPaymentField.setBackground(new Color(35, 45, 60));
        downPaymentField.setForeground(TEXT_LIGHT);
        panel.add(downPaymentField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel rateLabel = new JLabel("Interest Rate (%):");
        rateLabel.setForeground(TEXT_LIGHT);
        panel.add(rateLabel, gbc);

        gbc.gridx = 1;
        JTextField rateField = new JTextField("12");
        rateField.setBackground(new Color(35, 45, 60));
        rateField.setForeground(TEXT_LIGHT);
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
        JButton calculateBtn = new JButton("📊 Calculate EMI");
        calculateBtn.setBackground(ACCENT_BLUE);
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        calculateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(calculateBtn, gbc);

        gbc.gridy = 6;
        JPanel resultPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        resultPanel.setBackground(new Color(20, 28, 40, 240));
        resultPanel.setBorder(BorderFactory.createLineBorder(ACCENT_GREEN));

        JLabel loanAmountLabel = new JLabel("Loan Amount: Rs. 0");
        loanAmountLabel.setForeground(TEXT_LIGHT);
        JLabel emiLabel = new JLabel("Monthly EMI: Rs. 0");
        emiLabel.setForeground(ACCENT_GREEN);
        JLabel totalInterestLabel = new JLabel("Total Interest: Rs. 0");
        totalInterestLabel.setForeground(ACCENT_ORANGE);
        JLabel totalPaymentLabel = new JLabel("Total Payment: Rs. 0");
        totalPaymentLabel.setForeground(ACCENT_GOLD);

        resultPanel.add(loanAmountLabel);
        resultPanel.add(emiLabel);
        resultPanel.add(totalInterestLabel);
        resultPanel.add(totalPaymentLabel);
        panel.add(resultPanel, gbc);

        calculateBtn.addActionListener(e -> {
            try {
                if (carCombo.getSelectedIndex() < 0) return;
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

    private JPanel createCarComparison() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📊 COMPARE CARS");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        title.setForeground(ACCENT_GOLD);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JPanel comparePanel = new JPanel(new GridLayout(1, 2, 30, 30));
        comparePanel.setOpaque(false);
        comparePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> car1Combo = new JComboBox<>();
        JComboBox<String> car2Combo = new JComboBox<>();

        car1Combo.setBackground(new Color(35, 45, 60));
        car1Combo.setForeground(ACCENT_GOLD);
        car1Combo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        car2Combo.setBackground(new Color(35, 45, 60));
        car2Combo.setForeground(ACCENT_GOLD);
        car2Combo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

        for (Car car : cars) {
            String displayText = car.id + " - " + car.brand + " " + car.model + " (" + car.year + ")";
            car1Combo.addItem(displayText);
            car2Combo.addItem(displayText);
        }

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(20, 28, 40, 230));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE), "🚗 CAR 1"));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(car1Combo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(20, 28, 40, 230));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN), "🚙 CAR 2"));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(car2Combo);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        comparePanel.add(leftPanel);
        comparePanel.add(rightPanel);
        panel.add(comparePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        JButton compareBtn = new JButton("🔍 COMPARE NOW");
        compareBtn.setBackground(ACCENT_ORANGE);
        compareBtn.setForeground(Color.WHITE);
        compareBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        compareBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        compareBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea resultArea = new JTextArea(15, 60);
        resultArea.setBackground(new Color(15, 25, 40));
        resultArea.setForeground(ACCENT_CYAN);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setBorder(BorderFactory.createLineBorder(ACCENT_GOLD, 2));
        resultArea.setText("═══ SELECT TWO CARS FROM THE DROPDOWNS ABOVE ═══\n\nClick 'COMPARE NOW' button to see detailed comparison");

        JScrollPane scrollPane = new JScrollPane(resultArea);

        compareBtn.addActionListener(e -> {
            int index1 = car1Combo.getSelectedIndex();
            int index2 = car2Combo.getSelectedIndex();

            if (index1 < 0 || index2 < 0) {
                resultArea.setText(" Please select both cars to compare!");
                return;
            }
            if (index1 == index2) {
                resultArea.setText(" Please select two different cars to compare!");
                return;
            }

            Car car1 = cars.get(index1);
            Car car2 = cars.get(index2);

            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════════════════════════════\n");
            sb.append("                        COMPARISON RESULT\n");
            sb.append("═══════════════════════════════════════════════════════════════════════════\n\n");
            sb.append(String.format("%-20s | %-30s | %-30s\n", "SPECIFICATION", car1.brand + " " + car1.model, car2.brand + " " + car2.model));
            sb.append("───────────────────────────────────────────────────────────────────────────\n");
            sb.append(String.format("%-20s | Rs.%,-28.0f | Rs.%,-28.0f\n", "PRICE", car1.price, car2.price));
            sb.append(String.format("%-20s | %-30s | %-30s\n", "YEAR", car1.year, car2.year));
            sb.append(String.format("%-20s | %-30s | %-30s\n", "COLOR", car1.color, car2.color));
            sb.append(String.format("%-20s | %-30s | %-30s\n", "FUEL TYPE", car1.fuelType, car2.fuelType));
            sb.append(String.format("%-20s | %-30s | %-30s\n", "TRANSMISSION", car1.transmission, car2.transmission));
            sb.append("═══════════════════════════════════════════════════════════════════════════\n");
            double diff = Math.abs(car1.price - car2.price);
            if (car1.price < car2.price) {
                sb.append("\n🏆 WINNER: ").append(car1.brand).append(" ").append(car1.model);
                sb.append("\n   ✓ More affordable by Rs. ").append(String.format("%,.0f", diff));
            } else if (car2.price < car1.price) {
                sb.append("\n🏆 WINNER: ").append(car2.brand).append(" ").append(car2.model);
                sb.append("\n   ✓ More affordable by Rs. ").append(String.format("%,.0f", diff));
            } else {
                sb.append("\n💰 TIE: Both cars have the same price!");
            }
            resultArea.setText(sb.toString());
        });

        bottomPanel.add(compareBtn);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomPanel.add(scrollPane);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTestDrive() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("📅 Book a Test Drive");
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
            if (car.available) {
                carCombo.addItem(car.id + " - " + car.brand + " " + car.model + " (" + car.year + ")");
            }
        }
        panel.add(carCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(TEXT_LIGHT);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(username);
        nameField.setBackground(new Color(35, 45, 60));
        nameField.setForeground(TEXT_LIGHT);
        nameField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(TEXT_LIGHT);
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField();
        phoneField.setBackground(new Color(35, 45, 60));
        phoneField.setForeground(TEXT_LIGHT);
        phoneField.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel dateLabel = new JLabel("Preferred Date:");
        dateLabel.setForeground(TEXT_LIGHT);
        panel.add(dateLabel, gbc);

        gbc.gridx = 1;
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setBackground(new Color(35, 45, 60));
        dateSpinner.setForeground(TEXT_LIGHT);
        panel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        JLabel timeLabel = new JLabel("Preferred Time:");
        timeLabel.setForeground(TEXT_LIGHT);
        panel.add(timeLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> timeCombo = new JComboBox<>(new String[]{"10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM"});
        timeCombo.setBackground(new Color(35, 45, 60));
        timeCombo.setForeground(ACCENT_GOLD);
        timeCombo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        panel.add(timeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton bookBtn = new JButton("✅ Book Test Drive");
        bookBtn.setBackground(ACCENT_GREEN);
        bookBtn.setForeground(Color.BLACK);
        bookBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(bookBtn, gbc);

        bookBtn.addActionListener(e -> {
            if (carCombo.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(panel, "Please select a vehicle!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (phoneField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter phone number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Car selectedCar = cars.get(carCombo.getSelectedIndex());
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            String time = (String) timeCombo.getSelectedItem();

            String message = "✅ Test Drive Booked Successfully!\n\n" +
                    "Vehicle: " + selectedCar.brand + " " + selectedCar.model + "\n" +
                    "Customer: " + nameField.getText() + "\n" +
                    "Phone: " + phoneField.getText() + "\n" +
                    "Date: " + new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate) + "\n" +
                    "Time: " + time + "\n\n" +
                    "Our representative will contact you shortly!";

            JOptionPane.showMessageDialog(panel, message, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            phoneField.setText("");
            carCombo.setSelectedIndex(0);
        });

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(5, 8, 12, 240));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel status = new JLabel("👤 Customer Mode | Premium Access | Ali Motors Premium Experience");
        status.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        status.setForeground(ACCENT_GREEN);

        JLabel supportLabel = new JLabel("📞 24/7 Support: +92-XXX-XXXXXXX | Email: care@alimotors.com");
        supportLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        supportLabel.setForeground(TEXT_DARK);

        footer.add(status, BorderLayout.WEST);
        footer.add(supportLabel, BorderLayout.EAST);

        return footer;
    }

    private void loadTables() {
        if (carModel != null) {
            carModel.setRowCount(0);
            for (Car car : cars) {
                if (car.available) {
                    ImageIcon carImage = loadCarImage(car.imageUrl);
                    carModel.addRow(new Object[]{car.id, carImage, car.brand, car.model, car.year,
                            "s." + String.format("%,.0f", car.price), car.color, car.fuelType, car.transmission,
                            "🛒 Buy Now"});
                }
            }
        }

        if (myCarsModel != null) {
            myCarsModel.setRowCount(0);
            for (Car car : purchasedCars) {
                myCarsModel.addRow(new Object[]{car.id, car.brand, car.model, car.year,
                        "Rs." + String.format("%,.0f", car.price), car.color, car.fuelType,
                        car.transmission, "✅ Purchased"});
            }
        }
    }
}