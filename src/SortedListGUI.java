import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SortedListGUI extends JFrame {
    private final ListSorter listSorter = new ListSorter();
    private final JTextArea outputArea = new JTextArea(15, 30);
    private final JTextField inputField = new JTextField(20);
    private final JLabel statusLabel = new JLabel(" ");
    private String listName;

    public SortedListGUI() {
        listName = promptForListName();
        setTitle("Sorted List: " + listName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter String:"));
        topPanel.add(inputField);

        JButton addButton = new JButton("Add");
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear List");

        topPanel.add(addButton);
        topPanel.add(searchButton);
        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                if (listSorter.binarySearch(text) >= 0) {
                    JOptionPane.showMessageDialog(this,
                            "\"" + text + "\" is already in the list.",
                            "Duplicate Entry",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    listSorter.add(text);
                    updateStatus("Added: " + text + " | Items in List: " + listSorter.size());
                    displayList();
                }
            }
            inputField.setText("");
        });

        searchButton.addActionListener(e -> {
            String text = inputField.getText().trim();
            outputArea.setText("");
            if (!text.isEmpty()) {
                int result = listSorter.binarySearch(text);
                int count = listSorter.size();

                ArrayList<String> items = listSorter.getDisplayList();
                StringBuilder builder = new StringBuilder();

                builder.append(listName).append("\n\n");
                builder.append("+------------+-----------------+\n");
                builder.append(String.format("| %-10s | %-15s |\n", "Position", "Item"));
                builder.append("+------------+-----------------+\n");

                for (int i = 0; i < items.size(); i++) {
                    String item = items.get(i);
                    boolean isMatch = (result == i);
                    String arrow = isMatch ? "→" : " ";
                    builder.append(String.format("| %s%-9d | %s%-14s |\n", arrow, i + 1, arrow, item));
                }

                builder.append("+------------+-----------------+\n");

                if (result >= 0) {
                    builder.append("\n").append(items.get(result)).append(" is found in Position ").append(result + 1).append("\n");
                    updateStatus("Search: \"" + text + "\" → Found at position " + (result + 1) + " | Items in List: " + count);
                } else {
                    builder.append("\n\"").append(text).append("\" not found\n");
                    updateStatus("Search: \"" + text + "\" → Not found | Items in List: " + count);
                }

                outputArea.setText(builder.toString());
            }
            inputField.setText("");
        });

        clearButton.addActionListener(e -> {
            listSorter.clear();
            outputArea.setText("");
            updateStatus("List cleared | Items in List: 0");
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String promptForListName() {
        String name = JOptionPane.showInputDialog(this, "Enter a name for your list:", "Name Your List", JOptionPane.PLAIN_MESSAGE);
        return (name != null && !name.trim().isEmpty()) ? name.trim() : "Untitled";
    }

    private void displayList() {
        ArrayList<String> items = listSorter.getDisplayList();
        StringBuilder builder = new StringBuilder();

        builder.append(listName).append("\n\n");
        builder.append("+------------+-----------------+\n");
        builder.append(String.format("| %-10s | %-15s |\n", "Position", "Item"));
        builder.append("+------------+-----------------+\n");

        for (int i = 0; i < items.size(); i++) {
            builder.append(String.format("| %-10d | %-15s |\n", i + 1, items.get(i)));
        }

        builder.append("+------------+-----------------+\n");

        outputArea.setText(builder.toString());
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
