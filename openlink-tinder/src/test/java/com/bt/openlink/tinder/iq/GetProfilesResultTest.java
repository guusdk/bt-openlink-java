package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetProfilesFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Profile;

@SuppressWarnings({ "ConstantConditions" })
public class GetProfilesResultTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(CoreFixtures.PROFILE)
                .addProfile(GetProfilesFixtures.PROFILE_2)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_NO_NOTES).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final GetProfilesResult result = (GetProfilesResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_NO_NOTES));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(result.getType(), is(IQ.Type.result));
        final List<Profile> profiles = result.getProfiles();

        assertThat(EqualsBuilder.reflectionEquals(CoreFixtures.PROFILE, profiles.get(0), false, null, true), is(true));
        assertThat(EqualsBuilder.reflectionEquals(GetProfilesFixtures.PROFILE_2, profiles.get(1), false, null, true), is(true));

        assertThat(profiles.size(), is(2));

        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() {

        final IQ iq = Fixtures.iqFrom(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_BAD_VALUES);

        final GetProfilesResult result = GetProfilesResult.from(iq);

        assertThat(result.getParseErrors(), contains(
                "Invalid get-profiles result; no profiles present",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute"
                ));
    }

    @Test
    public void willReturnANoProfilesParsingError() {

        final IQ iq = Fixtures.iqFrom(GetProfilesFixtures.GET_PROFILES_RESULT_WITH_NO_PROFILES);

        final GetProfilesResult result = GetProfilesResult.from(iq);

        assertThat(result.getParseErrors(), contains("Invalid get-profiles result; no profiles present"));
    }

    @Test
    public void willBuildAResultFromARequest() {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setId(CoreFixtures.STANZA_ID)
                .setJID(Fixtures.USER_FULL_JID)
                .build();

        final GetProfilesResult result = GetProfilesResult.Builder.start(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }
}