package com.seibel.distanthorizons;

import com.seibel.distanthorizons.common.wrappers.McObjectConverter;
import com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import com.seibel.distanthorizons.core.api.internal.ClientApi;
import com.seibel.distanthorizons.core.util.math.Mat4f;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IClientLevelWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.nio.FloatBuffer;

public class RenderHelper {
	public static void drawLods() {
		boolean alphaTest = GL32.glIsEnabled(GL32.GL_ALPHA_TEST);
		boolean blend = GL32.glIsEnabled(GL32.GL_BLEND);
		int depthFunc = GL32.glGetInteger(GL32.GL_DEPTH_FUNC);
		
		GL32.glDisable(GL32.GL_ALPHA_TEST);
		
		Matrix4f rawModelView = new Matrix4f(ActiveRenderInfo.MODELVIEW);
		Matrix4f rawProjection = new Matrix4f(ActiveRenderInfo.PROJECTION);
		
		if (rawModelView == null || rawProjection == null) {
			if (alphaTest) GL32.glEnable(GL32.GL_ALPHA_TEST);
			if (blend) GL32.glEnable(GL32.GL_BLEND);
			GL32.glDepthFunc(depthFunc);
			return;
		}
		
		Mat4f mcModelViewMatrix = McObjectConverter.Convert(rawModelView);
		Mat4f mcProjectionMatrix = McObjectConverter.Convert(rawProjection);
		
		float frameTime = Minecraft.getMinecraft().timer.renderPartialTicks;
		IClientLevelWrapper levelWrapper = ClientLevelWrapper.getWrapper(Minecraft.getMinecraft().world);
		
		ClientApi.INSTANCE.renderLods(levelWrapper, mcModelViewMatrix, mcProjectionMatrix, frameTime);
		
		GL32.glDepthFunc(depthFunc);
		
		if (alphaTest) {
			GL32.glEnable(GL32.GL_ALPHA_TEST);
		} else {
			GL32.glDisable(GL32.GL_ALPHA_TEST);
		}
		
		if (blend) {
			GL32.glEnable(GL32.GL_BLEND);
		} else {
			GL32.glDisable(GL32.GL_BLEND);
		}
		
		// Will probably cause issue when iris gets ported
		GL32.glBindVertexArray(0);
		GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, 0);
		GL32.glBindBuffer(GL32.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glUseProgram(0);
	}
}
