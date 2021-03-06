package io.github.daanipuui.swing.inflater.constraints.conversion;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.constraints.ConstraintsConversion;

import java.awt.CardLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CardLayoutConstraints implements ConstraintsConversion<CardLayout> {

    @Override
    public List<String> getHandledLayouts() {
        return Arrays.asList(CardLayout.class.getName(), CardLayout.class.getSimpleName());
    }

    @Override
    public Object convert(ContextProvider contextProvider, CardLayout layoutManager, Map<String, String> map) {
        return map.getOrDefault("cardId", "");
    }
}
