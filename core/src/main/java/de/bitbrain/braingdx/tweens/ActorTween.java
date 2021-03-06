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

package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Tween facility for actors
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @version 1.0.0
 * @since 1.0.0
 */
public class ActorTween implements TweenAccessor<Actor> {

   public static final int ALPHA = 1;

   public static final int SCALE = 2;

   public static final int SIZE = 3;

   public static final int ROTATION = 4;

   public static final int X = 5;

   public static final int Y = 6;

   @Override
   public int getValues(Actor target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case ALPHA:
            returnValues[0] = target.getColor().a;
            return 1;
         case SCALE:
            returnValues[0] = target.getScaleX();
            returnValues[1] = target.getScaleY();
            return 1;
         case SIZE:
            returnValues[0] = target.getWidth();
            returnValues[1] = target.getHeight();
            return 1;
         case ROTATION:
            returnValues[0] = target.getRotation();
            return 1;
         case X:
            returnValues[0] = target.getX();
            return 1;
         case Y:
            returnValues[0] = target.getY();
            return 1;
         default:
            return 0;
      }
   }

   @Override
   public void setValues(Actor target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case ALPHA:
            target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
            break;
         case ROTATION:
            target.setRotation(newValues[0]);
            break;
         case SCALE:
            if (target instanceof Label)
               ((Label) target).setFontScale(newValues[0]);
            target.setScaleX(newValues[0]);
            target.setScaleY(newValues[1]);
            break;
         case SIZE:
            target.setWidth(newValues[0]);
            target.setHeight(newValues[1]);
            break;
         case X:
            target.setX(newValues[0]);
            break;
         case Y:
            target.setY(newValues[0]);
            break;
      }
   }

}
