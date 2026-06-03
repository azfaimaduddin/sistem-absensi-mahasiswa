package view;

import controller.AbsensiController;
import exception.AbsensiException;
import model.*;
import thread.ClockThread;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.List;

public class MainFrame extends JFrame {

    private final AbsensiController controller;
    private JTabbedPane tabbedPane;

    private JLabel lblJam;
    private JLabel lblStatus;
    private JLabel lblJadwalInfo;
    private JComboBox<String> cbJadwalParalel;
    private JTextField tfNim;
    private JButton btnAbsen;
    private JTextArea taHasil;

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnLogout;
    private JLabel lblAsdosInfo;
    private JTable tablePending;
    private DefaultTableModel modelPending;
    
    private JTextField tfAsdosInputNim;
    private JComboBox<String> cbAsdosStatus;
    private JButton btnAsdosTambah;
    private JButton btnAsdosEdit;
    private JButton btnAsdosHapus;

    private JTable tableRekap;
    private DefaultTableModel modelRekap;

    private ClockThread clockThread;
    private Thread clockThreadObj;
    
    private List<JadwalKuliah> daftarJadwalHariIni;
    private JadwalKuliah jadwalTerpilihGUI;

    public MainFrame() {
        this.controller = new AbsensiController();
        initUI();
        loadJadwal();
        startClock();
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

        add(buildHeader(), BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_LABEL);
        tabbedPane.addTab(" Presensi Mahasiswa", buildTabMahasiswa());
        tabbedPane.addTab("👨‍🏫 Panel Asisten", buildTabAsdos());
        tabbedPane.addTab(" Laporan Real-time",   buildTabRekap());

        tabbedPane.addChangeListener(e -> onTabChange());
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(UITheme.SECONDARY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        JLabel lblContext = new JLabel("Sistem Manajemen Absensi");
        lblContext.setFont(UITheme.FONT_SMALL);
        lblContext.setForeground(new Color(149, 165, 166));
        statusBar.add(lblContext, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (clockThread != null) clockThread.stop();
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

    private JPanel buildTabMahasiswa() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(buildFormAbsensi(), BorderLayout.WEST);
        panel.add(buildHasilPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildFormAbsensi() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(380, 0));

        JLabel lblTitle = UITheme.createHeader("📝 Presensi Mandiri");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCombo = new JLabel("Pilih Jadwal Kuliah Hari Ini:");
        lblCombo.setFont(UITheme.FONT_LABEL);
        lblCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbJadwalParalel = new JComboBox<>();
        cbJadwalParalel.setFont(UITheme.FONT_BODY);
        cbJadwalParalel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbJadwalParalel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cbJadwalParalel.addActionListener(e -> aksiGantiKelasTerpilih());

        lblJadwalInfo = new JLabel("<html><i>Memuat data kelas...</i></html>");
        lblJadwalInfo.setFont(UITheme.FONT_BODY);
        lblJadwalInfo.setForeground(UITheme.TEXT_MUTED);
        lblJadwalInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblStatus = new JLabel("Menghubungkan...");
        lblStatus.setFont(UITheme.FONT_LABEL);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JLabel lblNim = new JLabel("Input NIM Mahasiswa:");
        lblNim.setFont(UITheme.FONT_LABEL);
        lblNim.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfNim = UITheme.createTextField(20);
        tfNim.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfNim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        btnAbsen = UITheme.createButton("✅ PRESENSI", UITheme.SUCCESS);
        btnAbsen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAbsen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        btnAbsen.addActionListener(e -> prosesAbsensi());
        tfNim.addActionListener(e -> prosesAbsensi());

        card.add(lblTitle); card.add(Box.createVerticalStrut(15));
        card.add(lblCombo); card.add(Box.createVerticalStrut(6));
        card.add(cbJadwalParalel); card.add(Box.createVerticalStrut(15));
        card.add(lblJadwalInfo); card.add(Box.createVerticalStrut(10));
        card.add(lblStatus); card.add(Box.createVerticalStrut(15));
        card.add(sep); card.add(Box.createVerticalStrut(15));
        card.add(lblNim); card.add(Box.createVerticalStrut(6));
        card.add(tfNim); card.add(Box.createVerticalStrut(20));
        card.add(btnAbsen); card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel buildHasilPanel() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 15));
        
        JLabel lblTitle = UITheme.createHeader("📄 Log Aktivitas Kelas Terpilih");

        String[] cols = {"NIM", "Nama Mahasiswa", "Waktu Log In", "Status Kehadiran"};
        modelRekap = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableRekap = new JTable(modelRekap);
        UITheme.styleTable(tableRekap);

        JScrollPane sp = new JScrollPane(tableRekap);
        sp.setBorder(BorderFactory.createEmptyBorder());

        taHasil = UITheme.createTextArea(3, 40);
        taHasil.setEditable(false);
        taHasil.setBackground(new Color(245, 247, 250));
        taHasil.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        taHasil.setText("Terminal log sistem: Menunggu input kartu mahasiswa.");

        JButton btnRefresh = UITheme.createButton("🔄 Refresh", UITheme.PRIMARY);
        btnRefresh.addActionListener(e -> refreshTableRekap());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(btnRefresh, BorderLayout.EAST);

        card.add(topBar,   BorderLayout.NORTH);
        card.add(sp,       BorderLayout.CENTER);
        card.add(taHasil,  BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildTabAsdos() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(buildLoginPanel(),     BorderLayout.WEST);
        panel.add(buildKonfirmasiPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLoginPanel() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 0));

        JLabel lblTitle = UITheme.createHeader("🔐 Otorisasi Asisten Dosen");
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAsdosInfo = new JLabel("Status Sesi: Guest");
        lblAsdosInfo.setFont(UITheme.FONT_SMALL);
        lblAsdosInfo.setForeground(UITheme.TEXT_MUTED);
        lblAsdosInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(UITheme.FONT_LABEL);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfUsername = UITheme.createTextField(15);
        tfUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tfUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(UITheme.FONT_LABEL);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        pfPassword = UITheme.createPasswordField(15);
        pfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        pfPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin = UITheme.createButton("🔑 LOG IN", UITheme.PRIMARY);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogout = UITheme.createButton("🚪 LOG OUT", UITheme.DANGER);
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setVisible(false);

        btnLogin.addActionListener(e -> prosesLogin());
        btnLogout.addActionListener(e -> prosesLogout());

        card.add(lblTitle); card.add(Box.createVerticalStrut(4));
        card.add(lblAsdosInfo); card.add(Box.createVerticalStrut(15));
        card.add(sep); card.add(Box.createVerticalStrut(15));
        card.add(lblUser); card.add(Box.createVerticalStrut(6));
        card.add(tfUsername); card.add(Box.createVerticalStrut(12));
        card.add(lblPass); card.add(Box.createVerticalStrut(6));
        card.add(pfPassword); card.add(Box.createVerticalStrut(20));
        card.add(btnLogin); card.add(btnLogout);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel buildKonfirmasiPanel() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 15));
        JLabel lblTitle = UITheme.createHeader(" Editor Database Presensi");

        String[] cols = {"ID ENTRY", "NIM", "Nama Mahasiswa", "Waktu Sinkron", "Status Real"};
        modelPending = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePending = new JTable(modelPending);
        UITheme.styleTable(tablePending);

        JScrollPane sp = new JScrollPane(tablePending);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel crudPanel = new JPanel(new BorderLayout(10, 10));
        crudPanel.setOpaque(false);

        JPanel inputBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        inputBar.setOpaque(false);
        
        inputBar.add(new JLabel("NIM Manual:"));
        tfAsdosInputNim = UITheme.createTextField(10);
        inputBar.add(tfAsdosInputNim);
        
        inputBar.add(new JLabel("Set Status:"));
        cbAsdosStatus = new JComboBox<>(new String[]{"HADIR", "TELAT"});
        cbAsdosStatus.setFont(UITheme.FONT_BODY);
        inputBar.add(cbAsdosStatus);

        btnAsdosTambah = UITheme.createButton(" INSERT DATA", UITheme.SUCCESS);
        btnAsdosEdit   = UITheme.createButton("️ UPDATE STATUS", UITheme.WARNING);
        btnAsdosHapus  = UITheme.createButton("️ DELETE DATA", UITheme.DANGER);

        btnAsdosTambah.addActionListener(e -> {
            if (jadwalTerpilihGUI == null) return;
            try {
                controller.asdosTambahAbsen(jadwalTerpilihGUI.getId(), tfAsdosInputNim.getText().trim(), cbAsdosStatus.getSelectedItem().toString());
                tfAsdosInputNim.setText("");
                loadDataAbsensiKePanelAsdos();
                refreshTableRekap();
            } catch (AbsensiException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Tambah", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAsdosEdit.addActionListener(e -> {
            int row = tablePending.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris pada tabel dahulu!"); return; }
            int idAbsen = (int) modelPending.getValueAt(row, 0);
            String currentStatus = modelPending.getValueAt(row, 4).toString();
            try {
                controller.asdosEditAbsen(idAbsen, currentStatus.equals("HADIR") ? "TELAT" : "HADIR");
                loadDataAbsensiKePanelAsdos();
                refreshTableRekap();
            } catch (AbsensiException ex) { /* abaikan */ }
        });

        btnAsdosHapus.addActionListener(e -> {
            int row = tablePending.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih data mahasiswa yang mau dihapus!"); return; }
            int idAbsen = (int) modelPending.getValueAt(row, 0);
            int conf = JOptionPane.showConfirmDialog(this, "Hapus baris presensi ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    controller.asdosHapusAbsen(idAbsen);
                    loadDataAbsensiKePanelAsdos();
                    refreshTableRekap();
                } catch (AbsensiException ex) { /* abaikan */ }
            }
        });

        JPanel actionButtons = new JPanel(new GridLayout(1, 3, 10, 0));
        actionButtons.setOpaque(false);
        actionButtons.add(btnAsdosTambah); actionButtons.add(btnAsdosEdit); actionButtons.add(btnAsdosHapus);

        crudPanel.add(inputBar, BorderLayout.NORTH);
        crudPanel.add(actionButtons, BorderLayout.SOUTH);

        JButton btnRefreshPending = UITheme.createButton("🔄 Sinkronkan Tabel", UITheme.INFO);
        btnRefreshPending.addActionListener(e -> loadDataAbsensiKePanelAsdos());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(btnRefreshPending, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);
        card.add(crudPanel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildTabRekap() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UITheme.BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 15));

        JLabel lblTitle = UITheme.createHeader(" Rekapitulasi Presensi Mahasiswa Kelas");
        
        String[] cols = {"No", "NIM", "Nama Mahasiswa", "Mata Kuliah", "Kelas", "Ruang", "Hari", "Jam Masuk", "Status"};
        DefaultTableModel modelFull = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableRekap = new JTable(modelFull);
        UITheme.styleTable(tableRekap);

        JScrollPane sp = new JScrollPane(tableRekap);
        sp.setBorder(BorderFactory.createEmptyBorder());
        
        JButton btnLoadRekap = UITheme.createButton("🔄 Update Data", UITheme.PRIMARY);
        btnLoadRekap.addActionListener(e -> loadRekapData(modelFull));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(btnLoadRekap, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);
        
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private void loadJadwal() {
        try {
            daftarJadwalHariIni = controller.getJadwalHariIni();
            cbJadwalParalel.removeAllItems();

            if (daftarJadwalHariIni.isEmpty()) {
                cbJadwalParalel.addItem("Tidak ada jadwal berjalan hari ini");
                lblJadwalInfo.setText("<html><font color='#e74c3c'><b>Hari perkuliahan kosong.</b></font></html>");
                btnAbsen.setEnabled(false);
                return;
            }

            for (JadwalKuliah j : daftarJadwalHariIni) {
                cbJadwalParalel.addItem("[" + j.getKelasMatakuliah() + "] " + j.getNamaMatakuliah());
            }

            if(cbJadwalParalel.getItemCount() > 0) {
                cbJadwalParalel.setSelectedIndex(0);
                aksiGantiKelasTerpilih();
            }

        } catch (AbsensiException e) {
            lblJadwalInfo.setText("<html><font color='red'>⚠ " + e.getMessage() + "</font></html>");
        }
    }

    private void aksiGantiKelasTerpilih() {
        int index = cbJadwalParalel.getSelectedIndex();
        if (index < 0 || daftarJadwalHariIni == null || daftarJadwalHariIni.isEmpty()) return;

        jadwalTerpilihGUI = daftarJadwalHariIni.get(index);

        lblJadwalInfo.setText("<html>Mata Kuliah: <b>" + jadwalTerpilihGUI.getNamaMatakuliah() + "</b> (" + jadwalTerpilihGUI.getKelasMatakuliah() + ")<br>"
            + "Waktu Kuliah: " + jadwalTerpilihGUI.getJamMulai() + " - " + jadwalTerpilihGUI.getBatasTelat()
            + "<br>Lokasi Ruang: <b>" + jadwalTerpilihGUI.getRuang() + "</b>"
            + " | Dosen: " + jadwalTerpilihGUI.getDosen() + "</html>");

        if (clockThread != null) {
            clockThread.setWaktuJadwal(jadwalTerpilihGUI.getJamMulai(), jadwalTerpilihGUI.getBatasTelat());
        }
        
        checkStatusWaktu();
        refreshTableRekap();
        loadDataAbsensiKePanelAsdos();
    }

    private void startClock() {
        clockThread = new ClockThread(lblJam, lblStatus);
        if (jadwalTerpilihGUI != null) {
            clockThread.setWaktuJadwal(jadwalTerpilihGUI.getJamMulai(), jadwalTerpilihGUI.getBatasTelat());
        }
        clockThreadObj = new Thread(clockThread);
        clockThreadObj.setDaemon(true);
        clockThreadObj.start();

        new javax.swing.Timer(1000, e -> checkStatusWaktu()).start();
    }

    private void checkStatusWaktu() {
        if (jadwalTerpilihGUI == null) return;
        LocalTime now = LocalTime.now();
        LocalTime mulai = jadwalTerpilihGUI.getJamMulai();
        LocalTime batas = jadwalTerpilihGUI.getBatasTelat();
        
        boolean belumMulai = now.isBefore(mulai);
        boolean habis = now.isAfter(batas.plusMinutes(45));

        btnAbsen.setEnabled(!belumMulai && !habis);

        if (belumMulai) {
            btnAbsen.setText("⏳ PINTU ABSEN BELUM DIBUKA");
        } else if (habis) {
            btnAbsen.setText("⛔ WAKTU DISPENSASI HABIS");
        } else if (now.isAfter(batas)) {
            btnAbsen.setText("⚠️ VERIFIKASI (STATUS: TERLAMBAT)");
        } else {
            btnAbsen.setText("✅ VERIFIKASI PRESENSI");
        }
    }

    private void prosesAbsensi() {
        if (jadwalTerpilihGUI == null) return;
        String nim = tfNim.getText().trim();

        if (nim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Absensi hasil = controller.prosesAbsensi(jadwalTerpilihGUI.getId(), nim);

            String msg = "✅ VERIFIKASI DATA BERHASIL\n"
                       + "━━━━━━━━━━━━━━━━━━━━━━━\n"
                       + "NIM   : " + hasil.getMahasiswa().getNim() + "\n"
                       + "Nama  : " + hasil.getMahasiswa().getNama() + "\n"
                       + "Kelas : " + jadwalTerpilihGUI.getKelasMatakuliah() + "\n"
                       + "Status: " + hasil.getStatus().name();

            taHasil.setText(msg);
            taHasil.setBackground(new Color(232, 245, 233));
            tfNim.setText("");
            refreshTableRekap();
            loadDataAbsensiKePanelAsdos();

        } catch (AbsensiException e) {
            taHasil.setBackground(new Color(255, 235, 238));
            taHasil.setText("❌ STRUKTUR PRESENSI GAGAL\n━━━━━━━━━━━━━━━━━━━━━━━\n" + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal Proses", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesLogin() {
        try {
            Asdos asdos = controller.loginAsdos(tfUsername.getText().trim(), new String(pfPassword.getPassword()));
            lblAsdosInfo.setText("<html>Sesi Aktif: <font color='#27ae60'><b>" + asdos.getNama() + "</b></font></html>");
            btnLogin.setVisible(false); btnLogout.setVisible(true);
            loadDataAbsensiKePanelAsdos();
        } catch (AbsensiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Otorisasi Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesLogout() {
        controller.logout();
        lblAsdosInfo.setText("Status Sesi: User");
        btnLogin.setVisible(true); btnLogout.setVisible(false);
        tfUsername.setText(""); pfPassword.setText("");
        modelPending.setRowCount(0);
    }

    private void loadDataAbsensiKePanelAsdos() {
        if (!controller.isAsdosLogin() || jadwalTerpilihGUI == null) return;
        try {
            List<Absensi> list = controller.getAbsensiByJadwal(jadwalTerpilihGUI.getId());
            modelPending.setRowCount(0);
            for (Absensi a : list) {
                modelPending.addRow(new Object[]{
                    a.getId(), a.getMahasiswa().getNim(), a.getMahasiswa().getNama(), a.getWaktuAbsenDisplay(), a.getStatus().name()
                });
            }
        } catch (AbsensiException e) { /* abaikan */ }
    }

    private void refreshTableRekap() {
        if (jadwalTerpilihGUI == null) return;
        try {
            List<Absensi> list = controller.getAbsensiByJadwal(jadwalTerpilihGUI.getId());
            modelRekap.setRowCount(0);
            for (Absensi a : list) {
                modelRekap.addRow(new Object[]{
                    a.getMahasiswa().getNim(), a.getMahasiswa().getNama(), a.getWaktuAbsenDisplay(), a.getStatus().name()
                });
            }
        } catch (Exception e) { /* abaikan */ }
    }

    private void loadRekapData(DefaultTableModel modelFull) {
        String sql = "SELECT m.nim, m.nama AS nama_mahasiswa, mk.nama AS mata_kuliah, mk.kelas, j.ruang, j.hari, a.waktu_absen, a.status "
                   + "FROM absensi a "
                   + "JOIN mahasiswa m ON a.mahasiswa_id = m.id "
                   + "JOIN jadwal_kuliah j ON a.jadwal_id = j.id "
                   + "JOIN matakuliah mk ON j.matakuliah_id = mk.id "
                   + "ORDER BY a.waktu_absen DESC";
                   
        try (java.sql.Connection conn = database.DatabaseConnection.getInstance().getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            modelFull.setRowCount(0);
            int no = 1;
            while (rs.next()) {
                modelFull.addRow(new Object[]{
                    no++,
                    rs.getString("nim"),
                    rs.getString("nama_mahasiswa"),
                    rs.getString("mata_kuliah"),
                    rs.getString("kelas"),
                    rs.getString("ruang"),
                    rs.getString("hari"),
                    rs.getTimestamp("waktu_absen").toString(),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat rekap: " + e.getMessage());
        }
    }

    private void onTabChange() {
        int idx = tabbedPane.getSelectedIndex();
        if (idx == 1) { loadDataAbsensiKePanelAsdos(); } 
    }
}