package me.kirkfox.noitemexplode.listener;

import me.kirkfox.noitemexplode.ConfigHandler;
import me.kirkfox.noitemexplode.RegionStorage;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Item && RegionStorage.isProtectedChunk(e.getEntity().getLocation().getChunk()) &&
                e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            Item i = (Item) e.getEntity();
            if(ConfigHandler.shouldProtect(i.getItemStack().getType(), e.getDamager().getType())) {
                e.setCancelled(true);
            }
        }
    }

}
