package com.alexander.dungeonsadditions.client.renderers;

import com.alexander.dungeonsadditions.DungeonsAdditions;
import com.alexander.dungeonsadditions.client.models.DefaultGeoModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nullable;

public class DefaultExtendedGeoEntityRenderer<T extends MobEntity & IAnimatable> extends ExtendedGeoEntityRenderer<T> {

    @SuppressWarnings("uncheck")
    public DefaultExtendedGeoEntityRenderer(EntityRendererManager renderManager, String geoNames, String animationNames, String texturesNames) {
        super(renderManager, new DefaultGeoModel(geoNames, animationNames, texturesNames));
    }

    @Override
    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 0.9375F * 1.2F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.isArmorBone(bone)) bone.setCubesHidden(true);

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return bone.getName().startsWith("armor");
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, T windcallerEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, T currentEntity) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return offHand;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
            case "bipedHandRight2" :
                return mainHand;
            case DefaultBipedBoneIdents.POTION_BONE_IDENT:
                break;
        }
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
            case "bipedHandRight2" :
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemCameraTransforms.TransformType.NONE;
        }
    }

    @Override
    protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, T currentEntity, IBone bone) {
        if(item == this.mainHand || item == this.offHand) {
            stack.scale(1.1F, 1.1F, 1.1F);
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean shieldFlag = item.getItem() instanceof ShieldItem;
            if(item == this.mainHand) {
                if(shieldFlag) {
                    stack.translate(0.0, 0.125, -0.25);
                }
            }
            else {
                if(shieldFlag) {
                    stack.translate(-0.15, 0.125, 0.05);
                    stack.mulPose(Vector3f.YP.rotationDegrees(90));
                }
            }
        }
    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, T currentEntity, IBone bone) {
    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, T currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, T currentEntity) {
    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, T currentEntity) {
    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, T currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return boots;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return leggings;
            case "armorBipedBody":
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
                return chestplate;
            case "armorBipedHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return EquipmentSlotType.FEET;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return EquipmentSlotType.LEGS;
            case "armorBipedRightHand":
                return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedLeftHand":
                return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
            case "armorBipedBody":
                return EquipmentSlotType.CHEST;
            case "armorBipedHead":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorBipedModel) {
        switch (name) {
            case "armorBipedLeftFoot":
            case "armorBipedLeftLeg":
                return armorBipedModel.leftLeg;
            case "armorBipedRightFoot":
            case "armorBipedRightLeg":
                return armorBipedModel.rightLeg;
            case "armorBipedRightArm":
            case "armorIllagerRightArm":
                return armorBipedModel.rightArm;
            case "armorBipedLeftArm":
            case "armorIllagerLeftArm":
                return armorBipedModel.leftArm;
            case "armorBipedBody":
                return armorBipedModel.body;
            case "armorBipedHead":
                return armorBipedModel.head;
            default:
                return null;
        }
    }
}