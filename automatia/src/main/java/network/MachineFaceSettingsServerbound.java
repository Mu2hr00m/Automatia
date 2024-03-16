package network;

import java.util.function.Supplier;

import com.mushroom.automatia.block.entity.EnergyCubeBE;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class MachineFaceSettingsServerbound {
	public final BlockPos bePos;
	public final short faceData;
	public MachineFaceSettingsServerbound(BlockPos pos,short data) {
		this.bePos = pos;
		this.faceData = data;
	}
	public MachineFaceSettingsServerbound(FriendlyByteBuf buffer) {
		this.bePos = buffer.readBlockPos();
		this.faceData = buffer.readShort();
	}
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(bePos);
		buffer.writeShort(faceData);
	}
	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			final BlockEntity be = context.get().getSender().level.getBlockEntity(this.bePos);
			if(be instanceof EnergyCubeBE) {
				((EnergyCubeBE)be).decompileTransfers(this.faceData);
				be.setChanged();
			}
		});
		context.get().setPacketHandled(true);
	}
}
