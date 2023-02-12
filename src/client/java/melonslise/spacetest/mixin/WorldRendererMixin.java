package melonslise.spacetest.mixin;

import melonslise.spacetest.compat.PlanetRendererFactory;
import melonslise.spacetest.render.PlanetWorldRenderer;
import melonslise.spacetest.render.planet.PlanetRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 1500)
public class WorldRendererMixin implements PlanetWorldRenderer
{
	private final PlanetRenderer planetRenderer = PlanetRendererFactory.createPlanetRenderer();

	@Override
	public PlanetRenderer getPlanetRenderer()
	{
		return this.planetRenderer;
	}

	@Inject(method = "setWorld", at = @At("RETURN"))
	private void setWorldInjectReturn(ClientWorld world, CallbackInfo ci)
	{
		this.planetRenderer.init(world, (WorldRenderer) (Object) this);
	}

	@Inject(method = "close", at = @At("RETURN"))
	private void closeInjectReturn(CallbackInfo ci)
	{
		this.planetRenderer.close();
	}

	@Inject(method = "scheduleBlockRenders(III)V", at = @At("RETURN"))
	private void scheduleBlockRenders3iInjectReturn(int x, int y, int z, CallbackInfo ci)
	{
		this.planetRenderer.scheduleRebuildForChunks(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1, false);
	}

	@Inject(method = "scheduleBlockRenders(IIIIII)V", at = @At("RETURN"))
	private void scheduleBlockRenders6iInjectReturn(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, CallbackInfo ci)
	{
		this.planetRenderer.scheduleRebuildForBlockArea(minX, minY, minZ, maxX, maxY, maxZ, false);
	}

	@Inject(method = "scheduleSectionRender", at = @At("RETURN"))
	private void scheduleSectionRenderInjectReturn(BlockPos pos, boolean important, CallbackInfo ci)
	{
		this.planetRenderer.scheduleRebuildForBlockArea(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, important);
	}

	@Inject(method = "scheduleChunkRender", at = @At("RETURN"))
	private void scheduleChunkRenderInjectReturn(int x, int y, int z, boolean important, CallbackInfo ci)
	{
		this.planetRenderer.scheduleRebuild(x, y, z, important);
	}
}