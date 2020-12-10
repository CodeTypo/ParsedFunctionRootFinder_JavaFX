import javax.management.InvalidAttributeValueException;

public interface RootFinder {

//An interface shared by all the classes representing different approaches to the function's root finding methods

    //A method that returns the number of arguments required by the RootFinder main method
    //If 2 - RootFinder requires 2 arguments Xl and Xh
    //If 1 - RootFinder requires 1 argument Xi
    int identifyYourself();

    //A method which is being used by The Ultimate Root Finder class to pass the arguments it got when initialized
    //To the RootFinder object so that it will be able to perform its task using that arguments.
    void initializeFields(FunctionOfX f, double xl, double xh);

    //Every RootFinder is required to implement a method returning the value of epsilionA
    double getEpsilonA();

    //Every RootFinder should provide a method returning the value of epsilonF
    double getEpsilonF();

    void setRealX(double realX);

    //Every RootFinder is required to provide a method returning the value of the function's root if present.
    double findRoot() throws InvalidAttributeValueException;
}
