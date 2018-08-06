package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Camera;
import de.bitbrain.braingdx.util.Resizeable;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Tracks a camera and make it following a target
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public interface GameCamera extends Resizeable {

   /**
    * Updates the tracker in a frame step
    *
    * @param delta the frame delta
    */
   void update(float delta);

   /**
    * Sets a new speed value
    *
    * @param speed
    */
   void setTargetTrackingSpeed(float speed);

   /**
    * Sets a new zoom scale factor
    *
    * @param zoomScale
    */
   void setZoomScalingFactor(float zoomScale);

   /**
    * Enables or disables the camera to stick to the current
    * {@link GameWorld} bounds. By default this is enabled.
    */
   void setStickToWorldBounds(boolean enabled);

   /**
    * Provides the current base zoom
    *
    * @return baseZoom
    */
   float getDefaultZoomFactor();

   /**
    * Sets the base zoom
    *
    * @param zoom baseZoom
    */
   void setDefaultZoomFactor(float zoom);

   /**
    * Zooms the current baseZoom.
    *
    * @param amount the base zoom amount
    */
   void zoom(float amount);

   /**
    * Focuses directly on the target
    */
   void focusCenteredOnObject();

   /**
    * When no target is defined or the given object is the target, it focuses the
    * game camera onto the given object.
    */
   void focusCenteredOnObject(GameObject object);

   /**
    * Sets a new target. Focuses by default.
    *
    * @param target the target
    */
   void setTrackingTarget(GameObject target);

   /**
    * Sets a new target and determines if focusCenteredOnObject or not.
    *
    * @param target the target
    * @param focus  focusCenteredOnObject target on attach
    */
   void setTrackingTarget(GameObject target, boolean focus);

   /**
    * Provides the internal camera
    *
    * @return the internal camera object
    */
   Camera getInternalCamera();

}
