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
package de.bitbrain.braingdx.graphics.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Handles the complete pipeline of rendering internally. The pipeline consists of render pipes.
 * Each pipe consists of two layers: the rendering layer and the shader layer which gets applied
 * afterwards.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class RenderPipeline {

    private List<RenderPipe> pipes;

    public RenderPipeline() {
	pipes = new ArrayList<RenderPipe>();
    }

    public void add(RenderLayer layer) {
	RenderPipe pipe = new RenderPipe(layer);
	pipes.add(pipe);
    }

    public void render(Batch batch, float delta) {
	for (RenderPipe pipe : pipes) {
	    pipe.render(batch, delta);
	}
    }
}
