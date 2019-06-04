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

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.movement.TiledCollisionResolver;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Provides extended operations on {@link TiledMap} objects.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TiledMapAPI extends TiledCollisionResolver {

   /**
    * Sets an optional event factory which is utilized to create
    * game events. A game event is any event matching the criteria
    * of the event factory.
    *
    * @param eventFactory a new event factory. Not defined by default.
    */
   void setEventFactory(GameEventFactory eventFactory);

   int highestZIndexAt(int tileX, int tileY);

   int highestZIndexAt(float x, float y);

   int layerIndexOf(GameObject object);

   int lastLayerIndexOf(GameObject object);

   int getNumberOfRows();

   int getNumberOfColumns();

   void setLayerIndex(GameObject object, int layerIndex);

   GameObject getGameObjectAt(int tileX, int tileY, int layer);

   MapProperties getPropertiesAt(int tileX, int tileY, int layer);

   float getCellWidth();

   float getCellHeight();

   float getWorldWidth();

   float getWorldHeight();

   boolean isDebug();

   void setDebug(boolean enabled);
}
