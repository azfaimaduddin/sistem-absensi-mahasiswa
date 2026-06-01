package util;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class UITheme {
    public static final Color PRIMARY       = new Color(41,  128, 185);
    public static final Color SECONDARY     = new Color(52,  73,  94);
    public static final Color SUCCESS       = new Color(39,  174, 96);
    public static final Color WARNING       = new Color(230, 126, 34);
    public static final Color DANGER        = new Color(231, 76,  60);
    public static final Color INFO          = new Color(52,  152, 219);
    public static final Color BG_LIGHT      = new Color(236, 240, 241);
    public static final Color BG_CARD       = Color.WHITE;
    public static final Color TEXT_DARK      = new Color(44,  62,  80);
    public static final Color TEXT_MUTED    = new Color(127, 140, 141);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_MONO   = new Font("Consolas",  Font.BOLD, 36);
    public static final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD, 13);

    public static JButton createButton(String teks, Color bgColor) {
        JButton btn = new JButton(teks);
        btn.setFont(FONT_LABEL);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return btn;
    }

    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 223, 230), 1, true),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        return panel;
    }

    public static JLabel createHeader(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    public static JTextField createTextField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }

    public static JPasswordField createPasswordField(int cols) {
        JPasswordField pf = new JPasswordField(cols);
        pf.setFont(FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return pf;
    }

    public static JTextArea createTextArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setFont(FONT_BODY);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 235, 240));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL);
        header.setBackground(SECONDARY);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
    }
}