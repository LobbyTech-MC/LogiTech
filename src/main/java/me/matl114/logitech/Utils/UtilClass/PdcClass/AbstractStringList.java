package me.matl114.logitech.Utils.UtilClass.PdcClass;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.matl114.logitech.Utils.AddUtils;

public class AbstractStringList implements PersistentDataType<PersistentDataContainer, List<String>>{
        public static final PersistentDataType<PersistentDataContainer, List<String>> TYPE = new AbstractStringList();

        private final Class clazz=(new ArrayList<String>()).getClass();

        public AbstractStringList() {
        }
        @Override
		@Nonnull
        public List<String> fromPrimitive(@Nonnull PersistentDataContainer primitive, @Nonnull PersistentDataAdapterContext context) {
            List<String> strings = new ArrayList();
            for (NamespacedKey key : primitive.getKeys()) {
                strings.add(primitive.get(key, STRING));
            }

            return strings;
        }
        @Override
		@Nonnull
        public Class<List<String>> getComplexType() {

            return clazz;
        }

        @Override
		@Nonnull
        public Class<PersistentDataContainer> getPrimitiveType() {
            return PersistentDataContainer.class;
        }

        @Override
		@Nonnull
        public PersistentDataContainer toPrimitive(@Nonnull List<String> complex, @Nonnull PersistentDataAdapterContext context) {
            PersistentDataContainer container = context.newPersistentDataContainer();

            for(int i = 0; i < complex.size(); ++i) {
                NamespacedKey key = AddUtils.getNameKey(String.valueOf(i)) ;
                container.set(key, STRING, complex.get(i));
            }

            return container;
        }

}
