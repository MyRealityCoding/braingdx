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

package de.bitbrain.braingdx.graphics.renderer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renderer implementation for sprites.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpriteRenderer extends GameObject2DRenderer {

   private final AssetManager assets = SharedAssetManager.getInstance();
   private final Vector2 offset = new Vector2();
   protected Sprite sprite;
   private Texture texture;

   public SpriteRenderer(String textureId) {
      if (textureId != null) {
         texture = assets.get(textureId, Texture.class);
         sprite = new Sprite(texture);
      }
   }

   public SpriteRenderer(Texture texture) {
      this.texture = texture;
      sprite = new Sprite(texture);
   }

   public void setOffset(float x, float y) {
      offset.set(x, y);
   }

   public Vector2 getOffset() {
      return offset;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      sprite.setPosition(object.getLeft() + object.getOffsetX() + offset.x,
            object.getTop() + object.getOffsetY() + offset.y);
      sprite.setSize(object.getWidth(), object.getHeight());
      sprite.setColor(object.getColor());
      sprite.setRotation(object.getRotation());
      sprite.setOrigin(object.getWidth() / 2f, object.getHeight() / 2f);
      sprite.setScale(object.getScaleX(), object.getScaleY());
      sprite.draw(batch, 1f);
   }
}
