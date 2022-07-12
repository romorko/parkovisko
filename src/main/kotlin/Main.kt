import java.io.File
import kotlin.random.Random.Default.nextInt

fun generujCasyList(pocet: Int = 10): MutableList<Rozsah>
{
    var casyPrichodOdchod = mutableListOf<Rozsah>()
    for (i in 0 until pocet)
    {
        var cOd = Cas(nextInt(0, 24), nextInt(0, 60))
        var cDo = Cas(nextInt(0, 24), nextInt(0, 60))
        if (cOd > cDo)
        {
            val temp = cOd
            cOd = cDo
            cDo = temp
        }
        casyPrichodOdchod.add(Rozsah(zaciatok = cOd, koniec = cDo))
    }
    return casyPrichodOdchod
}

fun generujCasySubor(pocet: Int, nazov: String = "vstup.txt"): Unit
{
    val zapis = generujCasyList(pocet).joinToString("\n")
    File("zapis.txt").writeText(zapis)
}

fun citajCasySubor(nazov: String = "vstup.txt"): List<Rozsah>
{
    var casyPrichodOdchod = mutableListOf<Rozsah>()
    val regCasy = "([0-1]{1}[0-9]|2[0-3]):([0-5][0-9])\\s+([0-1]{1}[0-9]|2[0-3]):([0-5][0-9])\$"
    File(nazov).forEachLine {
        if (regCasy.toRegex().matches(it))
        {
            val vysledok = regCasy.toRegex().find(it)
            val h1 = (vysledok!!.groups.get(1)!!.value).toInt()
            val m1 = (vysledok!!.groups.get(2)!!.value).toInt()
            val h2 = (vysledok!!.groups.get(3)!!.value).toInt()
            val m2 = (vysledok!!.groups.get(4)!!.value).toInt()
            casyPrichodOdchod.add(Rozsah(Cas(h1, m1), Cas(h2, m2)))
        }
        else
        {
            println("Chybny format zdroja")
        }
    }
    return casyPrichodOdchod
}

fun main(args: Array<String>)
{
    val hodinyDna = IntArray(25) { 0 }
    for (j in 1..24)
    {
        print(String.format("%3s", j))
    }
    println()
    //val ziskaneCasy = generujCasyList(15)
    generujCasySubor(15)
    val ziskaneCasy = citajCasySubor("zapis.txt")
    //ziskaneCasy.forEach { println(it) }
    for (rozsah in ziskaneCasy)
    {
        val zacniH = rozsah.zaciatok.dajHodinu()
        val zacniM = rozsah.zaciatok.dajMinutu()
        val skonciH = rozsah.koniec.dajHodinu()
        val skonciM = rozsah.koniec.dajMinutu()
        val zacniHod = if (zacniM > 0 && zacniH < 24) zacniH + 1 else zacniH
        val skonciHod = if (skonciM > 0 && skonciH < 24) skonciH + 1 else skonciH
        for (j in 1..24)
        {
            if (j in zacniHod..skonciHod)
            {
                hodinyDna[j]++
                print(String.format("%3s", "*"))
            }
            else
            {
                print(String.format("%3s", "-"))
            }
        }
        println(" ${rozsah.zaciatok} ${rozsah.koniec}")
    }
    for (i in 1..24)
    {
        print(String.format("%3s", hodinyDna[i]))
    }
    println("\nNajcastejsie je " + hodinyDna.maxOrNull() ?: 0)
}
