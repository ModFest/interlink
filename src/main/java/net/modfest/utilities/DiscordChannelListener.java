package net.modfest.utilities;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import javax.annotation.Nonnull;

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
            MutableText hoverText = Text.literal(event.getAuthor().getName()).formatted(Formatting.GRAY)
                    .append(Text.literal("#" + event.getAuthor().getDiscriminator()).formatted(Formatting.DARK_GRAY));

            if (event.getMember() != null && event.getMember().getRoles().size() != 0) {
                hoverText.append(Text.literal("\n\nRoles:").formatted(Formatting.GRAY));
                for (Role role : event.getMember().getRoles()) {
                    hoverText.append(Text.literal("\n" + role.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(role.getColorRaw()))));
                }
            }

            MutableText text = Text.literal("<@" + event.getAuthor().getName() + ">")
                    .setStyle(Style.EMPTY.withHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)
                    ).withColor(Formatting.BLUE)).append(Text.literal(" " + event.getMessage().getContentStripped()).formatted(Formatting.GRAY));

            playerManager.broadcast(text, false);
        }
    }
}
