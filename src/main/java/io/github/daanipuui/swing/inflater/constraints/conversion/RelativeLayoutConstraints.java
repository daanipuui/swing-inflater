package io.github.daanipuui.swing.inflater.constraints.conversion;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.constraints.ConstraintsConversion;
import io.github.daanipuui.swing.inflater.layout.RelativeLayout;
import io.github.daanipuui.swing.inflater.type.TypeConverter;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RelativeLayoutConstraints implements ConstraintsConversion<RelativeLayout> {

    private static final Map<String, Class> attributeToTypes = init();

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(RelativeLayout.class.getName(), RelativeLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, RelativeLayout layoutManager, Map<String, String> map) {
        Map<String, Object> constraints = new HashMap<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String attribute = entry.getKey();
            Class type = attributeToTypes.get(attribute);
            if (Objects.isNull(type)) {
                throw new IllegalArgumentException(String.format("Unknown attribute [%s].", attribute));
            }

            constraints.put(attribute, TypeConverter.convert(contextProvider, type, entry.getValue()));
        }

        return constraints;
    }

    private static Map<String, Class> init() {
        Map<String, Class> map = new HashMap<>();

        map.put("above", Component.class);
        map.put("alignBottom", Component.class);
        map.put("alignLeft", Component.class);
        map.put("alignParentBottom", Boolean.class);
        map.put("alignParentLeft", Boolean.class);
        map.put("alignParentRight", Boolean.class);
        map.put("alignParentTop", Boolean.class);
        map.put("alignRight", Component.class);
        map.put("alignTop", Component.class);
        map.put("below", Component.class);
        map.put("centerHorizontal", Component.class);
        map.put("centerHorizontalInParent", Boolean.class);
        map.put("centerVertical", Component.class);
        map.put("centerVerticalInParent", Boolean.class);
        map.put("leftOf", Component.class);
        map.put("rightOf", Component.class);
        map.put("width", Double.class);
        map.put("height", Double.class);

        return Collections.unmodifiableMap(map);
    }
}