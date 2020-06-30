package sound

enum class SoundEffect {
    hover,
    placing,
    waves;


    val correspondingFile: String
        get() {
            val r = Math.random()
            when (this) {
                hover -> return "/sounds/hover2.wav"
                placing -> {
                    if (r < 0.25) {
                        return "/sounds/placing.wav"
                    } else if (r < 0.5) {
                        return "/sounds/placing1.wav"
                    } else if (r < 0.75) {
                        return "/sounds/placing2.wav"
                    }
                    return "/sounds/placing3.wav"
                }
                waves -> {
                    if (r < 0.2) {
                        return "/sounds/waves0.wav"
                    } else if (r < 0.4) {
                        return "/sounds/waves1.wav"
                    } else if (r < 0.6) {
                        return "/sounds/waves3.wav"
                    } else if (r < 0.8) {
                        return "/sounds/waves5.wav"
                    }
                    return "/sounds/waves4.wav"
                }
            }
        }
}
