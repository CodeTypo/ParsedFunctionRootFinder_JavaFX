import javax.management.InvalidAttributeValueException;

public class NewtonRaphsonRF implements RootFinder {

    private FunctionOfX f;
    private double xi = Double.NaN;
    private double epsilonA;
    private double oldXi;
    private double realX; //Used in getEpsilonF method


    @Override
    public int identifyYourself() {
        return 1;
    }

    @Override
    public void initializeFields(FunctionOfX f, double xi, double notUsedInThisClass) {
        this.f = f;
        this.xi = xi;
        this.epsilonA = Double.MAX_VALUE;
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
            xi = oldXi - (f.fOfX(oldXi) / f.dfOfX(oldXi));
            epsilonA = Math.abs(oldXi - xi) / oldXi * 100;
        return xi;
    }
}
