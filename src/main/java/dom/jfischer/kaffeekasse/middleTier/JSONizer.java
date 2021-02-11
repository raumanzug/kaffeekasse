/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import dom.jfischer.kaffeekasse.backend.ar.AccountEntry;
import dom.jfischer.kaffeekasse.backend.ar.AccountPeriod;
import dom.jfischer.kaffeekasse.backend.ar.Participant;
import java.util.List;
import org.json.simple.JsonArray;
import org.json.simple.Jsoner;
import org.json.simple.JsonObject;

/**
 * convert objects of some tyoes to Json objects.
 *
 * we use json-simple.
 *
 * @author jfischer
 * @version $Id: $Id
 * @see <a href="https://cliftonlabs.github.io/json-simple">json-simple</a>
 */
public final class JSONizer {

    /**
     * get a json object representing an <code>AccountEntry</code> object.
     *
     * @param arg a {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry}
     * object.
     * @return json object representing an <code>AccountEntry</code> object.
     */
    public static JSONObject jsonize(final AccountEntry arg) {
        JsonObject retval = new JsonObject();

        retval.put("id", Jsoner.escape(arg.getId()));
        retval.put("timestamp", arg.getTimestamp().toString());
        retval.put("amount", arg.getAmount());
        retval.put("nrCups", arg.getNrCups());
        retval.put("participant", arg.getParticipant().getName());
        retval.put("postingText", Jsoner.escape(arg.getPostingText()));

        return new JSONObject(retval);
    }

    /**
     * get a json object representing an <code>AccountPeriod</code> object.
     *
     * @param arg a {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod}
     * object.
     * @return json object representing an <code>AccountPeriod</code> object.
     */
    public static JSONObject jsonize(final AccountPeriod arg) {
        JsonObject retval = new JsonObject();

        retval.put("id", Jsoner.escape(arg.getId()));
        retval.put("timestamp", arg.getTimestamp().toString());
        retval.put("price", arg.getPrice());

        return new JSONObject(retval);
    }

    /**
     * get a json object representing an <code>Participant</code> object.
     *
     * @param arg a {@link dom.jfischer.kaffeekasse.backend.ar.Participant}
     * object.
     * @return json object representing an <code>Participant</code> object.
     */
    public static JSONObject jsonize(final Participant arg) {
        JsonObject retval = new JsonObject();

        retval.put("id", Jsoner.escape(arg.getId()));
        retval.put("name", Jsoner.escape(arg.getName()));
        retval.put("deposit", arg.getDeposit());
        retval.put("nrCups", arg.getNrCups());

        return new JSONObject(retval);
    }

    /**
     * get a json object representing a list of <code>AccountEntry</code>
     * objects.
     *
     * @param args an array of
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountEntry} objects.
     * @return josn object representing an <code>AccountEntry</code> object
     * list.
     */
    public static JSONObject jsonizeAccountEntryList(
            final List<AccountEntry> args) {
        JsonArray retval = new JsonArray();

        for (AccountEntry arg : args) {
            retval.add(jsonize(arg).getJsonObject());
        }

        return new JSONObject(retval);
    }

    /**
     * get a json object representing a list of <code>AccountPeriod</code>
     * objects.
     *
     * @param args an array of
     * {@link dom.jfischer.kaffeekasse.backend.ar.AccountPeriod} objects.
     * @return json object representing an <code>AccountPeriod</code> object
     * list.
     */
    public static JSONObject jsonizeAccountPeriodList(
            final List<AccountPeriod> args) {
        JsonArray retval = new JsonArray();

        for (AccountPeriod arg : args) {
            retval.add(jsonize(arg).getJsonObject());
        }

        return new JSONObject(retval);
    }

    /**
     * get a josn object representing a list of <code>Participant</code>
     * objects.
     *
     * @param args an array of
     * {@link dom.jfischer.kaffeekasse.backend.ar.Participant} objects.
     * @return json object representing a <code>Participant</code> object list.
     */
    public static JSONObject jsonizeParticipantList(
            final List<Participant> args) {
        JsonArray retval = new JsonArray();

        for (Participant arg : args) {
            retval.add(jsonize(arg).getJsonObject());
        }

        return new JSONObject(retval);
    }

    /**
     * get a json object representing an integer value.
     *
     * @param arg a int.
     * @return json object representing an integer value.
     */
    public static JSONObject jsonize(final int arg) {
        JsonObject retval = new JsonObject();

        retval.put("value", arg);

        return new JSONObject(retval);
    }

    /**
     * prohibited constructor.
     */
    private JSONizer() {
    }

}
