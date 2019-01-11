/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shahbazw100msimulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Wais Shahbaz
 */
public class ShahbazW100mSimulation {

    /**
     * @param args the command line arguments
     */
    //Core GUI variables
    private JFrame frame;
    private Container contentPane;
    private JPanel centralPanel;
    private JMenuBar menuBar;

    //buttons
    private JButton raceStarterButton;
    private JButton addToHeatButton;

    //radio buttons 
    //for character selections
    private JRadioButton shaqButton;
    private JRadioButton FlamingoButton;
    private JRadioButton popButton;
    private JRadioButton waisButton;
    private ButtonGroup runnerButtonGroup;
    //for accessory selections
    private JRadioButton textbookButton;
    private JRadioButton liftingBeltButton;
    private JRadioButton cheerleaderButton;
    private JRadioButton rageButton;
    private JRadioButton daBoisButton;
    private JRadioButton chickenButton;
    private ButtonGroup accessoryButtonGroup;

    //textFields - to get text inputs
    //runner's name
    private JTextField runnerNameText;
    //runner's stats
    private JTextField speedText;
    private JTextField accelerationText;
    private JTextField staminaText;
    private JTextField reactionsText;

    //text areas to display names and results
    private JTextArea raceResultsText;
    private JTextArea runnerHeatNamesText;

    //scroll panes added to compensate for extended inputs
    private JScrollPane rnnerHeatScrollArea;
    private JScrollPane rnnerResultScrollArea;

    //array lists to hold data for x number of runners
    //one current array, one previous array (for keeping data on additional functions)
    private ArrayList<ShahbazWRunner> runnerHeat = new ArrayList<ShahbazWRunner>();
    private ArrayList<ShahbazWRunner> previousHeat = new ArrayList<ShahbazWRunner>();

    //printing and clearing file variables
    String savedRunners = "";
    String savedHeats = "";
    PrintStream oFile, clearFile;

    public static void main(String[] args) {
        //initialize gui object and start appilcation
        ShahbazW100mSimulation raceSim = new ShahbazW100mSimulation();
        raceSim.start();
    }

    /**
     * this method initializes the GUI - sets frame, calls makeMenus and
     * makeContent methods pre: none post: a working GUI application
     */
    public void start() {

        //clear text files from previous session
        clearPreviousRunners();

        //title
        frame = new JFrame("100m Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        //make menus in this method
        makeMenus();
        //make the content in the window in this method
        makeContent();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    //this method calls the seperate menu tabs to be created with thier own methods
    private void makeMenus() {
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        makeFileMenu();
        makeHelpMenu();

    }

    /**
     * this method creates the File Menu tab, complete with a new, save runner,
     * save heat, and exit tabs pre: none post: complete file menu with
     * additional File tabs and hotkeys and shortcuts
     */
    public void makeFileMenu() {
        //set up the File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        //new race tab
        JMenuItem newItem = new JMenuItem("New Race");
        newItem.addActionListener(new newListener());
        newItem.setMnemonic(KeyEvent.VK_N);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        fileMenu.add(newItem);

        //save runner tab
        JMenuItem saveRunnerItem = new JMenuItem("Save Runner");
        saveRunnerItem.addActionListener(new saveRunnerListener());
        saveRunnerItem.setMnemonic(KeyEvent.VK_S);
        saveRunnerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        fileMenu.add(saveRunnerItem);

        //save heat tab
        JMenuItem saveHeatItem = new JMenuItem("Save Heat");
        saveHeatItem.addActionListener(new saveHeatListener());
        saveHeatItem.setMnemonic(KeyEvent.VK_A);
        saveHeatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        fileMenu.add(saveHeatItem);

        //line seperator to make menu look clean
        fileMenu.addSeparator();

        //exit tab
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new exitListener());
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
        fileMenu.add(exitItem);
    }

    /**
     * creates the help menu tab on the menu bar pre: none post: complete help
     * menu tab, with instructions, runner/item details, and game specifics tab
     */
    public void makeHelpMenu() {
        //set up Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);

        //instructions tab
        JMenuItem InstructionsItem = new JMenuItem("Instructions");
        InstructionsItem.addActionListener(new instructionsListener());
        InstructionsItem.setMnemonic(KeyEvent.VK_I);
        InstructionsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
        helpMenu.add(InstructionsItem);

        //runner details tab
        JMenuItem characterDetailsItem = new JMenuItem("Runner Details");
        characterDetailsItem.addActionListener(new runnerDetailsListener());
        characterDetailsItem.setMnemonic(KeyEvent.VK_R);
        characterDetailsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        helpMenu.add(characterDetailsItem);

        //items details tab
        JMenuItem itemDetailsItem = new JMenuItem("Item Details");
        itemDetailsItem.addActionListener(new itemDetailsListener());
        itemDetailsItem.setMnemonic(KeyEvent.VK_D);
        itemDetailsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        helpMenu.add(itemDetailsItem);

        //specifics tab
        JMenuItem specificsItem = new JMenuItem("Game Specifics");
        specificsItem.addActionListener(new specificsListener());
        specificsItem.setMnemonic(KeyEvent.VK_G);
        specificsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
        helpMenu.add(specificsItem);

        //add a line seperator to make menu look clean
        helpMenu.addSeparator();

        //about tab
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new aboutListener());
        aboutItem.setMnemonic(KeyEvent.VK_Y);
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
        helpMenu.add(aboutItem);
    }

    /**
     * creates the content on the window frame pre: none post: complete GUI,
     * with ability to function
     */
    public void makeContent() {
        //set up the content in the window
        contentPane = (JPanel) frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));

        //each region has its own method for organization
        makeNorthRegion();
        makeWestRegion();
        makeCentralRegion();
        makeEastRegion();
        makeSouthRegion();

    }

    /**
     * North region of GUI, containing a picture and a texfield for runner queue
     * pre: none post: completed North Region of GUI
     */
    public void makeNorthRegion() {
        //create panel to add items to
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        //add image to panel
        JLabel imgLabel = new JLabel(new ImageIcon("runners.png"), JLabel.CENTER);
        imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 200));
        panel.add(imgLabel);

        //add textfield of runner queue into panel
        runnerHeatNamesText = new JTextArea("Runners in this heat:\n");
        runnerHeatNamesText.setEditable(false);
        runnerHeatNamesText.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        runnerHeatNamesText.setMaximumSize(new Dimension(420, 300));
        panel.add(runnerHeatNamesText);

        //add scroll features to textfield
        rnnerHeatScrollArea = new JScrollPane(runnerHeatNamesText);
        rnnerHeatScrollArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rnnerHeatScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(rnnerHeatScrollArea);

        contentPane.add(panel, BorderLayout.NORTH);
    }

    /**
     * South region of GUI, containing two buttons - one to add runner to queue
     * and another to start the race pre: none post: completed South Region of
     * GUI
     */
    public void makeSouthRegion() {
        JPanel panel = new JPanel();

        //create the Add to Heat button
        addToHeatButton = new JButton("ADD TO HEAT");
        addToHeatButton.setAlignmentX(panel.CENTER_ALIGNMENT);
        addToHeatButton.setPreferredSize(new Dimension(450, 70));
        addToHeatButton.addActionListener(new createRunnerListener());
        panel.add(addToHeatButton);

        //create the Start the Race button
        raceStarterButton = new JButton("START THE RACE");
        raceStarterButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        raceStarterButton.setPreferredSize(new Dimension(700, 70));
        raceStarterButton.addActionListener(new startRaceListener());
        panel.add(raceStarterButton);
        contentPane.add(panel, BorderLayout.SOUTH);
    }

    /**
     * West region of GUI with name selection and character selection pre: none
     * post: completed West Region, containing a name textfield and a character
     * selection radio button group
     */
    public void makeWestRegion() {
        //make the panel to put components in
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Customize your Runner!"));
        panel.setPreferredSize(new Dimension(400, 0));

        //reusable panel to format components
        JPanel smallPanel = new JPanel();

        //name textfield and header
        smallPanel.add(new JLabel("Name "));
        smallPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        runnerNameText = new JTextField("", 20);
        runnerNameText.setMaximumSize(new Dimension(50, 24));
        smallPanel.add(runnerNameText);
        panel.add(smallPanel);

        //panel to display title for character selection
        smallPanel = new JPanel();
        smallPanel.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
        JLabel runnerTitle = new JLabel("Choose your Runner!");
        runnerTitle.setFont(new Font("Serif", Font.BOLD, 20));
        smallPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        smallPanel.add(runnerTitle);
        panel.add(smallPanel);

        //create button group for character radio buttons and resuable imgLabel JLabel for pictures
        runnerButtonGroup = new ButtonGroup();
        JLabel imgLabel = new JLabel();

        //shaq radio button and picture
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        shaqButton = new JRadioButton("Shaqtus", true);
        runnerButtonGroup.add(shaqButton);
        smallPanel.add(shaqButton);
        smallPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        imgLabel = new JLabel(new ImageIcon("shaq.jpg"), JLabel.RIGHT);
        smallPanel.add(imgLabel);
        panel.add(smallPanel);

        //Mr.Popeye radio button and picture
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        popButton = new JRadioButton("Owe-In-A-Lot-Of-Marks Popeye", true);
        runnerButtonGroup.add(popButton);
        smallPanel.add(popButton);
        smallPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        imgLabel = new JLabel(new ImageIcon("popeye.jpg"), JLabel.RIGHT);
        smallPanel.add(imgLabel);
        panel.add(smallPanel);

        //Mr.Flamingo radio button and picture
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        FlamingoButton = new JRadioButton("Vittorious Flamingo", true);
        runnerButtonGroup.add(FlamingoButton);
        smallPanel.add(FlamingoButton);
        smallPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        imgLabel = new JLabel(new ImageIcon("flamingo.jpg"), JLabel.RIGHT);
        smallPanel.add(imgLabel);
        panel.add(smallPanel);

        //Boss radio button and picture
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        waisButton = new JRadioButton("Dab0$$", true);
        runnerButtonGroup.add(waisButton);
        smallPanel.add(waisButton);
        smallPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        imgLabel = new JLabel(new ImageIcon("boss.jpg"), JLabel.RIGHT);
        smallPanel.add(imgLabel);
        panel.add(smallPanel);

        contentPane.add(panel, BorderLayout.WEST);
    }

    /**
     * Central region of GUI; choose stats (as text inputs) and accessory (as
     * radio button) pre: none post: completed Central Region, containing text
     * inputs and header
     */
    public void makeCentralRegion() {
        //panel to hold multiple additional panels and format them
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Choose your Skills!"));
        panel.setPreferredSize(new Dimension(200, 0));

        //header for section
        JLabel instructions = new JLabel("<html> Choose your stats with 20 total skill points <br> Maximum 10 points per category<html>");
        panel.add(instructions);

        //speed stat textfield and label
        JPanel smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        speedText = new JTextField("", 2);
        speedText.setMaximumSize(new Dimension(30, 24));
        smallPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        smallPanel.add(speedText);
        smallPanel.add(new JLabel(" Speed (Maximum Speed)"));
        smallPanel.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
        panel.add(smallPanel);

        //acceleration stat textfield and label
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        accelerationText = new JTextField("", 2);
        accelerationText.setMaximumSize(new Dimension(30, 24));
        smallPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        smallPanel.add(accelerationText);
        smallPanel.add(new JLabel(" Acceleration (Rate at Which Speed Increases)"));
        smallPanel.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
        panel.add(smallPanel);

        //stamina stat textfield and label
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        staminaText = new JTextField("", 2);
        staminaText.setMaximumSize(new Dimension(30, 24));
        smallPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        smallPanel.add(staminaText);
        smallPanel.add(new JLabel(" Stamina (Decelleration Factor)"));
        smallPanel.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
        panel.add(smallPanel);

        //reactions stat textfield and label
        smallPanel = new JPanel();
        smallPanel.setLayout(new BoxLayout(smallPanel, BoxLayout.X_AXIS));
        reactionsText = new JTextField("", 2);
        reactionsText.setMaximumSize(new Dimension(30, 24));
        smallPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        smallPanel.add(reactionsText);
        smallPanel.add(new JLabel(" Reactions (Time Delay to Start Running)"));
        smallPanel.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
        panel.add(smallPanel);

        //title for accessory selection list
        JLabel accessTitle = new JLabel("Pick One of the Following Accessories: ");
        accessTitle.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
        panel.add(accessTitle);

        //add all of the accessory buttons to the panel
        accessoryButtonGroup = new ButtonGroup();
        //calculus textbook button
        textbookButton = new JRadioButton("Mr.Flamingo's Calculus Textbook", true);
        textbookButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accessoryButtonGroup.add(textbookButton);
        panel.add(textbookButton);
        //lifting belt button
        liftingBeltButton = new JRadioButton("Mr.Popeye's Lifting Belt", false);
        liftingBeltButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accessoryButtonGroup.add(liftingBeltButton);
        panel.add(liftingBeltButton);
        //cheerleader button
        cheerleaderButton = new JRadioButton("Male Cheerleader", false);
        cheerleaderButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accessoryButtonGroup.add(cheerleaderButton);
        panel.add(cheerleaderButton);
        //gummies button
        rageButton = new JRadioButton("DaBawss' Rage Gummies", false);
        rageButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accessoryButtonGroup.add(rageButton);
        panel.add(rageButton);
        //da bois button
        daBoisButton = new JRadioButton("Da Bois", false);
        daBoisButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accessoryButtonGroup.add(daBoisButton);
        panel.add(daBoisButton);
        //kentucky fried chicken button
        chickenButton = new JRadioButton("Kentucky Fried Chicken", false);
        chickenButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        accessoryButtonGroup.add(chickenButton);
        panel.add(chickenButton);

        contentPane.add(panel, BorderLayout.CENTER);
    }

    /**
     * East region of GUI; displays the results of each race in a textArea pre:
     * none post: completed East Region, containing TextArea, with a header, to
     * display race results
     */
    public void makeEastRegion() {
        //the panel that holds other components and title
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Race Results: "));
        panel.setPreferredSize(new Dimension(400, 0));

        //create the textArea, make it uneditable, and add to panel
        raceResultsText = new JTextArea("\n");
        raceResultsText.setEditable(false);
        panel.add(raceResultsText);

        //add scrolling capability to the Text Area
        rnnerResultScrollArea = new JScrollPane(raceResultsText);
        rnnerResultScrollArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rnnerResultScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(rnnerResultScrollArea);

        contentPane.add(panel, BorderLayout.EAST);
    }

    /**
     * arranges the runners' time from lowest to highest pre: none post:
     * runnerHeat array list has objects sorted from lowest time as position 0
     * to highest time at position x
     */
    public void arrangeTimes() {
        for (int i = 0; i < runnerHeat.size(); i++) {
            for (int j = i; j < runnerHeat.size(); j++) {
                //check if the current position runner has a lower time than the next runner using an overriden compareTo method
                if (runnerHeat.get(j).compareTo(runnerHeat.get(i)) < 0) {
                    //if true, swap elements in the array list
                    ShahbazWRunner tempRunner = runnerHeat.get(i);
                    runnerHeat.set(i, runnerHeat.get(j));
                    runnerHeat.set(j, tempRunner);
                }
            }
        }
    }

    /**
     * clears the runner and heats text files pre: none post: runners.txt and
     * heats.txt are clear
     */
    public void clearPreviousRunners() {
        //this method clears the order txt file
        try {
            //clear runners.txt
            clearFile = new PrintStream("runners.txt");
            clearFile.print(savedRunners);
            clearFile.close();
            //clear heats.txt
            clearFile = new PrintStream("heats.txt");
            clearFile.print(savedHeats);
            clearFile.close();
        } catch (IOException e) {
            System.out.println("***** I/O Error ****\r\n" + e);
        }
    }

    /**
     * clears all selection choices and the runner queue and the runnerHeat
     * arrayist, but leaves race results text area as is pre: none post:
     * everything on screen is clear except for the race results text area
     */
    public void clearAllButResults() {
        //this method clears textfields and presses default radio buttons
        clearStats();

        //clear runner queue text area - set it to only the title
        runnerHeatNamesText.setText("Runners in this Heat:\n");

        //clear the runners array list
        runnerHeat.clear();
    }

    /**
     * clears all selection choices and sets radio buttons to default picks pre:
     * none post: cleared textfields for user input, and radio buttons on
     * default picks
     */
    public void clearStats() {
        //set textfields clear
        runnerNameText.setText("");
        speedText.setText("");
        accelerationText.setText("");
        staminaText.setText("");
        reactionsText.setText("");

        //have default radio buttons selected
        shaqButton.setSelected(true);
        textbookButton.setSelected(true);

    }

    //this class is used for the instructions menue item
    private class instructionsListener implements ActionListener {

        //string containing instructions
        private String helpText = "Intructions:\n"
                + "1. Enter your name of your runner\n"
                + "2. Pick your runner character\n"
                + "3. Choose your skills (follow the specified rules):\n"
                + "    a. Enter a number between 1-10 for each skill\n"
                + "    b. You must ensure that the 4 categories add up to 20\n"
                + "4. Press 'Add to Heat' to update runner queue\n"
                + "5. Repeat from step 1 if adding more runners. If not, proceed to step 6\n"
                + "6. Press 'Start the Race' to simulate the race results\n"
                + "7. Save runners, save the heat, start a new race, or exit the program";

        //this method displays the instructions in a help menu when the instructions menu item is clicked
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, helpText,
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //this class is used for the about menu item
    private class aboutListener implements ActionListener {

        //string containing about information
        private String helpText = "Wais Shahbaz's 100m Simulator\n\n"
                + "Version 10.19\n\n\n"
                + "(c)Copywright Wais Shahbaz 2018\nAll Rights Reserved\n\n"
                + "Extremely complex 100m simulation algoritm to yield accurate results.\n"
                + "Test your knowledge of running fundamentals with this interactive simulator.\n\n\n";

        //this method displays the about information when the about menu item is clicked
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, helpText,
                    "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //this class is used for the item details menu item
    private class itemDetailsListener implements ActionListener {

        //string containing item details 
        private String helpText = "Item Details\n\n"
                + "Mr.Flamingo's Textbook: really icreases reactions. FOR MATTTTTHHHHHH\n"
                + "Mr.Popeye's lifting belt: stronger thrust, but less stamina and reactions. BUFF BUFF BUFF\n"
                + "Male Cheerleader: less speed, more acceleration and stamina. They really get you going!\n"
                + "DaBawss' Rage Gummies: A lot more stamina, DaBawss' secret to everlasting ragequits\n"
                + "Da Bois: More speed and acceleration, less stamina and reactions. It's furDaBois\n"
                + "Kentucky Fried Chicken: A lot more speed, a lot less stamina and reactions. \n"
                + "    Premium South Louisiana Cayenne Spiced Deep Fried Kentucky Chicken. It's Finger Licking Good!\n";

        //this method displays the item details when the item details menu item is clicked
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, helpText,
                    "Item Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //this class is used for the game specifics menu item
    private class specificsListener implements ActionListener {

        //string containing specifics' details 
        private String helpText = "Game Specifics\n\n"
                + "Stats Conversion:\n"
                + "Speed = 13 + 0.5 * Speed Points\n"
                + "Acceleration = 1.95 + 0.04 * Acceleration Points\n"
                + "Stamina = 0.5 - 0.04 * Stamina Points\n"
                + "Reactions = 0.71 - 0.075 * Reactions Points \n\n"
                + "Character Specifics: \n"
                + "Shaqtus: +2 Speed, -1 Acceleration, -2 Stamina, -1 Reactions\n"
                + "Owe-In-A-Lot-Of-Marks Popeye: +1 Acceleration, +1 Stamina, -1 Reactions\n"
                + "Mr.Flamingo: (undefined)\n"
                + "DaBawss: +1 Speed, -1 Stamina, -2 Reactions\n\n"
                + "Item Specifics:\n"
                + "Mr.Flamingo's textbook: +2 Reactions \n"
                + "Mr.Popeye's lifting belt: +1 Acceleration, -1 Stamina, -1 Reactions\n"
                + "Male Cheerleader: -1 Speed, +1 Acceleration, +1 Stamina\n"
                + "DaBawss' rage gummies: +2 Stamina\n"
                + "Da Bois: +1 Speed & Acceleration, -2 Stamina & Reactions\n"
                + "Kentucky Fried Chicken: +2 Speed, -3 Stamina, -1 Reactions";

        //this method displays the specifics' details when the game specifics menu item is clicked
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, helpText,
                    "Game Specifics", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //this class is used for the runner details menu item
    private class runnerDetailsListener implements ActionListener {

        //string containing runners details 
        private String helpText = "Runner Details\n\n"
                + "Shaqtus: He's a big man with attidude. Loves to run. Loves to eat.\n"
                + "Owe-In-A-Lot-Of-Marks Popeye: The stingiest bonus marker in WO. Secretely watches sports videos during class\n"
                + "Vittorious Flamingo: He's slow to react. Just like Italy was during qualifiers...\n"
                + "DaBawss: He's a ragequitter.";

        //this method displays the runners' details when the runner details menu item is clicked
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, helpText,
                    "Runner Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //this class is used for the new race menu item
    private class newListener implements ActionListener {

        //this listener is called when user presses the new Race menu item
        public void actionPerformed(ActionEvent e) {
            //set everything to how it was before, and clear arrayList
            //this method clears the textfields and chooses default buttons
            clearStats();

            //set labels to clear or default text
            raceResultsText.setText("");
            runnerHeatNamesText.setText("Runners in this Heat:\n");

            //clear array list of runners
            runnerHeat.clear();
        }
    }

    //this class is used for the exit menu item
    private class exitListener implements ActionListener {

        //this listener is called when user exits progrm, using the program's exit menu button
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    //this class is used to create a new runner when the Add to Head button is clickeed
    private class createRunnerListener implements ActionListener {

        //this listener creates a runner object and adds it to the runnerHeat Arraylist
        public void actionPerformed(ActionEvent e) {

            //boolean variable to keep track if the user entered all required fields of runner
            boolean realRunner = true;
            //variables holding data for textfields
            String name = "", character = "", access = "";
            //variables for stats
            int speed = 0;
            int accel = 0;
            int stamina = 0;
            int reactions = 0;

            //get name of runner; display error message if there's nothing and set RealRunner to false
            if (runnerNameText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter a name for your runner", "Name Error", JOptionPane.ERROR_MESSAGE);
                realRunner = false;
            } else {
                name = runnerNameText.getText();
            }

            /**
             * try getting an integer out of each stats textfield if there is an
             * input that isn't a number from 1-10, or the total of the four
             * textfields isn't 20; display an error message and set RealRunner
             * to false otherwise set each stat as its entered value in the text
             * field
             *
             */
            try {
                //try getting each stat from user input
                if (!speedText.getText().isEmpty()) {
                    speed = Integer.parseInt(speedText.getText());
                }
                if (!accelerationText.getText().isEmpty()) {
                    accel = Integer.parseInt(accelerationText.getText());
                }
                if (!staminaText.getText().isEmpty()) {
                    stamina = Integer.parseInt(staminaText.getText());
                }
                if (!reactionsText.getText().isEmpty()) {
                    reactions = Integer.parseInt(reactionsText.getText());
                }

                //go over possible errors and give feedback on user for that error
                if (speed > 10 || accel > 10 || stamina > 10 || reactions > 10 || speed<0 || accel <0 || stamina<0 || reactions<0) {
                    JOptionPane.showMessageDialog(frame,
                            "Runner stats must be numbers from 1-10, \nand must be whole numbers",
                            "Runner Stats Error", JOptionPane.ERROR_MESSAGE);
                    realRunner = false;
                }
                if (speed + accel + stamina + reactions > 20) {
                    JOptionPane.showMessageDialog(frame,
                            "Total skill points used cannot exceed 20",
                            "Runner Stats Order Error", JOptionPane.ERROR_MESSAGE);
                    realRunner = false;
                } else if (speed + accel + stamina + reactions != 20) {
                    JOptionPane.showMessageDialog(frame,
                            "You must use all 20 skill points",
                            "Runner Stats Order Error", JOptionPane.ERROR_MESSAGE);
                    realRunner = false;
                }

            } //catch if the user enters a string other than a number
            catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame,
                        "Runner stats must be numbers from 1-10, \nand must be whole numbers",
                        "Runner Stats Error", JOptionPane.ERROR_MESSAGE);
                realRunner = false;
            }

            //if all the conditions of the runner are true, and realRunner remains true, create a runner object with the data so far
            if (realRunner) {
                ShahbazWRunner tempRunner = new ShahbazWRunner(name, speed, accel, stamina, reactions);

                //get the runner character that they chose, and update their stats based off character
                //Shaq
                if (shaqButton.isSelected()) {
                    character = "Shaqtus";
                    speed += 2;
                    stamina -= 2;
                    accel -= 1;
                    reactions -= 1;
                } //Mr.Popeye
                else if (popButton.isSelected()) {
                    character = "Owe-In-A-Lot-Of-Marks Popeye";
                    accel += 1;
                    stamina += 1;
                    reactions -= 1;
                } //Mr.Flamingo
                else if (FlamingoButton.isSelected()) {
                    character = "Vittorious FolinDawg";
                    reactions -= 20;
                } //Wais
                else if (waisButton.isSelected()) {
                    character = "Dab0$$";
                    speed += 1;
                    stamina -= 1;
                    reactions -= 2;
                } //if user doesn't select a runner, final fail safe makes realRunner false and displays error message
                else {
                    JOptionPane.showMessageDialog(frame, "You must select a runner!",
                            "Runner Type Error", JOptionPane.ERROR_MESSAGE);
                    realRunner = false;
                }

                //get the accessory chosen: update its respective skill, and record the accessory
                //calculus textbook
                if (textbookButton.isSelected()) {
                    access = "Flamingo's Calculus Textbook";
                    reactions += 2;
                } //lifting belt
                else if (liftingBeltButton.isSelected()) {
                    access = "Mr.Popeye's Lifting Belt";
                    accel += 1;
                    stamina -= 1;
                    reactions -= 1;
                } //male cheerleader
                else if (cheerleaderButton.isSelected()) {
                    access = "Male Cheerleader";
                    speed -= 1;
                    accel += 1;
                    stamina += 1;
                } //rage gummies
                else if (rageButton.isSelected()) {
                    access = "DaBawss' Rage Gummies";
                    stamina += 2;
                } //da bois
                else if (daBoisButton.isSelected()) {
                    access = "Da Bois";
                    speed += 1;
                    accel += 1;
                    stamina -= 2;
                    reactions -= 2;
                } //kentucky fried chicken
                else if (chickenButton.isSelected()) {
                    access = "Kentucky Fried Chicken";
                    speed += 2;
                    stamina -= 3;
                    reactions -= 1;
                } //as a final fail safe, make realRunner false and display an error message
                else {
                    JOptionPane.showMessageDialog(frame, "You must select an accessory!",
                            "Acessory Type Error", JOptionPane.ERROR_MESSAGE);
                    realRunner = false;
                }

                //if the user entered information is still valid, proceed to finalize the runner and then add them to runnerHeat arraylist
                if (realRunner) {
                    //finalize the runner; update his stats, put in his character and accessory information
                    tempRunner.finalizeRunner(character, access, speed, accel, stamina, reactions);
                    //get runner's name and add it to runner heat textfield (the runner queue)
                    String result = tempRunner.getName();
                    runnerHeatNamesText.append(result + "\n");
                    //add runner to runnerHeat array list
                    runnerHeat.add(tempRunner);
                    //clear the selection portion of the screen to make it simpler to create new runners
                    clearStats();
                }

            }
        }
    }

    //this class is used to start the race simulation, arranging the runnners from lowest time to highest time and displaying it in results textArea
    private class startRaceListener implements ActionListener {

        //this listener is called when the user starts the race; when the Start the Race button is clocked
        public void actionPerformed(ActionEvent e) {
            //order racers from lowest time to highest time. Announce the lowest time as winner.
            arrangeTimes();
            //create a string that displays all the runners frm lowest time to highest time
            String runnerResults = "The " + runnerHeat.size() + " Man Heat: \n";
            int i = 1;
            for (ShahbazWRunner runner : runnerHeat) {
                //use the Runner class's toStringRaceResult method to generate desired string 
                runnerResults += "Ranking " + i + ": " + runner.toStringRaceResult() + "\n";
                i++;
            }
            //set the text in the results textArea as the string created from the resutls
            raceResultsText.setText(runnerResults);

            //save the runners into a previous heat list and clear the screen
            previousHeat = (ArrayList<ShahbazWRunner>) runnerHeat.clone();
            //clear everything except for results
            clearAllButResults();
        }
    }

    //this class is used to save a runner's profile to the runner.txt file
    private class saveRunnerListener implements ActionListener {

        //this listener is called when user chooses to save a runner
        public void actionPerformed(ActionEvent e) {
            //string holding search
            String searchText;
            //get the runner that the user desires to save from an input pop-up screen
            searchText = JOptionPane.showInputDialog(
                    frame,
                    "Which runner would you like to save? ",
                    "Input Dialog",
                    JOptionPane.QUESTION_MESSAGE);

            //this variable declares if the runner has been found or not
            boolean realRunner = false;
            //check if user entered a search before searching
            if (searchText != null) {
                //check if it's a real runner by checking every element in the arraylist and comparing their first names
                for (int i = 0; i < previousHeat.size(); i++) {
                    if (previousHeat.get(i).getName().equalsIgnoreCase(searchText)) {
                        //if the user is found, set realRunner to true, and add that runner's profile to the savedRunners string
                        realRunner = true;
                        savedRunners += previousHeat.get(i).toFileResult() + "\r\n";

                        //open runners.txt and update it with recently saved runner
                        try {
                            oFile = new PrintStream("runners.txt");
                            oFile.print(savedRunners);
                        } catch (IOException u) {
                            System.out.println("***** I/O Error ****\r\n" + u);
                        }
                    }
                    //notifiy the user if the runner they searched for is real, and exit the loop if the runner exists and is found
                    //otherwise, notify the user that they have entered a user that does not exist
                    if (realRunner) {
                        //display message saying save was successful
                        JOptionPane.showMessageDialog(frame, "Runner Successfully Saved",
                                "Runner File Save", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
                if (!realRunner) {
                    //if loop doesn't exit, that means that the runner was not found
                    JOptionPane.showMessageDialog(frame, "Runner could not be found",
                            "Runner Search Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    //this class is used to save a heat of runners' profiles to the heats.txt file
    private class saveHeatListener implements ActionListener {

        //string containing the heats of runners' data
        String runnerHeatData = "";
        //this listener is called when user chooses to save a heat

        public void actionPerformed(ActionEvent e) {

            //update the string of saved heats by adding every runner's information into that string
            for (ShahbazWRunner runner : previousHeat) {
                runnerHeatData += runner.toFileResult() + "\r\n";
            }

            //update the heats.txt file with the updated string
            try {
                oFile = new PrintStream("heats.txt");
                oFile.print(runnerHeatData);
            } catch (IOException u) {
                System.out.println("***** I/O Error ****\r\n" + u);
            }

            //display message saying save was successful
            JOptionPane.showMessageDialog(frame, "Heat Successfully Saved",
                    "Heat File Save", JOptionPane.INFORMATION_MESSAGE);
        }

    }
}
