package de.bitbrain.braingdx.graphics.pipeline;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.postprocessing.PostProcessor;

public class LayeredRenderPipelineFactory implements RenderPipelineFactory {

    @Override
    public RenderPipeline create() {
	ShaderConfig config = mock(ShaderConfig.class);
	PostProcessor processorMock = mock(PostProcessor.class);
	FrameBufferFactory factory = mock(FrameBufferFactory.class);
	FrameBuffer buffer = mock(FrameBuffer.class);
	when(factory.create(Mockito.anyInt(), Mockito.anyInt())).thenReturn(buffer);
	return new LayeredRenderPipeline(config, processorMock, factory);
    }

}
