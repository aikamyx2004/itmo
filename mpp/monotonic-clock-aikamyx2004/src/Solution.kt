/**
 * В теле класса решения разрешено использовать только переменные делегированные в класс RegularInt.
 * Нельзя volatile, нельзя другие типы, нельзя блокировки, нельзя лазить в глобальные переменные.
 *
 * @author : Mukhtarov Ainur
 */
class Solution : MonotonicClock {
    private var c1 by RegularInt(0)
    private var c2 by RegularInt(0)
    private var c3 by RegularInt(0)
    private var nc1 by RegularInt(0)
    private var nc2 by RegularInt(0)
    private var nc3 by RegularInt(0)

    override fun write(time: Time) {
        val r1 = time.d1
        val r2 = time.d2
        val r3 = time.d3
        nc1 = r1
        nc2 = r2
        nc3 = r3
        // write right-to-left
        c3 = nc3
        c2 = nc2
        c1 = nc1
    }

    override fun read(): Time {
        val clr1: Int = c1
        val clr2: Int = c2
        val clr3: Int = c3

        val crl3: Int = nc3
        val crl2: Int = nc2
        val crl1: Int = nc1
        if (clr1 == crl1 && clr2 == crl2 && clr3 == crl3) {
            return Time(clr1, clr2, clr3)
        }
        if (clr1 != crl1) {
            return Time(crl1, 0, 0)
        }
        if (clr2 != crl2) {
            return Time(crl1, crl2, 0)
        }
        return Time(clr1, clr2, clr3)
    }
}