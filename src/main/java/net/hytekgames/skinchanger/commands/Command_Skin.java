package net.hytekgames.skinchanger.commands;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.property.ProfileProperty;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.hytekgames.skinchanger.SkinAPI;
import net.hytekgames.skinchanger.SkinAPI.SkinRequestException;
import net.hytekgames.skinchanger.SkinChanger;

public class Command_Skin implements CommandExecutor {

	SkinChanger plugin;

	public Command_Skin(SkinChanger plugin) {
		this.plugin = plugin;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.builder(SkinChanger.prefix + " Sorry, this command can only be executed by players.")
					.color(TextColors.RED).build());
			return CommandResult.empty();
		}
		Player p = (Player) src;
		Collection<ProfileProperty> profile = p.getProfile().getPropertyMap().get("textures");
		String skin = args.<String>getOne("skin").get().toLowerCase();
		Optional<ProfileProperty> textures = null;
		try {
			String SkinUUID = SkinAPI.getUUID(skin);
			textures = SkinAPI.getSkinProperty(SkinUUID);
		} catch (SkinRequestException e) {
			if (e.getReason() == "USER_DOESNT_EXIST") {
				p.sendMessage(Text.builder(SkinChanger.prefix + "\u00a7c Sorry, This account does not exist.").build());
				return CommandResult.empty();
			}
		}

		profile.clear();
		profile.add(textures.get());
		plugin.getSkinApplier().setPlayerSkin(p);
		return CommandResult.success();

	}

}
