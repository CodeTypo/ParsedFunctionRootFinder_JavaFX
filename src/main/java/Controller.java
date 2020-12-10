import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;


import javax.management.InvalidAttributeValueException;

public class Controller {

    //Tutaj następuje inicjalizacja wszystkich FXML'owych zmiennych, zwinąłem ten kod dla zwiększenia czytelności
    @FXML
    private ResourceBundle resources;

    @FXML
    private ChoiceBox<String> RootFinderMethodBox;

    @FXML
    private URL location;

    @FXML
    private TextField minX;

    @FXML
    private TextField maxX;

    @FXML
    private TextArea consoleLog;

    @FXML
    private LineChart<Double, Double> graph;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextField lowerXlimit;

    @FXML
    private TextField upperXlimit;

    @FXML
    private Button btnFndRoot;

    @FXML
    private TextField maxIter;

    @FXML
    private TextField maxEpsilon;

    @FXML
    private CheckBox showLogs;

    @FXML
    private TextField formulaField;

    @FXML
    private Button btnPlot;


    ObservableList<String>rootFinderMethods = FXCollections.observableArrayList
            ("Bisection","Fixed Point","Metoda Siecznych","Newton Raphson","Regula Falsi");
    double rootFinderLowerXlimit;                   //Dolna granica przedziału, na którym szukamy pierwiastka
    double rootFinderUpperXlimit;                   //Górna granica przedziału, na którym szukamy pierwiastka
    double functionPlotxMin             = -10;      //Dolna granica przedziału, na którym rysowany jest wykres funkcji
    double functionPlotxMax             = 10;       //Górna granica przedziału, na którym rysowany jest wykres funkcji
    int iMax                            = 50;       //Maksymalna ilość iteracji metody
    double toleratedEpsilon             = 1e-10;    //Tolerowana wartość błędu wyznaczania pierwiastka
    FunctionOfX function;                           //Fukcja, którą będziemy analizować
    RootFinder rf = new BisectionRF();              //Metoda,przy pomocy której będziemy szukać miejsca zerowego


    // Funkcja wywoływana po naciśnięciu przycisku "plot"
    @FXML
    void onClickPlot(ActionEvent event) throws InvalidAttributeValueException {
        graph.getData().clear();                                                //Czyszczenie grafu, na którym będzie rysowana funkcja
        XYChart.Series series = new XYChart.Series();                           //Tworzenie nowej serii danych
        for(double x = functionPlotxMin; x<=functionPlotxMax; x+= 0.1){         //Pętla od dolnej granicy przedziału do górnej granicy przedziału
                series.getData().add(new XYChart.Data<>(x,function.fOfX(x)) );  //Dodawanie wartości funkcji w x, do serii danych
            }
        series.setName("f(x) = " + function.toString());
        graph.getData().add(series);                                            //Wykreślenie serii na grafie
    }

    // Funkcja wywoływana po naciśnięciu przycisku "find root"
    @FXML
    void onClickFindRoot() throws InvalidAttributeValueException {
        //Sprawdzanie, którą metodę wyznaczania miejsca zerowego funkcji wybrał użytkownik
        // I utworzenie odpwiedniego obiektu implementującego interfejs RootFinder
        String methodType = RootFinderMethodBox.getValue();
        switch (methodType) {
            case "Bisection"        -> rf = new BisectionRF();
            case "Fixed Point"      -> rf = new FixedPointRF();
            case "Metoda Siecznych" -> rf = new MetodaSiecznychRF();
            case "Newton Raphson"   -> rf = new NewtonRaphsonRF();
            case "Regula Fasli"     -> rf = new RegulaFalsiRF();
            default                 -> rf = new BisectionRF();
        }
        //Tworzy nowy obiekt urf
        UltimateRootFinder urf = new UltimateRootFinder(rootFinderLowerXlimit,rootFinderUpperXlimit,iMax,toleratedEpsilon,function,rf);
        if(showLogs.isSelected()){urf.setPrintEpsilons(true);}
        double x = urf.calculateRoot();   //przezchowuje wartość obliczonego pierwiastka funkcji w zmiennej x
        System.out.println("Obliczona wartosc pierwiastka funkcji [ " + methodType + " ]: " + x);
        if(!Double.isInfinite(x)) {
            XYChart.Series series = new XYChart.Series();
            series.setName(methodType);
            series.getData().add(new XYChart.Data<>(x, function.fOfX(x)));
            graph.getData().add(series);        //dodawanie wartości pierwiastka do wykresu funkcji
        }
    }

    // Funkcja wywoływana po potwierdzeniu klawiszem "enter" zakończenia procesu wpisywania wzoru funkjci do analizy
    @FXML
    void onFormulaInputFinished() {
        function = new FunctionOfX() {  //Inicjalizacja zadeklarowanej uprzednio klasy implementującej interfejs FunctionOfX
            @Override
            public double fOfX(double x) {
                // Seria poleceń prowadzących do zparse'owania łańcucha wprowadzonego przez użytkownika na funkcję argumentu x
                Argument xarg = new Argument("x",x);
                Expression e1 = new Expression(formulaField.getText(),xarg);
                return e1.calculate();
            }

            @Override
            public double dfOfX(double x) {
                // Seria poleceń prowadzących do zparse'owania łańcucha wprowadzonego przez użytkownika na pochodną funkcji argumentu x
                Argument xarg = new Argument("x",x);
                Expression e1 = new Expression("der(" + formulaField.getText() + ",x)",xarg);
                return e1.calculate();
            }

            @Override
            public String toString() {
               return formulaField.getText();
            }
        };
    }


    //Seria poleceń przypisujących wartości wprowadzone do Text fieldów / chekboxów przez użytkownika odpowiadającym im zmiennym
    @FXML
    void onMaxXInserted() {
        functionPlotxMax = Double.parseDouble(maxX.getText());
    }
    @FXML
    void onMinXInserted() {
        functionPlotxMin = Double.parseDouble(minX.getText());
    }
    @FXML
    void onLowerLimitInserted() {
        rootFinderLowerXlimit = Double.parseDouble(lowerXlimit.getText());
    }
    @FXML
    void onUpperLimitInserted() {
        rootFinderUpperXlimit = Double.parseDouble(upperXlimit.getText());
    }
    @FXML
    void maxEpsilonPassed() { toleratedEpsilon = Double.parseDouble(maxEpsilon.getText()); }
    @FXML
    void maxIterPassed() { iMax = Integer.parseInt(maxIter.getText()); }
    @FXML
    void showLogsChanged() {}

    //klasa pozwalająca na przekierowanie outputu konsoli do text Area w GUI programu
    private static class Console extends OutputStream {
        private final TextArea console;
        public Console(TextArea console) {
            this.console = console;
        }
        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }
        public void write(int b) {
            appendText(String.valueOf((char)b));
        }
    }

    @FXML
    void initialize() {
        assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'graph.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'graph.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'graph.fxml'.";
        assert RootFinderMethodBox != null : "fx:id=\"RootFinderMethodBox\" was not injected: check your FXML file 'graph.fxml'.";
        RootFinderMethodBox.setItems(rootFinderMethods);
        assert consoleLog != null : "fx:id=\"consoleLog\" was not injected: check your FXML file 'graph.fxml'.";
        assert lowerXlimit != null : "fx:id=\"lowerXlimit\" was not injected: check your FXML file 'graph.fxml'.";
        assert upperXlimit != null : "fx:id=\"upperXlimit\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnFndRoot != null : "fx:id=\"btnFndRoot\" was not injected: check your FXML file 'graph.fxml'.";
        assert formulaField != null : "fx:id=\"formulaField\" was not injected: check your FXML file 'graph.fxml'.";
        assert minX != null : "fx:id=\"minX\" was not injected: check your FXML file 'graph.fxml'.";
        assert maxX != null : "fx:id=\"maxX\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnPlot != null : "fx:id=\"btnPlot\" was not injected: check your FXML file 'graph.fxml'.";
        PrintStream ps = new PrintStream(new Console(consoleLog));
        System.setOut(ps);
        System.setErr(ps);
    }
}
