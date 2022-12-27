package com.review.nw.chap06;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AudioUDPClient {
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;

    public AudioUDPClient() {
        try {
            // 9786 포트에 바인드되는 소켓 생성
            DatagramSocket socket = new DatagramSocket(9786);
            byte[] audioBuffer = new byte[10000];

            while(true) { // 서버에서 패킷을 수신, AudioInputStream 생성, 패킷이 수신될때까지 패킷은 생성되고 이후에 블록된다
                DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
                socket.receive(packet);

                try { // 오디오 스트림 생성, 바이트 배열에 패킷이 추출되며 실제 오디오 스트림을 생성하기 위해 오디오 포맷 정보와 함께 ByteArrayInputStream 사용
                    byte[] audioData = packet.getData();
                    InputStream inputStream = new ByteArrayInputStream(audioData);
                    AudioFormat audioFormat = getAudioFormat();
                    audioInputStream = new AudioInputStream(inputStream, audioFormat, audioData.length / audioFormat.getFrameSize());
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                    sourceDataLine.open(audioFormat);
                    sourceDataLine.start();
                    playAudio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // AudioInputStream의 read 메소드는 소스 데이터 라인에 기록된 버퍼를 채운다
    private void playAudio() {
        byte[] buffer = new byte[10000];
        try {
            int count;
            while( (count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                if(count > 0) {
                    sourceDataLine.write(buffer, 0, count);
                }
            }
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
        new AudioUDPClient();
    }
}
