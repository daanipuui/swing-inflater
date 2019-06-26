package com.danielpuiu.swing.inflater;

import javax.swing.JComponent;

public interface ContextProvider extends PackageProvider{

    @SuppressWarnings("unused")
    void register(String... packageNames);

    @SuppressWarnings("unused")
    void register(String alias, Class<? extends JComponent> cls);

    Class<? extends JComponent> getAliasClass(String alias);
}
