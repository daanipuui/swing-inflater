package com.danielpuiu.swing.inflater;

import java.awt.Component;
import java.util.Set;

public interface ContextProvider {

    void register(String... packageNames);

    Set<String> getPackageNames();

    void register(String alias, Class<? extends Component> cls);

    Class<? extends Component> getAliasClass(String alias);
}
