package BigInt;

import java.util.ArrayList;

public class BigInt
{
    private ArrayList<Integer> number;
    private int decimalPlaces;
    private boolean positive;
    private static final BigInt One = new BigInt("1");
    private static final BigInt Zero = new BigInt("0");

    public BigInt(String value)
    {
        this.number = new ArrayList<Integer>();
        try {
            for (int i = 0; i < value.length(); i++) {
                if (i == 0 && value.charAt(0) == '-') {
                    this.positive = false;
                    continue;
                } else if (i == 0) {
                    this.positive = true;
                }
                char currentChar = value.charAt(i);
                if (!(currentChar - '0' <= 9 && currentChar - '0' >= 0))
                {
                    throw new IllegalArgumentException("Error creating BigInt from String: String contains illegal characters!");
                }
                this.number.add(currentChar - '0');
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString());
        }
        this.decimalPlaces = number.size();
    }

    public BigInt(int decimalPlaces)
    {
        this.number = new ArrayList<Integer>();
        this.decimalPlaces = decimalPlaces;
        this.positive = true;
    }

    public BigInt()
    {
        this.number = new ArrayList<Integer>();
        this.decimalPlaces = 0;
        this.positive = true;
    }

    public BigInt(BigInt num)
    { // copy-Konstruktor
        this.decimalPlaces = num.getDecimalPlaces();
        this.number = new ArrayList<>(num.getValue());
        this.positive = num.positive;
    }

    public BigInt add(BigInt b)
    {
        if (this.positive && !b.positive)
        { // Rückführung auf Subtraktion
            BigInt absValB = new BigInt(b);
            absValB.setPositive(true);
            return this.subtract(absValB);
        } else if (!this.positive && b.positive)
        { // Rückführung auf Subtraktion
            BigInt absValA = new BigInt(this);
            absValA.setPositive(true);
            return b.subtract(absValA);
        } else if (!this.positive && !b.positive)
        { // Umformung (-a) + (-b) = -(a+b)
            ArrayList<Integer> number = new ArrayList<>();
            BigInt absValA = new BigInt(this);
            BigInt absValB = new BigInt(b);
            absValA.setPositive(true);
            absValB.setPositive(true);
            int aSize = absValA.getDecimalPlaces();
            int bSize = absValB.getDecimalPlaces();
            int maxSize = Math.max(aSize, bSize);
            int remainder = 0;
            for (int i=0;i<maxSize;i++)
            {
                if (aSize-i-1<0)
                {
                    if (absValB.getValueAtPos(bSize - i -1) + remainder >= 10)
                    {
                        number.addFirst((absValB.getValueAtPos(bSize - i -1) + remainder)%10);
                        remainder = 1;
                    } else {
                        number.addFirst(absValB.getValueAtPos(bSize - i -1) + remainder);
                        remainder = 0;
                    }
                }
                else if (bSize-i-1<0)
                {
                    if (absValA.getValueAtPos(aSize - i -1) + remainder >= 10)
                    {
                        number.addFirst((absValA.getValueAtPos(aSize - i -1) + remainder)%10);
                        remainder = 1;
                    } else {
                        number.addFirst(absValA.getValueAtPos(aSize - i -1) + remainder);
                        remainder = 0;
                    }
                }
                else
                {
                    if (absValA.getValueAtPos(aSize - i - 1) + absValB.getValueAtPos(bSize - i -1) + remainder >= 10)
                    {
                        number.addFirst((absValA.getValueAtPos(aSize - i - 1) + absValB.getValueAtPos(bSize - i -1) + remainder)%10);
                        remainder = 1;
                    } else {
                        number.addFirst(absValA.getValueAtPos(aSize - i - 1) + absValB.getValueAtPos(bSize - i -1) + remainder);
                        remainder = 0;
                    }
                }
            }
            if (remainder == 1)
            {
                number.addFirst(1);
            }
            BigInt result = new BigInt(number.size());
            result.setValue(number);
            result.setPositive(false); // Ergebnis muss negativ sein, da Summanden negativ sind
            return result;
        }
        ArrayList<Integer> number = new ArrayList<>();
        int aSize = this.getDecimalPlaces();
        int bSize = b.getDecimalPlaces();
        int maxSize = Math.max(aSize, bSize);
        int remainder = 0;
        for (int i=0;i<maxSize;i++)
        {
            if (aSize-i-1<0)
            {
                if (b.getValueAtPos(bSize - i -1) + remainder >= 10)
                {
                    number.addFirst((b.getValueAtPos(bSize - i -1) + remainder)%10);
                    remainder = 1;
                } else {
                    number.addFirst(b.getValueAtPos(bSize - i -1) + remainder);
                    remainder = 0;
                }
            }
            else if (bSize-i-1<0)
            {
                if (this.getValueAtPos(aSize - i -1) + remainder >= 10)
                {
                    number.addFirst((this.getValueAtPos(aSize - i -1) + remainder)%10);
                    remainder = 1;
                } else {
                    number.addFirst(this.getValueAtPos(aSize - i -1) + remainder);
                    remainder = 0;
                }
            }
            else
            {
                if (this.getValueAtPos(aSize - i - 1) + b.getValueAtPos(bSize - i -1) + remainder >= 10)
                {
                    number.addFirst((this.getValueAtPos(aSize - i - 1) + b.getValueAtPos(bSize - i -1) + remainder)%10);
                    remainder = 1;
                } else {
                    number.addFirst(this.getValueAtPos(aSize - i - 1) + b.getValueAtPos(bSize - i -1) + remainder);
                    remainder = 0;
                }
            }
        }
        if (remainder == 1)
        {
            number.addFirst(1);
        }
        BigInt result = new BigInt(number.size());
        result.setValue(number);
        return result;
    }

    public BigInt subtract(BigInt b)
    {
        if (b.positive == false)
        { // Rückführung auf Addition
            BigInt absValB = new BigInt(b);
            absValB.setPositive(true);
            return this.add(absValB);
        }
        if (!b.isSmallerThanOrEqual(this))
        { // Fall this < b
            if (this.positive == false)
            { // Rückführung auf Addition
                BigInt absValA = new BigInt(this);
                absValA.setPositive(true);
                BigInt result = b.add(absValA);
                result.setPositive(false); // da a-b = -(b-a)
                return result;
            }
            ArrayList<Integer> number = new ArrayList<Integer>();
            int aSize = this.decimalPlaces;
            int bSize = b.getDecimalPlaces();
            int maxSize = bSize; // wegen this.number < b.number
            int remainder = 0;
            for (int i=0;i<maxSize;i++)
            {
                if (aSize-i-1 < 0)
                {
                    if (b.getValueAtPos(bSize-i-1) - remainder >= 0)
                    {
                        number.addFirst(b.getValueAtPos(bSize - i - 1) - remainder);
                        remainder = 0;
                    } else {
                        number.addFirst(10 + b.getValueAtPos(bSize - i - 1) - remainder);
                        remainder = 1;
                    }
                } else {
                    if (b.getValueAtPos(bSize-i-1) - this.getValueAtPos(aSize-i-1) - remainder >= 0)
                    {
                        number.addFirst(b.getValueAtPos(bSize-i-1) - this.getValueAtPos(aSize-i-1) - remainder);
                        remainder = 0;
                    }
                    else {
                        number.addFirst(10 + b.getValueAtPos(bSize-i-1) - this.getValueAtPos(aSize-i-1) - remainder);
                        remainder = 1;
                    }
                }
            }
            int numberSize = number.size();
            for (int i=0;i<numberSize-1;i++)
            {
                if (number.get(0) == 0)
                {
                    number.remove(0);
                } else {
                    break;
                }
            }
            BigInt result = new BigInt(number.size());
            result.setValue(number);
            result.setPositive(false); // da a-b=-(b-a)
            return result;
        }
        ArrayList<Integer> number = new ArrayList<Integer>();
        int aSize = this.decimalPlaces;
        int bSize = b.getDecimalPlaces();
        int maxSize = aSize; // this.number >= b.number, da sonst das Ergebnis negativ wäre
        int remainder = 0;
        for (int i=0;i<maxSize;i++)
        {
            if (bSize-i-1 < 0)
            {
                if (this.getValueAtPos(aSize-i-1) - remainder >= 0) {
                    number.addFirst(this.getValueAtPos(aSize - i - 1) - remainder);
                    remainder = 0;
                } else {
                    number.addFirst(10 + this.getValueAtPos(aSize - i - 1) - remainder);
                    remainder = 1;
                }
            } else {
                if (this.getValueAtPos(aSize-i-1) - b.getValueAtPos(bSize-i-1) - remainder >= 0)
                {
                    number.addFirst(this.getValueAtPos(aSize-i-1) - b.getValueAtPos(bSize-i-1) - remainder);
                    remainder = 0;
                }
                else {
                    number.addFirst(10 + this.getValueAtPos(aSize-i-1) - b.getValueAtPos(bSize-i-1) - remainder);
                    remainder = 1;
                }
            }
        }
        int numberSize = number.size();
        for (int i=0;i<numberSize-1;i++)
        {
            if (number.get(0) == 0)
            {
                number.remove(0);
            } else {
                break;
            }
        }
        BigInt result = new BigInt(number.size());
        result.setValue(number);
        return result;
    }

    public BigInt multiply(BigInt b)
    { // Multiplikation mit dem Algorithmus von Karazuba
        boolean resultPositive = true; // Flag für Vorzeichen des Ergebnisses
        if (this.positive ^ b.positive)
        {
            resultPositive = false;
        }
        BigInt absValA = new BigInt(this);
        BigInt absValB = new BigInt(b);
        absValA.positive = true;
        absValB.positive = true;
        if (absValA.getDecimalPlaces() == 1)
        {
            if (absValA.getValueAtPos(0) == 0)
            {
                return Zero;
            }
            ArrayList<Integer> number = new ArrayList<>();
            int remainder = 0;
            for (int i=absValB.getDecimalPlaces()-1;i>=0;i--)
            {
                if (absValB.getValueAtPos(i) * absValA.getValueAtPos(0) + remainder >= 10)
                {
                    number.addFirst((absValB.getValueAtPos(i) * absValA.getValueAtPos(0) + remainder) % 10);
                    remainder = (int)(Math.floor((absValB.getValueAtPos(i) * absValA.getValueAtPos(0) + remainder)/10));
                } else {
                    number.addFirst(absValB.getValueAtPos(i) * absValA.getValueAtPos(0) + remainder);
                    remainder = 0;
                }
            }
            if (remainder != 0)
            {
                number.addFirst(remainder);
            }
            BigInt result = new BigInt(number.size());
            result.setValue(number);
            result.setPositive(resultPositive);
            return result;
        } else if (absValB.getDecimalPlaces() == 1) {
            if (absValB.getValueAtPos(0) == 0)
            {
                return Zero;
            }
            ArrayList<Integer> number = new ArrayList<>();
            int remainder = 0;
            for (int i=absValA.getDecimalPlaces()-1;i>=0;i--)
            {
                if (absValA.getValueAtPos(i) * absValB.getValueAtPos(0) + remainder >= 10)
                {
                    number.addFirst((absValA.getValueAtPos(i) * absValB.getValueAtPos(0) + remainder) % 10);
                    remainder = (int)(Math.floor((absValA.getValueAtPos(i) * absValB.getValueAtPos(0) + remainder) / 10));
                } else {
                    number.addFirst(absValA.getValueAtPos(i) * absValB.getValueAtPos(0) + remainder);
                    remainder = 0;
                }
            }
            if (remainder != 0)
            {
                number.addFirst(remainder);
            }
            BigInt result = new BigInt(number.size());
            result.setValue(number);
            result.setPositive(resultPositive);
            return result;
        }
        int length = 1 + Math.max(absValA.decimalPlaces, absValB.decimalPlaces);
        BigInt aFirstHalf = absValA.rightShift((int)(Math.floor(length / 2)));
        BigInt aSecondHalf = absValA.subtract(aFirstHalf.leftShift((int)(Math.floor(length / 2))));
        BigInt bFirstHalf = absValB.rightShift((int)(Math.floor(length / 2)));
        BigInt bSecondHalf = absValB.subtract(bFirstHalf.leftShift((int)(Math.floor(length / 2))));
        BigInt p = aFirstHalf.multiply(bFirstHalf); // rekursiver Aufruf
        BigInt q = aSecondHalf.multiply(bSecondHalf); // rekursiver Aufruf
        BigInt r = (aFirstHalf.add(aSecondHalf)).multiply(bFirstHalf.add(bSecondHalf)); // rekursiver Aufruf
        BigInt result = (p.leftShift(2*(int)(Math.floor(length / 2)))).add(q.add((r.subtract(p.add(q))).leftShift((int)(Math.floor(length / 2)))));
        result.setPositive(resultPositive);
        return result;
    }

    public BigInt rightShift(int places)
    {
        ArrayList<Integer> number = new ArrayList<>(this.getValue());
        if (places >= this.decimalPlaces)
        {
            return Zero;
        }
        for (int i=decimalPlaces-places-1;i>=0;i--)
        {
            number.set(i+places, number.get(i));
        }
        for (int i=0;i<places;++i)
        {
            number.remove(0);
        }
        BigInt result = new BigInt(0);
        result.setValue(number);
        return result;
    }

    public BigInt leftShift(int places)
    {
        if (this.decimalPlaces == 1 && this.number.get(0) == 0)
        {
            return Zero;
        }
        ArrayList<Integer> number = new ArrayList<>(this.getValue());
        for (int i=this.decimalPlaces;i<this.decimalPlaces+places;i++)
        {
            number.add(0);
        }
        BigInt result = new BigInt(0);
        result.setValue(number);
        return result;
    }

    public boolean isSmallerThanOrEqual(BigInt b)
    {
        if (!this.positive && b.positive)
        {
            return true;
        } else if (this.positive && !b.positive)
        {
            return false;
        }
        if (this.decimalPlaces < b.decimalPlaces)
        {
            return this.positive;
        } else if (this.decimalPlaces > b.decimalPlaces)
        {
            return !this.positive;
        }
        for (int i=0;i<this.decimalPlaces;i++)
        {
            if (this.number.get(i) < b.number.get(i))
            {
                return this.positive;
            } else if (this.number.get(i) > b.number.get(i))
            {
                return !this.positive;
            }
        }
        return true;
    }

    public boolean isGreaterThanOrEqual(BigInt b)
    {
        if (!this.positive && b.positive)
        {
            return false;
        } else if (this.positive && !b.positive)
        {
            return true;
        }
        if (this.decimalPlaces < b.decimalPlaces)
        {
            return !this.positive;
        } else if (this.decimalPlaces > b.decimalPlaces)
        {
            return this.positive;
        }
        for (int i=0;i<this.decimalPlaces;i++)
        {
            if (this.number.get(i) > b.number.get(i))
            {
                return this.positive;
            } else if (this.number.get(i) < b.number.get(i))
            {
                return !this.positive;
            }
        }
        return true;
    }

    public boolean isEqualTo(BigInt b)
    {
        if (this.decimalPlaces != b.decimalPlaces)
        {
            return false;
        }
        for (int i=0;i<this.decimalPlaces;i++)
        {
            if (this.number.get(i) != b.number.get(i))
            {
                return false;
            }
        }
        return true;
    }

    public BigInt greatestCommonDivisor(BigInt bOriginal)
    {
        BigInt a = new BigInt(this);
        BigInt b = new BigInt(bOriginal);
        while (!(b.decimalPlaces == 1 && b.number.get(0) == 0))
        {
            BigInt aModb = a.mod(b);
            a.setValue(b.getValue());
            b.setValue(aModb.getValue());
        }
        return new BigInt(a);
    }

    public BigInt mod(BigInt b)
    {
        if (!this.positive)
        {
            throw new IllegalArgumentException("a mod b is only defined for positive a!");
        }
        if (!(b.isSmallerThanOrEqual(this)))
        { // Fall a < b, also a mod b = a
            return new BigInt(this);
        }
        // a = 22222222
        // b = 5
        //
        // subtrahend = 5000000, immer weiter mit b auffüllen, bis a < b gilt, dann gilt result = a mod b
        BigInt result = new BigInt(this);
        while (result.isGreaterThanOrEqual(b))
        {
            BigInt subtrahend = new BigInt("0");
            for (int i=result.decimalPlaces-b.decimalPlaces-1; i>=0; i-=b.decimalPlaces)
            {
                subtrahend = (b.leftShift(i)).add(subtrahend);
            }
            if (subtrahend.decimalPlaces == 1 && subtrahend.number.get(0) == 0)
            { // falls die result und b also die gleiche Anzahl an Stellen haben
                break;
            }
            result = result.subtract(subtrahend);
        }
        if (result.isGreaterThanOrEqual(b))
        {
            while (result.isGreaterThanOrEqual(b))
            {
                result = result.subtract(b);
            }
        }
        return result;
    }

    public Pair modWithFlooredDivison(BigInt b)
    { // gibt ((int)(this/b), this mod b) zurück
        if (!(b.isSmallerThanOrEqual(this)))
        { // Fall a < b, also a = 0*b + a
            return new Pair(Zero, this);
        }
        BigInt mod = new BigInt(this);
        BigInt flooredQuotient = new BigInt("0");
        while (mod.isGreaterThanOrEqual(b))
        {
            BigInt subtrahend = new BigInt("0");
            for (int i = mod.decimalPlaces-b.decimalPlaces-1; i>=0; i-=b.decimalPlaces)
            {
                subtrahend = (b.leftShift(i)).add(subtrahend);
                flooredQuotient = flooredQuotient.add(One.leftShift(i));
            }
            if (subtrahend.decimalPlaces == 1 && subtrahend.number.get(0) == 0)
            { // falls mod und b also die gleiche Anzahl an Stellen haben
                break;
            }
            mod = mod.subtract(subtrahend);
        }
        if (mod.isGreaterThanOrEqual(b))
        {
            while (mod.isGreaterThanOrEqual(b))
            {
                mod = mod.subtract(b);
                flooredQuotient = flooredQuotient.add(new BigInt("1"));
            }
        }
        return new Pair(flooredQuotient, mod);
    }

    public Pair bezoutCoefficients(BigInt b)
    { // berechnet x,y \in Z sodass ggT(this,b)=x*this+y*b
        if (b.getDecimalPlaces() == 1 && b.getValueAtPos(0) == 0)
        {
            return new Pair(One, Zero);
        }
        Pair aModB = this.modWithFlooredDivison(b);
        Pair PreviousIteration = b.bezoutCoefficients(aModB.second()); // = b.bezoutCoefficients(a%b), rekursiver Aufruf
        return new Pair(PreviousIteration.second(), (PreviousIteration.first()).subtract(PreviousIteration.second().multiply(aModB.first()))); // = xPreviousIteration - yPreviousIteration * Math.floor(this / b)
    }

    public BigInt generateRandomSmallerThanThis()
    { // berechnet eine Pseudo-Zufallszahl mit maximal this.decimalPlaces Stellen
        if (this.positive == false)
        {
            throw new IllegalArgumentException("Negative numbers are not allowed for generating a random number smaller than this.");
        }
        ArrayList<Integer> number = new ArrayList<>();
        for (int i=0;i<this.decimalPlaces;i++)
        {
            int random = (int)(Math.random()*9);
            if (i == 0) {
                while (random >= this.getValueAtPos(0))
                {
                    random = (int)(Math.random()*9);
                }
            }
            number.add(random);
        }
        for (int i=0;i<this.decimalPlaces-1;i++)
        {
            if (number.get(0) == 0)
            {
                number.removeFirst();
            } else {
                break;
            }
        }
        BigInt result = new BigInt();
        result.setValue(number);
        return result;
    }

    public BigInt raiseToPowerMod(BigInt exponent, BigInt b)
    { // Berechnet this^exponent mod b
        if (exponent.isEqualTo(One))
        {
            return this.mod(b);
        }
        if (exponent.isEqualTo(Zero))
        {
            return One.mod(b);
        }
        BigInt result = new BigInt(One);
        BigInt aModB = this.mod(b);
        if (exponent.isEven())
        {
            result = aModB.multiply(aModB);
            result = result.mod(b);
            return result.raiseToPowerMod(exponent.divideByTwo(), b).mod(b);
        } else {
            result = aModB.multiply(aModB);
            result = result.mod(b);
            return (result.raiseToPowerMod((exponent.subtract(One)).divideByTwo(), b).multiply(aModB)).mod(b);
        }
    }

    public BigInt divideByTwo()
    {
        if (this.isEven() == false)
        {
            throw new IllegalArgumentException("cannot divide an uneven number by two.");
        }
        ArrayList<Integer> numberTrailingZero = new ArrayList<>();
        numberTrailingZero.add(0);
        numberTrailingZero.addAll(this.number);
        ArrayList<Integer> result = new ArrayList<>();
        for (int i=0;i<this.decimalPlaces;i++)
        {
            if (numberTrailingZero.get(i) % 2 == 0)
            {
                switch (numberTrailingZero.get(i + 1))
                {
                    case 0:
                    case 1:
                        result.add(0);
                        break;
                    case 2:
                    case 3:
                        result.add(1);
                        break;
                    case 4:
                    case 5:
                        result.add(2);
                        break;
                    case 6:
                    case 7:
                        result.add(3);
                        break;
                    case 8:
                    case 9:
                        result.add(4);
                        break;
                }
            } else {
                switch (numberTrailingZero.get(i + 1))
                {
                    case 0:
                    case 1:
                        result.add(5);
                        break;
                    case 2:
                    case 3:
                        result.add(6);
                        break;
                    case 4:
                    case 5:
                        result.add(7);
                        break;
                    case 6:
                    case 7:
                        result.add(8);
                        break;
                    case 8:
                    case 9:
                        result.add(9);
                        break;
                }
            }
        }
        if (result.get(0) == 0)
        {
            result.removeFirst();
        }
        BigInt resultBigInt = new BigInt();
        resultBigInt.setValue(result);
        return resultBigInt;
    }

    public boolean isEven()
    {
        return this.number.get(decimalPlaces-1)%2 == 0;
    }

    public int getDecimalPlaces()
    {
        return this.decimalPlaces;
    }

    public int getValueAtPos(int pos)
    {
        if (pos >= this.decimalPlaces || pos < 0)
        {
            throw new IndexOutOfBoundsException("Index " + pos + " out of bounds for size " + this.decimalPlaces + ".");
        }
        return this.number.get(pos);
    }

    public ArrayList<Integer> getValue()
    {
        return this.number;
    }

    public String getValueString()
    {
        String result = "";
        for (int i=0;i<this.decimalPlaces;i++)
        {
            result += this.number.get(i);
        }
        return result;
    }

    private void setValue(ArrayList<Integer> number)
    {
        this.number = number;
        this.decimalPlaces = number.size();
    }

    public void printValue()
    {
        if (this.positive == false)
        {
            System.out.print("-");
        }
        for (int i=0;i<this.decimalPlaces;i++)
        {
            System.out.print(this.getValueAtPos(i));
        }
        System.out.println();
    }

    public boolean getPositive()
    {
        return this.positive;
    }

    public void setPositive(boolean positive)
    {
        this.positive = positive;
    }
}