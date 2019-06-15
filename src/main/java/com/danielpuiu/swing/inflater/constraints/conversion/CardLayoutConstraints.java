package com.danielpuiu.swing.inflater.constraints.conversion;

import com.danielpuiu.swing.inflater.ContextProvider;
import com.danielpuiu.swing.inflater.constraints.ConstraintsConversion;

import java.awt.CardLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CardLayoutConstraints implements ConstraintsConversion {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(CardLayout.class.getName(), CardLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, Map<String, String> map) {
        return map.getOrDefault("cardId", "");
    }
}
