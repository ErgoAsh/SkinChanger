package net.hytekgames.skinchanger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import net.hytekgames.skinchanger.commands.Command_Help;
import net.hytekgames.skinchanger.commands.Command_SetSkin;
import net.hytekgames.skinchanger.commands.Command_Skin;

@Plugin(id = "skinchanger", name = "SkinChanger", version = "1.1")
public class SkinChanger {

	private static SkinChanger instance;
	public static String prefix = "\u00a79[SkinChanger]\u00a7r";
	private SkinApplier skinApplier;

	public static SkinChanger getInstance() {
		return instance;
	}

	@Listener
	public void onInitialize(GameInitializationEvent e) {
		instance = this;
		
		CommandSpec command_help = CommandSpec.builder()
				.description(Text.of("SkinChanger help command."))
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("subcommand"))))
				.permission("skinchanger.commands.help").executor(new Command_Help(this)).build();

		Sponge.getCommandManager().register(this, command_help, "sc", "skinchanger");

		CommandSpec command_skin = CommandSpec.builder()
				.description(Text.of("Set your skin."))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("skin"))))
				.permission("skinchanger.commands.skin").executor(new Command_Skin(this)).build();

		Sponge.getCommandManager().register(this, command_skin, "skin");

		CommandSpec command_setskin = CommandSpec.builder().description(Text.of("Set someone skin."))
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
						GenericArguments.string(Text.of("skin")))
				.permission("skinchanger.commands.setskin").executor(new Command_SetSkin(this)).build();

		Sponge.getCommandManager().register(this, command_setskin, "setskin");

		this.skinApplier = new SkinApplier();
	}

	public SkinApplier getSkinApplier() {
		return skinApplier;
	}
}