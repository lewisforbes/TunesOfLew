import com.amazonaws.services.lambda.runtime.Context;

public final class App {
    public static void handleRequest(String arg, Context context) {
        Main.main();
        System.out.println("--------\nEnd of App.handleRequest\n");
    }
}