import android.content.Context
import android.media.SoundPool
import com.ai.fusion.character.merge.video.generator.R

object SoundPoolManager {

    private var soundPool: SoundPool? = null
    private var soundId: Int = 0
    private var evolutionSoundId: Int = 0
    private var isLoaded = false

    fun init(context: Context){
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        soundId = soundPool?.load(context, R.raw.button_click, 1) ?: 0
        evolutionSoundId = soundPool?.load(context, R.raw.evolution_click_two, 1) ?: 0
        soundPool?.setOnLoadCompleteListener { _, _, _ ->
            isLoaded = true
        }
    }

    fun playClickSound(isEvolution: Boolean = false) {
        if (isLoaded) {
            soundPool?.play(if (isEvolution) evolutionSoundId else soundId, 1f, 1f, 1, 0, 1f)
        }
    }


    fun release() {
        soundPool?.release()
        soundPool = null
    }

}
