package edu.hitsz.music;

public class AudioManager {

    // 存储持续播放的背景音乐线程，方便后续停止它们
    private static MusicThread bgmThread;
    private static MusicThread bossBgmThread;


    private static final String PATH_BGM = "src/videos/bgm.wav";
    private static final String PATH_BOSS_BGM = "src/videos/bgm_boss.wav";
    private static final String PATH_HIT = "src/videos/bullet_hit.wav";
    private static final String PATH_BOMB = "src/videos/bomb_explosion.wav";
    private static final String PATH_SUPPLY = "src/videos/get_supply.wav";
    private static final String PATH_GAME_OVER = "src/videos/game_over.wav";

    // =============== 背景音乐控制 (循环) ===============

    public static void playBGM() {
        if (bgmThread == null || !bgmThread.isAlive()) {
            bgmThread = new MusicThread(PATH_BGM, true); // true 代表循环
            bgmThread.start();
        }
    }

    public static void stopBGM() {
        if (bgmThread != null) {
            bgmThread.stopMusic();
            bgmThread = null;
        }
    }

    public static void playBossBGM() {
        if (bossBgmThread == null || !bossBgmThread.isAlive()) {
            bossBgmThread = new MusicThread(PATH_BOSS_BGM, true);
            bossBgmThread.start();
        }
    }

    public static void stopBossBGM() {
        if (bossBgmThread != null) {
            bossBgmThread.stopMusic();
            bossBgmThread = null;
        }
    }

    // =============== 音效控制 (单次播放) ===============

    public static void playHitSound() {
        new MusicThread(PATH_HIT, false).start(); // false 代表单次播放
    }

    public static void playBombSound() {
        new MusicThread(PATH_BOMB, false).start();
    }

    public static void playGetSupplySound() {
        new MusicThread(PATH_SUPPLY, false).start();
    }

    public static void playGameOverSound() {
        new MusicThread(PATH_GAME_OVER, false).start();
    }
}