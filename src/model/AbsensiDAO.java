package model;

import database.DatabaseConnection;
import exception.AbsensiException;
import exception.AbsensiException.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbsensiDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public Mahasiswa findMahasiswaByNim(String nim) throws AbsensiException {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nim);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Mahasiswa m = new Mahasiswa();
                    m.setId(rs.getInt("id"));
                    m.setNim(rs.getString("nim"));
                    m.setNama(rs.getString("nama"));
                    return m;
                }
                throw new MahasiswaTidakDitemukanException(nim);
            }
        } catch (SQLException e) {
            throw new DatabaseException("findMahasiswaByNim", e);
        }
    }

    public List<JadwalKuliah> getJadwalHariIni() throws AbsensiException {
        List<JadwalKuliah> list = new ArrayList<>();
        String sql = "SELECT j.*, mk.nama AS nama_mk, mk.kelas, mk.dosen "
                   + "FROM jadwal_kuliah j "
                   + "JOIN matakuliah mk ON j.matakuliah_id = mk.id "
                   + "WHERE j.hari = ELT(WEEKDAY(CURDATE())+1, 'Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu') "
                   + "ORDER BY j.jam_mulai ASC";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                JadwalKuliah j = new JadwalKuliah();
                j.setId(rs.getInt("id"));
                j.setHari(rs.getString("hari"));
                j.setJamMulai(rs.getTime("jam_mulai").toLocalTime());
                j.setBatasTelat(rs.getTime("batas_telat").toLocalTime());
                j.setRuang(rs.getString("ruang"));
                j.setNamaMatakuliah(rs.getString("nama_mk"));
                j.setKelasMatakuliah(rs.getString("kelas"));
                j.setDosen(rs.getString("dosen"));
                list.add(j);
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("getJadwalHariIni", e);
        }
    }

    public boolean sudahAbsen(int jadwalId, int mahasiswaId) throws AbsensiException {
        String sql = "SELECT COUNT(*) FROM absensi WHERE jadwal_id=? AND mahasiswa_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, jadwalId);
            ps.setInt(2, mahasiswaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("sudahAbsen", e);
        }
    }

    public void simpanAbsensi(Absensi absensi) throws AbsensiException {
        String sql = "INSERT INTO absensi (jadwal_id, mahasiswa_id, waktu_absen, status, konfirmasi, dihitung_hadir) VALUES (?,?,NOW(),?,'NA',1)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, absensi.getJadwalId());
            ps.setInt(2, absensi.getMahasiswa().getId());
            ps.setString(3, absensi.getStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("simpanAbsensi", e);
        }
    }

    public List<Absensi> getAbsensiByJadwal(int jadwalId) throws AbsensiException {
        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, m.nim, m.nama AS nama_mhs FROM absensi a "
                   + "JOIN mahasiswa m ON a.mahasiswa_id = m.id "
                   + "WHERE a.jadwal_id = ? ORDER BY a.waktu_absen ASC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, jadwalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Absensi a = new Absensi();
                    a.setId(rs.getInt("id"));
                    Mahasiswa m = new Mahasiswa();
                    m.setNim(rs.getString("nim"));
                    m.setNama(rs.getString("nama_mhs"));
                    a.setMahasiswa(m);
                    a.setWaktuAbsen(rs.getTimestamp("waktu_absen").toLocalDateTime());
                    a.setStatus(Absensi.StatusAbsensi.valueOf(rs.getString("status")));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("getAbsensiByJadwal", e);
        }
        return list;
    }

    public void tambahAbsensiManual(int jadwalId, int mahasiswaId, String status) throws AbsensiException {
        String sql = "INSERT INTO absensi (jadwal_id, mahasiswa_id, waktu_absen, status, konfirmasi, dihitung_hadir) VALUES (?, ?, NOW(), ?, 'NA', 1)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, jadwalId);
            ps.setInt(2, mahasiswaId);
            ps.setString(3, status);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("tambahAbsensiManual", e);
        }
    }

    public void updateStatusAbsensi(int absensiId, String statusBaru) throws AbsensiException {
        String sql = "UPDATE absensi SET status = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setInt(2, absensiId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("updateStatusAbsensi", e);
        }
    }

    public void hapusAbsensi(int absensiId) throws AbsensiException {
        String sql = "DELETE FROM absensi WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, absensiId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("hapusAbsensi", e);
        }
    }

    public Asdos loginAsdos(String username, String password) throws AbsensiException {
        String sql = "SELECT * FROM asdos WHERE username=? AND password=MD5(?) AND aktif=1";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Asdos a = new Asdos();
                    a.setId(rs.getInt("id"));
                    a.setNama(rs.getString("nama"));
                    return a;
                }
                throw new AbsensiException("LOGIN_GAGAL", "Username atau password Asdos salah.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("loginAsdos", e);
        }
    }
}