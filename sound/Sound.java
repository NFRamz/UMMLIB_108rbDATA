package sound;

import javafx.scene.media.AudioClip;

public class Sound {

    public static void falseLogin(){
        AudioClip falseLoginAudio = new AudioClip("file:src/main/java/sound/false.mp3");
        falseLoginAudio.play();
    }
}

