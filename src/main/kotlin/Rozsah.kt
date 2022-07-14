class Rozsah(var zaciatok:Cas, var koniec:Cas):Comparable<Rozsah>
{
    override fun toString(): String
    {
        return "$zaciatok $koniec"
    }
    override operator fun compareTo(other:Rozsah):Int
    {
        return this.zaciatok.compareTo(other.zaciatok)
    }
}