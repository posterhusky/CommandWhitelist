package net.vanolex.commandwhitelist

import net.vanolex.lobbyutility.listeners.CommandListener
import org.bukkit.plugin.java.JavaPlugin


class CommandWhitelist : JavaPlugin() {
    override fun onEnable() {
        getLogger().info("Successfully enabled Command Whitelist!")

        server.pluginManager.registerEvents(CommandListener(this), this)

        config.options().copyDefaults()
        saveDefaultConfig()
    }

    override fun onDisable() {
        getLogger().info("Successfully disabled Command Whitelist!")
    }
}