package com.seibel.distanthorizons;

import com.seibel.distanthorizons.common.wrappers.McObjectConverter;
import com.seibel.distanthorizons.common.wrappers.world.ClientLevelWrapper;
import com.seibel.distanthorizons.core.api.internal.ClientApi;
import com.seibel.distanthorizons.core.util.math.Mat4f;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.IClientLevelWrapper;
import net.minecraft.client.Minecraft;
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
		
		Mat4f mcModelViewMatrix = getModelViewMatrix();
		Mat4f mcProjectionMatrix = getProjectionMatrix();
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
	}
	
	private static Matrix4f modelViewMatrix;
	private static Matrix4f projectionMatrix;
	
	public static Matrix4f getModelViewMatrixMC() {
		return new Matrix4f(modelViewMatrix);
	}
	
	public static Matrix4f getProjectionMatrixMC() {
		return new Matrix4f(projectionMatrix);
	}
	
	public static Mat4f getModelViewMatrix() {
		return McObjectConverter.Convert(modelViewMatrix);
	}
	
	public static Mat4f getProjectionMatrix() {
		return McObjectConverter.Convert(projectionMatrix);
	}
	
	public static void setModelViewMatrix(FloatBuffer modelview) {
		modelViewMatrix = new Matrix4f(modelview);
	}
	
	public static void setProjectionMatrix(FloatBuffer projection) {
		projectionMatrix = new Matrix4f(projection);
	}
	
	public static void HelpTess() {
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
	}
}
