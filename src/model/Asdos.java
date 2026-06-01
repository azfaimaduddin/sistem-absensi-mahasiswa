package model;

public class Asdos {
    private int id;
    private String nip;
    private String nama;
    private String username;
    private boolean aktif;

    public Asdos() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNip() { return nip; }
    public void setNip(String n) { this.nip = n; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean b) { this.aktif = b; }
}