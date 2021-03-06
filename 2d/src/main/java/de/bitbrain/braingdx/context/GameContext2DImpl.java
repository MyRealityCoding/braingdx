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

package de.bitbrain.braingdx.context;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.debug.DebugMetric;
import de.bitbrain.braingdx.debug.DebugStageRenderLayer;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.graphics.BatchResolver;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.SpriteBatchResolver;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerImpl;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerRenderLayer;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerImpl;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.pipeline.layers.ColoredRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.GameObjectRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.StageRenderLayer;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.physics.PhysicsManager;
import de.bitbrain.braingdx.physics.PhysicsManagerImpl;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapContextFactory;
import de.bitbrain.braingdx.tmx.TiledMapEvents.OnLoadGameObjectEvent;
import de.bitbrain.braingdx.tmx.TiledMapInfoExtractor;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.tmx.TiledMapManagerImpl;
import de.bitbrain.braingdx.tmx.events.TmxAudioConfigurer;
import de.bitbrain.braingdx.tmx.events.TmxLightingConfigurer;
import de.bitbrain.braingdx.util.ArgumentFactory;
import de.bitbrain.braingdx.util.Resizeable;
import de.bitbrain.braingdx.util.ViewportFactory;

/**
 * 2D Implementation of {@link GameContext}.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.1.0
 */
public class GameContext2DImpl extends GameContextImpl implements GameContext2D, Disposable, Resizeable {

   private final Stage worldStage;
   private final LightingManagerImpl lightingManager;
   private final ParticleManagerImpl particleManager;
   private final World boxWorld;
   private final TiledMapManager tiledMapManager;
   private final PhysicsManagerImpl physicsManager;
   private final ColoredRenderLayer coloredRenderLayer;
   private final GameEventRouter tiledMapEventRouter;

   private static final ArgumentFactory<GameContext, GameCamera> GAME_CAMERA_FACTORY = new ArgumentFactory<GameContext, GameCamera>() {
      @Override
      public GameCamera create(GameContext context) {
         return new VectorGameCamera(new OrthographicCamera(), context.getGameWorld());
      }
   };

   private static final ArgumentFactory<GameContext, BatchResolver<?>[]> BATCH_RESOLVER_FACTORY = new ArgumentFactory<GameContext, BatchResolver<?>[]>() {
      @Override
      public BatchResolver<?>[] create(GameContext supplier) {
         return new BatchResolver[]{
               new SpriteBatchResolver(supplier.getGameCamera().getInternalCamera())
         };
      }
   };

   public GameContext2DImpl(ViewportFactory viewportFactory, ShaderConfig shaderConfig, BrainGdxGame game, AbstractScreen<?, ?> screen) {
      super(shaderConfig, viewportFactory, GAME_CAMERA_FACTORY, game, screen, BATCH_RESOLVER_FACTORY);
      coloredRenderLayer = new ColoredRenderLayer();
      particleManager = new ParticleManagerImpl(getBehaviorManager(), getSettings().getGraphics());
      worldStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), getGameCamera().getInternalCamera()));
      boxWorld = new World(Vector2.Zero, true);
      physicsManager = new PhysicsManagerImpl(
            boxWorld,
            getGameWorld(),
            getBehaviorManager()
      );
      lightingManager = new LightingManagerImpl(
            new RayHandler(boxWorld),
            getBehaviorManager(),
            (OrthographicCamera) getGameCamera().getInternalCamera()
      );
      tiledMapEventRouter = new GameEventRouter(
            getEventManager(),
            getGameWorld(),
            new TiledMapInfoExtractor()
      );
      tiledMapManager = new TiledMapManagerImpl(
            getGameWorld(),
            getEventManager(),
            new TiledMapContextFactory(
                  getRenderManager(),
                  getGameWorld(),
                  getEventManager(),
                  tiledMapEventRouter,
                  getBehaviorManager(),
                  physicsManager
            )
      );
      configurePipeline(getRenderPipeline(), this);
      wire();
   }

   @Override
   public Stage getWorldStage() {
      return worldStage;
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
   public LightingManager getLightingManager() {
      return lightingManager;
   }

   @Override
   public TiledMapManager getTiledMapManager() {
      return tiledMapManager;
   }

   @Override
   public void dispose() {
      super.dispose();
      worldStage.dispose();
      particleManager.dispose();
      physicsManager.dispose();
      lightingManager.dispose();
   }

   public void updateAndRender(float delta) {
      physicsManager.update(delta);
      worldStage.act(delta);
      super.updateAndRender(delta);
   }

   @Override
   public void setBackgroundColor(Color color) {
      super.setBackgroundColor(color);
      coloredRenderLayer.setColor(color);
      getRenderPipeline().put(RenderPipeIds.BACKGROUND, coloredRenderLayer);
   }

   @Override
   public void resize(int width, int height) {
      super.resize(width, height);
      worldStage.getViewport().update(width, height, true);
      lightingManager.resize(width, height);
   }

   @Override
   public PhysicsManager getPhysicsManager() {
      return physicsManager;
   }

   private void configurePipeline(RenderPipeline pipeline, GameContext2D context) {
      pipeline.put(RenderPipeIds.BACKGROUND, new RenderLayer2D() {
         @Override
         public void render(Batch batch, float delta) {
         }
      });
      pipeline.put(RenderPipeIds.FOREGROUND, new RenderLayer2D() {
         @Override
         public void render(Batch batch, float delta) {
            // noOp
         }
      });
      pipeline.put(RenderPipeIds.WORLD, new GameObjectRenderLayer(context.getRenderManager()));
      pipeline.put(RenderPipeIds.LIGHTING, new LightingManagerRenderLayer(lightingManager));
      pipeline.put(RenderPipeIds.PARTICLES, new ParticleManagerRenderLayer(particleManager));
      pipeline.put(RenderPipeIds.WORLD_UI, new StageRenderLayer(context.getWorldStage()));
      pipeline.put(RenderPipeIds.UI, new StageRenderLayer(context.getStage()));
      pipeline.put(RenderPipeIds.DEBUG, new DebugStageRenderLayer(context));
   }

   private void wire() {
      getInputManager().register(worldStage);
      getBehaviorManager().apply(tiledMapEventRouter);

      // TiledMap features
      getEventManager().register(new TmxAudioConfigurer(getAudioManager()), OnLoadGameObjectEvent.class);
      getEventManager().register(new TmxLightingConfigurer(getLightingManager()), OnLoadGameObjectEvent.class);

      getDebugPanel().addMetric("light count", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(lightingManager.size());
         }
      });
      getDebugPanel().addMetric("box2d body count", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(physicsManager.getPhysicsWorld().getBodyCount());
         }
      });
      getDebugPanel().addMetric("particle effect count", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(particleManager.getTotalEffectCount());
         }
      });
   }
}
