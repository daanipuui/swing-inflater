package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;
import com.danielpuiu.swing.inflater.layout.RelativeLayout;
import com.danielpuiu.swing.inflater.type.TypeConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RelativeLayoutConstraints implements ConstraintsConversion<RelativeLayout> {

    private static final String COMPONENT = "Component";
    private static final String BOOLEAN = "Boolean";
    private static final String DOUBLE = "Double";

    private static final Map<String, String> attributeToTypeNames = init();

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(RelativeLayout.class.getName(), RelativeLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, RelativeLayout layoutManager, Map<String, String> map) {
        Map<String, Object> constraints = new HashMap<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String attribute = entry.getKey();
            String type = attributeToTypeNames.get(attribute);
            if (Objects.isNull(type)) {
                throw new IllegalArgumentException(String.format("Unknown attribute [%s].", attribute));
            }

            constraints.put(attribute, TypeConverter.convert(contextProvider, type, entry.getValue()));
        }

        return constraints;
    }

    private static Map<String, String> init() {
        Map<String, String> map = new HashMap<>();

        map.put("above", COMPONENT);
        map.put("alignBottom", COMPONENT);
        map.put("alignLeft", COMPONENT);
        map.put("alignParentBottom", BOOLEAN);
        map.put("alignParentLeft", BOOLEAN);
        map.put("alignParentRight", BOOLEAN);
        map.put("alignParentTop", BOOLEAN);
        map.put("alignRight", COMPONENT);
        map.put("alignTop", COMPONENT);
        map.put("below", COMPONENT);
        map.put("centerHorizontal", COMPONENT);
        map.put("centerHorizontalInParent", BOOLEAN);
        map.put("centerVertical", COMPONENT);
        map.put("centerVerticalInParent", BOOLEAN);
        map.put("leftOf", COMPONENT);
        map.put("rightOf", COMPONENT);
        map.put("width", DOUBLE);
        map.put("height", DOUBLE);

        return Collections.unmodifiableMap(map);
    }
}