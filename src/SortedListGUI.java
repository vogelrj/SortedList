import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SortedListGUI extends JFrame {
    private final ListSorter listSorter = new ListSorter();
    private final JTextArea outputArea = new JTextArea(15, 30);
    private final JTextField inputField = new JTextField(20);
    private final JLabel statusLabel = new JLabel(" ");
    private JButton addButton;
    private String listName;

    public SortedListGUI() {
        listName = promptForListName();
        setTitle("Sorted List: " + listName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter String:"));
        topPanel.add(inputField);

        addButton = new JButton("Add");
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

                if (result >= 0) {
                    displaySearchResult(text, result);
                    updateStatus("Search: \"" + text + "\" → Found at position " + (result + 1) + " | Items in List: " + count);
                } else {
                    int insertPos = -result - 1;
                    updateStatus("Search: \"" + text + "\" → Not found | Items in List: " + count);

                    int choice = JOptionPane.showConfirmDialog(
                            this,
                            "\"" + text + "\" was not found in the list.\n" +
                                    "Would you like to add it to the sorted list in position " + (insertPos + 1) + "?",
                            "Item Not Found",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        listSorter.add(text);
                        int newIndex = listSorter.binarySearch(text);
                        displaySearchResult(text, newIndex);
                        updateStatus("Search: \"" + text + "\" → Found at position " + (newIndex + 1) + " | Items in List: " + listSorter.size());
                    }
                }
            }

            inputField.setText("");
        });

        clearButton.addActionListener(e -> {
            listSorter.clear();
            outputArea.setText("");
            updateStatus("List cleared | Items in List: 0");
        });

        setKeyboardShortcuts();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displaySearchResult(String query, int matchIndex) {
        ArrayList<String> items = listSorter.getDisplayList();
        StringBuilder builder = new StringBuilder();

        builder.append(listName).append("\n\n");
        builder.append("+------------+-----------------+\n");
        builder.append(String.format("| %-10s | %-15s |\n", "Position", "Item"));
        builder.append("+------------+-----------------+\n");

        for (int i = 0; i < items.size(); i++) {
            String item = capitalize(items.get(i));
            builder.append(String.format("| %-10d | %-15s |\n", i + 1, item));
        }

        builder.append("+------------+-----------------+\n");
        builder.append("\n→ ").append(capitalize(items.get(matchIndex)))
                .append(" is found in Position ").append(matchIndex + 1).append("\n");

        outputArea.setText(builder.toString());
    }


    private void displayList() {
        ArrayList<String> items = listSorter.getDisplayList();
        StringBuilder builder = new StringBuilder();

        builder.append(listName).append("\n\n");
        builder.append("+------------+-----------------+\n");
        builder.append(String.format("| %-10s | %-15s |\n", "Position", "Item"));
        builder.append("+------------+-----------------+\n");

        for (int i = 0; i < items.size(); i++) {
            builder.append(String.format("| %-10d | %-15s |\n", i + 1, capitalize(items.get(i))));
        }

        builder.append("+------------+-----------------+\n");

        outputArea.setText(builder.toString());
    }

    private String promptForListName() {
        String name = JOptionPane.showInputDialog(this, "Enter a name for your list:", "Name Your List", JOptionPane.PLAIN_MESSAGE);
        return (name != null && !name.trim().isEmpty()) ? name.trim() : "Untitled";
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private void setKeyboardShortcuts() {
        JRootPane rootPane = getRootPane();

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "addItem");
        rootPane.getActionMap().put("addItem", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (inputField.hasFocus()) {
                    for (ActionListener al : addButton.getActionListeners()) {
                        al.actionPerformed(new ActionEvent(addButton, ActionEvent.ACTION_PERFORMED, null));
                    }
                }
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control F"), "focusInput");
        rootPane.getActionMap().put("focusInput", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                inputField.requestFocusInWindow();
            }
        });

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "clearInput");
        rootPane.getActionMap().put("clearInput", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                inputField.setText("");
            }
        });
    }
}
