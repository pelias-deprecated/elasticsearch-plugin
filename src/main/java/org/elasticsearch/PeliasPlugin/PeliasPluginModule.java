package org.elasticsearch.PeliasPlugin;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.settings.Settings;

public class PeliasPluginModule extends AbstractModule {
    private final Settings settings;

    public PeliasPluginModule(Settings settings){
        this.settings = settings;
    }

    @Override
    protected void configure() {

    }
}
