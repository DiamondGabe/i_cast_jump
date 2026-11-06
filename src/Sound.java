import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    private Clip clip;

    public void loadFile(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.out.println("Audio file not found or corrupted: " + filePath);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void setVolume(int percent) {
        if (clip == null) {
            return;
        }
        
        int clampedPercent = Math.max(0, Math.min(150, percent));
        if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
            BooleanControl muteControl =
                    (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(clampedPercent == 0);
        }

        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }

        FloatControl volumeControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = clampedPercent == 0
                ? volumeControl.getMinimum()
                : (float) (20.0 * Math.log10(clampedPercent / 100.0));

        volumeControl.setValue(Math.max(
                volumeControl.getMinimum(),
                Math.min(volumeControl.getMaximum(), gain)));
    }

    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
