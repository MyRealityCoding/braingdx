package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.world.GameObject;

public class OrthogonalMapLayerRendererFactory implements MapLayerRendererFactory {

   @Override
   public GameObjectRenderer create(int index, TiledMap tiledMap, Camera camera) {
      if (!(camera instanceof OrthographicCamera)) {
         throw new RuntimeException("OrthographicCamera must be provided for Orthographic TiledMaps!");
      }
      return new OrthogonalMapLayerRenderer(index, tiledMap, (OrthographicCamera) camera);
   }

   @Override
   public GameObjectRenderer createDebug(final TiledMapAPI api, final State state, final Camera camera) {
      if (!(camera instanceof OrthographicCamera)) {
         throw new RuntimeException("OrthographicCamera must be provided for Orthographic TiledMaps!");
      }
      return new GameObjectRenderer() {

         private Texture texture;

         @Override
         public void init() {
            Color color = new Color(Color.PINK);
            color.a = 0.2f;
            texture = GraphicsFactory.createTexture(2, 2, color);
         }

         @Override
         public void render(GameObject object, Batch batch, float delta) {
            if (!api.isDebug()) {
               return;
            }
            for (int x = 0; x < state.getMapIndexWidth(); ++x) {
               for (int y = 0; y < state.getMapIndexHeight(); y++) {
                  for (int layer = 0; layer < state.getNumberOfLayers(); ++layer) {
                     if (state.getState(x, y, layer).isCollision()) {
                        batch.setProjectionMatrix(camera.combined);
                        batch.draw(texture, x * state.getCellWidth(), y * state.getCellHeight(), state.getCellWidth(), state.getCellHeight());
                     }
                  }
               }
            }
         }

      };
   }

}
