package edu.hitsz.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicThread extends Thread {
    private String filename;
    private boolean isLoop;       // 是否循环播放
    private volatile boolean isPlaying; // 线程运行状态控制旗标
    private Clip clip;

    // 构造函数：传入音频路径和是否循环的标志
    public MusicThread(String filename, boolean isLoop) {
        this.filename = filename;
        this.isLoop = isLoop;
        this.isPlaying = true;
    }

    @Override
    public void run() {
        try {
            File musicFile = new File(filename);
            if (!musicFile.exists()) {
                System.err.println("❌ 找不到音频文件，请检查路径: " + musicFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInput);

            if (isLoop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
            } else {
                clip.start(); // 单次播放
            }

            // 强制让线程先睡 50 毫秒，确保底层音频流有时间真正启动
            Thread.sleep(50);

            // 只要 isPlaying 为 true 就一直维持线程存活
            while (isPlaying) {
                // 如果是单次播放的音效，且已经播放完毕，则跳出循环结束线程
                if (!isLoop && !clip.isRunning()) {
                    break;
                }
                Thread.sleep(100);
            }

            // 退出循环后，释放资源
            clip.stop();
            clip.close();
            audioInput.close();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("❌ 不支持的音频格式: " + filename);
        } catch (Exception e) {
            System.err.println("❌ 音频播放出错: " + e.getMessage());
        }
    }

    // 关键方法：供外部调用，用于随时掐断音乐（如 Game Over 或 Boss 阵亡时）
    public void stopMusic() {
        this.isPlaying = false;
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}