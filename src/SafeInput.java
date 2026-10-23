import java.util.Scanner;
public class SafeInput {
    /**
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a String response that is not zero length
     */
    public static String getNonZeroLenString(Scanner pipe, String prompt) {
        String retString = ""; // Set this to zero length. Loop runs until it isn't
        do {
            System.out.print("\n" +prompt + ": "); // show prompt add space
            retString = pipe.nextLine();
        } while (retString.length() == 0);
        return retString;
    }
    /**
     * Get an int value with no constraints
     * @param pipe - Scanner instance to read the data System.in in most cases
     * @param prompt - input prompt msg should not include range info
     * @return - unconstrained int value
     */
    public static int getInt(Scanner pipe, String prompt) {
        int retVal = 0;
        String trash = "";
        boolean done = false;
        do {
            System.out.print("\n" + prompt + ": ");
            if (pipe.hasNextInt()) {
                retVal = pipe.nextInt();
                pipe.nextLine();
                done = true;
            } else {
                trash = pipe.nextLine();
                System.out.println("You must enter an int: " + trash);
            }
        } while (!done);
        return retVal;
    }

    /**
     * Prompts user for any double
     * */

    public static double getDouble(Scanner pipe, String prompt) {
        double result = 0.00;
        boolean done = false;
        String trash = "";
        do {
            System.out.print("\n" + prompt + ": ");
            if (pipe.hasNextDouble()) {
                result = pipe.nextDouble();
                pipe.nextLine(); //Clear buffer
                done = true;
            } else {
                trash = pipe.nextLine();
                pipe.nextLine();
                System.out.println("You must enter a double. You entered: " + trash);
            }
        }while (!done);
        return result;
    }

    /*
     * Prompts user for an integer within a range
     */
    public static int getRangedInt(Scanner pipe, String prompt, int low, int high) {
        int result = 0;
        boolean done = false;
        String trash = "";

        do {
            System.out.print(prompt + " [" + low + " - " +  high + "]: ");
            if (pipe.hasNextInt()) {
                result = pipe.nextInt();
                pipe.nextLine(); // clear buffer
                if (result >= low && result <= high) {
                    done = true;
                } else {
                    System.out.println("\n Error: Input not in range [" + low + " - " + high + "].");
                }
            }else {
                trash = pipe.nextLine();
                System.out.println ("\n Error: invalid input. Please enter an integer. You entered: " + trash);
            }
        } while (!done);
        return result;
    }

    /*
     * Prompts for a double within a provided range
     */
    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high) {
        double result = 0.00;
        boolean done = false;
        String trash = "";
        do {
            System.out.print(prompt + "[ " + low + " - " + high + " ].");
            if (pipe.hasNextDouble()) {
                result = pipe.nextDouble();
                pipe.nextLine(); //clear buffer
                if (result >= low && result <= high) {
                    done = true;
                } else {
                    System.out.println("\n Error: Input not in range [" + low + " - " + high + "].");
                }
            } else {
                trash = pipe.nextLine();
                System.out.println("\n Error: invalid input. Please enter an integer. You entered: " + trash);
            }
        } while (!done);
        return result;
    }
    /*
     * Prompts for yes/no and returns true for yes, false for no
     */
    public static boolean getYNConfirm(Scanner pipe, String prompt) {
        String response = "";
        boolean valid = true;
        boolean result = false;

        do {
            System.out.print(prompt + " [Y/N]: ");
            response = pipe.nextLine().trim();
            if (response.equalsIgnoreCase("Y")) {
                result = true;
                valid = true;
            } else if (response.equalsIgnoreCase("N")) {
                result = true;
                valid = false;
            } else {
                System.out.println("\n Error: Please enter 'Y' or 'N.' You entered: " + response);
            }
        } while (!result);
        return valid;
    }

    /**
     * Get a string that matches a RegEx pattern! This is a very powerful method
     * @param pipe - Scanner instance to read the data System.in in most cases
     * @param prompt - prompt for user
     * @param regExPattern - java style RegEx pattern to constrain the input
     * @return a String that matches the RegEx pattern supplied
     */
    public static String getRegExString(Scanner pipe, String prompt, String regExPattern) {
        String response = "";
        boolean gotAVal = false;
        do {
            System.out.print("\n" + prompt + ": ");
            response = pipe.nextLine();
            if (response.matches(regExPattern)) {
                gotAVal = true;
            } else {
                System.out.println("\n" + response + " must match the pattern " + regExPattern);
                System.out.println("Try again!");
            }
        } while (!gotAVal);
        return response;
    }
}