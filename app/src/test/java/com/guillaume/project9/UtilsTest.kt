package com.guillaume.project9


import com.guillaume.project9.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {


    @Test
    fun conversion_id_ok(){
        var euro = 100
        val goodResult = 110
        val dollar: Int = Utils.convertEuroToDollar(euro)

        assertEquals(goodResult , dollar)
    }

    @Test
    fun displayDateInFrench(){
        val dateFormatFrench: DateFormat = SimpleDateFormat("dd/MM/yyy")
        val frenchDate = dateFormatFrench.format(Date())

        val dateFormatEnglish: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val englishDate = dateFormatEnglish.format(Date())

        assertEquals(frenchDate, Utils.getTodayDate())
        assertNotEquals(englishDate, Utils.getTodayDate())
    }
}
