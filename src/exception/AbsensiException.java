package exception;

public class AbsensiException extends Exception {
    private final String kode;

    public AbsensiException(String kode, String pesan) {
        super(pesan);
        this.kode = kode;
    }

    public String getKode() { return kode; }

    public static class MahasiswaTidakDitemukanException extends AbsensiException {
        public MahasiswaTidakDitemukanException(String nim) {
            super("MHS_NOT_FOUND", "Mahasiswa dengan NIM '" + nim + "' tidak ditemukan.");
        }
    }

    public static class JadwalTidakDitemukanException extends AbsensiException {
        public JadwalTidakDitemukanException() {
            super("JADWAL_NOT_FOUND", "Tidak ada jadwal kuliah berjalan untuk kelas ini hari ini.");
        }
    }

    public static class SudahAbsenException extends AbsensiException {
        public SudahAbsenException(String nim) {
            super("SUDAH_ABSEN", "Mahasiswa dengan NIM " + nim + " sudah melakukan absensi hari ini.");
        }
    }

    public static class DatabaseException extends AbsensiException {
        public DatabaseException(String operasi, Exception cause) {
            super("DB_ERROR", "Kesalahan database pada '" + operasi + "': " + cause.getMessage());
        }
    }
}