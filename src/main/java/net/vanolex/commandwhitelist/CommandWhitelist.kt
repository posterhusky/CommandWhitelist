package net.vanolex.commandwhitelist

import org.bukkit.plugin.java.JavaPlugin


class CommandWhitelist : JavaPlugin() {
    override fun onEnable() {
        logger.info("Successfully enabled Command Whitelist!")

        // register listener
        server.pluginManager.registerEvents(CommandListener(this), this)

        // load config
        config.options().copyDefaults()
        saveDefaultConfig()
    }

    override fun onDisable() {
        logger.info("Successfully disabled Command Whitelist!")
    }
}