import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.awt.HeadlessException;
import java.lang.NumberFormatException;

/**
 * This is a demo to manage tools during the competition.
 */
public class ManagementSystemDemo
{
    private Scanner scan;
    private String currentFile;
    private boolean menuShow;

    private ArrayList<String> tools, toolsName;
    private ArrayList<Integer> number_tools_take;
    private ArrayList<Integer> number_tools_back;

    public static final int numberOfDefaultToolsList = 20;
    private ArrayList<String> defaultTools = new ArrayList<String>(numberOfDefaultToolsList);
    private ArrayList<Integer> default_number_tools_take = new ArrayList<Integer>(numberOfDefaultToolsList);

    

    public static void main(String[] args)
    {
        ManagementSystemDemo system_demo = new ManagementSystemDemo();
        system_demo.toolManagement(); // starting here
    }

    private void initialDefaultList()
    {
        defaultTools.add("Screw driver");       default_number_tools_take.add(5);
        defaultTools.add("rancher");            default_number_tools_take.add(5);
        defaultTools.add("plier");              default_number_tools_take.add(4);
        defaultTools.add("zitiscutter");        default_number_tools_take.add(1);
        defaultTools.add("scissor");            default_number_tools_take.add(1);
        defaultTools.add("file");               default_number_tools_take.add(5);
        defaultTools.add("saw");                default_number_tools_take.add(1);
        defaultTools.add("Big drill");          default_number_tools_take.add(1);
        defaultTools.add("small drill");        default_number_tools_take.add(1);
        defaultTools.add("yellow cuter");       default_number_tools_take.add(2);
        defaultTools.add("Remote");             default_number_tools_take.add(1);
        defaultTools.add("switch");             default_number_tools_take.add(1);
        defaultTools.add("battery");            default_number_tools_take.add(10);
        defaultTools.add("green fan");          default_number_tools_take.add(2);
        defaultTools.add("air pump");           default_number_tools_take.add(1);
        defaultTools.add("Cart");               default_number_tools_take.add(1);
        defaultTools.add("Robot box");          default_number_tools_take.add(1);
        defaultTools.add("robot box cover");    default_number_tools_take.add(1);
        defaultTools.add("Power bar");          default_number_tools_take.add(1);
        defaultTools.add("Super cool");         default_number_tools_take.add(2);
        defaultTools.add("Tweezer");            default_number_tools_take.add(4);
    }

    /**
     * Main tool management system.
     * Handles user menu and interaction with other methods.
     */
    public void toolManagement()
    {
        initialDefaultList();
        do
        {
            System.out.println("\f"); // Clears the screen
            menuShow = true;
            currentFile = null;
            
            // Initialize the tool list and scanner for each menu loop
            tools = new ArrayList<String>(); // Initialize the tool list
            number_tools_take = new ArrayList<Integer>(tools.size());
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
        System.out.println("2: Change tools' information");
        System.out.println("3: Remove tools");
        System.out.println("4: List all tools");
        System.out.println("5: Compare the tools you took away and took back");
        System.out.println("6: Save");
        System.out.println("7: Exit");
        // Get a valid menu option from the user
        int choice = getValidIntInput("Enter your choice (1-7): ", 1, 7);
        
        if (choice == 1)
        {
            addTools(); // Call method to add tools
        }
        else if (choice == 2)
        {
            changeToolsData(); // Change the information of tools in the list
        }
        else if (choice == 3)
        {
            deleteTools(); // Call method to delete tools
        }
        else if (choice == 4)
        {
            FileReading(true); // Display tools list
        }
        else if(choice == 5)
        {
            compareData(tools); // Create a new list
        }
        else if(choice == 6)
        {
            saveToFile(tools, false); //Save to the current file or another file
        }
        else if (choice == 7)
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
        System.out.println("Choose \"Y\" to create a new list, \"N\" to load a list from file, \"D\" to use the default list from preset: ");
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
                    
                    getToolsName(tools);
                    getTakeNumber(tools);
                    getBackNumber(tools);
                }
                catch (FileNotFoundException e)
                {
                    System.out.print("Error: File not found.\n");
                }
            }
        }
        if(decision.equals("D"))
        {
            copyTheStringList(tools, defaultTools);
            copyTheIntList(number_tools_take, default_number_tools_take);
            System.out.println("\nSuccessfully use the default list");
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
        
        loopAsterisk(20);
        System.out.println();
        for(int i = 0; i < toolsList.size(); i++)
        {
            System.out.print("Number of " + toolsList.get(i) + " you took back: ");
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
        ArrayList<String> missingTools = new ArrayList<String>();
        ArrayList<Integer> missingToolsNumber = new ArrayList<Integer>();
        ArrayList<String> ExtraTools = new ArrayList<String>();
        ArrayList<Integer> ExtraToolsNumber = new ArrayList<Integer>();
        
        if(toolsList.isEmpty())
        {
            System.out.println("The tools list is empty. Please add tools first.\n");
            addTools();
        }
        if(number_tools_back.isEmpty())
        {
            System.out.println("The tools back list is empty. Please add tools first.\n");
            number_tools_back = new ArrayList<Integer>(toolsList.size());
            countToolsBack(toolsList);
        }
        
        System.out.println("\nComparison of tools taken away and brought back:");
        System.out.println("***********************************************");
        for(int i = 0; i < toolsList.size(); i++)
        {
            String toolName = toolsName.get(i);
            int tookAway = number_tools_take.get(i);
            int broughtBack = number_tools_back.get(i);
            int difference = tookAway - broughtBack;

            System.out.print("Tool: " + toolName);
            System.out.print("    Took Away: " + tookAway);
            System.out.println("    Brought Back: " + broughtBack);
            if(difference > 0)
            {
                missingTools.add(toolName);
                missingToolsNumber.add(difference);
                System.out.println("Missing: " + difference);
            }
            else if(difference < 0)
            {
                ExtraTools.add(toolName);
                ExtraToolsNumber.add(-difference);
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
                    System.out.println(toolName + " is backed.");
                }
            }
            System.out.println("-----------------------------------------------");
        }
        
        System.out.println("\nSummary: \n");
        if(!missingTools.isEmpty())
        {
            for(int i = 0; i < missingTools.size(); i++)
            {
                System.out.println(missingTools.get(i) + ": " + missingToolsNumber.get(i) + " missing");
            }
            
            System.out.println();
        }
        if(!ExtraTools.isEmpty())
        {
            for(int i = 0; i < ExtraTools.size(); i++)
            {
                System.out.println(ExtraTools.get(i) + ": " + ExtraToolsNumber.get(i) + " extra");
            }
            
            System.out.println();
        }
        if(missingTools.isEmpty() && ExtraTools.isEmpty())
        {
            System.out.println("Everything is back");
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
        String newToolData = "";
        tools = new ArrayList<String>();
        System.out.println("Enter tool's informations: ");// in the format (no space between comma): \"Tool's name,number that you took away,number that you took back\"");
        //System.out.println("example: \"Screw driver,5,5\" or \"Screw driver,5,\"");
        System.out.println("Type \"DONE\" when you are finished, or enter another tool's informations");
        
        while (true)
        {
            System.out.println("Enter \"START\" to start or \"DONE\" to stop: ");
            String input = scan.nextLine().trim();
    
            if(input.equalsIgnoreCase("DONE"))
            {
                break;
            }
            else if(input.equalsIgnoreCase("START"))
            {
                newToolData = getNewData();
                tools.add(newToolData);
                
                System.out.println();
                loopAsterisk(newToolData.length() + 24);
                System.out.println("\n* tool's information added: " + newToolData + " *");
                loopAsterisk(newToolData.length() + 24);
                System.out.println("\n");
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
    
    private String getNewData()
    {
        StringBuilder result = new StringBuilder();
        String newToolName, number = "";
        int toolNumber = 0;
        
        System.out.print("Tool's name: ");
        newToolName = scan.nextLine().trim();
        result.append(newToolName);
        
        for(int i = 0; i < 2; i++)
        {
            if(i == 0)
            {
                System.out.print("\nnumber that you took away: ");
            }
            else
            {
                System.out.println("\nnumber that you brought back: (0 indicates that nothing was brought back or not count yet.)");
            }
            try
            {
                number = scan.nextLine().trim();
                Double.parseDouble(number);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please input valid number!");
            }
            toolNumber = Integer.parseInt(number);
            result.append(",").append(number);
        }
        
        return result.toString();
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
    
    private void getToolsName(ArrayList<String> toolList)
    {
        toolsName = new ArrayList<String>();
        
        for (int i = 0; i < toolList.size(); i++)
        {
            String toolsDataInFile = tools.get(i);
            StringTokenizer tokenizer = new StringTokenizer(toolsDataInFile, ",");
            String toolName = tokenizer.nextToken();
            toolsName.add(toolName);
        }
    }
    
    private void getTakeNumber(ArrayList<String> toolList)
    {
        number_tools_take = new ArrayList<Integer>();
        String number;
        int takeNumber;
        
        for (int i = 0; i < toolList.size(); i++)
        {
            String toolsDataInFile = tools.get(i);
            StringTokenizer tokenizer = new StringTokenizer(toolsDataInFile, ",");
            String toolName = tokenizer.nextToken();
            
            int numberOfToolTake = 0;
            try
            {
                number = tokenizer.nextToken();
                Double.parseDouble(number);
                takeNumber = Integer.parseInt(number);
                numberOfToolTake = takeNumber;//replace the number until the end, which is exactly the number of tool back
            }
            catch (NumberFormatException e)
            {
                System.out.println(e);
                System.out.println("Please check the file, make sure the format is in \"name,number,number\"");
                return;
            }
            number_tools_take.add(numberOfToolTake);
        }
    }
    
    private void getBackNumber(ArrayList<String> toolList)
    {
        number_tools_back = new ArrayList<Integer>();
        String number;
        int backNumber;
        
        for (int i = 0; i < toolList.size(); i++)
        {
            String toolsDataInFile = tools.get(i);
            StringTokenizer tokenizer = new StringTokenizer(toolsDataInFile, ",");
            String toolName = tokenizer.nextToken();
            
            int numberOfToolBack = 0;
            while (tokenizer.hasMoreTokens())
            {
                try
                {
                    number = tokenizer.nextToken();
                    Double.parseDouble(number);
                    backNumber = Integer.parseInt(number);
                    numberOfToolBack = backNumber;//replace the number until the end, which is exactly the number of tool back
                }
                catch (NumberFormatException e)
                {
                    System.out.println(e);
                    System.out.println("Please check the file, make sure the format is in \"name,number,number\"");
                    return;
                }
            }
            number_tools_back.add(numberOfToolBack);
        }
    }
    
    private void changeToolsData()
    {
        StringBuilder newData = new StringBuilder();
        String newName, newNumber;
        
        printToolsData();
        
        while(true)
        {
            System.out.println("Which tool's data do you want to change, input \"0\" to finish at anywhere: ");
            int toolChoice = getValidIntInput("Enter your choice (1-" + tools.size() + "): ", 0, tools.size());
            if(toolChoice == 0)
            {
                break;
            }
            
            System.out.println("Please start changing, input \"DONE\" to finish at anywhere");
            for(int i = 0; i < 3; i++)
            {
                if(i == 0)
                {
                    System.out.print("New tool's name: ");
                    newName = scan.nextLine().trim();
                    if (newName.equalsIgnoreCase("DONE"))
                    {
                        break;
                    }
                    newData.append(newName);
                }
                if(i > 0)
                {
                    if(i == 1)
                    {
                        System.out.print("\nNumber you took away: ");
                    }
                    if(i == 2)
                    {
                        System.out.println("\nNumber you brought back");
                    }
                    try
                    {
                        newNumber = scan.nextLine().trim();
                        if (newNumber.equalsIgnoreCase("DONE"))
                        {
                            break;
                        }
                        Double.parseDouble(newNumber);
                        newData.append(",").append(newNumber);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Please input valid number!");
                    }
                }
            }
            
            tools.set(toolChoice - 1, newData.toString());
        }
        
        saveToFile(tools, false);
    }
    
    private void copyTheStringList(ArrayList<String> emptyList, ArrayList<String> contentList)
    {
        for(String data: contentList)
        {
            emptyList.add(data);
        }
    }

    private void copyTheIntList(ArrayList<Integer> emptyList, ArrayList<Integer> contentList)
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