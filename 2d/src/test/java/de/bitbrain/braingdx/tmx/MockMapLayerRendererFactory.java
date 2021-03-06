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

package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;

import static org.mockito.Mockito.mock;

public class MockMapLayerRendererFactory implements MapLayerRendererFactory {

   @Override
   public GameObjectRenderer create(int index, TiledMap tiledMap, Camera camera) {
      return mock(GameObjectRenderer.class);
   }

   @Override
   public GameObjectRenderer createDebug(TiledMapContext context, State state, Camera camera) {
      return mock(GameObjectRenderer.class);
   }

}
