package com.bt.openlink.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Profile {
    @Nonnull private final List<String> parseErrors;
    @Nullable private final ProfileId profileId;
    @Nullable private final Boolean isDefault;
    @Nullable private final String device;
    @Nullable private final String label;
    @Nullable private final Boolean online;
    @Nullable private final Site site;

    private Profile(@Nonnull final Builder builder, @Nullable final List<String> parseErrors) {
        this.profileId = builder.profileId;
        this.isDefault = builder.isDefault;
        this.device = builder.device;
        this.label = builder.label;
        this.online = builder.online;
        this.site = builder.site;
        if (parseErrors == null) {
            this.parseErrors = Collections.emptyList();
        } else {
            this.parseErrors = parseErrors;
        }
    }

    @Nonnull
    public List<String> parseErrors() {
        return new ArrayList<>(parseErrors);
    }

    @Nonnull
    public Optional<ProfileId> profileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<Boolean> isDefault() {
        return Optional.ofNullable(isDefault);
    }

    @Nonnull
    public Optional<String> getDevice() {
        return Optional.ofNullable(device);
    }

    @Nonnull
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Nonnull
    public Optional<Boolean> isOnline() {
        return Optional.ofNullable(online);
    }

    @Nonnull
    public Optional<Site> getSite() {
        return Optional.ofNullable(site);
    }

    public static final class Builder {

        @Nullable private ProfileId profileId = null;
        @Nullable private Site site = null;
        @Nullable private Boolean isDefault;
        @Nullable private String device;
        @Nullable private String label;
        @Nullable private Boolean online;

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Profile build() {
            if (profileId == null) {
                throw new IllegalStateException("The profileId has not been set");
            }
            if (site == null) {
                throw new IllegalStateException("The site has not been set");
            }
            if (isDefault == null) {
                throw new IllegalStateException("The default indicator has not been set");
            }
            if (label == null) {
                throw new IllegalStateException("The label has not been set");
            }
            if (online == null) {
                throw new IllegalStateException("The online indicator has not been set");
            }
            return new Profile(this, null);
        }

        @Nonnull
        public Profile build(final List<String> parseErrors) {
            return new Profile(this, parseErrors);
        }

        public Builder setProfileId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder setDefault(final boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public Builder setDevice(@Nonnull final String device) {
            this.device = device;
            return this;
        }

        public Builder setLabel(@Nonnull final String label) {
            this.label = label;
            return this;
        }

        public Builder setOnline(final boolean online) {
            this.online = online;
            return this;
        }

        public Builder setSite(@Nonnull Site site) {
            this.site = site;
            return this;
        }
    }
}
