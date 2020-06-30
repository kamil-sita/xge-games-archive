package sound

import resourcemanaging.ResourceIO
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

object SimpleMusicPlayer {

    fun play(musicFile: String) {
        try {
            val clip = AudioSystem.getClip()
            val audio: AudioInputStream = ResourceIO.loadSound(musicFile)
            clip.open(audio)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
