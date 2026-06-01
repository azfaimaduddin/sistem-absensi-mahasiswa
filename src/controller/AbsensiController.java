package controller;

import exception.AbsensiException;
import exception.AbsensiException.*;
import model.*;
import java.util.List;

public class AbsensiController {
    private final AbsensiDAO dao;
    private Asdos asdosLogin;

    public AbsensiController() { this.dao = new AbsensiDAO(); }

    public Asdos loginAsdos(String username, String password) throws AbsensiException {
        asdosLogin = dao.loginAsdos(username, password);
        return asdosLogin;
    }

    public Asdos getAsdosLogin() { return asdosLogin; }
    public boolean isAsdosLogin() { return asdosLogin != null; }
    public void logout() { asdosLogin = null; }
    
    public List<JadwalKuliah> getJadwalHariIni() throws AbsensiException { return dao.getJadwalHariIni(); }
    public List<Absensi> getAbsensiByJadwal(int jadwalId) throws AbsensiException { return dao.getAbsensiByJadwal(jadwalId); }

    public Absensi prosesAbsensi(int jadwalId, String nim) throws AbsensiException {
        List<JadwalKuliah> daftarJadwal = dao.getJadwalHariIni();
        JadwalKuliah jadwal = null;
        for (JadwalKuliah j : daftarJadwal) {
            if (j.getId() == jadwalId) { jadwal = j; break; }
        }
        if (jadwal == null) throw new JadwalTidakDitemukanException();

        Mahasiswa mahasiswa = dao.findMahasiswaByNim(nim.trim().toUpperCase());
        if (dao.sudahAbsen(jadwal.getId(), mahasiswa.getId())) {
            throw new SudahAbsenException(nim);
        }

        Absensi absensi = new Absensi();
        absensi.setJadwalId(jadwal.getId());
        absensi.setMahasiswa(mahasiswa);
        
        if (java.time.LocalTime.now().isAfter(jadwal.getBatasTelat())) {
            absensi.setStatus(Absensi.StatusAbsensi.TELAT);
        } else {
            absensi.setStatus(Absensi.StatusAbsensi.HADIR);
        }

        dao.simpanAbsensi(absensi);
        return absensi;
    }

    public void asdosTambahAbsen(int jadwalId, String nim, String status) throws AbsensiException {
        if (asdosLogin == null) throw new AbsensiException("AUTH_ERR", "Harap login Asdos dahulu!");
        Mahasiswa m = dao.findMahasiswaByNim(nim.trim().toUpperCase());
        if (dao.sudahAbsen(jadwalId, m.getId())) throw new AbsensiException("DUP", "Mahasiswa tersebut sudah diabsenkan!");
        dao.tambahAbsensiManual(jadwalId, m.getId(), status);
    }

    public void asdosEditAbsen(int absensiId, String statusBaru) throws AbsensiException {
        if (asdosLogin == null) throw new AbsensiException("AUTH_ERR", "Harap login Asdos dahulu!");
        dao.updateStatusAbsensi(absensiId, statusBaru);
    }

    public void asdosHapusAbsen(int absensiId) throws AbsensiException {
        if (asdosLogin == null) throw new AbsensiException("AUTH_ERR", "Harap login Asdos dahulu!");
        dao.hapusAbsensi(absensiId);
    }
}