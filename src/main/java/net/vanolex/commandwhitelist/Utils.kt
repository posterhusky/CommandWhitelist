package net.vanolex.commandwhitelist

import net.kyori.adventure.text.minimessage.MiniMessage

val mm = MiniMessage.miniMessage();

val suggestCmd = { s: String -> "<hover:show_text:'<blue><bold>Click to get /${s}'><click:suggest_command:/${s}>/${s}" }