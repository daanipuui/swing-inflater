package io.github.daanipuui.swing.inflater.constraints.conversion;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.constraints.ConstraintsConversion;

import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FlowLayoutConstraints implements ConstraintsConversion<FlowLayout> {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(FlowLayout.class.getName(), FlowLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, FlowLayout layoutManager, Map<String, String> map) {
        return null;
    }
}
