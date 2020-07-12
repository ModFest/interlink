package net.modfest.utilities.discord;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.MessageType;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.modfest.utilities.config.Config;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(!event.getChannel().getId().equals(Config.getInstance().getChannel())
        || event.getAuthor().isBot()) return;
        MinecraftDedicatedServer server = ((MinecraftDedicatedServer)FabricLoader.getInstance().getGameInstance());
        if(server.playerManager != null) {

            LiteralText hoverText = (LiteralText) new LiteralText(event.getAuthor().getName()).formatted(Formatting.GRAY)
                    .append(new LiteralText("#" + event.getAuthor().getDiscriminator()).formatted(Formatting.DARK_GRAY));

            if(event.getMember() != null && event.getMember().getRoles().size() != 0) {
                hoverText.append(new LiteralText("\n\nRoles:").formatted(Formatting.GRAY));
                for(Role role : event.getMember().getRoles()) {
                    hoverText.append(new LiteralText("\n" + role.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(role.getColorRaw()))));
                }
            }

            LiteralText text = (LiteralText) new LiteralText("<" + event.getAuthor().getName() + ">")
                    .setStyle(Style.EMPTY.setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)
                    ).withColor(Formatting.BLUE)).append(new LiteralText(" " + event.getMessage().getContentStripped()).formatted(Formatting.GRAY));

            server.playerManager.broadcastChatMessage(text, MessageType.CHAT, UUID.randomUUID());
        }
    }

//    private Text contructText(String messageRaw) {
//
//
//
//        return new LiteralText(messageRaw);
//    }
}
