package io.jenkins.plugins.credentials.secretsmanager.config;

import net.sf.json.JSONObject;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;

@Extension
@Symbol("awsCredentialsProvider")
public class PluginConfiguration extends GlobalConfiguration {

    private static final int DEFAULT_CACHE_DURATION = 5;

    /**
     * The credential cache duration, in minutes.
     *
     * A value of 0 or less disables the cache.
     */
    private Integer cacheDuration;

    /**
     * The AWS Secrets Manager endpoint configuration. If this is null, the default will be used. If
     * this is specified, the user's override will be used.
     */
    private EndpointConfiguration endpointConfiguration;

    private Filters filters;

    public PluginConfiguration() {
        load();

        if (cacheDuration == null) {
            cacheDuration = DEFAULT_CACHE_DURATION;
        }
    }

    public static PluginConfiguration getInstance() {
        return all().get(PluginConfiguration.class);
    }

    public int getCacheDuration() {
        return cacheDuration;
    }

    @DataBoundSetter
    @SuppressWarnings("unused")
    public void setCacheDuration(int cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    public EndpointConfiguration getEndpointConfiguration() {
        return endpointConfiguration;
    }

    @DataBoundSetter
    @SuppressWarnings("unused")
    public void setEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
        this.endpointConfiguration = endpointConfiguration;
        save();
    }

    public Filters getFilters() {
        return filters;
    }

    @DataBoundSetter
    @SuppressWarnings("unused")
    public void setFilters(Filters filters) {
        this.filters = filters;
        save();
    }

    @Override
    public synchronized boolean configure(StaplerRequest req, JSONObject json) {
        // This method is unnecessary, except to apply the following workaround.
        // Workaround: Set any optional struct fields to null before binding configuration.
        // https://groups.google.com/forum/#!msg/jenkinsci-dev/MuRJ-yPRRoo/AvoPZAgbAAAJ
        this.endpointConfiguration = null;
        this.filters = null;

        req.bindJSON(this, json);
        save();
        return true;
    }
}
