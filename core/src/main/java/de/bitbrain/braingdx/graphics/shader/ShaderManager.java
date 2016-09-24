/* Copyright 2016 Miguel Gonzalez Sanchez
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

package de.bitbrain.braingdx.graphics.shader;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.utils.ShaderLoader;

/**
 * Manages GLSL shaders internally
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class ShaderManager {

    private static final boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);

    private PostProcessor processor;

    public ShaderManager(String baseDirectory, PostProcessorEffect... effects) {
	ShaderLoader.BasePath = baseDirectory;
	processor = new PostProcessor(true, true, isDesktop);
	for (PostProcessorEffect effect : effects) {
	    processor.addEffect(effect);
	}
    }

    public void begin() {
	processor.capture();
    }

    public void end(FrameBuffer buffer) {
	processor.render(buffer);
    }

    public void end() {
	processor.render();
    }

    public void dispose() {
	processor.dispose();
    }

    public void resume() {
	processor.rebind();
    }
}
