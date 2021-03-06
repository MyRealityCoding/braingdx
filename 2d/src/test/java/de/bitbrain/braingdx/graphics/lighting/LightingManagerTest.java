package de.bitbrain.braingdx.graphics.lighting;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerImpl.LightFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static de.bitbrain.braingdx.utils.GdxUtils.mockApplicationContext;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LightingManagerTest {

   @Mock
   private RayHandler rayHandler;

   @Mock
   private LightFactory lightFactory;

   @Mock
   PointLight pointLightMock;

   @InjectMocks
   private LightingManagerImpl lightingManager;

   @Before
   public void beforeTest() {
      when(lightFactory.newPointLight(any(RayHandler.class), anyInt(), any(Color.class), anyFloat(), anyFloat(), anyFloat())).thenReturn(pointLightMock);
      mockApplicationContext();
   }

   @Test
   public void testRemoveLight_Point() {
      lightingManager.destroyLight(lightingManager.createPointLight(new Vector2(), 0f, Color.WHITE));
      verify(pointLightMock, times(1)).remove();
   }

   @Test
   public void testClear() {
      lightingManager.clear();
      lightingManager.destroyLight(lightingManager.createPointLight(new Vector2(), 0f, Color.WHITE));
      verify(pointLightMock, times(1)).remove();
   }

}
