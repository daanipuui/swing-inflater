package com.danielpuiu.swing.inflater;

import javax.swing.JComponent;
import java.awt.Component;
import java.util.Set;

public interface ContextProvider {

    void register(String... packageNames);

    void register(String alias, Class<? extends JComponent> cls);

    Set<String> getPackageNames();

    Class<? extends JComponent> getAliasClass(String alias);
}
