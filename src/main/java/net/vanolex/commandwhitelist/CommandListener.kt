package net.vanolex.commandwhitelist

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.server.TabCompleteEvent

class CommandListener(private val plugin: CommandWhitelist) : Listener {

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val p: Player = e.player

        // return if the command is allowed
        for (i in plugin.config.getStringList("command-whitelist")) {
            if (e.message.startsWith("/$i")) {
                return
            }
        }

        val msg: String = plugin.config.getString("unknown-command-msg")
                ?: "<red><bold>Unknown command<reset> <dark_gray>Type <bold>@help@<reset> <dark_gray>to get the command list."

        // users with the net.vanolex.admin permission can still execute the commands
        if (!p.hasPermission("net.vanolex.admin")) {
            e.isCancelled = true
            p.sendMessage(mm.deserialize(msg.replace("@help@", suggestCmd("help"))))
        } else {
            p.sendPlainMessage("This command is inaccessible to normal players.")
        }
    }

    @EventHandler
    fun onCommandTab(e: PlayerCommandSendEvent) {
        if (e.player.hasPermission("net.vanolex.admin")) return

        val allowedCommands = plugin.config.getStringList("command-whitelist")

        // removes all the commands that aren't allowed (errors will occur if you don't clone e.commands)
        //
        // can you use         e.commands.clear() here?
        //
        for (i in ArrayList(e.commands)) {
            if (i !in allowedCommands) {
                e.commands.remove(i)
            }
        }

        // command sorting (puts aliases to the bottom of the tab completion list)
        e.commands.sortedWith { s1: String, s2: String ->
            var result = 0
            if (s1.length > 2 && s2.length <= 2) {
                result = 1
            } else if (s1.length <= 2 && s2.length > 2) {
                result = -1
            }
            result
        }

        plugin.logger.info("Provided ${e.player.name} with: ${e.commands}")
    }

    @EventHandler
    fun onHelpTab(e: TabCompleteEvent) {
        // removes the /help tab completions
        if (e.buffer.startsWith("/help", true)) {
            e.completions = listOf()
        }
    }

}