package com.alexander.dungeonsadditions.client.renderers;

import com.alexander.dungeonsadditions.client.models.RovingMarketerModel;
import com.alexander.dungeonsadditions.entities.RovingMarketerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RovingMarketerRenderer extends GeoEntityRenderer<RovingMarketerEntity> {
	
	public RovingMarketerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new RovingMarketerModel());
	}
	
	protected void applyRotations(RovingMarketerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {
       // float scaleFactor = 0.9375F;
       // matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(RovingMarketerEntity animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
