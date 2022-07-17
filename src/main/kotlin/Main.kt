import java.io.File
import java.io.FileNotFoundException
import kotlin.random.Random.Default.nextInt
import kotlin.system.exitProcess

fun generujCasyList(pocet: Int = 10): MutableList<Rozsah>
{
    val casyPrichodOdchod = mutableListOf<Rozsah>()
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

fun generujCasySubor(pocet: Int, nazov: String = "vstup.txt")
{
    val zapis = generujCasyList(pocet).joinToString("\n")
    try
    {
        File(nazov).writeText(zapis)
    }
    catch (e: FileNotFoundException)
    {
        println("Subor sa nepodarilo otvorit!")
    }
}

fun citajCasySubor(nazov: String = "vstup.txt"): List<Rozsah>
{
    val casyPrichodOdchod = mutableListOf<Rozsah>()
    val regCasy = "([0-1]\\d|2[0-3]):([0-5]\\d)\\s+([0-1]\\d|2[0-3]):([0-5]\\d)\$"
    File(nazov).forEachLine {
        if (regCasy.toRegex().matches(it))
        {
            val vysledok = regCasy.toRegex().find(it)
            val h1 = (vysledok!!.groups[1]!!.value).toInt()
            val m1 = (vysledok.groups[2]!!.value).toInt()
            val h2 = (vysledok.groups[3]!!.value).toInt()
            val m2 = (vysledok.groups[4]!!.value).toInt()
            casyPrichodOdchod.add(Rozsah(Cas(h1, m1), Cas(h2, m2)))
        }
        else
        {
            println("Chybny format zdroja!")
        }
    }
    return casyPrichodOdchod
}

fun vytvorZahlavie(): String
{
    var vratZahlavie = ""
    for (j in 1..24)
    {
        print(String.format("%3s", j))
        vratZahlavie += String.format("%3s", j)
    }
    vratZahlavie += "\n"
    return vratZahlavie
}

fun vytvorPaticku(hodinyPocty: IntArray): String
{
    var vratPaticku = ""
    for (i in 1 until hodinyPocty.size)
    {
        print(String.format("%3s", hodinyPocty[i]))
        vratPaticku += String.format("%3s", hodinyPocty[i])
    }
    return vratPaticku
}

fun main()
{
    val hodinyDna = IntArray(25) { 0 }
    var vysledokDoSuboru = ""
    //val ziskaneCasy = generujCasyList(15)
    val ziskaneCasy: List<Rozsah>
    //generujCasySubor(15, "vstup.txt")
    try
    {
        ziskaneCasy = citajCasySubor("vstup.txt")
    }
    catch (e: FileNotFoundException)
    {
        println("Subor na citanie sa nepodarilo otvorit! Koncim!")
        exitProcess(1)
    }
    //vytvori zahlavie
    vysledokDoSuboru = vytvorZahlavie()
    println()
    val utriedene = ziskaneCasy.sortedBy { it.zaciatok }
    for (rozsah in utriedene)
    {
        val zacniH = rozsah.zaciatok.dajHodinu()
        val zacniM = rozsah.zaciatok.dajMinutu()
        val skonciH = rozsah.koniec.dajHodinu()
        val skonciM = rozsah.koniec.dajMinutu()
        val zacniHod = if (zacniM > 0 && zacniH < 24) zacniH + 1 else zacniH
        val skonciHod = if (skonciM > 0 && skonciH < 24) skonciH + 1 else skonciH
        for (j in 1..24)
        {
            vysledokDoSuboru += if (j in zacniHod..skonciHod)
            {
                hodinyDna[j]++
                print(String.format("%3s", "*"))
                String.format("%3s", "*")
            }
            else
            {
                print(String.format("%3s", "-"))
                String.format("%3s", "-")
            }
        }
        println(" ${rozsah.zaciatok} ${rozsah.koniec}")
        vysledokDoSuboru += " ${rozsah.zaciatok} ${rozsah.koniec}\n"
    }
    vysledokDoSuboru += vytvorPaticku(hodinyDna)
    var max = 0
    var maxIndex = 0
    for ((index, hodnota) in hodinyDna.withIndex())
    {
        if (hodnota > max)
        {
            max = hodnota
            maxIndex = index
        }
    }
    println()
    println("\nNajcastejsie sa vyskytuje hodina $maxIndex a to $max krat.")
    vysledokDoSuboru += "\n\nNajcastejsie sa vyskytuje hodina $maxIndex a to $max krat."
    try
    {
        File("vysledok.txt").writeText(vysledokDoSuboru)
    }
    catch (e: FileNotFoundException)
    {
        println("Subor sa nepodarilo vytvorit!")
    }
    val maximum = hodinyDna.maxOf { it }
    print("Maximum bolo $maximum v hodinach: ")
    hodinyDna.mapIndexed { index, i -> if (i == maximum) print("$index ") }
    //println(maxima)
    //hodinyDna.sort()
    //hodinyDna.forEachIndexed{index,element-> println("$index,$element") }
}

