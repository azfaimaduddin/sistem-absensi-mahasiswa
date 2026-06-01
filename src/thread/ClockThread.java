package thread;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockThread implements Runnable {
    private final JLabel labelJam;
    private final JLabel labelStatus;
    private volatile boolean running = true;
    
    private LocalTime jamMulai;
    private LocalTime batasTelat;
    private LocalTime batasMutlak;

    public ClockThread(JLabel labelJam, JLabel labelStatus) {
        this.labelJam = labelJam;
        this.labelStatus = labelStatus;
    }

    public void setWaktuJadwal(LocalTime jamMulai, LocalTime batasTelat) {
        this.jamMulai = jamMulai;
        this.batasTelat = batasTelat;
        this.batasMutlak = batasTelat.plusMinutes(45);
    }

    @Override
    public void run() {
        while (running) {
            LocalDateTime now = LocalDateTime.now();
            LocalTime waktu = now.toLocalTime();
            final String display = waktu.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            SwingUtilities.invokeLater(() -> {
                labelJam.setText(display);
                updateStatusLabel(waktu);
            });

            try { Thread.sleep(1000); } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void updateStatusLabel(LocalTime waktu) {
        if (batasTelat == null || labelStatus == null) return;
        
        if (jamMulai != null && waktu.isBefore(jamMulai)) {
            long selisih = java.time.Duration.between(waktu, jamMulai).getSeconds();
            labelStatus.setText("⏳ Pintu presensi belum dibuka (Mulai dalam " + (selisih / 60) + "m " + (selisih % 60) + "s)");
            labelStatus.setForeground(new java.awt.Color(41, 128, 185));
        } else if (waktu.isBefore(batasTelat)) {
            long selisih = java.time.Duration.between(waktu, batasTelat).getSeconds();
            labelStatus.setText("✅ Status kelas berjalan: Tepat Waktu (" + (selisih / 60) + "m " + (selisih % 60) + "s sisa)");
            labelStatus.setForeground(new java.awt.Color(39, 174, 96));
        } else if (waktu.isBefore(batasMutlak)) {
            labelStatus.setText("⚠️ Status kelas berjalan: Masa Keterlambatan");
            labelStatus.setForeground(new java.awt.Color(230, 126, 34));
        } else {
            labelStatus.setText("❌ Batas dispensasi pengisian mandiri HABIS");
            labelStatus.setForeground(new java.awt.Color(231, 76, 60));
        }
    }

    public void stop() { running = false; }
}