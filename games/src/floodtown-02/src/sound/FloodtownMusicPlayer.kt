package sound

object FloodtownMusicPlayer {
    fun play(soundEffect: SoundEffect) {
        SimpleMusicPlayer.play(soundEffect.correspondingFile)
    }
}
