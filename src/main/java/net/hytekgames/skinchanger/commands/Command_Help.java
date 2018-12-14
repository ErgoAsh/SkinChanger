package net.hytekgames.skinchanger.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import net.hytekgames.skinchanger.SkinChanger;

public class Command_Help implements CommandExecutor {

	SkinChanger plugin;

	public Command_Help(SkinChanger plugin) {
		this.plugin = plugin;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player p = (Player) src;
		Optional<String> subcommand = args.getOne("subcommand");
		
		if (!subcommand.isPresent()) {
			p.sendMessage(Text.builder("\u00a79--- SkinChanger help page (commands) ---").build());
			p.sendMessage(Text.builder("\u00a7a/sc <subcommand> - Type /sc help to get subcommands.").build());
			p.sendMessage(Text.builder("\u00a7a/skin <skin> - Set your skin.").build());
			p.sendMessage(Text.builder("\u00a7a/setskin <player> <skin> - Set someone skin.").build());
			return CommandResult.success();
		} else if (subcommand.get().equals("help")){
			p.sendMessage(Text.builder("\u00a79--- SkinChanger help page (subcommands) ---").build());
			p.sendMessage(Text.builder("\u00a7a/sc version - Get the current version of SkinChanger.").build());
			p.sendMessage(Text.builder("\u00a7a/sc github - Get the link to the SkinChanger github.").build());
			p.sendMessage(Text.builder("\u00a7a/sc permissions - Shows you the commands permissions.").build());
			// p.sendMessage(Text.builder("\u00a7a/sc reload - Reload the plugin configuration.").build()); TODO: ADD this
			return CommandResult.success();
		} else if (subcommand.get().equals("version")){
			p.sendMessage(Text.builder(SkinChanger.prefix + " \u00a7aActual version: " + 
					Sponge.getPluginManager().getPlugin("skinchanger").get().getVersion().get()).build());
			return CommandResult.success();
		} else if (subcommand.get().equals("github")){
			try {
				p.sendMessage(Text.builder(SkinChanger.prefix + " \u00a7aGithub link: https://github.com/TeKGameR950/SkinChanger")
					.onClick(TextActions.openUrl(new URL("https://github.com/TeKGameR950/SkinChanger"))).build());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return CommandResult.success();
		} else if (subcommand.get().equals("permissions")){
			p.sendMessage(Text.builder("\u00a79--- SkinChanger permissions page ---").build());
			p.sendMessage(Text.builder("\u00a7a/sc <subcommand> - \u00a79skinchanger.commands.help").build());
			p.sendMessage(Text.builder("\u00a7a/skin <skin> - \u00a79skinchanger.commands.skin").build());
			p.sendMessage(Text.builder("\u00a7a/setskin <player> <skin> - \u00a79skinchanger.commands.setskin").build());
			return CommandResult.success();
		} else {
			return CommandResult.success();
		}	
	}
}
