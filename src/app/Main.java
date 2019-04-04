package app;


import app.helpers.Input;
import app.services.AlgorithmService;

public class Main {
    public static void main(String[] args) {
        Input $input = new Input();

        (new AlgorithmService($input)).start();
    }
}
