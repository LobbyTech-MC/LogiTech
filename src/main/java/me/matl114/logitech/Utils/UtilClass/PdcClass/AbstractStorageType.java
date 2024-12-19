package me.matl114.logitech.Utils.UtilClass.PdcClass;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.jeff_media.morepersistentdatatypes.DataType;

import me.matl114.logitech.Utils.AddUtils;

public class AbstractStorageType implements PersistentDataType<PersistentDataContainer, ItemStack> {
    public final static AbstractStorageType TYPE = new AbstractStorageType();
    public static final NamespacedKey ITEM= AddUtils.getNameKey("data");
    @Override
    @Nonnull
    public ItemStack fromPrimitive(@Nonnull PersistentDataContainer primitive, @Nonnull PersistentDataAdapterContext context) {
        final ItemStack item = primitive.get(ITEM, DataType.ITEM_STACK);
        return item;
    }
    public Class<ItemStack> getComplexType(){
        return ItemStack.class;
    }
    public Class<PersistentDataContainer> getPrimitiveType(){
        return PersistentDataContainer.class;
    }

    @Override
    @Nonnull
    public PersistentDataContainer toPrimitive(@Nonnull ItemStack complex, @Nonnull PersistentDataAdapterContext context) {
        final PersistentDataContainer container = context.newPersistentDataContainer();
        if(complex.getAmount()==1){
        container.set(ITEM, DataType.ITEM_STACK, complex);
        }
        else{
            complex=complex.clone();
            complex.setAmount(1);
            container.set(ITEM, DataType.ITEM_STACK, complex);

        }
        return container;
    }

}
