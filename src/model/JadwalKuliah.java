package model;

import java.time.LocalTime;

public class JadwalKuliah {
    private int id;
    private String hari;
    private LocalTime jamMulai;
    private LocalTime batasTelat;
    private String ruang;
    private String namaMatakuliah;
    private String kelasMatakuliah;
    private String dosen;

    public JadwalKuliah() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getHari() { return hari; }
    public void setHari(String h) { this.hari = h; }
    public LocalTime getJamMulai() { return jamMulai; }
    public void setJamMulai(LocalTime t) { this.jamMulai = t; }
    public LocalTime getBatasTelat() { return batasTelat; }
    public void setBatasTelat(LocalTime t) { this.batasTelat = t; }
    public String getRuang() { return ruang; }
    public void setRuang(String r) { this.ruang = r; }
    public String getNamaMatakuliah() { return namaMatakuliah; }
    public void setNamaMatakuliah(String n) { this.namaMatakuliah = n; }
    public String getKelasMatakuliah() { return kelasMatakuliah; }
    public void setKelasMatakuliah(String k) { this.kelasMatakuliah = k; }
    public String getDosen() { return dosen; }
    public void setDosen(String d) { this.dosen = d; }
}