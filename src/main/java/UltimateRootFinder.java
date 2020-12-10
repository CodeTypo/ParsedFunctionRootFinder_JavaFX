import javax.management.InvalidAttributeValueException;

public class UltimateRootFinder {

    //Ultimate Root Finder = URF in descriptions presented below
    //A class designed to simplify evaluating of function's root values using different approaches
    //User only need to provide the Function, the RootFinder object he/she wants to use
    //And pass the maximum amount of iterations alongside with desired approximation error value

    private final double xl;                  //lower boundary of the range URF should analyse.
    private final double xh;                  //upper boundary of the range URF should analyse.
    private final double iMax;                //the max amount of iterations it should take URF to find the root.
    private final double toleratedEpsilonA;   //Maximum approximation error value tolerated by the user
    private boolean printEpsilons = false;    //A boolean flag acting like a switch, when its on, URF logs every iteration's
                                              // epsilons values to the console
    private double realX = Double.NaN;        //An optional field, contains the real value of X we are trying to calculate using URF
                                              //It is mandatory to set this if the user wants URF to calculate epsilonF.

    private final FunctionOfX f;              //A Function object representing the function URF should analyse.
    private RootFinder rf;                    //A RootFinder object representing the approach taken

    //URF constructor requiring the user to pass all the necessary fields values
    public UltimateRootFinder(double xl, double xh, double iMax, double toleratedEpsilonA, FunctionOfX f, RootFinder rf) {
        this.xl = xl;
        this.xh = xh;
        this.iMax = iMax;
        this.toleratedEpsilonA = toleratedEpsilonA;
        this.f = f;
        this.rf = rf;
    }

    public void setPrintEpsilons(boolean printEpsilons) {
        this.printEpsilons = printEpsilons;
    }

    public void setRealX(double realX){ this.realX = realX; }

    public void setRf (RootFinder rf){
        this.rf = rf;
    }

    //The only method in URF class
    public double calculateRoot() throws InvalidAttributeValueException {

        //Checking whether the RootFinder object uses a method needing 1 or 2 parameters;
        if(rf.identifyYourself() == 2){
            rf.initializeFields(f,xl,xh);
        }

        if(rf.identifyYourself() == 1){
            rf.initializeFields(f,(xl +xh)/2,Double.NaN);
        }

        //If the field realX has been assigned a value, it passes the value to the RootFinder object
        if(!Double.isNaN(realX))
            rf.setRealX(realX);

        // When the object is initialized with all the required data, a loop starts
        //The loop iterates as many times as iMax
        //Or until there is a root calculated with desired accuracy

        //First iteration of the RF findRoot() method is done outside of the loop
        double xr = rf.findRoot();
        //A check is being performed, every RF is implemented in such way, that if there is no possibility
        //of finding a root, a NaN value is being returned. If so, the URF returns NaN value and skips the loop.
        if (Double.isNaN(xr)) {
            return xr;
        }

        //Since one RF's findRoot() method has already been used, a loop iterates < iMax times.
        for(int i = 0; i < iMax ; i++){

            xr = rf.findRoot();
            if (printEpsilons) {
                System.out.printf("Iteration: %d\tEpsilonA [%%]: %.10e",i+1,rf.getEpsilonA());
                if (!Double.isNaN(realX)) {
                    System.out.printf("\t\tEpsilonF [%%] %.10e: \n",rf.getEpsilonF());
                }else{
                    System.out.println();
                    //System.out.print("\t\t To calculate epsilonF value, You must input the realX value via the setRealX() method!\n");
                }
            }
            //During every iteration of loop a current value of EpsilonA is checked, if it's less than toleratedEpsilonA
            if(rf.getEpsilonA() < toleratedEpsilonA) {
                //Calculated value is being returned, breaking out of the loop
                return xr;
            }
        }
        //If the method did not manage to get to the desired value of max tolerated epsilonA a message is being printed
        System.out.println("\nURF didn't manage to reach required precision :(\tcurrent epsilon a is: " + rf.getEpsilonA()+
                            "\t(Maybe try a few more iterations!)");
        //And returns the value of xr it managed to evaluate within given amount of iterations
        return xr;
    }
}
