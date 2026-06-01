package model;

public class Mahasiswa {
    private int id;
    private String nim;
    private String nama;
    private String prodi;
    private int angkatan;

    public Mahasiswa() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }
    public int getAngkatan() { return angkatan; }
    public void setAngkatan(int angkatan) { this.angkatan = angkatan; }
}