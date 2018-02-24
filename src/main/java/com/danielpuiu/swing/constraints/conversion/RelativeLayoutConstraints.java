package com.danielpuiu.swing.constraints.conversion;

import com.danielpuiu.swing.constraints.ConstraintsConversion;
import com.danielpuiu.swing.layout.RelativeLayout;
import com.danielpuiu.swing.type.TypeConverter;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RelativeLayoutConstraints implements ConstraintsConversion {

    private static final Map<String, String> attributeToTypeNames = init();

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(RelativeLayout.class.getName(), RelativeLayout.class.getSimpleName());
    }

    @Override
    public Object convert(Map<String, String> map) {
        Map<String, Object> constraints = new HashMap<>();

        for (Map.Entry<String, String> entry: map.entrySet()) {
            String attribute = entry.getKey();
            String type = attributeToTypeNames.get(attribute);
            if (Objects.isNull(type)) {
                throw new IllegalArgumentException(String.format("Unknown attribute [%s].", attribute));
            }

            constraints.put(attribute, TypeConverter.convert(type, entry.getValue()));
        }

        return constraints;
    }

    private static Map<String, String> init() {
        Map<String, String> map = new HashMap<>();

        map.put("above", Component.class.getSimpleName());
        map.put("alignBottom", Component.class.getSimpleName());
        map.put("alignLeft", Component.class.getSimpleName());
        map.put("alignParentBottom", Boolean.class.getSimpleName());
        map.put("alignParentLeft", Boolean.class.getSimpleName());
        map.put("alignParentRight", Boolean.class.getSimpleName());
        map.put("alignParentTop", Boolean.class.getSimpleName());
        map.put("alignRight", Component.class.getSimpleName());
        map.put("alignTop", Component.class.getSimpleName());
        map.put("below", Component.class.getSimpleName());
        map.put("centerHorizontal", Component.class.getSimpleName());
        map.put("centerHorizontalInParent", Boolean.class.getSimpleName());
        map.put("centerVertical", Component.class.getSimpleName());
        map.put("centerVerticalInParent", Boolean.class.getSimpleName());
        map.put("leftOf", Component.class.getSimpleName());
        map.put("rightOf", Component.class.getSimpleName());
        map.put("width", Double.class.getSimpleName());
        map.put("height", Double.class.getSimpleName());

        return Collections.unmodifiableMap(map);
    }

}
