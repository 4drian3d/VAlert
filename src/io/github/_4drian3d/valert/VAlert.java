package io.github._4drian3d.valert;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class VAlert {
    @Inject
    private ComponentLogger logger;
    @Inject
    private ProxyServer proxyServer;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info(miniMessage().deserialize("<gradient:red:blue:green>Starting VAlert"));

        final var node = BrigadierCommand.literalArgumentBuilder("valert")
                .requires(src -> src.hasPermission("valert.command"))
                .then(BrigadierCommand.requiredArgumentBuilder("message", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            final String argument = StringArgumentType.getString(ctx, "message");
                            final Component message = miniMessage().deserialize(argument, ctx.getSource(), MiniPlaceholders.audienceGlobalPlaceholders());
                            proxyServer.sendMessage(message);
                            return Command.SINGLE_SUCCESS;
                        })
                ).build();
        final BrigadierCommand command = new BrigadierCommand(node);

        final CommandManager commandManager = proxyServer.getCommandManager();
        commandManager.register(commandManager.metaBuilder(command).plugin(this).build(), command);
    }
}
