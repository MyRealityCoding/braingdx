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

package de.bitbrain.braingdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.util.ArgumentFactory;
import de.bitbrain.braingdx.util.Factory;
import de.bitbrain.braingdx.util.ViewportFactory;

/**
 * Abstract base class for screens
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractScreen<T extends BrainGdxGame, C extends GameContext> implements Screen {

   private final ViewportFactory viewportFactory = new ViewportFactory() {

      @Override
      public Viewport create(int width, int height, Camera camera) {
         return getViewport(width, height, camera);
      }

   };

   private T game;
   private Color backgroundColor = Color.BLACK.cpy();
   private C gameContext;
   private ArgumentFactory<AbstractScreen, C> contextFactory;

   public AbstractScreen(T game, ArgumentFactory<AbstractScreen, C> contextFactory) {
      this.game = game;
      this.contextFactory = contextFactory;
   }

   public T getGame() {
      return game;
   }

   @Override
   public final void show() {
      ScreenTransitions.init(game, this);
      gameContext = contextFactory.create(this);
      onCreate(gameContext);
   }

   @Override
   public final void render(float delta) {
      Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      onUpdate(delta);
      gameContext.updateAndRender(delta);
   }

   @Override
   public final void resize(int width, int height) {
      gameContext.resize(width, height);
   }

   @Override
   public void pause() {

   }

   @Override
   public void resume() {

   }

   @Override
   public void hide() {

   }

   protected abstract void onCreate(C context);

   protected void onUpdate(float delta) {
      // noOp
   }

   public ViewportFactory getViewportFactory() {
      return viewportFactory;
   }

   public ShaderConfig getShaderConfig() {
      return new ShaderConfig();
   }

   public Viewport getViewport(int width, int height, Camera camera) {
      return new ScreenViewport(camera);
   }

   @Override
   public void dispose() {
      gameContext.dispose();
   }
}
