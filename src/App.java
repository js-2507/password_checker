import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class App {
    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    // Inputs
    private JTextField websiteField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel inputErrorLabel;

    // Progress bar for search screen
    private JProgressBar progressBar;
    private JLabel progressStatusLabel;
    private Timer searchTimer;

    // Card identifiers
    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_INPUT = "INPUT";
    private static final String CARD_SEARCHING = "SEARCHING";
    private static final String CARD_RESULT = "RESULT";

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new App().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Compromised Password Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);
        frame.setMinimumSize(new Dimension(1000, 900));
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Build individual screens
        cardsPanel.add(createMainScreen(), CARD_MAIN);
        cardsPanel.add(createInputScreen(), CARD_INPUT);
        cardsPanel.add(createSearchingScreen(), CARD_SEARCHING);
        cardsPanel.add(createResultScreen(), CARD_RESULT);

        frame.setContentPane(cardsPanel);
        frame.setVisible(true);
    }

    /**
     * Screen 1: Main window with title and description
     */
    private JPanel createMainScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(35, 35, 35, 35));

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Compromised Password Checker");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Check if Your Logins are Secure");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 35));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel(
            "<html><div style='text-align: center; width: 320px; line-height: 1.5; color: #495057;'>"
            + "This tool checks if your password has been found in databases of compromised passwords."
            + "</div></html>"
        );
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalGlue());

        JButton startButton = new JButton("Get Started");
        stylePrimaryButton(startButton);
        startButton.addActionListener(e -> cardLayout.show(cardsPanel, CARD_INPUT));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(startButton);

        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Screen 2: Email and password input screen (with masked password)
     */
    private JPanel createInputScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        JLabel headerLabel = new JLabel("Enter Account Credentials");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        headerLabel.setForeground(new Color(33, 37, 41));
        headerPanel.add(headerLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Website label & field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel websiteLabel = new JLabel("Website:");
        websiteLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        formPanel.add(websiteLabel, gbc);

        gbc.gridy = 1;
        websiteField = new JTextField(20);
        websiteField.setFont(new Font("SansSerif", Font.PLAIN, 25));
        formPanel.add(websiteField, gbc);

        // Email label & field
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 3;
        emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 25));
        formPanel.add(emailField, gbc);

        // Password label & masked field
        gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        // Password masking using JPasswordField
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 25));
        formPanel.add(passwordField, gbc);

        // Show/Hide password toggle
        gbc.gridy = 6;
        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setOpaque(false);
        showPasswordCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 18));
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        formPanel.add(showPasswordCheckbox, gbc);

        // Error message label
        gbc.gridy = 7;
        inputErrorLabel = new JLabel(" ");
        inputErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        inputErrorLabel.setForeground(new Color(220, 53, 69));
        formPanel.add(inputErrorLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            inputErrorLabel.setText(" ");
            cardLayout.show(cardsPanel, CARD_MAIN);
        });

        JButton submitButton = new JButton("Check Password");
        stylePrimaryButton(submitButton);
        submitButton.addActionListener(e -> handleCheckPassword());

        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Screen 3: "Searching the database" with a 7-second loading bar animation
     */
    private JPanel createSearchingScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Searching password brach databases...");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressStatusLabel = new JLabel("Connecting to compromised password records...");
        progressStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 26));
        progressStatusLabel.setForeground(new Color(108, 117, 125));
        progressStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("SansSerif", Font.BOLD, 22));
        progressBar.setPreferredSize(new Dimension(680, 48));
        progressBar.setMaximumSize(new Dimension(720, 48));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        centerPanel.add(progressStatusLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Screen 4: Final screen stating no matches found
     */
    private JPanel createResultScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(35, 35, 35, 35));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel statusBadge = new JLabel("✓");
        statusBadge.setFont(new Font("SansSerif", Font.BOLD, 70));
        statusBadge.setForeground(new Color(40, 167, 69));
        statusBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusBadge.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("No Matches Found");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel messageLabel = new JLabel(
            "<html><div style='text-align: center; width: 330px; line-height: 1.5; color: #495057;'>"
            + "Your Password is secure.<br><br>We did not find any matches for the entered password in the database of compromised credentials."
            + "</div></html>"
        );
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(statusBadge);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(messageLabel);
        centerPanel.add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton checkAnotherButton = new JButton("Check Another Password");
        stylePrimaryButton(checkAnotherButton);
        checkAnotherButton.addActionListener(e -> {
            websiteField.setText("");
            emailField.setText("");
            passwordField.setText("");
            inputErrorLabel.setText(" ");
            cardLayout.show(cardsPanel, CARD_INPUT);
        });

        JButton exitButton = new JButton("Exit");
        styleSecondaryButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(checkAnotherButton);
        buttonPanel.add(exitButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Validates input, saves to file, and begins the 7-second search animation
     */
    private void handleCheckPassword() {
        String website = websiteField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (website.isEmpty()) {
            inputErrorLabel.setText("Please enter the website.");
            return;
        }
        if (email.isEmpty()) {
            inputErrorLabel.setText("Please enter your email.");
            return;
        }
        if (password.isEmpty()) {
            inputErrorLabel.setText("Please enter your password.");
            return;
        }

        inputErrorLabel.setText(" ");

        // Writing website, email, and password to data.txt file
        // Temporary testing solution, final version will send the strings to a database
        saveCredentialsToFile(website, email, password);

        // Switch to searching screen and start 7-second loading bar animation
        cardLayout.show(cardsPanel, CARD_SEARCHING);
        startSevenSecondSearch();
    }

    /**
     * Appends website, email, and password to data.txt
     */
    private void saveCredentialsToFile(String website, String email, String password) {
        String info = website + " " + email + " " + password + System.lineSeparator();
        try {
            Path targetPath = Path.of("src/data.txt");
            if (!Files.exists(targetPath)) {
                targetPath = Path.of("data.txt");
            }
            if (!Files.exists(targetPath)) {
                Files.createFile(targetPath);
            }
            Files.writeString(
                targetPath,
                info,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("An error occurred writing to file: " + e.getMessage());
        }
    }

    /**
     * Animates the loading bar over exactly 7 seconds (7,000 ms)
     */
    private void startSevenSecondSearch() {
        progressBar.setValue(0);
        progressStatusLabel.setText("Connecting to compromised password database...");

        final int totalDurationMs = 7000; // 7 seconds
        final int intervalMs = 70;        // update every 70ms (100 steps total)
        final long startTime = System.currentTimeMillis();

        if (searchTimer != null && searchTimer.isRunning()) {
            searchTimer.stop();
        }

        searchTimer = new Timer(intervalMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                int percent = (int) Math.min(100, (elapsed * 100) / totalDurationMs);
                progressBar.setValue(percent);

                if (percent < 30) {
                    progressStatusLabel.setText("Connecting to database records...");
                } else if (percent < 70) {
                    progressStatusLabel.setText("Scanning breach records...");
                } else if (percent < 95) {
                    progressStatusLabel.setText("Comparing password hashes...");
                } else {
                    progressStatusLabel.setText("Finalizing search...");
                }

                if (elapsed >= totalDurationMs) {
                    searchTimer.stop();
                    progressBar.setValue(100);
                    // Move to final result screen
                    cardLayout.show(cardsPanel, CARD_RESULT);
                }
            }
        });

        searchTimer.start();
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 26));
        button.setBackground(new Color(13, 110, 253));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(13, 110, 253), 2),
            new EmptyBorder(16, 32, 16, 32)
        ));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 26));
        button.setBackground(new Color(230, 235, 240));
        button.setForeground(new Color(33, 37, 41));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2),
            new EmptyBorder(16, 32, 16, 32)
        ));
    }
}