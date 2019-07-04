package io.github.daanipuui.swing.inflater.constraints;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.Conversion;

import java.awt.LayoutManager;
import java.util.List;
import java.util.Map;

public interface ConstraintsConversion<T extends LayoutManager> extends Conversion {

    List<String> getHandledLayouts();

    Object convert(ContextProvider contextProvider, T layoutManager, Map<String, String> map);
}
