package lejarza.asier.cuisineasier;

/**
 * Created by Asier on 16/10/2016.
 */
public class Utility {

    //1: Normal screen
    //2: Small screen
    private static int screen_size = 0;
    public static int getData_screen_size() {
        return screen_size;
    }
    public static void setData_screen_size(int new_screen_size) {
        Utility.screen_size = new_screen_size;
    }

}
