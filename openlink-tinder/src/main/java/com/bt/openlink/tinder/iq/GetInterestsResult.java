package com.bt.openlink.tinder.iq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.iq.GetInterestsResultBuilder;
import com.bt.openlink.tinder.internal.TinderPacketUtil;
import com.bt.openlink.type.Interest;
import com.bt.openlink.type.InterestId;
import com.bt.openlink.type.InterestType;

public class GetInterestsResult extends OpenlinkIQ2 {
    private static final String DESCRIPTION = "get-interests result";

    @Nonnull private final List<Interest> interests;

    private GetInterestsResult(@Nonnull Builder builder, @Nullable List<String> parseErrors) {
        super(builder, parseErrors);
        this.interests = Collections.unmodifiableList(builder.getInterests());
        final Element outElement = TinderPacketUtil.addCommandIOOutputElement(this, OpenlinkXmppNamespace.OPENLINK_GET_INTERESTS);
        final Element interestsElement = outElement.addElement("interests", OpenlinkXmppNamespace.OPENLINK_INTERESTS.uri());
        for (final Interest interest : interests) {
            final Element interestElement = interestsElement.addElement("interest");
            interest.getId().ifPresent(interestId -> interestElement.addAttribute("id", interestId.value()));
            interest.getType().ifPresent(interestType -> interestElement.addAttribute("type", interestType.value()));
            interest.getLabel().ifPresent(label -> interestElement.addAttribute("label", label));
            interest.isDefaultInterest().ifPresent(isDefault -> interestElement.addAttribute("default", String.valueOf(isDefault)));
        }
    }

    @Nonnull
    public List<Interest> getInterests() {
        return interests;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static GetInterestsResult from(@Nonnull IQ iq) {
        final List<String> parseErrors = new ArrayList<>();
        final Builder builder = Builder.start(iq);
        final Element outElement = TinderPacketUtil.getIOOutElement(iq);
        final Element interestsElement = TinderPacketUtil.getChildElement(outElement, "interests");
        if (interestsElement != null) {
            final List<Element> interestElements = interestsElement.elements("interest");
            for (final Element interestElement : interestElements) {
                final Interest.Builder interestBuilder = Interest.Builder.start();
                final Optional<InterestId> interestId = InterestId.from(TinderPacketUtil.getStringAttribute(interestElement, "id", false, DESCRIPTION, parseErrors).orElse(null));
                interestId.ifPresent(interestBuilder::setId);
                final Optional<InterestType> interestType = InterestType.from(TinderPacketUtil.getStringAttribute(interestElement, "type", false, DESCRIPTION, parseErrors).orElse(null));
                interestType.ifPresent(interestBuilder::setType);
                final Optional<String> label = Optional.ofNullable(TinderPacketUtil.getStringAttribute(interestElement, "label", false, DESCRIPTION, parseErrors).orElse(null));
                label.ifPresent(interestBuilder::setLabel);
                final Optional<Boolean> isDefault = TinderPacketUtil.getBooleanAttribute(interestElement, "default", false, DESCRIPTION, parseErrors);
                isDefault.ifPresent(interestBuilder::setDefault);
                builder.addInterest(interestBuilder.build(parseErrors));
            }
        }

        final GetInterestsResult request = builder.build(parseErrors);
        request.setID(iq.getID());
        return request;
    }

    public static final class Builder extends GetInterestsResultBuilder<Builder, JID, Type> {

        @Nonnull
        public static Builder start() {
            return new Builder();
        }

        @Nonnull
        private static Builder start(@Nonnull final IQ iq) {
            final Builder builder = start();
            TinderIQBuilder.setIQBuilder(builder, iq);
            return builder;
        }

        @Nonnull
        public static Builder start(@Nonnull final GetInterestsRequest request) {
            return start(IQ.createResultIQ(request));
        }

        private Builder() {
            super(IQ.Type.class);
        }

        @Nonnull
        public GetInterestsResult build() {
            validate();
            return new GetInterestsResult(this, null);
        }

        @Nonnull
        private GetInterestsResult build(@Nonnull final List<String> parseErrors) {
            validate(parseErrors);
            return new GetInterestsResult(this, parseErrors);
        }
    }

}
