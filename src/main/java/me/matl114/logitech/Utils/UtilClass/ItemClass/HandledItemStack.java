package me.matl114.logitech.Utils.UtilClass.ItemClass;

import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class HandledItemStack extends ItemStack {
    public ItemStack handle;
    public ItemMeta handleMeta;
    public HandledItemStack(ItemStack itemStack) {
        super();
        this.handle = itemStack;
    }
    public void addEnchantment(Enchantment ench, int level) {
        this.handle.addEnchantment(ench, level);
    }
    public void addEnchantments( Map<Enchantment, Integer> enchantments) {
        this.handle.addEnchantments(enchantments);

    }

    public void addUnsafeEnchantment( Enchantment ench, int level) {
        this.handle.addUnsafeEnchantment(ench, level);
    }

    public void addUnsafeEnchantments( Map<Enchantment, Integer> enchantments) {
        this.handle.addUnsafeEnchantments(enchantments);

    }



    public ItemStack clone() {
        return this.handle.clone();
    }

    public boolean containsEnchantment( Enchantment ench) {
        return this.handle.containsEnchantment(ench);
    }

    private ItemMeta createItemMeta(){
        handleMeta = handle.getItemMeta();
        return handleMeta;
    }

    public boolean equals(Object obj) {
        return this.handle.equals(obj);
    }

    public int getAmount() {
        return this.handle.getAmount();
    }

    @Nullable
    public MaterialData getData() {
        return this.handle.getData();
    }

    /** @deprecated */
    @Deprecated
    public short getDurability() {
        return   this.handle.getDurability();
    }

    public int getEnchantmentLevel( Enchantment ench) {
        return this.handle.getEnchantmentLevel(ench);
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.handle.getEnchantments();
    }



    @Nullable
    public ItemMeta getItemMeta() {
        return this.handleMeta==null?createItemMeta():this.handleMeta;
    }

    public int getMaxStackSize() {
        return this.handle.getMaxStackSize();
    }

    public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    public Material getType() {
        return this.handle.getType();
    }

    public int hashCode() {
        return this.handle.hashCode();
    }

    public boolean hasItemMeta() {
        return handle.hasItemMeta();
    }

    public boolean isSimilar(@Nullable ItemStack stack) {
        return this.handle.isSimilar(stack);
    }


    public int removeEnchantment( Enchantment ench) {
        return this.handle.removeEnchantment(ench);
    }

    public void removeEnchantments() {
        this.handle.removeEnchantments();
    }

    public Map<String, Object> serialize() {
        return this.handle.serialize();
    }

    public void setAmount(int amount) {
        this.handle.setAmount(amount);
    }

    public void setData(@Nullable MaterialData data) {
        this.handle.setData(data);
    }

    /** @deprecated */
    @Deprecated
    public void setDurability(short durability) {
       this.handle.setDurability(durability);

    }

    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {

        this.handleMeta = itemMeta;
        return true;
    }

    public void setType( Material type) {
        this.handle.setType(type);
    }





    public String toString() {
        return this.handle.toString();
    }
}
