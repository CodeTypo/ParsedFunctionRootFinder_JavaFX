import javax.management.InvalidAttributeValueException;

//A basic interface shared by all of classes representing some functions of variable x.
public interface FunctionOfX {

    //Function implementation should have a main function method
    double fOfX (double x)throws InvalidAttributeValueException;

    //It should also come with pre-calculated derivative function method.
    double dfOfX(double x)throws InvalidAttributeValueException;
}
