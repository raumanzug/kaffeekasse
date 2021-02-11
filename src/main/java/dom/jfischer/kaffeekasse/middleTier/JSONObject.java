/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import org.json.simple.JsonArray;
import org.json.simple.Jsoner;
import org.json.simple.JsonObject;

/**
 * auxiliary class for handling objects of {@link org.json.simple.JsonArray} and
 * {@link org.json.simple.JsonObject} as objects of the same type.
 *
 * @author jfischer
 * @version $Id: $Id
 */
public class JSONObject {

    /**
     * holds an {@link JsonArray} if object was constructed with an
     * {@link JsonArray} object.  <code>null</code> otherwise.
     */
    private final JsonArray jsonArray;

    /**
     * holds an {@link JsonObject} if object was constructed with an
     * {@link JsonObject} object.  <code>null</code> otherwise.
     */
    private final JsonObject jsonObject;

    /**
     * constructor.
     *
     * constructs with an {@link org.json.simple.JsonArray} object.
     *
     * @param jsonArray a {@link org.json.simple.JsonArray} object.
     */
    public JSONObject(final JsonArray jsonArray) {
        this.jsonArray = jsonArray;
        this.jsonObject = null;
    }

    /**
     * construtor.
     *
     * constructs with an {@link org.json.simple.JsonObject} object.
     *
     * @param jsonObject a {@link org.json.simple.JsonObject} object.
     */
    public JSONObject(final JsonObject jsonObject) {
        this.jsonArray = null;
        this.jsonObject = jsonObject;
    }

    /**
     * get either the {@link org.json.simple.JsonArray} object if the object was
     * constructed with this {@link org.json.simple.JsonArray} or
     * <code>null</code> otherwise.
     *
     * @return {@link org.json.simple.JsonArray} object stored in this object or
     * <code>null</code>.
     */
    public JsonArray getJsonArray() {
        return this.jsonArray;
    }

    /**
     * get either the {@link org.json.simple.JsonObject} object if the object
     * was constructed with this {@link org.json.simple.JsonObject} or
     * <code>null</code> otherwise.
     *
     * @return {@link org.json.simple.JsonObject} object stored in this object
     * or <code>null</code>.
     */
    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    /**
     * calls <code>toJson</code> method.
     *
     * Both, {@link org.json.simple.JsonArray} als auch
     * {@link org.json.simple.JsonObject} contains a method <code>toJson</code>.
     *
     * @return result of calling <code>toJson</code> method.
     */
    public String toJson() {
        String retval;

        if (this.jsonArray != null) {
            retval = this.jsonArray.toJson();
        } else {
            retval = this.jsonObject.toJson();
        }

        return retval;
    }

    /**
     * {@inheritDoc}
     *
     * get string representing this object.
     */
    @Override
    public String toString() {
        return Jsoner.prettyPrint(toJson());
    }

}
