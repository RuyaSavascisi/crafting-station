package tfar.craftingstation.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ClearButton extends Button {


  public ClearButton(int x, int y, int widthIn, int heightIn, OnPress callback) {
    super(x, y, widthIn, heightIn, Component.empty(), callback,Button.DEFAULT_NARRATION);
  }

  private static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));



  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    Minecraft minecraft = Minecraft.getInstance();
    guiGraphics.setColor(1,0,0,this.alpha);
    RenderSystem.enableBlend();
    RenderSystem.enableDepthTest();
    guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    int i = 0xff0000;//getFGColor();
    this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
  }

  private int getTextureY() {
    int i = 1;
    if (!this.active) {
      i = 0;
    } else if (this.isHoveredOrFocused()) {
      i = 2;
    }

    return 46 + i * 20;
  }

}