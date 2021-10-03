package me.kirkfox.noitemexplode.listener;

import me.kirkfox.noitemexplode.ConfigHandler;
import me.kirkfox.noitemexplode.WorldStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntityType() == EntityType.DROPPED_ITEM && WorldStorage.isProtectedChunk(e.getEntity().getLocation().getChunk()) &&
                e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION &&
                ConfigHandler.isProtectedEntity(e.getDamager().getType())) {
            e.setCancelled(true);
        }
    }

}
