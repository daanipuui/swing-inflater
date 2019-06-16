package com.danielpuiu.swing.inflater;

import javax.swing.JComponent;

public interface ContextProvider extends PackageProvider{

    void register(String... packageNames);

    void register(String alias, Class<? extends JComponent> cls);

    Class<? extends JComponent> getAliasClass(String alias);
}
