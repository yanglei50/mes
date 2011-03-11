package com.qcadoo.plugin;

import org.w3c.dom.Node;

public interface ModuleFactory<T extends Module> {

    void postInitialize();

    T parse(final Node node);

    String getIdentifier();

}