package net.hytekgames.skinchanger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import com.flowpowered.math.vector.Vector3d;

public class SkinApplier {

	private Player receiver;

	public void setPlayerSkin(Player p) {
		receiver = p;

		sendUpdate();
	}

	private void sendUpdate() {
		sendUpdateSelf();

		receiver.offer(Keys.VANISH, true);
		Sponge.getScheduler().createTaskBuilder().execute(() -> receiver.offer(Keys.VANISH, false)).delayTicks(1)
				.submit(SkinChanger.getInstance());
	}

	// /!\ THIS IS GOING TO BE REMOVED WHEN THE AARON1011 NEW SKIN UPDATE PULL
	// REQUEST WILL BE ACCEPTED ON SPONGE /!\
	private void sendUpdateSelf() {
		receiver.getTabList().removeEntry(receiver.getUniqueId());
		receiver.getTabList()
				.addEntry(TabListEntry.builder().displayName(receiver.getDisplayNameData().displayName().get())
						.latency(receiver.getConnection().getLatency()).list(receiver.getTabList())
						.gameMode(receiver.getGameModeData().type().get()).profile(receiver.getProfile()).build());

		Location<World> loc = receiver.getLocation();
		Vector3d rotation = receiver.getRotation();

		WorldProperties other = null;
		for (WorldProperties w : Sponge.getServer().getAllWorldProperties()) {
			if (other != null) {
				break;
			}

			if (!w.getUniqueId().equals(receiver.getWorld().getUniqueId())) {
				Sponge.getServer().loadWorld(w.getUniqueId());
				other = w;
			}
		}

		if (other != null) {
			receiver.setLocation(Sponge.getServer().getWorld(other.getUniqueId()).get().getSpawnLocation());
			receiver.setLocationAndRotation(loc, rotation);
			receiver.sendMessage(Text.builder(SkinChanger.prefix + "\u00a7a Your skin has successfully been changed!").build());
		}
	}

}
