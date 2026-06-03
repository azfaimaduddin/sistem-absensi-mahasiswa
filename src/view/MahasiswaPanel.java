package view;

import controller.AbsensiController;
import exception.AbsensiException;
import model.Absensi;
import model.JadwalKuliah;
import thread.ClockThread;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

public class MahasiswaPanel extends JPanel {
    private final AbsensiController controller;
    private final JLabel lblJam;
    private final JLabel lblStatus;
    private final JLabel lblJadwalInfo;
    private JComboBox<String> cbJadwalParalel;
    private JTextField tfNim;
    private JButton btnAbsen;
    private JTextArea taHasil;
    private JTable tableRekap;
    private DefaultTableModel modelRekap;
    
    private ClockThread clockThread;
    private List<JadwalKuliah> daftarJadwalHariIni;
    private JadwalKuliah jadwalTerpilihGUI;

    public MahasiswaPanel(AbsensiController controller, JLabel lblJam) {
        this.controller = controller;
        this.lblJam = lblJam;
        this.lblStatus = new JLabel("Menghubungkan...");
        this.lblJadwalInfo = new JLabel("<html><i>Memuat data kelas...</i></html>");
        
        setBackground(UITheme.BG_LIGHT);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(buildFormAbsensi(), BorderLayout.WEST);
        add(buildHasilPanel(), BorderLayout.CENTER);
        
        loadJadwal();
        startClock();
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

        lblJadwalInfo.setFont(UITheme.FONT_BODY);
        lblJadwalInfo.setForeground(UITheme.TEXT_MUTED);
        lblJadwalInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

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

    public void loadJadwal() {
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

    public void aksiGantiKelasTerpilih() {
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
    }

    private void startClock() {
        clockThread = new ClockThread(lblJam, lblStatus);
        if (jadwalTerpilihGUI != null) {
            clockThread.setWaktuJadwal(jadwalTerpilihGUI.getJamMulai(), jadwalTerpilihGUI.getBatasTelat());
        }
        Thread clockThreadObj = new Thread(clockThread);
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
            btnAbsen.setText("⏳ LOG PRESENSI BELUM DIBUKA");
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

        } catch (AbsensiException e) {
            taHasil.setBackground(new Color(255, 235, 238));
            taHasil.setText("❌ STRUKTUR PRESENSI GAGAL\n━━━━━━━━━━━━━━━━━━━━━━━\n" + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal Proses", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTableRekap() {
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

    public JadwalKuliah getJadwalTerpilihGUI() {
        return jadwalTerpilihGUI;
    }
    
    public ClockThread getClockThread() {
        return clockThread;
    }
}