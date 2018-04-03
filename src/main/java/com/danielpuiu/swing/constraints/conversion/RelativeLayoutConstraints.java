package com.danielpuiu.swing.constraints.conversion;

import com.danielpuiu.swing.constraints.ConstraintsConversion;
import com.danielpuiu.swing.layout.RelativeLayout;
import com.danielpuiu.swing.type.TypeConverter;

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

        map.put("above", "Component");
        map.put("alignBottom", "Component");
        map.put("alignLeft", "Component");
        map.put("alignParentBottom", "Boolean");
        map.put("alignParentLeft", "Boolean");
        map.put("alignParentRight", "Boolean");
        map.put("alignParentTop", "Boolean");
        map.put("alignRight", "Component");
        map.put("alignTop", "Component");
        map.put("below", "Component");
        map.put("centerHorizontal", "Component");
        map.put("centerHorizontalInParent", "Boolean");
        map.put("centerVertical", "Component");
        map.put("centerVerticalInParent", "Boolean");
        map.put("leftOf", "Component");
        map.put("rightOf", "Component");
        map.put("width", "Double");
        map.put("height", "Double");

        return Collections.unmodifiableMap(map);
    }

}
