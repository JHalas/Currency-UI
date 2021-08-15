package core;

import core.service.CurrenciesRatesService;

import java.io.IOException;
import java.util.Scanner;

public class Main {


    private static final Scanner scanner = new Scanner(System.in);
    private static CurrenciesRatesService currenciesRatesService;
    private static boolean mainLoop = true;
    private static final int RATES_FETCH_INTERVAL_IN_SECONDS = 5;

    public static void main(String[] args) throws IOException {
        currenciesRatesService = new CurrenciesRatesService(RATES_FETCH_INTERVAL_IN_SECONDS);
        while (mainLoop) {
            printMenu();
            int choice = scanner.nextInt();
            menuSwitch(choice);
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println(" |||||||| ||||||  ||   ||  ||   ||");
        System.out.println(" || || || ||      |||  ||  ||   ||");
        System.out.println(" || || || ||||||  || | ||  ||   ||");
        System.out.println(" || || || ||      ||  |||  ||   ||");
        System.out.println(" || || || ||||||  ||   ||  |||||||");
        System.out.println();
        System.out.println("1. Show actual rates");
        System.out.println("2. Filter rates");
        System.out.println("3. Exit");
    }

    private static void menuSwitch(int choice) throws IOException {
        switch (choice) {
            case 1: {
                showRealtimeRates();
                break;
            }
            case 2: {
                showFilteredRates();
                break;
            }
            case 3: {
                mainLoop = false;
                currenciesRatesService.stop();
                break;
            }
            default: {
                System.out.println("Unknown choice");
                break;
            }
        }
    }

    private static void showRealtimeRates() throws IOException {
        currenciesRatesService.printRealtimeRates();
    }

    private static void showFilteredRates() throws IOException {
        System.out.println("Put from: (epoch in seconds)");
        long fromTime = scanner.nextLong();
        System.out.println("Put to: (epoch in seconds)");
        long toTime = scanner.nextLong();
        System.out.println("Put baseCode: (put NONE if not needed)");
        String baseCode = scanner.next();
        if (!isCodeValid(baseCode)) currenciesRatesService.printFilteredRates(fromTime, toTime);
        else currenciesRatesService.printFilteredRates(fromTime, toTime, baseCode);
        System.out.println("Click any to menu...");
        System.in.read();
    }

    private static boolean isCodeValid(String code) {
        return code != null && code.length() == 3 && code.chars().allMatch(Character::isLetter);
    }
}
