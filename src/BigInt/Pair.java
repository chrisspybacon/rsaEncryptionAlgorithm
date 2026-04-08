package BigInt;

public class Pair
{
    private BigInt first;
    private BigInt second;
    public Pair(BigInt first, BigInt second)
    {
        this.first = new BigInt(first);
        this.second = new BigInt(second);
    }

    public BigInt first()
    {
        return this.first;
    }

    public BigInt second()
    {
        return this.second;
    }
}