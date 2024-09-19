package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.util.AppConstants;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    private User currentUser;

    public ConsoleService(){
    }

    public ConsoleService(User user) {
        this.currentUser = user;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        String greeting = "* Welcome to TEnmo! *";
        printCliBorder(greeting.length());
        System.out.println(greeting);
        printCliBorder(greeting.length());
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        String balanceString = "Your current balance: " + currency.format(balance);
        printCliBorder(balanceString.length());
        System.out.println(balanceString);
        printCliBorder(balanceString.length());
    }

    public void printCliBorder(int length) {
        System.out.println("*".repeat(Math.max(0, length)));
    }


    public void printHistoryHeader() {
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
    }

    public void printFullHistoryTransfer(TransferDto transfer, String username) {
        String otherUser = transfer.getAccount_from().equals(username) ?
                "To: " + transfer.getAccount_to() :
                "From: " + transfer.getAccount_from();
        System.out.printf("%-10d %-25s %s%n", transfer.getTransferId(), otherUser, currency.format(transfer.getAmount()));
    }

    public void printPendingHeader() {
        System.out.println("Pending Transfers");
        System.out.printf("%-10s %-20s %s%n", "Id", "To", "Amount");
    }

    public void printPendingTransferHistory(TransferDto transfer) {
        System.out.printf("%-10d %-25s %s%n", transfer.getTransferId(), transfer.getAccount_to(), currency.format(transfer.getAmount()));
    }


    public void printTransferDetails(TransferDto transfer) {
        printCliBorder(40);
        System.out.println("Transfer Id: " + transfer.getTransferId());
        System.out.println("Transfer Type: " + transfer.getTransferType());
        System.out.println("Transfer Status: " + transfer.getTransferStatus());
        System.out.println("Account From: " + transfer.getAccount_from());
        System.out.println("Account To: " + transfer.getAccount_to());
        System.out.println("Amount: " + currency.format(transfer.getAmount()));
        printCliBorder(40);
    }

    public void printApproveRejectMenu() {
        System.out.println("1: " + AppConstants.Status.APPROVED.toString());
        System.out.println("2: " + AppConstants.Status.REJECTED.toString());
        System.out.println("0: Don't approve or reject");
        printCliBorder(10);
        System.out.println("Please choose an option:");
    }

    public int promptForApprovalOption() {
        while (true) {
            try {
                int entry = Integer.parseInt(scanner.nextLine());
                if (entry < 3 && entry > 0) {
                    return entry;
                } else {
                    System.out.println("The number was an invalid option");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public void printUserSelectMenu(List<User> userList) {
        printCliBorder(35);
        System.out.println("Users");
        System.out.printf("%-10s %-20s%n", "ID", "Name");
        printCliBorder(35);
        for (User user : userList) {
            if (user.getId() != currentUser.getId()) {
                System.out.printf("%-10s %-20s%n", user.getId(), user.getUsername());
            }
        }
        printCliBorder(20);
    }

    public void printNewTransferHeading(AppConstants.Type type) {
        String heading = type.toString() + " Funds";
        printCliBorder(heading.length());
        System.out.println(heading);
    }
}
