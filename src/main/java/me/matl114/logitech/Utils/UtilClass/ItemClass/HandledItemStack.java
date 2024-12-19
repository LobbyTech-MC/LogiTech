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
    @Override
	public void addEnchantment(Enchantment ench, int level) {
        this.handle.addEnchantment(ench, level);
    }
    @Override
	public void addEnchantments( Map<Enchantment, Integer> enchantments) {
        this.handle.addEnchantments(enchantments);

    }

    @Override
	public void addUnsafeEnchantment( Enchantment ench, int level) {
        this.handle.addUnsafeEnchantment(ench, level);
    }

    @Override
	public void addUnsafeEnchantments( Map<Enchantment, Integer> enchantments) {
        this.handle.addUnsafeEnchantments(enchantments);

    }



    @Override
	public ItemStack clone() {
        return this.handle.clone();
    }

    @Override
	public boolean containsEnchantment( Enchantment ench) {
        return this.handle.containsEnchantment(ench);
    }

    private ItemMeta createItemMeta(){
        handleMeta = handle.getItemMeta();
        return handleMeta;
    }

    @Override
	public boolean equals(Object obj) {
        return this.handle.equals(obj);
    }

    @Override
	public int getAmount() {
        return this.handle.getAmount();
    }

    @Override
	@Nullable
    public MaterialData getData() {
        return this.handle.getData();
    }

    /** @deprecated */
    @Override
	@Deprecated
    public short getDurability() {
        return   this.handle.getDurability();
    }

    @Override
	public int getEnchantmentLevel( Enchantment ench) {
        return this.handle.getEnchantmentLevel(ench);
    }

    @Override
	public Map<Enchantment, Integer> getEnchantments() {
        return this.handle.getEnchantments();
    }



    @Override
	@Nullable
    public ItemMeta getItemMeta() {
        return this.handleMeta==null?createItemMeta():this.handleMeta;
    }

    @Override
	public int getMaxStackSize() {
        return this.handle.getMaxStackSize();
    }

    @Override
	public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    @Override
	public Material getType() {
        return this.handle.getType();
    }

    @Override
	public int hashCode() {
        return this.handle.hashCode();
    }

    @Override
	public boolean hasItemMeta() {
        return handle.hasItemMeta();
    }

    @Override
	public boolean isSimilar(@Nullable ItemStack stack) {
        return this.handle.isSimilar(stack);
    }


    @Override
	public int removeEnchantment( Enchantment ench) {
        return this.handle.removeEnchantment(ench);
    }

    @Override
	public void removeEnchantments() {
        this.handle.removeEnchantments();
    }

    @Override
	public Map<String, Object> serialize() {
        return this.handle.serialize();
    }

    @Override
	public void setAmount(int amount) {
        this.handle.setAmount(amount);
    }

    @Override
	public void setData(@Nullable MaterialData data) {
        this.handle.setData(data);
    }

    /** @deprecated */
    @Override
	@Deprecated
    public void setDurability(short durability) {
       this.handle.setDurability(durability);

    }

    @Override
	public boolean setItemMeta(@Nullable ItemMeta itemMeta) {

        this.handleMeta = itemMeta;
        return true;
    }

    @Override
	public void setType( Material type) {
        this.handle.setType(type);
    }





    @Override
	public String toString() {
        return this.handle.toString();
    }
}
