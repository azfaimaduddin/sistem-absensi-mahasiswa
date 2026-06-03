package view;

import controller.AbsensiController;
import exception.AbsensiException;
import model.Absensi;
import model.Asdos;
import model.JadwalKuliah;
import util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AsdosPanel extends JPanel {
    private final AbsensiController controller;
    private final MahasiswaPanel mahasiswaPanel; // Dibutuhkan untuk sinkronisasi kelas terpilih
    
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

    public AsdosPanel(AbsensiController controller, MahasiswaPanel mahasiswaPanel) {
        this.controller = controller;
        this.mahasiswaPanel = mahasiswaPanel;
        
        setBackground(UITheme.BG_LIGHT);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(buildLoginPanel(), BorderLayout.WEST);
        add(buildKonfirmasiPanel(), BorderLayout.CENTER);
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
            JadwalKuliah jadwalTerpilih = mahasiswaPanel.getJadwalTerpilihGUI();
            if (jadwalTerpilih == null) return;
            try {
                controller.asdosTambahAbsen(jadwalTerpilih.getId(), tfAsdosInputNim.getText().trim(), cbAsdosStatus.getSelectedItem().toString());
                tfAsdosInputNim.setText("");
                loadDataAbsensiKePanelAsdos();
                mahasiswaPanel.refreshTableRekap();
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
                mahasiswaPanel.refreshTableRekap();
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
                    mahasiswaPanel.refreshTableRekap();
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

    public void loadDataAbsensiKePanelAsdos() {
        if (!controller.isAsdosLogin() || mahasiswaPanel.getJadwalTerpilihGUI() == null) return;
        try {
            List<Absensi> list = controller.getAbsensiByJadwal(mahasiswaPanel.getJadwalTerpilihGUI().getId());
            modelPending.setRowCount(0);
            for (Absensi a : list) {
                modelPending.addRow(new Object[]{
                    a.getId(), a.getMahasiswa().getNim(), a.getMahasiswa().getNama(), a.getWaktuAbsenDisplay(), a.getStatus().name()
                });
            }
        } catch (AbsensiException e) { /* abaikan */ }
    }
}