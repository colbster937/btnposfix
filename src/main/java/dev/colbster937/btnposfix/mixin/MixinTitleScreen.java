package dev.colbster937.btnposfix.mixin;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {
  private static final List<Integer> FIX_BTNS;

  @Inject(method = "init", at = @At("TAIL"))
  private void fixBtns(CallbackInfo ci) {
    for (Object obj : this.buttons) {
      if (obj instanceof ButtonWidget) {
        ButtonWidget btn = (ButtonWidget) obj;
        if (FIX_BTNS.contains(btn.id)) btn.y -= 12;
      }
    }
  }

  static {
    FIX_BTNS = new ArrayList<>();
    FIX_BTNS.add(0);
    FIX_BTNS.add(4);
    FIX_BTNS.add(5);
  }
}
