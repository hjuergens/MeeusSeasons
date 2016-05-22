package net.goldenbogen.lib.datetime;

import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static net.goldenbogen.lib.datetime.MeeusSeasons.getNorthernHemisphereSeason;
import static org.testng.Assert.assertEquals;

public class MeeusSeasonsTest {

    private final DateFormat formatter =
            new SimpleDateFormat("EEE, MMM d, ''yy 'at' H:mm z", Locale.ENGLISH);

    /**
     * Equinoxes and Solstices for year 2016
     * @link http://www.geoastro.de/astro/astroJS/seasons/index.htm
     * @throws Exception
     */
    @Test
    public void testGetNorthernHemisphereSeason2016() throws Exception {

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Sun, Mar 20, '16 at 4:29 UTC")), 1);// : Winter

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Sun, Mar 20, '16 at 4:30 UTC")), 2);// : Spring

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Mon, Jun 20, '16 at 22:33 UTC")), 2);// : Spring

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Mon, Jun 20, '16 at 22:34 UTC")), 3);// : Summer

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Thu, Sep 22, '16 at 14:20 UTC")), 3);// : Summer

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Thu, Sep 22, '16 at 14:21 UTC")), 4);// : Autumn

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Wed, Dec 21, '16 at 10:43 UTC")), 4);// : Autumn

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Wed, Dec 21, '16 at 10:44 UTC")), 4);// : Autumn !!!

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Wed, Dec 21, '16 at 10:45 UTC")), 0);// : Winter
    }

    /**
     * Equinoxes and Solstices for year 2017
     * @link http://www.geoastro.de/astro/astroJS/seasons/index.htm
     * @throws Exception
     */
    @Test
    public void testGetNorthernHemisphereSeason2017() throws Exception {

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Mon, Mar 20, '17 at 10:28 UTC")), 1);// : Winter

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Mon, Mar 20, '17 at 10:29 UTC")), 2);// : Spring

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Wed, Jun 21, '17 at 4:23 UTC")), 2);// : Spring

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Wed, Jun 21, '17 at 4:24 UTC")), 3);// : Summer

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Fri, Sep 22, '17 at 20:00 UTC")), 3);// : Summer

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Fri, Sep 22, '17 at 20:01 UTC")), 3);// : Autumn !!!

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Fri, Sep 22, '17 at 20:02 UTC")), 4);// : Autumn

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Thu, Dec 21, '17 at 16:27 UTC")), 4);// : Autumn

        assertEquals(getNorthernHemisphereSeason(
                formatter.parse("Thu, Dec 21, '17 at 16:28 UTC")), 0);// : Winter
    }

}