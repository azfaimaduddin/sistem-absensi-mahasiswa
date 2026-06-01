package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Absensi {
    public enum StatusAbsensi { HADIR, TELAT }

    private int id;
    private int jadwalId;
    private Mahasiswa mahasiswa;
    private LocalDateTime waktuAbsen;
    private StatusAbsensi status;

    public Absensi() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJadwalId() { return jadwalId; }
    public void setJadwalId(int id) { this.jadwalId = id; }
    public Mahasiswa getMahasiswa() { return mahasiswa; }
    public void setMahasiswa(Mahasiswa m) { this.mahasiswa = m; }
    public LocalDateTime getWaktuAbsen() { return waktuAbsen; }
    public void setWaktuAbsen(LocalDateTime t) { this.waktuAbsen = t; }
    public StatusAbsensi getStatus() { return status; }
    public void setStatus(StatusAbsensi s) { this.status = s; }

    public String getWaktuAbsenDisplay() {
        return waktuAbsen != null ? waktuAbsen.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "-";
    }
}