
class Cas(h: Int, m: Int) : Comparable<Cas>
{
    private var hod: Int = 0
        set(value)
        {
            //println("seter hod")
            field = if (value in 0 until 24) value else value % 24
        }

    private var min: Int=0
        set(value)
        {
            //println("seter min")
            if (value in 0..60)
            {
                field = value
            }
            else
            {
                field = value % 60
                hod += value / 60
            }
        }
    fun dajHodinu():Int = hod
    fun dajMinutu():Int= min

    init
    {
        //println("init m h $m $h")
        val m1 = m % 60  //zmestim do 60
        val m2 = m / 60  //pridam k hodinam
        //println("init m1 m2 $m1 $m2")
        val h1 = h + m2 //hodiny zvacsim o ziskane z minut
        val h2 = h1 % 24 //hodiny zmestim do 24
        //println("init h1 h2 $h1 $h2")
        this.hod = h2
        this.min = m1
    }

    override fun compareTo(other: Cas): Int
    {
        return when
        {
            this.hod * 60 + this.min < other.hod * 60 + other.min -> -1
            this.hod * 60 + this.min > other.hod * 60 + other.min -> 1
            else                                                  -> 0
        }
    }

    override fun toString(): String
    {
        val h = if (hod < 10) "0$hod" else "$hod"
        val m = if (min < 10) "0$min" else "$min"
        return "$h:$m"
    }
}

