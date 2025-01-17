package com.willfp.libreforge.filters.filters

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.items.Items
import com.willfp.libreforge.filters.Filter
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.wrappers.WrappedDropEvent

object FilterItems : Filter() {
    override fun passes(data: TriggerData, config: Config): Boolean {
        val itemList = listOf(data.item)
        val unfilteredItems = (data.event as? WrappedDropEvent<*>)?.items ?: itemList
        val items = unfilteredItems.filterNotNull()
        if (items.isEmpty()) {
            return true
        }

        return config.withInverse("items", Config::getStrings) {
            it.map { lookup -> Items.lookup(lookup) }.any { testableItem ->
                items.any { item -> testableItem.matches(item) }
            }
        }
    }
}
