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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Internal state for tiled map operations.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
class State {

   private List<String> layerIds = Collections.emptyList();
   private Integer[][] heightMap;
   private final Map<String, CellState> stateMap = new HashMap<String, CellState>();
   private int mapIndexHeight;
   private int mapIndexWidth;
   private float cellWidth = 1f;
   private float cellHeight = 1f;
   private int numberOfLayers;

   public List<String> getLayerIds() {
      return layerIds;
   }

   public void setLayerIds(List<String> layerIds) {
      this.layerIds = Collections.unmodifiableList(layerIds);
   }

   public void setNumberOfLayers(int numberOfLayers) {
      this.numberOfLayers = numberOfLayers;
   }

   public Integer[][] getHeightMap() {
      return heightMap;
   }

   public void setHeightMap(Integer[][] heightMap) {
      this.heightMap = heightMap;
   }

   public int getMapIndexWidth() {
      return mapIndexWidth;
   }

   public int getMapIndexHeight() {
      return mapIndexHeight;
   }

   public float getCellWidth() {
      return cellWidth;
   }

   public void setCellWidth(float width) {
      if (width > 0) {
         this.cellWidth = width;
      }
   }

   public float getCellHeight() {
      return cellHeight;
   }

   public void setCellHeight(float height) {
      if (height > 0) {
         this.cellHeight = height;
      }
   }

   public CellState getState(int tileX, int tileY, int layerIndex) {
      if (layerIndex >= numberOfLayers) {
         throw new TiledMapException("Invalid state! Layer with index=" + layerIndex + " does not exist. Highest is " + numberOfLayers);
      }
      if (tileX >= getMapIndexWidth()) {
         tileX = getMapIndexWidth() - 1;
      } else if (tileX < 0) {
         tileX = 0;
      }
      if (tileY >= getMapIndexHeight()) {
         tileY = getMapIndexHeight() - 1;
      } else if (tileY < 0) {
         tileY = 0;
      }
      String key = layerIndex +"_" + tileX + "_" + tileY;
      CellState state = stateMap.get(key);
      if (state == null) {
         state = new CellState();
         stateMap.put(key, state);
      }
      return state;
   }

   public void setIndexDimensions(int indexX, int indexY) {
      this.mapIndexWidth = indexX;
      this.mapIndexHeight = indexY;
   }

   public void clear() {
      heightMap = null;
      layerIds = Collections.emptyList();
      stateMap.clear();
   }

   public int getNumberOfLayers() {
      return this.numberOfLayers;
   }

   public static class CellState {
      private boolean collision;
      private Object fingerprint;
      private MapProperties properties;

      public boolean isCollision() {
         return collision;
      }

      public void setCollision(boolean collision) {
         this.collision = collision;
      }

      public void setFingerprint(String fingerprint) {
         this.fingerprint = fingerprint;
      }

      public boolean isFingerprint(Object fingerprint) {
         return this.fingerprint != null && this.fingerprint.equals(fingerprint);
      }

      public MapProperties getProperties() {
         return properties;
      }

      public void setProperties(MapProperties properties) {
         this.properties = properties;
      }

      @Override
      public String toString() {
         return "CellState [collision=" + collision + ", properties=" + properties + "]";
      }

   }
}
