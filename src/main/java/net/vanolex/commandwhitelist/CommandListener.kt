package net.vanolex.lobbyutility.listeners

import net.vanolex.commandwhitelist.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.server.TabCompleteEvent
import org.bukkit.permissions.Permission

class CommandListener(val plugin: CommandWhitelist): Listener {

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val p: Player = e.player

        for (i in plugin.config.getStringList("command-whitelist")) if (e.message.startsWith("/"+i)) return

        val msg: String = plugin.config.getString("unknown-command-msg")
            ?: "<red><bold>Unknown command<reset> <dark_gray>Type <bold>@help@<reset> <dark_gray>to get the command list."

        if (!p.hasPermission("net.vanolex.admin")) {
            e.isCancelled = true
            p.sendMessage(mm.deserialize(msg.replace("@help@", suggestCmd("help"))))
        } else {
            p.sendMessage("This command is inaccessible to normal players.")
        }
    }

    @EventHandler
    fun onCommandTab(e: PlayerCommandSendEvent) {
        if (e.player.hasPermission("net.vanolex.admin")) return
        val allowedCommands = plugin.config.getStringList("command-whitelist")
        val commandsToBlock: MutableList<String> = mutableListOf<String>()
        for (i in e.commands) if (i !in allowedCommands) commandsToBlock.add(i)
        for (i in commandsToBlock) e.commands.remove(i)
        val sorter = Comparator { s1: String, s2: String ->
            var result = 0
            if (s1.length > 2 && s2.length <= 2) result = 1
            else if (s1.length <= 2 && s2.length > 2) result = -1
            result
        }
        e.commands.sortedWith(sorter)
    }

    @EventHandler
    fun onHelpTab(e: TabCompleteEvent) {
        if (e.buffer.startsWith("/help", true)) e.completions = listOf()
    }

}