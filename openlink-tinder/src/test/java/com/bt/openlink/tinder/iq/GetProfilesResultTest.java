package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Profile;
import com.bt.openlink.type.ProfileId;
import com.bt.openlink.type.RequestAction;
import com.bt.openlink.type.Site;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class GetProfilesResultTest {

    private static final Site SITE_1 = Site.Builder.start()
            .setId(1)
            .setDefault(false)
            .setType(Site.Type.IPT)
            .setName("test-site-name")
            .build();
    private static final Profile PROFILE_1 = Profile.Builder.start()
            .setId(Fixtures.PROFILE_ID)
            .setDefault(true)
            .setDevice("uta")
            .setLabel("7001")
            .setOnline(true)
            .setSite(SITE_1)
            .addAction(RequestAction.ANSWER_CALL)
            .addAction(RequestAction.CLEAR_CALL)
            .build();
    private static final Site SITE_2 = Site.Builder.start()
            .setId(11)
            .setDefault(true)
            .setType(Site.Type.ITS)
            .setName("another-test-site-name")
            .build();
    public static final ProfileId PROFILE_ID_2 = ProfileId.from("test-profile-id-2").get();
    private static final Profile PROFILE_2 = Profile.Builder.start()
            .setId(PROFILE_ID_2)
            .setDefault(true)
            .setDevice("uta")
            .setLabel("7001")
            .setOnline(true)
            .setSite(SITE_2)
            .addAction(RequestAction.ANSWER_CALL)
            .addAction(RequestAction.CLEAR_CALL)
            .build();

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final String GET_PROFILES_RESULT_WITH_NO_NOTES = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <profiles xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/profiles\">\n" +
            "          <profile default=\"true\" device=\"uta\" id=\"" + Fixtures.PROFILE_ID + "\" label=\"7001\" online=\"true\">\n" +
            "            <site default=\"false\" id=\"1\" type=\"IPT\">test-site-name</site>\n" +
            "            <actions>\n" +
            "              <action id=\"AnswerCall\" label=\"Answer a ringing call\"/>\n" +
            "              <action id=\"ClearCall\" label=\"Remove all participants from a call\"/>\n" +
            "            </actions>\n" +
            "          </profile>\n" +
            "          <profile default=\"true\" device=\"uta\" id=\"" + PROFILE_ID_2 + "\" label=\"7001\" online=\"true\">\n" +
            "            <site default=\"true\" id=\"11\" type=\"ITS\">another-test-site-name</site>\n" +
            "            <actions>\n" +
            "              <action id=\"AnswerCall\" label=\"Answer a ringing call\"/>\n" +
            "              <action id=\"ClearCall\" label=\"Remove all participants from a call\"/>\n" +
            "            </actions>\n" +
            "          </profile>\n" +
            "        </profiles>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_PROFILES_RESULT_WITH_BAD_VALUES = "<iq type=\"set\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" action=\"execute\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_PROFILES_RESULT_WITH_NO_PROFILES = "<iq type=\"result\" id=\"" + Fixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-profiles\" status=\"completed\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"output\">\n" +
            "      <out>\n" +
            "        <profiles xmlns=\"http://xmpp.org/protocol/openlink:01:00:00/profiles\">\n" +
            "        </profiles>\n" +
            "      </out>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .build();

        assertThat(result.getType(), is(IQ.Type.result));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        GetProfilesResult.Builder.start()
                .build();
    }

    @Test
    public void willConvertToXML() throws Exception {

        final GetProfilesResult result = GetProfilesResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(PROFILE_1)
                .addProfile(PROFILE_2)
                .build();

        assertThat(result.toXML(), isIdenticalTo(GET_PROFILES_RESULT_WITH_NO_NOTES).ignoreWhitespace());
    }

    @Test
    public void willNotBuildAPacketWithDuplicateProfileIds() throws Exception {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("The profile id must be unique");
        GetProfilesResult.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addProfile(PROFILE_1)
                .addProfile(PROFILE_1)
                .build();
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetProfilesResult result = (GetProfilesResult) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_PROFILES_RESULT_WITH_NO_NOTES));
        assertThat(result.getTo(), is(Fixtures.TO_JID));
        assertThat(result.getFrom(), is(Fixtures.FROM_JID));
        assertThat(result.getID(), is(Fixtures.STANZA_ID));
        assertThat(result.getType(), is(IQ.Type.result));
        final List<Profile> profiles = result.getProfiles();
        int i = 0;

        Profile profile = profiles.get(i++);
        Site site = profile.getSite().get();
        assertThat(profile.getId().get(), is(Fixtures.PROFILE_ID));
        assertThat(profile.isDefault().get(), is(true));
        assertThat(profile.getDevice().get(), is("uta"));
        assertThat(profile.getLabel().get(), is("7001"));
        assertThat(profile.isOnline().get(), is(true));
        assertThat(profile.getActions(), contains(RequestAction.ANSWER_CALL, RequestAction.CLEAR_CALL));
        assertThat(site.getId().get(), is(1L));
        assertThat(site.getType().get(), is(Site.Type.IPT));
        assertThat(site.isDefault().get(), is(false));
        assertThat(site.getName().get(), is("test-site-name"));

        profile = profiles.get(i++);
        site = profile.getSite().get();
        assertThat(profile.getId().get(), is(PROFILE_ID_2));
        assertThat(profile.isDefault().get(), is(true));
        assertThat(profile.getDevice().get(), is("uta"));
        assertThat(profile.getLabel().get(), is("7001"));
        assertThat(profile.isOnline().get(), is(true));
        assertThat(profile.getActions(), contains(RequestAction.ANSWER_CALL, RequestAction.CLEAR_CALL));
        assertThat(site.getId().get(), is(11L));
        assertThat(site.getType().get(), is(Site.Type.ITS));
        assertThat(site.isDefault().get(), is(true));
        assertThat(site.getName().get(), is("another-test-site-name"));

        assertThat(profiles.size(), is(i));

        assertThat(result.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final IQ iq = Fixtures.iqFrom(GET_PROFILES_RESULT_WITH_BAD_VALUES);

        final GetProfilesResult result = GetProfilesResult.from(iq);

        assertThat(result.getParseErrors(), contains(
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid get-profiles result; missing 'profiles' element is mandatory"));
    }

    @Test
    public void willReturnANoProfilesParsingError() throws Exception {

        final IQ iq = Fixtures.iqFrom(GET_PROFILES_RESULT_WITH_NO_PROFILES);

        final GetProfilesResult result = GetProfilesResult.from(iq);

        assertThat(result.getParseErrors(), contains(
                "Invalid get-profiles result; no 'profile' elements present"));
    }

    @Test
    public void willBuildAResultFromARequest() throws Exception {

        final GetProfilesRequest request = GetProfilesRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setID(Fixtures.STANZA_ID)
                .setJID(Fixtures.USER_JID)
                .build();

        final GetProfilesResult result = GetProfilesResult.Builder.start(request)
                .build();

        assertThat(result.getID(), is(request.getID()));
        assertThat(result.getTo(), is(request.getFrom()));
        assertThat(result.getFrom(), is(request.getTo()));
    }
}