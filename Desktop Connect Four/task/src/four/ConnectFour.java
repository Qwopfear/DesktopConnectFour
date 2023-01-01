package four;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class ConnectFour extends JFrame {

    JPanel gamePanel;
    Map<Character, ArrayList<JButton>> columns;
    int turn = 0;

    public ConnectFour() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
        setLayout(new BorderLayout());

        setTitle("Connect Four");

        gamePanel = initGamePanel();


        add(gamePanel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(e -> {
            columns.values().forEach(
                    column -> column.forEach(cell -> {
                        cell.setBackground(new Color(123, 123, 123));
                        cell.setText(" ");
                        cell.setEnabled(true);
                    })
            );
            turn = 0;

        });
        add(resetButton, BorderLayout.SOUTH);
    }

    private JPanel initGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 7, 0, 0));
        panel.setVisible(true);
        createCells(panel);
        return panel;
    }

    private void createCells(JPanel panel) {
        columns = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                JButton cell = new JButton(" ");
                cell.setName("Button" + (char) (65 + j) + "" + (6 - i));
                cell.setVisible(true);
                cell.setBackground(new Color(123, 123, 123));
                cell.setFocusable(false);
                cell.addActionListener(e -> clickColumn(cell.getName().substring(6, 8)));
                ArrayList<JButton> column = columns.getOrDefault((char) (65 + j), new ArrayList<>());
                column.add(cell);
                columns.put((char) (65 + j), column);

                panel.add(cell);
            }


        }


    }

    private void clickColumn(String row) {
        System.out.println(row);
        JButton button = columns.get(row.charAt(0)).stream()
                .filter(el -> el.getText().equals(" "))
                .max((e1, e2) -> (int) e2.getName().charAt(7) - (int) e1.getName().charAt(7)).orElse(null);

        if (button != null) {
            if (turn % 2 == 0) {
                button.setText("X");
            } else {
                button.setText("O");
            }
            turn++;
            checkBoard(button.getName().substring(6, 8));
        }
    }

    private void checkBoard(String cell) {
        checkRows((int) cell.charAt(1) - 48);
        checkColumns(cell.charAt(0));
        checkDias(cell);
    }

    private void checkDias(String cell) {
        ArrayList<String> rD = new ArrayList<>();
        ArrayList<String> lD = new ArrayList<>();

        // Right diagonal start letter
        char rdsl = (char)(cell.charAt(0) - ((int)cell.charAt(1) - 49)) <= 'A' ? 'A' : (char)(cell.charAt(0) - ((int)cell.charAt(1) - 49));
        // Right diagonal start number
        int rdsn = rdsl != 'A' ? 1 : 'A' - (cell.charAt(0) - ((int)cell.charAt(1) - 48));
        // Left diagonal start letter
        char ldsl = (char)(cell.charAt(0) + ((int)cell.charAt(1) - 49)) >= 'G' ? 'G' : (char)(cell.charAt(0) + ((int)cell.charAt(1) - 49));
        // Left diagonal start number
        int ldsn = ldsl != 'G' ? 1 : (cell.charAt(0) + ((int)cell.charAt(1) - 48)) - 'G';

        for (int i = rdsn; i < 7; i++) {
            if (rdsl > 'G') {
                break;
            }
            rD.add(rdsl++ + "" + rdsn++);
        }

        for (int i = ldsn; i < 7; i++) {
            if (ldsl < 'A') {
                break;
            }
            lD.add(ldsl-- + "" + ldsn++);
        }

        System.out.println(rD);
        System.out.println(lD);

        ArrayList<JButton> rd = new ArrayList<>();
        ArrayList<JButton> ld = new ArrayList<>();
        for (ArrayList<JButton> buttons: columns.values()) {
            System.out.println(buttons.get(1).getName().substring(6,8));
            rd.add(buttons.stream().filter(el -> rD.contains(el.getName().substring(6,8))).findFirst().orElse(null));
            ld.add(buttons.stream().filter(el -> lD.contains(el.getName().substring(6,8))).findFirst().orElse(null));
        }

        rd = (ArrayList<JButton>) rd.stream().filter(Objects::nonNull).collect(Collectors.toList());
        ld = (ArrayList<JButton>) ld.stream().filter(Objects::nonNull).collect(Collectors.toList());


        check(ld);
        check(rd);
    }

    private void checkColumns(char columnName) {
        ArrayList<JButton> column = columns.get(columnName);
        check(column);
    }

    private void checkRows(int rowNumber) {
        System.out.println(rowNumber);
        ArrayList<JButton> row = (ArrayList<JButton>) columns.values().stream().map(el -> el.get(el.size() - 1 - (rowNumber - 1))).collect(Collectors.toList());
        check(row);
    }

    void check(ArrayList<JButton> buttons) {
        for (int i = 0; i < buttons.size() - 3; i++) {
            String cell1 = buttons.get(i).getText();
            String cell2 = buttons.get(i + 1).getText();
            String cell3 = buttons.get(i + 2).getText();
            String cell4 = buttons.get(i + 3).getText();
            if (cell1.equals(cell2) && cell2.equals(cell3) && cell3.equals(cell4) && !cell4.equals(" ")) {
                for (int j = i; j < i + 4; j++) {
                    buttons.get(j).setBackground(new Color(12, 222, 222));
                }
                columns.values().forEach(el -> el.forEach(el1 -> el1.setEnabled(false)));
            }
        }
    }
}

