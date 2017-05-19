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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.BehaviorManagerAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManagerAdapter;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.pipeline.ColoredRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.CombinedRenderPipelineFactory;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipelineFactory;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.tmx.TiledMapManagerImpl;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Abstract base class for screens
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public abstract class AbstractScreen<T extends BrainGdxGame> implements Screen, GameContext {

   private T game;
   private GameWorld world;
   private BehaviorManager behaviorManager;
   private GameObjectRenderManager renderManager;
   private GameCamera gameCamera;
   private OrthographicCamera camera;
   private Color backgroundColor = Color.BLACK.cpy();
   private Batch batch;
   private Stage stage;
   private RenderPipeline renderPipeline;
   private LightingManager lightingManager;
   private ParticleManager particleManager;
   private World boxWorld;
   private TiledMapManager tiledMapManager;
   private TweenManager tweenManager = SharedTweenManager.getInstance();
   private InputMultiplexer input;
   private ColoredRenderLayer coloredRenderLayer;

   private boolean uiInitialized = false;

   public AbstractScreen(T game) {
      this.game = game;
   }

   public T getGame() {
      return game;
   }

   @Override
   public final void show() {
      coloredRenderLayer = new ColoredRenderLayer();
      camera = new OrthographicCamera();
      world = new GameWorld(camera);
      behaviorManager = new BehaviorManager();
      batch = new SpriteBatch();
      input = new InputMultiplexer();
      boxWorld = new World(Vector2.Zero, false);
      lightingManager = new LightingManager(boxWorld, camera);
      renderManager = new GameObjectRenderManager(batch);
      gameCamera = new VectorGameCamera(camera);
      particleManager = new ParticleManager();
      stage = new Stage(getViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
      renderPipeline = getRenderPipelineFactory().create();
      tiledMapManager = new TiledMapManagerImpl(behaviorManager, world, renderManager);
      ScreenTransitions.init(game, renderPipeline, this);
      wire();
   }

   @Override
   public final void render(float delta) {
      Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      onUpdate(delta);
      tweenManager.update(delta);
      gameCamera.update(delta);
      stage.act(delta);
      batch.setProjectionMatrix(camera.combined);
      renderPipeline.render(batch, delta);
   }

   @Override
   public final void resize(int width, int height) {
      if (!uiInitialized) {
         input.addProcessor(stage);
         onCreateStage(stage, width, height);
         Gdx.input.setInputProcessor(input);
         uiInitialized = true;
      }
      stage.getViewport().update(width, height);
      renderPipeline.resize(width, height);
      camera.setToOrtho(false, width, height);
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

   @Override
   public GameWorld getGameWorld() {
      return world;
   }

   public Color getBackgroundColor() {
      return backgroundColor;
   }

   @Override
   public Stage getStage() {
      return stage;
   }

   @Override
   public RenderPipeline getRenderPipeline() {
      return renderPipeline;
   }

   @Override
   public World getBox2DWorld() {
      return boxWorld;
   }

   @Override
   public ParticleManager getParticleManager() {
      return particleManager;
   }

   @Override
   public TweenManager getTweenManager() {
      return tweenManager;
   }

   @Override
   public BehaviorManager getBehaviorManager() {
      return behaviorManager;
   }

   @Override
   public GameObjectRenderManager getRenderManager() {
      return renderManager;
   }

   @Override
   public GameCamera getGameCamera() {
      return gameCamera;
   }

   @Override
   public LightingManager getLightingManager() {
      return lightingManager;
   }

   @Override
   public InputMultiplexer getInput() {
      return input;
   }

   @Override
   public TiledMapManager getTiledMapManager() {
      return tiledMapManager;
   }

   @Override
   public ScreenTransitions getScreenTransitions() {
      return ScreenTransitions.getInstance();
   }

   @Override
   public AudioManager getAudioManager() {
      return AudioManager.getInstance();
   }

   protected void onCreateStage(Stage stage, int width, int height) {

   }

   protected void onUpdate(float delta) {

   }

   protected ShaderConfig getShaderConfig() {
      return new ShaderConfig();
   }

   protected Viewport getViewport(int width, int height) {
      return new ScreenViewport();
   }

   @Override
   public void dispose() {
      world.clear();
      stage.dispose();
      input.clear();
      particleManager.dispose();
      renderPipeline.dispose();
      tweenManager.killAll();
      renderManager.dispose();
   }

   public void setBackgroundColor(Color color) {
      this.backgroundColor = color;
      coloredRenderLayer.setColor(color);
      this.getRenderPipeline().set(RenderPipeIds.BACKGROUND, coloredRenderLayer);
   }

   private void wire() {
      world.addListener(new BehaviorManagerAdapter(behaviorManager));
      world.addListener(new GameObjectRenderManagerAdapter(renderManager));
   }

   protected RenderPipelineFactory getRenderPipelineFactory() {
      return new CombinedRenderPipelineFactory(getShaderConfig(), world, lightingManager, stage);
   }
}