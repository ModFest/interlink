package net.modfest.utilities;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.modfest.utilities.config.Config;

import javax.annotation.Nonnull;
import java.util.UUID;

public class DiscordChannelListener extends ListenerAdapter {
    private final MinecraftServer server;
    private final String channelId;

    public DiscordChannelListener(MinecraftServer server, String channelId) {
        this.server = server;
        this.channelId = channelId;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (!event.isFromGuild() || !event.getChannel().getId().equals(channelId) || event.getAuthor().isBot()) return;

        PlayerManager playerManager = server.getPlayerManager();
        if (playerManager != null) {
            LiteralText hoverText = (LiteralText) new LiteralText(event.getAuthor().getName()).formatted(Formatting.GRAY)
                    .append(new LiteralText("#" + event.getAuthor().getDiscriminator()).formatted(Formatting.DARK_GRAY));

            if (event.getMember() != null && event.getMember().getRoles().size() != 0) {
                hoverText.append(new LiteralText("\n\nRoles:").formatted(Formatting.GRAY));
                for (Role role : event.getMember().getRoles()) {
                    hoverText.append(new LiteralText("\n" + role.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(role.getColorRaw()))));
                }
            }

            LiteralText text = (LiteralText) new LiteralText("<@" + event.getAuthor().getName() + ">")
                    .setStyle(Style.EMPTY.withHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)
                    ).withColor(Formatting.BLUE)).append(new LiteralText(" " + event.getMessage().getContentStripped()).formatted(Formatting.GRAY));

            playerManager.broadcastChatMessage(text, MessageType.CHAT, UUID.randomUUID());
        }
    }
}
