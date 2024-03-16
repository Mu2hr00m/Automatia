package network;

import com.mushroom.automatia.automatia;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketInit {
	private PacketInit() {
	}
	private static final String PROTOCOL = "1";
	public static final SimpleChannel MAIN = NetworkRegistry.newSimpleChannel(new ResourceLocation(automatia.MOD_ID,automatia.MOD_ID),() -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
	public static void init() {
		int index = 0;
		MAIN.messageBuilder(MachineFaceSettingsServerbound.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(MachineFaceSettingsServerbound::encode).decoder(MachineFaceSettingsServerbound::new).consumer(MachineFaceSettingsServerbound::handle).add();
	}
}
