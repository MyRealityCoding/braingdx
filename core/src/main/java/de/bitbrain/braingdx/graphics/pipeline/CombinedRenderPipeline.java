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

package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.postprocessing.PostProcessor;
import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.ShaderLoader;
import de.bitbrain.braingdx.util.ViewportFactory;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.Collection;

/**
 * Combined implementation of {@link RenderPipeline}. This pipeline will bake together all layers
 * and apply shaders for all layers underneath: <br/>
 *
 * <pre>
 * <code>{layer1}{layer2}{layer3}{end-layer3}{end-layer2}{end-layer1}</code>
 * </pre>
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class CombinedRenderPipeline implements RenderPipeline {

   private static final boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);

   private final ListOrderedMap orderedPipes = new ListOrderedMap();

   private final PostProcessor processor;

   private final FrameBufferFactory bufferFactory;

   private final ShaderConfig config;
   private final SpriteBatch internalBatch;
   private final ViewportFactory viewportFactory;
   private FrameBuffer buffer;
   private OrthographicCamera camera;
   private Viewport viewport;

   public CombinedRenderPipeline(ShaderConfig config, SpriteBatch internalBatch, OrthographicCamera camera, ViewportFactory viewportFactory) {
      this(config, new PostProcessor(true, true, isDesktop), new FrameBufferFactory() {

         @Override
         public FrameBuffer create(int width, int height) {
            return new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
         }

      }, internalBatch, camera, viewportFactory);
   }

   public CombinedRenderPipeline(ShaderConfig config, ViewportFactory viewportFactory) {
      this(config, new PostProcessor(true, true, isDesktop), new FrameBufferFactory() {

         @Override
         public FrameBuffer create(int width, int height) {
            return new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
         }

      }, new SpriteBatch(), new OrthographicCamera(), viewportFactory);
   }

   CombinedRenderPipeline(ShaderConfig config, PostProcessor processor, FrameBufferFactory factory,
                          SpriteBatch internalBatch, OrthographicCamera camera, ViewportFactory viewportFactory) {
      this.config = config;
      ShaderLoader.BasePath = this.config.basePath;
      ShaderLoader.PathResolver = this.config.pathResolver;
      this.processor = processor;
      this.bufferFactory = factory;
      this.internalBatch = internalBatch;
      this.camera = camera;
      this.viewportFactory = viewportFactory;
   }

   @Override
   public void dispose() {
      processor.dispose();
   }

   @SuppressWarnings("unchecked")
   @Override
   public void resize(int width, int height) {
      for (CombinedRenderPipe pipe : (Collection<CombinedRenderPipe>) orderedPipes.values()) {
         pipe.resize(width, height);
      }
      processor.setViewport(new Rectangle(0f, 0f, width, height));
      if (buffer != null) {
         buffer.dispose();
      }
      buffer = bufferFactory.create(width, height);
      camera.setToOrtho(true, width, height);
   }

   @Override
   public void put(String id, RenderLayer layer, PostProcessorEffect... effects) {
      CombinedRenderPipe pipe = new CombinedRenderPipe(layer, processor, camera, internalBatch, effects);
      orderedPipes.put(id, pipe);
   }

   @Override
   public void putAfter(String existing, String id, RenderLayer layer, PostProcessorEffect... effects) {
      int index = orderedPipes.indexOf(existing);
      if (index < 0) {
         Gdx.app.error("FATAL", "Unable add layer '" + id + "'!");
         return;
      }
      orderedPipes.put(index + 1, id, new CombinedRenderPipe(layer, processor, camera, internalBatch, effects));
   }

   @Override
   public void putBefore(String existing, String id, RenderLayer layer, PostProcessorEffect... effects) {
      int index = orderedPipes.indexOf(existing);
      if (index < 0) {
         Gdx.app.error("FATAL", "Unable add layer '" + id + "'!");
         return;
      }
      orderedPipes.put(index > 0 ? index - 1 : index, id, new CombinedRenderPipe(layer, processor, camera, internalBatch, effects));
   }

   @Override
   public RenderPipe getPipe(String id) {
      return (RenderPipe) (orderedPipes.containsKey(id) ? orderedPipes.get(id) : null);
   }

   @SuppressWarnings("unchecked")
   @Override
   public Collection<String> getPipeIds() {
      return orderedPipes.keySet();
   }

   @SuppressWarnings("unchecked")
   @Override
   public void render(Batch batch, float delta) {
      if (viewport == null) {
         this.viewport = viewportFactory.create((int) camera.viewportWidth, (int) camera.viewportHeight);
         this.viewport.setCamera(camera);
      }
      clearBuffer();
      viewport.update((int) camera.viewportWidth, (int) camera.viewportHeight);
      for (CombinedRenderPipe pipe : (Collection<CombinedRenderPipe>) orderedPipes.values()) {
         pipe.beforeRender();
         pipe.render(batch, delta, buffer);
      }
      internalBatch.setProjectionMatrix(camera.combined);
      internalBatch.begin();
      internalBatch.setColor(Color.WHITE);
      internalBatch.draw(buffer.getColorBufferTexture(), 0f, 0f);
      internalBatch.end();
   }

   private void clearBuffer() {
      buffer.begin();
      Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      buffer.end();
   }
}