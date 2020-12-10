import javax.management.InvalidAttributeValueException;

public class MetodaSiecznychRF implements RootFinder {

    private FunctionOfX f;
    private double oldXi;
    private double xi = Double.NaN;
    private double futureXi;
    private double epsilonA;
    private double realX; //Used in getEpsilonF method

    @Override
    public int identifyYourself() {
        return 1;
    }

    @Override
    public void initializeFields(FunctionOfX f, double xi, double notUsedInThisMethod) {
        this.f = f;
        this.epsilonA = Double.MAX_VALUE;
        this.xi = xi * 1.1;
        this.futureXi = xi;
    }

    @Override
    public double getEpsilonA() {
        return epsilonA;
    }

    @Override
    public double getEpsilonF() {
        if(Double.isNaN(realX))
            return Double.NaN;
        return Math.abs((xi - realX)/realX) * 100;
    }

    @Override
    public void setRealX(double realX) {
        this.realX = realX;
    }

    @Override
    public double findRoot() throws InvalidAttributeValueException {
            oldXi = xi;
            xi = futureXi;
            futureXi = xi - ((f.fOfX(xi) * (oldXi - xi)) / (f.fOfX(oldXi) - f.fOfX(xi)));
            epsilonA = Math.abs(((futureXi - xi) / xi) * 100);
        return xi;
    }
}
