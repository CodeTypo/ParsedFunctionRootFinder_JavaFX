import javax.management.InvalidAttributeValueException;

public class FixedPointRF implements RootFinder {

    private FunctionOfX f;
    private double xr = Double.NaN;
    private double epsilonA;
    private double oldXr;
    private double realX; //Used in getEpsilonF method

    //Fixed point returns one while identifyYourself, since it only needs one parameter to perform the findRoot method
    @Override
    public int identifyYourself() {
        return 2;
    }

    //Since the method initializeFields is described as a method that takes four parameters
    @Override
    public void initializeFields(FunctionOfX f, double xl, double notUsedInThisMethod) {
        this.f = f;
        this.xr = xl;
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
        return Math.abs((xr - realX)/realX) * 100;
    }

    @Override
    public void setRealX(double realX) {
        this.realX = realX;
    }

    @Override
    public double findRoot() throws InvalidAttributeValueException {
            oldXr = xr;
            xr = f.fOfX(oldXr) + oldXr;
            epsilonA = Math.abs((xr - oldXr) / xr) * 100;
        return xr;
    }
}
