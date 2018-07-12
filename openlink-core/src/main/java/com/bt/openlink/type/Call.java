package com.bt.openlink.type;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Call implements Serializable {
    private static final long serialVersionUID = -2696465843616780273L;
    @Nullable private final CallId callId;
    @Nullable private final TelephonyCallId telephonyCallId;
    @Nullable private final ConferenceId conferenceId;
    @Nullable private final Site site;
    @Nullable private final ProfileId profileId;
    @Nullable private String deviceId;
    @Nullable private final UserId userId;
    @Nullable private final InterestId interestId;
    @Nullable private final Changed changed;
    @Nullable private final CallState state;
    @Nullable private final CallDirection direction;
    @Nullable private PhoneNumber callerNumber;
    @Nullable private String callerName;
    @Nonnull private final List<PhoneNumber> callerE164Numbers;
    @Nullable private PhoneNumber calledNumber;
    @Nullable private String calledName;
    @Nullable private PhoneNumber calledDestination;
    @Nonnull private final List<PhoneNumber> calledE164Numbers;
    @Nonnull private List<OriginatorReference> originatorReferences;
    @SuppressWarnings("squid:S3437") @Nullable private final Instant startTime;
    @SuppressWarnings("squid:S3437") @Nullable private final Duration duration;
    @Nonnull private final List<RequestAction> actions;
    @Nonnull private final List<CallFeature> features;
    @Nonnull private final List<Participant> participants;

    private Call(@Nonnull final Builder builder) {
        this.callId = builder.callId;
        this.telephonyCallId = builder.telephonyCallId;
        this.conferenceId = builder.conferenceId;
        this.site = builder.site;
        this.profileId = builder.profileId;
        this.deviceId = builder.deviceId;
        this.userId = builder.userId;
        this.interestId = builder.interestId;
        this.changed = builder.changed;
        this.state = builder.state;
        this.direction = builder.direction;
        this.callerNumber = builder.callerNumber;
        this.callerName = builder.callerName;
        this.callerE164Numbers = Collections.unmodifiableList(builder.callerE164Numbers);
        this.calledNumber = builder.calledNumber;
        this.calledName = builder.calledName;
        this.calledDestination = builder.calledDestination;
        this.calledE164Numbers = Collections.unmodifiableList(builder.calledE164Numbers);
        this.originatorReferences = Collections.unmodifiableList(builder.originatorReferences);
        this.startTime = builder.startTime;
        this.duration = builder.duration;
        this.actions = Collections.unmodifiableList(builder.actions);
        this.features = Collections.unmodifiableList(builder.features);
        this.participants = Collections.unmodifiableList(builder.participants);
    }

    @Nonnull
    public Optional<CallId> getId() {
        return Optional.ofNullable(callId);
    }

    @Nonnull
    public Optional<TelephonyCallId> getTelephonyCallId() {
        return Optional.ofNullable(telephonyCallId);
    }

    @Nonnull
    public Optional<ConferenceId> getConferenceId() {
        return Optional.ofNullable(conferenceId);
    }

    @Nonnull
    public Optional<Site> getSite() {
        return Optional.ofNullable(site);
    }

    @Nonnull
    public Optional<ProfileId> getProfileId() {
        return Optional.ofNullable(profileId);
    }

    @Nonnull
    public Optional<String> getDeviceId() {
        return Optional.ofNullable(deviceId);
    }

    @Nonnull
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(userId);
    }

    @Nonnull
    public Optional<InterestId> getInterestId() {
        return Optional.ofNullable(interestId);
    }

    @Nonnull
    public Optional<Changed> getChanged() {
        return Optional.ofNullable(changed);
    }

    @Nonnull
    public Optional<CallState> getState() {
        return Optional.ofNullable(state);
    }

    @Nonnull
    public Optional<CallDirection> getDirection() {
        return Optional.ofNullable(direction);
    }

    @Nonnull
    public Optional<PhoneNumber> getCallerNumber() {
        return Optional.ofNullable(callerNumber);
    }

    @Nonnull
    public Optional<String> getCallerName() {
        return Optional.ofNullable(callerName);
    }

    @Nonnull
    public List<PhoneNumber> getCallerE164Numbers() {
        return callerE164Numbers;
    }

    @Nonnull
    public Optional<PhoneNumber> getCalledNumber() {
        return Optional.ofNullable(calledNumber);
    }

    @Nonnull
    public Optional<String> getCalledName() {
        return Optional.ofNullable(calledName);
    }

    @Nonnull
    public Optional<PhoneNumber> getCalledDestination() {
        return Optional.ofNullable(calledDestination);
    }

    @Nonnull
    public List<PhoneNumber> getCalledE164Numbers() {
        return calledE164Numbers;
    }

    @Nonnull
    public List<OriginatorReference> getOriginatorReferences() {
        return originatorReferences;
    }

    @Nonnull
    public Optional<Instant> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    @Nonnull
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    @Nonnull
    public List<RequestAction> getActions() {
        return actions;
    }

    @Nonnull
    public List<CallFeature> getFeatures() {
        return features;
    }

    @Nonnull
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Determines the id, if any, of the active handset. Note, if two or more handsets are active, one of them is
     * selected in a nondeterministic manner.
     * 
     * @return the id of an active handset
     */
    @Nonnull
    public Optional<FeatureId> getActiveHandset() {
        return getFeatures().stream()
                .filter(CallFeatureBoolean.class::isInstance)
                .map(CallFeatureBoolean.class::cast)
                .filter(feature -> {
                    final Optional<FeatureType> type = feature.getType();
                    return type.isPresent() && type.get() == FeatureType.HANDSET && feature.isEnabled().orElse(false);
                })
                .findAny()
                .flatMap(Feature::getId);
    }

    /**
     * Determines the id, if any, of the active headset. Note, if two or more headsets are active, one of them is
     * selected in a nondeterministic manner.
     *
     * @return the id of an active handset
     */
    @Nonnull
    public Optional<FeatureId> getActiveHeadset() {
        return getFeatures().stream()
                .filter(CallFeatureBoolean.class::isInstance)
                .map(CallFeatureBoolean.class::cast)
                .filter(feature -> {
                    final Optional<FeatureType> type = feature.getType();
                    return type.isPresent() && type.get() == FeatureType.HEADSET && feature.isEnabled().orElse(false);
                })
                .findAny()
                .flatMap(Feature::getId);
    }

    /**
     * Determines the channel, if any, of the active speaker. Note, if two or more speakers are active, one of them is
     * selected in a nondeterministic manner.
     * 
     * @return the id of an active speaker
     */
    @Nonnull
    public Optional<Long> getActiveSpeakerChannel() {
        return getFeatures().stream()
                .filter(CallFeatureSpeakerChannel.class::isInstance)
                .map(CallFeatureSpeakerChannel.class::cast)
                .findAny()
                .flatMap(CallFeatureSpeakerChannel::getChannel);
    }

    /**
     * Indicates if the call is public or private.
     *
     * @return {@code empty} if there is no privacy indication, otherwise {@code true} or {@code false}.
     */
    @Nonnull
    public Optional<Boolean> isPrivate() {
        return getFeatures().stream()
                .filter(CallFeatureBoolean.class::isInstance)
                .map(CallFeatureBoolean.class::cast)
                .filter(feature -> {
                    final Optional<FeatureType> type = feature.getType();
                    return type.isPresent() && type.get() == FeatureType.PRIVACY && feature.isEnabled().isPresent();
                })
                .findAny()
                .flatMap(CallFeatureBoolean::isEnabled);
    }

    @Nonnull
    public CallStatus toCallStatus() {
        return CallStatus.Builder.start().addCall(this).build();
    }

    @Nonnull
    public CallStatus toCallStatus(final boolean callStatusBusy) {
        return CallStatus.Builder.start()
                .setCallStatusBusy(callStatusBusy)
                .addCall(this).build();
    }

    /**
     * Indicates if the call is public or private.
     *
     * @return {@code empty} if there is no privacy indication, otherwise {@code true} or {@code false}.
     */
    @Nonnull
    public Optional<Boolean> isPublic() {
        return isPrivate().map(isPrivate -> !isPrivate);
    }

    /**
     * Indicates if the call is currently being participated in by the user.
     * 
     * @return {@code empty} if there is insufficient information to determine the participition, otherwise {@code true}
     *         or {@code false}.
     */
    public Optional<Boolean> isParticipating() {
        if (state == null || direction == null) {
            return Optional.empty();
        } else {
            return Optional.of(state.isParticipating(direction));
        }
    }

    public static final class Builder {
        @Nullable private CallId callId;
        @Nullable private TelephonyCallId telephonyCallId;
        @Nullable private ConferenceId conferenceId;
        @Nullable private Site site;
        @Nullable private ProfileId profileId;
        @Nullable private String deviceId;
        @Nullable private UserId userId;
        @Nullable private InterestId interestId;
        @Nullable private Changed changed;
        @Nullable private CallState state;
        @Nullable private CallDirection direction;
        @Nullable private PhoneNumber callerNumber;
        @Nullable private String callerName;
        @Nonnull private final List<PhoneNumber> callerE164Numbers = new ArrayList<>();
        @Nullable private PhoneNumber calledNumber;
        @Nullable private String calledName;
        @Nullable private PhoneNumber calledDestination;
        @Nonnull private final List<PhoneNumber> calledE164Numbers = new ArrayList<>();
        @Nonnull private final List<OriginatorReference> originatorReferences = new ArrayList<>();
        @Nullable private Instant startTime;
        @Nullable private Duration duration;
        @Nonnull private final List<RequestAction> actions = new ArrayList<>();
        @Nonnull private final List<CallFeature> features = new ArrayList<>();
        @Nonnull private final List<Participant> participants = new ArrayList<>();

        private Builder() {
        }

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        public Call build() {
            if (callId == null) {
                throw new IllegalStateException("The call id has not been set");
            }
            if (site == null) {
                throw new IllegalStateException("The call site has not been set");
            }
            if (state == null) {
                throw new IllegalStateException("The call state has not been set");
            }
            if (direction == null) {
                throw new IllegalStateException("The call direction has not been set");
            }
            if (startTime == null) {
                throw new IllegalStateException("The call start time has not been set");
            }
            if (duration == null) {
                throw new IllegalStateException("The call duration has not been set");
            }

            return new Call(this);
        }

        @Nonnull
        public Call build(@Nonnull final List<String> errors) {
            if (callId == null) {
                errors.add("Invalid call status; missing call id is mandatory");
            }
            if (site == null) {
                errors.add("Invalid call status; missing call site is mandatory");
            }
            if (state == null) {
                errors.add("Invalid call status; missing call state is mandatory");
            }
            if (direction == null) {
                errors.add("Invalid call status; missing call direction is mandatory");
            }
            if (startTime == null) {
                errors.add("Invalid call status; missing call start time is mandatory");
            }
            if (duration == null) {
                errors.add("Invalid call status; missing call duration is mandatory");
            }
            return new Call(this);
        }

        @Nonnull
        public Builder setId(@Nonnull final CallId callId) {
            this.callId = callId;
            return this;
        }

        @Nonnull
        public Builder setTelephonyCallId(@Nonnull final TelephonyCallId telephonyCallId) {
            this.telephonyCallId = telephonyCallId;
            return this;
        }

        @Nonnull
        public Builder setConferenceId(@Nonnull final ConferenceId conferenceId) {
            this.conferenceId = conferenceId;
            return this;
        }

        @Nonnull
        public Builder setDeviceId(@Nonnull final String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        @Nonnull
        public Builder setSite(@Nonnull final Site site) {
            this.site = site;
            return this;
        }

        @Nonnull
        public Builder setProfileId(@Nonnull final ProfileId profileId) {
            this.profileId = profileId;
            return this;
        }

        @Nonnull
        public Builder setUserId(@Nonnull final UserId userId) {
            this.userId = userId;
            return this;
        }

        @Nonnull
        public Builder setInterestId(@Nonnull final InterestId interestId) {
            this.interestId = interestId;
            return this;
        }

        @Nonnull
        public Builder setState(@Nonnull final CallState state) {
            this.state = state;
            return this;
        }

        @Nonnull
        public Builder setChanged(@Nonnull final Changed changed) {
            this.changed = changed;
            return this;
        }

        @Nonnull
        public Builder setDirection(@Nonnull final CallDirection direction) {
            this.direction = direction;
            return this;
        }

        @Nonnull
        public Builder setStartTime(@Nonnull final Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        @Nonnull
        public Builder setCallerNumber(@Nonnull final PhoneNumber callerNumber) {
            this.callerNumber = callerNumber;
            return this;
        }

        @Nonnull
        public Builder setCallerName(@Nonnull final String callerName) {
            this.callerName = callerName;
            return this;
        }

        @Nonnull
        public Builder addCallerE164Number(@Nonnull final PhoneNumber callerE164Number) {
            this.callerE164Numbers.add(callerE164Number);
            return this;
        }

        @Nonnull
        public Builder addCallerE164Numbers(@Nonnull final List<PhoneNumber> callerE164Numbers) {
            this.callerE164Numbers.addAll(callerE164Numbers);
            return this;
        }

        @Nonnull
        public Builder setCalledNumber(@Nonnull final PhoneNumber calledNumber) {
            this.calledNumber = calledNumber;
            return this;
        }

        @Nonnull
        public Builder setCalledDestination(@Nonnull final PhoneNumber calledDestination) {
            this.calledDestination = calledDestination;
            return this;
        }

        @Nonnull
        public Builder setCalledName(@Nonnull final String calledName) {
            this.calledName = calledName;
            return this;
        }

        @Nonnull
        public Builder addCalledE164Number(@Nonnull final PhoneNumber calledE164Number) {
            this.calledE164Numbers.add(calledE164Number);
            return this;
        }

        @Nonnull
        public Builder addCalledE164Numbers(@Nonnull final List<PhoneNumber> calledE164Numbers) {
            this.calledE164Numbers.addAll(calledE164Numbers);
            return this;
        }

        @Nonnull
        public Builder addOriginatorReference(@Nonnull final String key, @Nonnull final String value) {
            this.originatorReferences.add(new OriginatorReference(key, value));
            return this;
        }

        @Nonnull
        public Builder addOriginatorReference(@Nonnull final OriginatorReference originatorReference) {
            this.originatorReferences.add(originatorReference);
            return this;
        }

        @Nonnull
        public Builder setDuration(@Nonnull final Duration duration) {
            this.duration = duration;
            return this;
        }

        @Nonnull
        public Builder addAction(@Nonnull final RequestAction action) {
            actions.add(action);
            return this;
        }

        @Nonnull
        public Builder addFeature(@Nonnull final CallFeature feature) {
            features.add(feature);
            return this;
        }

        @Nonnull
        public Builder addParticipant(@Nonnull final Participant participant) {
            participants.add(participant);
            return this;
        }

    }
}
