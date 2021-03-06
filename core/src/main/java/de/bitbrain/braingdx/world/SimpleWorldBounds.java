package de.bitbrain.braingdx.world;

import com.badlogic.gdx.math.Rectangle;
import de.bitbrain.braingdx.world.GameWorld.WorldBounds;

public class SimpleWorldBounds implements WorldBounds {

   private final float width, height;

   private final Rectangle rect, target;

   public SimpleWorldBounds(float width, float height) {
      this.width = width;
      this.height = height;
      this.rect = new Rectangle();
      this.rect.width = width;
      this.rect.height = height;
      this.target = new Rectangle();
   }

   @Override
   public boolean isInBounds(GameObject object) {
      target.x = object.getLeft();
      target.y = object.getTop();
      target.width = object.getWidth();
      target.height = object.getHeight();
      return rect.contains(target) || rect.overlaps(target);
   }

   @Override
   public float getWorldWidth() {
      return width;
   }

   @Override
   public float getWorldHeight() {
      return height;
   }

   @Override
   public float getWorldOffsetX() {
      return 0;
   }

   @Override
   public float getWorldOffsetY() {
      return 0;
   }

}
