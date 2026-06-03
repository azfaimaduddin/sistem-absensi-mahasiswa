package view;

import controller.AbsensiController;
import util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private final AbsensiController controller;
    private JTabbedPane tabbedPane;
    private JLabel lblJam;
    
    // Memanggil class modular hasil pecahan kita
    private MahasiswaPanel mahasiswaPanel;
    private AsdosPanel asdosPanel;
    private RekapPanel rekapPanel;

    public MainFrame() {
        this.controller = new AbsensiController();
        initUI();
    }

    private void initUI() {
        setTitle("🎓 Sistem Absensi Mahasiswa - Pemrograman Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 740);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(950, 650));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* abaikan */ }

        getContentPane().setBackground(UITheme.BG_LIGHT);
        setLayout(new BorderLayout());

        // 1. Build Header Utama Component
        add(buildHeader(), BorderLayout.NORTH);

        // 2. Inisialisasi Sub-Panel Modular View
        mahasiswaPanel = new MahasiswaPanel(controller, lblJam);
        asdosPanel = new AsdosPanel(controller, mahasiswaPanel);
        rekapPanel = new RekapPanel();

        // 3. Masukkan Panel ke dalam TabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_LABEL);
        tabbedPane.addTab(" Presensi Mahasiswa", mahasiswaPanel);
        tabbedPane.addTab("👨‍🏫 Panel Asisten", asdosPanel);
        tabbedPane.addTab(" Laporan Real-time",   rekapPanel);

        tabbedPane.addChangeListener(e -> onTabChange());
        add(tabbedPane, BorderLayout.CENTER);
        
        // 4. Build Bottom Status Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(UITheme.SECONDARY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        JLabel lblContext = new JLabel("Sistem Manajemen Absensi (Refactored Clean Code)");
        lblContext.setFont(UITheme.FONT_SMALL);
        lblContext.setForeground(new Color(149, 165, 166));
        statusBar.add(lblContext, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (mahasiswaPanel.getClockThread() != null) {
                    mahasiswaPanel.getClockThread().stop();
                }
            }
        });
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.SECONDARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("🎓 Sistem Absensi Mahasiswa");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Informatika UPNVY");
        subtitle.setFont(UITheme.FONT_SMALL);
        subtitle.setForeground(new Color(189, 195, 199));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(title); left.add(subtitle);

        lblJam = new JLabel("00:00:00", JLabel.RIGHT);
        lblJam.setFont(UITheme.FONT_MONO);
        lblJam.setForeground(UITheme.INFO);

        header.add(left, BorderLayout.WEST);
        header.add(lblJam, BorderLayout.EAST);
        return header;
    }

    private void onTabChange() {
        int idx = tabbedPane.getSelectedIndex();
        if (idx == 1) { 
            asdosPanel.loadDataAbsensiKePanelAsdos(); 
        } else if (idx == 2) {
            rekapPanel.loadRekapData();
        }
    }
}