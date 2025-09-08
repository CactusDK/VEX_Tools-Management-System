import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.awt.HeadlessException;

/**
 * This is a demo to manage tools during the competition.
 */
public class ManagementSystemDemo
{
    private Scanner scan;
    private String currentFile;
    private boolean menuShow;

    private ArrayList<String> tools;
    private ArrayList<Integer> number_tools_take = new ArrayList<Integer>(tools.size());
    private ArrayList<Integer> number_tools_back;

    private ArrayList<String> defaultTools = new ArrayList<String>();
    private ArrayList<Integer> default_number_tools_take = new ArrayList<Integer>(defaultTools.size());

    

    public static void main(String[] args)
    {
        ManagementSystemDemo system_demo = new ManagementSystemDemo();
        system_demo.toolManagement(); // starting here
    }

    private void initialDefaultList()
    {
        defaultTools.set(0, "Screw driver");       default_number_tools_take.set(0, 5);
        defaultTools.set(1, "rancher");            default_number_tools_take.set(1, 5);
        defaultTools.set(2, "plier");              default_number_tools_take.set(2, 4);
        defaultTools.set(3, "zitiscutter");        default_number_tools_take.set(3, 1);
        defaultTools.set(4, "scissor");            default_number_tools_take.set(4, 1);
        defaultTools.set(5, "file");               default_number_tools_take.set(5, 5);
        defaultTools.set(6, "saw");                default_number_tools_take.set(6, 1);
        defaultTools.set(7, "Big drill");          default_number_tools_take.set(7, 1);
        defaultTools.set(8, "small drill");        default_number_tools_take.set(8, 1);
        defaultTools.set(9, "yellow cuter");       default_number_tools_take.set(9, 2);
        defaultTools.set(10, "Remote");            default_number_tools_take.set(10, 1);
        defaultTools.set(11, "switch");            default_number_tools_take.set(11, 1);
        defaultTools.set(12, "battery");           default_number_tools_take.set(12, 10);
        defaultTools.set(13, "green fan");         default_number_tools_take.set(13, 2);
        defaultTools.set(14, "air pump");          default_number_tools_take.set(14, 1);
        defaultTools.set(15, "Cart");              default_number_tools_take.set(15, 1);
        defaultTools.set(16, "Robot box");         default_number_tools_take.set(16, 1);
        defaultTools.set(17, "robot box cover");   default_number_tools_take.set(17, 1);
        defaultTools.set(18, "Power bar");         default_number_tools_take.set(18, 1);
        defaultTools.set(19, "Super cool");        default_number_tools_take.set(19, 2);
        defaultTools.set(20, "Tweezer");           default_number_tools_take.set(20, 4);
    }

    /**
     * Main tool management system.
     * Handles user menu and interaction with other methods.
     */
    public void toolManagement()
    {
        do
        {
            System.out.println("\f"); // Clears the screen
            menuShow = true;
            currentFile = null;
            
            // Initialize the tool list and scanner for each menu loop
            tools = new ArrayList<String>(); // Initialize the tool list
            scan = new Scanner(System.in); // Prepare scanner for input
    
            loadToolsFromFile();
            
            if(menuShow)
            {
                menu();
            }
        }
        while(doAgain()); // Repeat menu if the user chooses
    }

    private void menu()
    {
        // Display menu options
        System.out.println("Choose an option:");
        System.out.println("1: Add tools");
        System.out.println("2: Remove tools");
        System.out.println("3: List all tools");
        System.out.println("4: Compare the tools you took away and took back");
        System.out.println("5: Save");
        System.out.println("6: Exit");
        // Get a valid menu option from the user
        int choice = getValidIntInput("Enter your choice (1-6): ", 1, 6);
        
        if (choice == 1)
        {
            addTools(); // Call method to add tools
        }
        else if (choice == 2)
        {
            deleteTools(); // Call method to delete tools
        }
        else if (choice == 3)
        {
            FileReading(true); // Display tools list
        }
        else if(choice == 4)
        {
            compareData(); // Create a new list
        }
        else if(choice == 5)
        {
            saveToFile(tools, false); //Save to the current file or another file
        }
        else if (choice == 6)
        {
            System.out.println("Exit.");
            return; // Exit program
        }
    }

    /**
     * Allows select a file using a file chooser.
     * @return The absolute path of the selected file, or null if the operation is canceled.
     */
    private String selectFile()
    {
        try
        {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Select a file to load");
            int userSelection = fileChooser.showOpenDialog(null);

            // Return the selected file path or inform the user if canceled
            if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooser.getSelectedFile();
                return selectedFile.getAbsolutePath();
            }
            else
            {
                System.out.println("File selection canceled.");
            }
        }
        catch (HeadlessException e)
        {
            System.out.println("Error: GUI is not supported in this environment.");
        }
        catch (SecurityException e)
        {
            System.out.println("Error: Insufficient permissions to access the file chooser.");
        }

        return null;
    }

    /**
     * Loads tool data from a specified file.
     * @param defaultFileName The name of the file to load by default.
     * @return True if the file was loaded successfully, false otherwise.
     */
    private void loadToolsFromFile()
    {
        String fileName;
        System.out.println("Choose \"Y\" to create a new list, \"N\" to load a list from file (Y/N), \"D\" to use the default list from preset: ");
        System.out.println("example: \"Screw driver,5,5\" or \"Screw driver,5,\"");
        String decision = scan.nextLine().trim().toUpperCase();

        if (decision.equals("N"))
        {// choose a different file
            fileName = selectFile();
            
            if (fileName == null)
            {
                System.out.println("File loading aborted.\n");
            }
            else
            {
                try
                {
                    Scanner fileReading = new Scanner(new File(fileName));
                    while (fileReading.hasNext())
                    {
                        tools.add(fileReading.nextLine());
                    }
                    fileReading.close();
                    currentFile = fileName;
                }
                catch (FileNotFoundException e)
                {
                    System.out.print("Error: File not found.\n");
                }
            }
        }
        if(decision.equals("D"))
        {
            copyTheList(tools, defaultTools);
            copyTheList(number_tools_take, default_number_tools_take);
            countToolsBack(tools);
            compareData(tools);
            menuShow = false;
            return;
        }
        if(decision.equals("Y"))
        {
            System.out.println();
            createNewList(false);
        }
    }

    private void countToolsBack(ArrayList<String> toolsList)
    {
        number_tools_back = new ArrayList<Integer>(toolsList.size());

        for(int i = 0; i < toolsList.size(); i++)
        {
            System.out.print("Number of " + toolsList.get(i) + " you took back: ");
            if()
            int number = scan.nextInt();
            

            while(number < 0)
            {
                System.out.print("\nInvalid format! Please enter the data again:");
                number = scan.nextInt();
            }
            number_tools_back.add(number);
        }
    }

    private void compareData(ArrayList<String> toolsList)
    {
        if(toolsList.isEmpty())
        {
            System.out.println("The tools list is empty. Please add tools first.\n");
            return;
        }
        if(number_tools_back.isEmpty())
        {
            System.out.println("The tools back list is empty. Please add tools first.\n");
            number_tools_back = new ArrayList<Integer>(toolsList.size());
            countToolsBack(toolsList);
        }
        
        System.out.println("\nComparison of tools taken away and taken back:");
        System.out.println("***********************************************");
        for(int i = 0; i < toolsList.size(); i++)
        {
            String toolName = toolsList.get(i);
            int tookAway = number_tools_take.get(i);
            int tookBack = number_tools_back.get(i);
            int difference = tookAway - tookBack;

            System.out.print("Tool: " + toolName);
            System.out.print("    Took Away: " + tookAway);
            System.out.println("    Took Back: " + tookBack);
            if(difference > 0)
            {
                System.out.println("Missing: " + difference);
            }
            else if(difference < 0)
            {
                System.out.println("Extra: " + (-difference));
            }
            else
            {
                if(tookAway > 1)
                {
                    System.out.println("All " + toolName + "s backed.");
                }
                else
                {
                    System.out.println(toolName + "is backed.");
                }
            }
            System.out.println("-----------------------------------------------");
        }
        System.out.println("***********************************************\n");
    }

    /**
     * Adds new tools to the list.
     */
    private void addTools()
    {
        createNewList(true);
    }

    /**
     * Deletes tools from the list based on user input.
     */
    private void deleteTools()
    {
        ArrayList<Integer> removeNumbers = new ArrayList<Integer>();

        // Display the current list of tools
        FileReading(false);
        System.out.println("Enter the line numbers of the tools to delete, input \"DONE\" to stop editing.");

        // Allow the user to specify ranges of tools to remove
        while (true)
        {
            System.out.print("From: ");
            String inputFrom = scan.nextLine().trim();
            if (inputFrom.equalsIgnoreCase("DONE"))
            {
                break;
            }
            System.out.print("To: ");
            String inputTo = scan.nextLine().trim();
            if (inputTo.equalsIgnoreCase("DONE"))
            {
                break;
            }

            // Validate the range and add indices to removeNumbers
            try
            {
                int indexFrom = Integer.parseInt(inputFrom) - 1;
                int indexTo = Integer.parseInt(inputTo) - 1;

                if (indexFrom >= 0 && indexTo < tools.size() && indexFrom <= indexTo)
                {
                    for (int i = indexFrom; i <= indexTo; i++)
                    {
                        if (!removeNumbers.contains(i)) 
                        {
                            removeNumbers.add(i); // Ensure no duplicates
                        }
                    }
                }
                else
                {
                    System.out.println("Invalid number! Please enter a number in range.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input! Please enter a number.");
            }
        }

        // Ask if the user wants to save the changes
        System.out.print("\nDo you want to save the changes? (Y/N): ");
        String decision = scan.nextLine().trim().toUpperCase();
        if (decision.equals("Y"))
        {
            Collections.sort(removeNumbers, Collections.reverseOrder());
            for (int removeIndex : removeNumbers)
            {
                tools.remove(removeIndex);
            }
            saveToFile(tools, false); // Save the updated list
        }
        else
        {
            System.out.println("Changes were not be saved.\n");
        }
    }
    
    /**
     * Creates a new list of tools or appends to an existing list.
     * @param append True to append to the current list, false to overwrite.
     */
    private void createNewList(boolean notReplaceAll)
    {
        tools = new ArrayList<String>();
        System.out.println("Enter tool's informations in the format (no space between comma): \"Tool's name,number of it which you took away,number of it which you took back\"");
        System.out.println("Type \"DONE\" when you are finished, or enter another tool's informations");
    
        while (true)
        {
            System.out.println("Enter tool's data (or \"DONE\" to stop): ");
            String input = scan.nextLine().trim();
    
            if (input.equalsIgnoreCase("DONE"))
            {
                break;
            }
    
            if (validateToolsData(input))
            {
                tools.add(input);
    
                System.out.println();
                loopAsterisk(input.length() + 24);
                System.out.println("\n* tool's information added: " + input + " *");
                loopAsterisk(input.length() + 24);
                System.out.println("\n");
            }
            else
            {
                System.out.println("Invalid format! Please enter the data again.");
            }
        }
    
        System.out.print("\nDo you want to save the changes? (Y/N): ");
        String decision = scan.nextLine().trim().toUpperCase();
        if (decision.equals("Y") && !tools.isEmpty())
        {
            saveToFile(tools, notReplaceAll);
        }
        else
        {
            System.out.println("Changes were not saved.\n");
            return;
        }
    }

    /**
     * Saves the tools list to a file.
     * @param data The list of tools to save.
     * @param append True to append to the file, false to overwrite.
     */
    private void saveToFile(ArrayList<String> data, boolean notReplaceData)
    {
        int i = 1;
        
        System.out.println("\nWhere do you want to save the file?\n");
        if(currentFile != null)
        {
            System.out.println("1: Save to current file (" + currentFile + ")\n");
            i++; //2
        }
        System.out.println(i + ": Save to a new location\n");
        i++;//2 or 3
        System.out.println(i + ": Cancel\n");
        i++;//3 or 4
        int choice = getValidIntInput("Choose one of them: ", 1, i - 1);
        
        String savePath = currentFile;
        
        if(choice == 2 && i == 3 || choice == 3 && i == 4)
        {
            return;//choose cancel
        }
    
        if (choice == 2 && i == 4 || choice == 1 && i == 3)
        {//choose new location
            try
            {
                javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
                fileChooser.setDialogTitle("Choose save location");
                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION)
                {
                    savePath = fileChooser.getSelectedFile().getAbsolutePath();
                    currentFile = savePath;
                }
                else
                {
                    System.out.println("\nSave operation canceled.");
                    return;
                }
            }
            catch (HeadlessException e)
            {
                System.out.println("\nGUI is not supported in this environment.");
            }
            catch (SecurityException e)
            {
                System.out.println("\nError: Insufficient permissions to access the file chooser.");
            }
        }
        
        try (PrintWriter output = new PrintWriter(new FileWriter(savePath, notReplaceData)))
        {
            for (String toolsName : data)
            {
                output.println(toolsName);
            }
            System.out.println("\nFile saved successfully to: " + savePath + "\n");
        }
        catch (IOException e)
        {
            System.out.println("\nError saving file: " + e.getMessage());
        }
    }

    /**
     * Use regular expressions to check whether the data string meets the format requirements
     */
    private boolean validateToolsData(String dataInput)
    {
        return dataInput.matches("[A-Za-z]+,[A-Za-z]+(,(100|[0-9]{1,2}))+");
    }

    /**
     * Validates and retrieves an integer input within a specific range.
     * @param prompt The prompt to display to the user.
     * @param min The minimum valid value.
     * @param max The maximum valid value.
     * @return A valid integer input within the range.
     */
    private int getValidIntInput(String prompt, int min, int max)
    { 
        int input;
        while (true)
        {
            try
            {
                System.out.print(prompt);
                input = Integer.parseInt(scan.nextLine().trim());
                if (input >= min && input <= max)
                {
                    return input;
                }
                System.out.print("Input must be between " + min + " and " + max);
            }
            catch (NumberFormatException e)
            {
                System.out.print("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays all tools.
     */
    private void FileReading(boolean listEighty)
    {
        System.out.println("\nNumber of total tools: " + tools.size());
        System.out.println("******************************");
        printToolsData();
        System.out.println("**************************************");
    }

    private void printToolsData()
    {

        for (int i = 0; i < tools.size(); i++)
        {
            String toolsDataInFile = tools.get(i);
            StringTokenizer tokenizer = new StringTokenizer(toolsDataInFile, ",");
            String toolName = tokenizer.nextToken();
            System.out.println("(" + (i + 1) + ") Tool's name: " + toolName);
            
            int moreTokensCount = 0;    
            while (tokenizer.hasMoreTokens())
            {
                moreTokensCount++;
                int number = Integer.parseInt(tokenizer.nextToken());
                if(moreTokensCount == 1)
                {
                    System.out.println("Take Away: " + number);
                }
                else
                {
                    System.out.println("Take Back: " + number);
                }
                
                
            }

            if(i == tools.size() - 1)
            {
                System.out.println("******************************");
            }
            else
            {
                System.out.println("******************************");
            }
        }
    }

    /**
     * Asks the if want to perform another action.
     * @return True if wants to continue, false otherwise.
     */
    private boolean doAgain()
    {
        System.out.println("Exit or do again?");
        System.out.println("1: Exit");
        System.out.println("2: Do again");
        int choice = getValidIntInput("Choose a number to continue: ", 1, 2);
        if(choice == 1)
        {
            System.out.println("Exit.");
            return false;
        }
        else if(choice == 2)
        {
            return true;
        }
        return false;
    }

    private void copyTheList(ArrayList<String> emptyList, ArrayList<String> contentList)
    {
        for(String data: contentList)
        {
            emptyList.add(data);
        }
    }

    private void copyTheList(ArrayList<Integer> emptyList, ArrayList<Integer> contentList)
    {
        for(Integer data: contentList)
        {
            emptyList.add(data);
        }
    }

    /**
     * Draws a line of asterisks for formatting.
     * @param count The number of asterisks to draw.
     */
    private void loopAsterisk(int loopNumber)
    {
        for(int i = 0; i < loopNumber; i++)
        {
            System.out.print("*");
        }
    }
}