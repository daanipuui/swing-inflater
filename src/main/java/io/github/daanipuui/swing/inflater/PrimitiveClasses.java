package io.github.daanipuui.swing.inflater;

import java.util.HashMap;
import java.util.Map;

class PrimitiveClasses {

    private static Map<String, Class> primitiveClassByName = init();

    static boolean isPrimitiveClass(String className) {
        return primitiveClassByName.containsKey(className);
    }

    static <T> Class<T> getClass(String className) {
        return primitiveClassByName.get(className);
    }

    private static Map<String, Class> init() {
        Map<String, Class> map = new HashMap<>();
        map.put("int", int.class );
        map.put("long", long.class );
        map.put("double", double.class );
        map.put("float", float.class );
        map.put("bool", boolean.class );
        map.put("char", char.class );
        map.put("byte", byte.class );
        map.put("short", short.class );

        return map;
    }
}
