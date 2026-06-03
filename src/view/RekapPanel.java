package view;

import util.UITheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RekapPanel extends JPanel {
    private JTable tableRekapFull;
    private DefaultTableModel modelFull;

    public RekapPanel() {
        setBackground(UITheme.BG_LIGHT);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        add(buildTabRekap(), BorderLayout.CENTER);
    }

    private JPanel buildTabRekap() {
        JPanel card = UITheme.createCard();
        card.setLayout(new BorderLayout(0, 15));

        JLabel lblTitle = UITheme.createHeader(" Rekapitulasi Presensi Mahasiswa Kelas");
        
        String[] cols = {"No", "NIM", "Nama Mahasiswa", "Mata Kuliah", "Kelas", "Ruang", "Hari", "Jam Masuk", "Status"};
        modelFull = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableRekapFull = new JTable(modelFull);
        UITheme.styleTable(tableRekapFull);

        JScrollPane sp = new JScrollPane(tableRekapFull);
        sp.setBorder(BorderFactory.createEmptyBorder());
        
        JButton btnLoadRekap = UITheme.createButton("🔄 Update Data", UITheme.PRIMARY);
        btnLoadRekap.addActionListener(e -> loadRekapData());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(lblTitle, BorderLayout.WEST);
        topBar.add(btnLoadRekap, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);
        card.add(sp,     BorderLayout.CENTER);
        
        return card;
    }

    public void loadRekapData() {
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
}