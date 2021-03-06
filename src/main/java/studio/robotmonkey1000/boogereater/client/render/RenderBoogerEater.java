package studio.robotmonkey1000.boogereater.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import studio.robotmonkey1000.boogereater.BoogerMain;
import studio.robotmonkey1000.boogereater.client.model.ModelBoogerEater;
import studio.robotmonkey1000.boogereater.client.model.ModelBubble;
import studio.robotmonkey1000.boogereater.common.configuration.ModConfigurations;
import studio.robotmonkey1000.boogereater.common.entity.EntityBoogerEater;

@OnlyIn(value=Dist.CLIENT)
public class RenderBoogerEater extends BipedRenderer<EntityBoogerEater, ModelBoogerEater> {
	
	//Texture for the booger eaters skin
	private static final ResourceLocation TEXTURE = new ResourceLocation(BoogerMain.MOD_ID, "textures/entity/booger_eater/booger_eater.png");
	
	//Speech Bubbles model
	public final ModelBubble MODEL_BUBBLE = new ModelBubble();
	
	//Texture for the speech bubble
	public static final ResourceLocation BUBBLE = new ResourceLocation(BoogerMain.MOD_ID, "textures/entity/bubble/bubble.png");
	
	//Font Renderer for rendering text in the world
	public static FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;

	public RenderBoogerEater(EntityRendererManager rendererManager) {
		super(rendererManager, new ModelBoogerEater(), 0.5f);
	}
	
	@Override
	public void render(EntityBoogerEater ent, float f, float f2, MatrixStack matrix, IRenderTypeBuffer buffer, int i) {
		
		//Render Biped Model
		super.render(ent, f, f2, matrix, buffer, i);
		
		//If enableSpeechBubble is set to true in the config then render the speech bubble
		if(ModConfigurations.BOOGER_CONFIG.enableSpeechBubble) {
			matrix.push();
			
			//Bind the texture for the Bubble and set color
			this.renderManager.textureManager.bindTexture(BUBBLE);
			buffer.getBuffer(MODEL_BUBBLE.getRenderType(BUBBLE)).color(1.0F, 1.0F, 1.0F, 1.0f);
			
			//Move the bubble above the head of the booger eater.
			matrix.translate(0, 1.01F, 0);
			
			//Rotate based on the entities rotation 
			Quaternion q = new Quaternion(0, 180 - ent.renderYawOffset, 1, true);
			matrix.rotate(q);
			
			//Scale
			matrix.scale(1.0F, 0.35F, 0.01F);
			
			//Get the vertex builder from the texture
			IVertexBuilder vertex = ItemRenderer.getBuffer(buffer, MODEL_BUBBLE.getRenderType(BUBBLE), false, false);
			
			//Render the bubble
			MODEL_BUBBLE.render( matrix, vertex, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0f);
			matrix.pop();
			
			//Render Text Section
			matrix.push();
			
			//Render the text
			renderText(ent, 1, matrix, buffer);
			
			matrix.pop();
		}
			
	}
	
	//Renders the booger eaters message
	protected void renderText(EntityBoogerEater ent, float rotation, MatrixStack matrix, IRenderTypeBuffer buffer){
		
		//Translate the text into the position within the speech bubble
		matrix.translate(-Math.sin(ent.renderYawOffset * 0.017f) * 0.01F, 1.5F, Math.cos(ent.renderYawOffset * 0.017f) * 0.01F);
		
		//Scale the text down to fit it into
		matrix.scale(0.010416667F, 0.010416667F, 0.010416667F);		

		//Rotate the text based on the view of the mob
		Quaternion q = new Quaternion(0, 180 - ent.renderYawOffset, 1, true);
		matrix.rotate(q);
		
		//Flip the text because its upside down
		q = new Quaternion(0, 0, 180, true);
		matrix.rotate(q);

		
		buffer.getBuffer(MODEL_BUBBLE.getRenderType(BUBBLE)).normal(0, 0, -0.010416667F);
		
//		GlStateManager.depthMask(false);
		
		//Render the message in the world with the RGB value
		fontrenderer.drawString(matrix, ent.getMessage(), -40, 0, 100);
		
//		GlStateManager.depthMask(true);

		buffer.getBuffer(MODEL_BUBBLE.getRenderType(BUBBLE)).color(1.0F, 1.0F, 1.0F, 1.0f);
	}

	public ResourceLocation getEntityTexture(EntityBoogerEater entity) {
		return TEXTURE;
	}
	
}