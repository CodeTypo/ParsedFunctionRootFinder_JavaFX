import javax.management.InvalidAttributeValueException;

//An implementation of bisection method of finding function's root
public class BisectionRF implements RootFinder {

    //All the class fields below are described in the URF class, there is no need in describing them again
    private FunctionOfX f;
    private double xl; private double xh; private double xr = Double.NaN; private double epsilonA;
    private double realX = Double.NaN;

    private double oldXr;                   //a variable storing the value of Xr calculated in previous iteration
    private boolean rangeChecked = false;   //a boolean flag telling the class if the range has already been verified

    public BisectionRF(){}

    @Override
    //identify yourself returns 2 since the class method calculateRoot needs two arguments
    public int identifyYourself() {
        return 2;
    }

    @Override
    public void initializeFields(FunctionOfX f, double xl, double xh){
        this.f = f;
        this.xl = xl;
        this.xh = xh;
        this.oldXr = this.xr;
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
    public void setRealX(double realX) { this.realX = realX;
    }

    @Override
    public double findRoot() throws InvalidAttributeValueException {

        //A range check is being performed
        if(!rangeChecked) {
            if (f.fOfX(xl) * f.fOfX(xh) > 0) {
                System.out.println("BisectionRF: Same signs at the end of intervals");
                return Double.NaN;
            }
            rangeChecked = true;
        }
            //Standard bisection method implementation
            xr = (xl + xh) / 2;
            epsilonA = Math.abs((xr - oldXr)/xr) * 100;
            oldXr = xr;

            if(f.fOfX(xl) * f.fOfX(xr)< 0){
                xh = xr;
            } else {
                xl = xr;
            } // end of if / else

        return xr;
    } // end of findRoot method
}
