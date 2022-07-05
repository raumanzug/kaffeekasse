/*
 * (C) 2021; Joerg Fischer <georgi.rybakov@gmx.net>
 */
package dom.jfischer.kaffeekasse.middleTier;

import backend.fake.DAOImpl;
import dom.jfischer.kaffeekasse.backend.BackendRetcode;
import dom.jfischer.kaffeekasse.backend.DAO;
import frontend.fake.Slave;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * unit test.
 *
 * @author jfischer
 */
public class CommandImplTest {

    /**
     * frontend.
     */
    private Command frontend;

    /**
     * channel for transmitting error information from backend to frontend.
     */
    private Slave slave;

    /**
     * constructor.
     */
    public CommandImplTest() {
    }

    /**
     * not used.
     */
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * not used.
     */
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * constructs items.
     */
    @BeforeEach
    public void setUp() {
        this.slave = new Slave();
        DAO dao = new DAOImpl(this.slave);
        TallyPDF tallyPDF = new TallyPDFImpl(dao, this.slave);
        this.frontend = new CommandImpl(dao, this.slave, tallyPDF);
    }

    /**
     * not really used.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addParticipant method, of class CommandImpl.
     */
    @Test
    public void testAddParticipant() {

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(0, jArray.size());
        }

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
        }

        {
            assertTrue(this.slave.isErrorMode());
            assertTrue(this.slave.getState()
                    == BackendRetcode.PARTICIPANT_ALREADY_EXISTS);
        }

        this.slave.resetState();

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

    }

    /**
     * Test of clear method, of class CommandImpl.
     */
    @Test
    public void testClear() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.setPrice(7);
            this.frontend.coffeeIn(name, 500);
            this.frontend.coffeeOut(name, 3);
            this.frontend.clear();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 479);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listPeriods();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(2, jArray.size());

            {
                JsonObject jObject = (JsonObject) jArray.get(0);
                Integer value = (Integer) jObject.get("price");
                assertTrue(value == 7);
            }
            {
                JsonObject jObject = (JsonObject) jArray.get(1);
                Integer value = (Integer) jObject.get("price");
                assertTrue(value == 7);
            }
        }

    }

    /**
     * Test of coffeeIn method, of class CommandImpl.
     */
    @Test
    public void testCoffeeIn() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int amount = 500;
            this.frontend.coffeeIn(name, amount);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 500);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("amount");
                assertTrue(value == 500);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

    }

    /**
     * Test of coffeeOut method, of class CommandImpl.
     */
    @Test
    public void testCoffeeOut() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int nrCups = 3;
            this.frontend.coffeeOut(name, nrCups);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("amount");
                assertTrue(value == 0);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

    }

    /**
     * Test of deleteEntry method, of class CommandImpl.
     */
    @Test
    public void testDeleteEntry() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int nrCups = 3;
            this.frontend.coffeeOut(name, nrCups);
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            String id;

            {
                JSONObject jsonObject = this.slave.getJSONObject();
                assertTrue(jsonObject.getJsonObject() == null);

                JsonArray jArray = jsonObject.getJsonArray();
                assertEquals(1, jArray.size());

                JsonObject jObject = (JsonObject) jArray.get(0);
                id = (String) jObject.get("id");
            }

            this.frontend.deleteEntry(id);

            {
                assertFalse(this.slave.isErrorMode());
                assertTrue(this.slave.getState() == BackendRetcode.OK);
            }

            this.frontend.listEntries();

            {
                assertFalse(this.slave.isErrorMode());
                assertTrue(this.slave.getState() == BackendRetcode.OK);
            }

            {
                JSONObject jsonObject = this.slave.getJSONObject();
                assertTrue(jsonObject.getJsonObject() == null);

                JsonArray jArray = jsonObject.getJsonArray();
                assertEquals(0, jArray.size());
            }

            this.frontend.deleteEntry(id);
        }

        {
            assertTrue(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.ENTRY_NOT_FOUND);
        }

    }

    /**
     * Test of getBankDeposit method, of class CommandImpl.
     */
    @Test
    public void testGetBankDeposit() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.pay(name, 3000);
            this.frontend.getBankDeposit();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 3000);
        }

    }

    /**
     * Test of getDeposit method, of class CommandImpl.
     */
    @Test
    public void testGetDeposit() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeIn(name, 500);
            this.frontend.getDeposit(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 500);
        }

    }

    /**
     * Test of getNrCups method, of class CommandImpl.
     */
    @Test
    public void testGetNrCups() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeOut(name, 3);
            this.frontend.getNrCups(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 3);
        }

    }

    /**
     * Test of getPrice method, of class CommandImpl.
     */
    @Test
    public void testGetPrice() {

        {
            this.frontend.setPrice(7);
            this.frontend.getPrice();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 7);
        }

    }

    /**
     * Test of inactivateParticipant method, of class CommandImpl.
     */
    @Test
    public void testInactivateParticipant() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.inactivateParticipant(name);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(0, jArray.size());
        }

    }

    /**
     * Test of listEntries method, of class CommandImpl.
     */
    @Test
    public void testListEntries() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeOut(name, 3);
            this.frontend.listEntries();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

    }

    /**
     * Test of listParticipants method, of class CommandImpl.
     */
    @Test
    public void testListParticipants() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            this.frontend.coffeeOut(name, 3);
            this.frontend.listParticipants();
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 3);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

    }

    /**
     * Test of listPeriods method, of class CommandImpl.
     */
    @Test
    public void testListPeriods() {

        this.frontend.listPeriods();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());
        }

    }

    /**
     * Test of pay method, of class CommandImpl.
     */
    @Test
    public void testPay() {

        {
            String name = "Mickey Mouse";
            this.frontend.addParticipant(name);
            int amount = 3000;
            this.frontend.pay(name, amount);
        }

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.listParticipants();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("deposit");
                assertTrue(value == 3000);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("name");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.listEntries();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonObject() == null);

            JsonArray jArray = jsonObject.getJsonArray();
            assertEquals(1, jArray.size());

            JsonObject jObject = (JsonObject) jArray.get(0);
            {
                Integer value = (Integer) jObject.get("amount");
                assertTrue(value == 3000);
            }
            {
                Integer value = (Integer) jObject.get("nrCups");
                assertTrue(value == 0);
            }
            {
                String value = (String) jObject.get("participant");
                assertEquals("Mickey Mouse", value);
            }
        }

        this.frontend.getBankDeposit();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 3000);
        }

    }

    /**
     * Test of setPrice method, of class CommandImpl.
     */
    @Test
    public void testSetPrice() {

        this.frontend.setPrice(7);

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        this.frontend.getPrice();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }
        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 7);
        }

        this.frontend.setPrice(0);

        {
            assertTrue(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.ZERO_PRICE);
        }

        this.slave.resetState();

        this.frontend.getPrice();

        {
            assertFalse(this.slave.isErrorMode());
            assertTrue(this.slave.getState() == BackendRetcode.OK);
        }

        {
            JSONObject jsonObject = this.slave.getJSONObject();
            assertTrue(jsonObject.getJsonArray() == null);

            JsonObject jObject = jsonObject.getJsonObject();
            Integer value = (Integer) jObject.get("value");
            assertTrue(value == 7);
        }

    }

}
