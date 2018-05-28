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

import net.hytekgames.skinchanger.SkinAPI;
import net.hytekgames.skinchanger.SkinAPI.SkinRequestException;
import net.hytekgames.skinchanger.SkinChanger;

public class Command_Setskin implements CommandExecutor {

	SkinChanger plugin;

	public Command_Setskin(SkinChanger plugin) {
		this.plugin = plugin;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player p = (Player) src;
		Player target = (Player) args.<Player>getOne("player").get();

		Collection<ProfileProperty> profile = target.getProfile().getPropertyMap().get("textures");
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
		plugin.getSkinApplier().setPlayerSkin(target);
		return CommandResult.success();

	}
}
