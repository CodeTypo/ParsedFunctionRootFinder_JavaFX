import javax.management.InvalidAttributeValueException;


public class RegulaFalsiRF implements RootFinder {

    //All the class fields below are described in the URF class, there is no need in describing them again

    private FunctionOfX f;
    private double xl;
    private double xh;
    private double xr = Double.NaN;
    private double epsilonA;
    private double oldXr;
    private double realX; //Used in getEpsilonF method



    public RegulaFalsiRF() {
    }

    @Override
    public int identifyYourself() {
        return 2;
    }

    @Override
    public void initializeFields(FunctionOfX f, double xl, double xh) {
        this.f = f;
        this.xl = xl;
        this.xh = xh;
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

    //Standard Regula Falsi implementation, similar to the one provided in List_4
    public double findRoot() throws InvalidAttributeValueException {
            oldXr = xr;
            xr = xl - ((f.fOfX(xl) * (xh - xl)) / (f.fOfX(xh) - f.fOfX(xl)));
            epsilonA = (Math.abs(xr - oldXr) / oldXr) * 100;

            if (f.fOfX(xl) * f.fOfX(xr) < 0) {
                xh = xr;
            } else  {
                xl = xr;
            }
        return xr;
    }
}
