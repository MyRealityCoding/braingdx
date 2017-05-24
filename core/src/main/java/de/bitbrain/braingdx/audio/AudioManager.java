/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.MusicTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import java.util.HashMap;
import java.util.Map;

/**
 * This audio manager implementation provides utility methods to fade between different music and
 * sounds.
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public class AudioManager {

   private final static float DEFAULT_DURATION = 1f;
   private final static float DEFAULT_VOLUME = 1f;
   private final static AudioManager INSTANCE = new AudioManager();
   private final static TweenManager TWEEN_MANAGER = SharedTweenManager.getInstance();
   private final static AssetManager ASSET_MANAGER = SharedAssetManager.getInstance();

   private float volume = DEFAULT_VOLUME;
   
   private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
   

   public AudioManager() {
      Tween.registerAccessor(MusicClassWrapper.class, new MusicTween());
   }

   public static AudioManager getInstance() {
      return INSTANCE;
   }
   
   public void setVolume(float volume) {
      this.volume = volume;
   }

   public void crossFadeMusic(String fromPath, String toPath) {
      crossFadeMusic(fromPath, fromPath, DEFAULT_DURATION);
   }

   public void crossFadeMusic(String fromPath, String toPath, float duration) {
      crossFadeMusic(ASSET_MANAGER.get(fromPath, Music.class), ASSET_MANAGER.get(toPath, Music.class), duration);
   }

   public void crossFadeMusic(Music from, Music to, float duration) {
      fadeOutMusic(from, duration);
      fadeInMusic(to, duration);
   }

   public void fadeOutMusic(final String path) {
      fadeOutMusic(path, DEFAULT_DURATION);
   }

   public void fadeOutMusic(final String path, float duration) {
      fadeOutMusic(ASSET_MANAGER.get(path, Music.class), duration);
   }

   public void fadeOutMusic(final Music music, float duration) {
	  final MusicClassWrapper wrapper = new MusicClassWrapper(music);
      Tween.to(wrapper, MusicTween.VOLUME, duration)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .target(0f)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int arg0, BaseTween<?> arg1) {
                  wrapper.stop();
               }
            })
            .ease(TweenEquations.easeNone)
            .start(TWEEN_MANAGER);
   }

   public void fadeInMusic(String path) {
      fadeInMusic(path, DEFAULT_DURATION);
   }

   public void fadeInMusic(String path, float duration) {
      fadeInMusic(ASSET_MANAGER.get(path, Music.class), duration);
   }

   public void fadeInMusic(Music music, float duration) {
	  MusicClassWrapper wrapper = new MusicClassWrapper(music);
      wrapper.setVolume(0f);
      wrapper.play();
      Tween.to(wrapper, MusicTween.VOLUME, duration)
           .target(volume)
           .ease(TweenEquations.easeNone)
           .start(TWEEN_MANAGER);
   }

   public void playMusic(String path) {
      Music lastMusic = ASSET_MANAGER.get(path, Music.class);
      lastMusic.setVolume(volume);
      lastMusic.play();
   }

   public void stopMusic(String path) {
      ASSET_MANAGER.get(path, Music.class).stop();
   }

   public void pauseMusic(String path) {
      ASSET_MANAGER.get(path, Music.class).pause();
   }
   
    public void playSound(String key, String path) {
      startSound(key, path, false);
   }
   
   public void loopSound(String key, String path) {
       startSound(key, path, true);
   }
   
   public void stopSound(String key) {
      if(soundMap.get(key) != null) {
         soundMap.get(key).stop();
      }
   }
   
   public void stopAllSounds() {
      for( Map.Entry<String, Sound> entry: soundMap.entrySet()) {
         if(entry.getValue() != null) {
            entry.getValue().stop();
         }
      }
   }
   
   private void startSound(String key, String path, boolean loop) {
      if (soundMap.get(key) != null) {
         throw new RuntimeException("Sound: key: " + key + " path: " + path + " already playing");
      }

      Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
      soundMap.put(key, sound);
      if (loop) {
         sound.loop();
      }
      else {
         sound.play();
      }
   }


   private static class MusicClassWrapper implements Music {

      private Music music;

      public MusicClassWrapper(Music music) {
         this.music = music;
      }

      @Override
      public void play() {
         music.play();
      }

      @Override
      public void pause() {
         music.pause();
      }

      @Override
      public void stop() {
         music.stop();
      }

      @Override
      public boolean isPlaying() {
         return music.isPlaying();
      }

      @Override
      public void setLooping(boolean isLooping) {
         music.setLooping(isLooping);
      }

      @Override
      public boolean isLooping() {
         return music.isLooping();
      }

      @Override
      public void setVolume(float volume) {
         music.setVolume(volume);
      }

      @Override
      public float getVolume() {
         return music.getVolume();
      }

      @Override
      public void setPan(float pan, float volume) {
         music.setPan(pan, volume);
      }

      @Override
      public void setPosition(float position) {
         music.setPosition(position);
      }

      @Override
      public float getPosition() {
         return music.getPosition();
      }

      @Override
      public void dispose() {
         music.dispose();
      }

      @Override
      public void setOnCompletionListener(OnCompletionListener listener) {
         music.setOnCompletionListener(listener);
      }

   }
   
   
}
