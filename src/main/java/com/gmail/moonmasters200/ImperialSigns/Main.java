package com.gmail.moonmasters200.ImperialSigns;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
  }
  
  HashMap<String, Integer> soupCoolDown1 = new HashMap();
  HashMap<String, Integer> soupCoolDownAll = new HashMap();
  public final String oneBowl = ChatColor.GOLD + "[" + ChatColor.RED + "Soup" + ChatColor.GOLD + "]";
  public final String allBowls = ChatColor.GOLD + "[" + ChatColor.RED + "Fillall" + ChatColor.GOLD + "]";
  int task;
  private Object bleh;
  
  @EventHandler
  public void onSignPlaceEvent(SignChangeEvent e)
  {
    if (!e.getPlayer().isOp()) {
      return;
    }
    String s = e.getLine(0);
    if (!s.equalsIgnoreCase("[fillone]")) {
      return;
    }
    e.setLine(0, this.oneBowl);
  }
  
  @EventHandler
  public void onSignPlaceEvent1(SignChangeEvent e)
  {
    if (!e.getPlayer().isOp()) {
      return;
    }
    String s = e.getLine(0);
    if (!s.equalsIgnoreCase("[fillall]")) {
      return;
    }
    e.setLine(0, this.allBowls);
  }
  
  @EventHandler
  public void onInteractSign(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      return;
    }
    Block b = e.getClickedBlock();
    if ((!b.getType().equals(Material.SIGN)) && (!b.getType().equals(Material.WALL_SIGN)) && (!b.getType().equals(Material.SIGN_POST))) {
      return;
    }
    Sign sign = (Sign)b.getState();
    String line = sign.getLine(0);
    if (line.equals(this.oneBowl))
    {
      if (this.soupCoolDown1.containsKey(p.getName()))
      {
        p.sendMessage(ChatColor.RED + "You can't get one soup for" + ChatColor.GOLD + formatTime(getTimeLeft(p, this.soupCoolDown1)));
        return;
      }
      if (p.getInventory().firstEmpty() == -1)
      {
        p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.MUSHROOM_SOUP, 1));
        p.sendMessage(ChatColor.RED + "Inventory full soup dropped on floor");
      }
      else
      {
        p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.MUSHROOM_SOUP, 1) });
        p.sendMessage(ChatColor.GREEN + "Soup added!");
      }
      setCooldownLength(p, 15, this.soupCoolDown1);
      startCooldown(p, this.soupCoolDown1);
    }
    else if (line.equals(this.allBowls))
    {
      if (this.soupCoolDownAll.containsKey(p.getName()))
      {
        p.sendMessage(ChatColor.RED + "You can't get all bowls for" + ChatColor.GOLD + formatTime(getTimeLeft(p, this.soupCoolDownAll)));
        return;
      }
      int slot = 0;
      for (ItemStack i : p.getInventory().getContents()) {
        if (i == null)
        {
          p.getInventory().setItem(slot, new ItemStack(Material.MUSHROOM_SOUP, 1));
          slot++;
        }
        else
        {
          slot++;
        }
      }
      p.sendMessage(ChatColor.GREEN + "Soup filled!");
      setCooldownLength(p, 1200, this.soupCoolDownAll);
      startCooldown(p, this.soupCoolDownAll);
    }
    else if (line.equals(this.bleh))
    {
      
      
      
    }
  }
  
  public void setCooldownLength(Player player, int time, HashMap<String, Integer> hashmap)
  {
    hashmap.put(player.getName(), Integer.valueOf(time));
  }
  
  public int getTimeLeft(Player player, HashMap<String, Integer> hashmap)
  {
    int time = ((Integer)hashmap.get(player.getName())).intValue();
    return time;
  }
  
  @SuppressWarnings("deprecation")
  public void startCooldown(final Player player, final HashMap<String, Integer> hashmap)
  {
    this.task = Bukkit.getServer().getScheduler()
      .scheduleSyncRepeatingTask(this, new BukkitRunnable()
      {
        public void run()
        {
          if (hashmap.containsKey(player.getName()))
          {
            int time = ((Integer)hashmap.get(player.getName())).intValue();
            if (time != 0)
            {
              hashmap.put(player.getName(), Integer.valueOf(time - 1));
            }
            else
            {
              hashmap.remove(player.getName());
              Bukkit.getServer().getScheduler().cancelTask(Main.this.task);
            }
          }
        }
      }, 0L, 20L);
  }
  
  public static String formatTime(int secs)
  {
    if (secs == 0) {
      return " 1 second";
    }
    int remainder = secs % 86400;
    
    int days = secs / 86400;
    int hours = remainder / 3600;
    int minutes = remainder / 60 - hours * 60;
    int seconds = remainder % 3600 - minutes * 60;
    
    String fDays = days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "";
    String fHours = hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "";
    String fMinutes = minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "";
    String fSeconds = seconds > 0 ? " " + seconds + " second" + (seconds > 1 ? "s" : "") : "";
    
    return fDays + fHours + 
      fMinutes + fSeconds;
  }
  
  public void subtractCost(String playerName, String cost)
  {
    /** Command: /points remove <amount> <player> */
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "points remove " + cost + " " + playerName);
  }
}
