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

package de.bitbrain.braingdx.graphics.shader;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages GLSL shaders internally
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class BatchPostProcessor {

   private PostProcessor processor;

   private List<PostProcessorEffect> effects;

   public BatchPostProcessor(PostProcessor processor, PostProcessorEffect... effects) {
      this.processor = processor;
      this.effects = new ArrayList<PostProcessorEffect>();
      addEffects(effects);
   }

   public void addEffects(PostProcessorEffect... effects) {
      for (PostProcessorEffect effect : effects) {
         this.effects.add(effect);
         processor.addEffect(effect);
         effect.setEnabled(false);
      }
   }

   public void begin() {
      setEffectsEnabled(true);
      processor.setClearColor(0f, 0f, 0f, 0f);
      processor.setClearBits(GL20.GL_COLOR_BUFFER_BIT);
      processor.capture();
   }

   public void end(FrameBuffer buffer) {
      processor.render(buffer);
      setEffectsEnabled(false);
   }

   public boolean hasEffects() {
      return !effects.isEmpty();
   }

   public void end() {
      processor.render();
      setEffectsEnabled(false);
   }

   public void resume() {
      processor.rebind();
   }

   private void setEffectsEnabled(boolean enabled) {
      for (PostProcessorEffect effect : effects) {
         effect.setEnabled(enabled);
      }
   }

   public void clear() {
      for (PostProcessorEffect effect : effects) {
         effect.dispose();
      }
      effects.clear();
   }
}
