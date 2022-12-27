package com.review.nw.chap06;

import javax.sound.sampled.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AudioUDPServer {
    private final byte[] audioBuffer = new byte[10000];
    private TargetDataLine targetDataLine;

    public AudioUDPServer() {
        setupAudio();
        broadcastAudio();
    }

    private void broadcastAudio() {
        try {
            // 루프백 IP, 8000 포트로 생성
            DatagramSocket socket = new DatagramSocket(8000);
            InetAddress address = InetAddress.getByName("127.0.0.1");
            while(true) {
                // read 메소드를 통해 audioBuffer를 채우고 읽은 바이트의 수를 반환한다. 9786 포트에서 리스닝하여 클라이언트에게 송신
                int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
                if(count > 0) {
                    DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, address, 9786);
                    socket.send(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupAudio() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(format);
            targetDataLine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000F;
        int sampleSizeIntBits = 16;
        int channels = 1;
        boolean signed = true;

        // 바이트 순서 의미 빅인디언: 최상위 바이트를 가장 작은 메모리 주소에 저장하고, 최하위 바이트를 가장 큰 메모리 주소에 저장/리틀인디언: 이 순서를 뒤집는다.
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeIntBits, channels, signed, bigEndian);
    }

    public static void main(String[] args) {
        new AudioUDPServer();
    }
}
