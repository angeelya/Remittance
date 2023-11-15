package org.example;

import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);
    static int choice;

    public static void main(String[] args) {
        System.out.println("Enter number:\n 1-start the remittances\n 2-get the remittances history");
        if (in.hasNextInt()) {
            choice = in.nextInt();
            if(choice==1) startRemittances();
            else if (choice==2) getRemittancesHistory();
            else System.out.println("There aren't such options");
        } else
            System.out.println("It isn't number");
    }

    private static void startRemittances() {

    }

    private static void getRemittancesHistory() {
    }

}