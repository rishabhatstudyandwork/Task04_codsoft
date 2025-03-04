import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverterApp {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverterApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBackground(new Color(255, 235, 230)); // Light reddish background

        JLabel baseLabel = new JLabel("Base Currency:");
        JComboBox<String> baseCurrency = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "INR", "JPY", "CAD", "AUD", "CHF", "CNY", "SGD", "NZD"});

        JLabel targetLabel = new JLabel("Target Currency:");
        JComboBox<String> targetCurrency = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "INR", "JPY", "CAD", "AUD", "CHF", "CNY", "SGD", "NZD"});

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JButton convertButton = new JButton("Convert");
        convertButton.setBackground(new Color(200, 50, 50)); // Reddish button
        convertButton.setForeground(Color.WHITE);
        convertButton.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String base = (String) baseCurrency.getSelectedItem();
                String target = (String) targetCurrency.getSelectedItem();
                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText());
                    double rate = getExchangeRate(base, target);
                    double convertedAmount = amount * rate;
                    resultLabel.setText("Converted Amount: " + String.format("%.2f", convertedAmount) + " " + target);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(baseLabel);
        panel.add(baseCurrency);
        panel.add(targetLabel);
        panel.add(targetCurrency);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(convertButton);
        panel.add(resultLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static double getExchangeRate(String base, String target) {
        try {
            URL url = new URL(API_URL + base);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getJSONObject("rates").getDouble(target);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error fetching exchange rates.", "Error", JOptionPane.ERROR_MESSAGE);
            return 1.0;
        }
    }
}
