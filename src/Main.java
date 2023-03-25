import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame implements ActionListener {

    private Screen mainPanel;
    public static ArrayList<String> industryList;
    public static ArrayList<String> stateList;
    public static ArrayList<String> monthList;
    public static ArrayList<Integer> january22Data;
    public static ArrayList<Integer> february22Data;
    public static ArrayList<Integer> march22Data;
    public static ArrayList<Integer> april22Data;
    public static ArrayList<Integer> may22Data;
    public static ArrayList<Integer> june22Data;
    public static ArrayList<Integer> july22Data;
    public static ArrayList<Integer> august22Data;
    public static ArrayList<Integer> september22Data;
    public static ArrayList<Integer> october22Data;
    public static ArrayList<Integer> november22Data;
    public static ArrayList<Integer> december22Data;
    public static ArrayList<Integer> january23Data;


    private JMenuBar createMenu() {
        // Create menu bar
        JMenuBar mb = new JMenuBar();

        // Create menu items
        JMenu category = new JMenu("Category");

        // Create menu bar items
        JMenuItem industry = new JMenuItem("Industry");
        industry.addActionListener(this);
        industry.setActionCommand("Industry");
        JMenuItem state = new JMenuItem("State");
        state.addActionListener(this);
        state.setActionCommand("State");

        // Assign menu bar items in proper menu items
        category.add(industry);
        category.add(state);

        // Assign menu items to menu bar
        mb.add(category);

        return mb;
    }

    public Main() {
        JMenuBar menu = createMenu();
        setJMenuBar(menu);

        mainPanel = new Screen();
        stateList = new ArrayList<String>();
        industryList = new ArrayList<String>();
        january22Data = new ArrayList<Integer>();
        february22Data = new ArrayList<Integer>();
        march22Data = new ArrayList<Integer>();
        april22Data = new ArrayList<Integer>();
        may22Data = new ArrayList<Integer>();
        june22Data = new ArrayList<Integer>();
        july22Data = new ArrayList<Integer>();
        august22Data = new ArrayList<Integer>();
        september22Data = new ArrayList<Integer>();
        october22Data = new ArrayList<Integer>();
        november22Data = new ArrayList<Integer>();
        december22Data = new ArrayList<Integer>();
        january23Data = new ArrayList<Integer>();
        monthList = new ArrayList<String>();

        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tech Layoffs 2022-2023");
        setVisible(true);

        monthList.add("01/22");
        monthList.add("02/22");
        monthList.add("03/22");
        monthList.add("04/22");
        monthList.add("05/22");
        monthList.add("06/22");
        monthList.add("07/22");
        monthList.add("08/22");
        monthList.add("09/22");
        monthList.add("10/22");
        monthList.add("11/22");
        monthList.add("12/22");
        monthList.add("01/23");
    }

    public static void main(String[] args) {
        new Main();
    }

    public static String topThreeBasedOnIndustry(String month, int year, String industry) {
        String result = "";
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:layoffs");
            Statement stmt = conn.createStatement();
            ResultSet topThree = stmt.executeQuery("SELECT company, total_layoffs FROM tech_layoffs where reported_month = '" + month + "' AND reported_year = " + year + " AND industry = '" + industry + "' ORDER BY total_layoffs DESC");

            int count = 0;
            while(topThree.next() && count < 3) {
                result += topThree.getString("company");
                result += ": ";
                result += topThree.getString("total_layoffs");
                result += " | ";
                count++;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String topThreeBasedOnState(String month, int year, String state) {
        String result = "";
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:layoffs");
            Statement stmt = conn.createStatement();
            ResultSet topThree = stmt.executeQuery("SELECT company, total_layoffs FROM tech_layoffs where reported_month = '" + month + "' AND reported_year = " + year + " AND headquarter_location = '" + state + "' ORDER BY total_layoffs DESC");

            int count = 0;
            while(topThree.next() && count < 3) {
                result += topThree.getString("company");
                result += ": ";
                result += topThree.getString("total_layoffs");
                result += " | ";
                count++;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Industry":
                mainPanel.query = "Industry";
                try {
                    // Reinitialize
                    industryList.clear();
                    january22Data.clear();
                    february22Data.clear();
                    march22Data.clear();
                    april22Data.clear();
                    may22Data.clear();
                    june22Data.clear();
                    july22Data.clear();
                    august22Data.clear();
                    september22Data.clear();
                    october22Data.clear();
                    november22Data.clear();
                    december22Data.clear();
                    january23Data.clear();

                    Connection conn = DriverManager.getConnection("jdbc:derby:layoffs");
                    Statement stmt = conn.createStatement();
                    ResultSet industryNames = stmt.executeQuery("SELECT DISTINCT industry FROM tech_layoffs");
                    // Get all the industry names
                    while (industryNames.next()) {
                        String industry = industryNames.getString("industry");
                        industryList.add(industry);
                    }

                    // Copy the size of industryList for each month's list, initialize it to 0
                    for (String s : industryList) {
                        january22Data.add(0);
                        february22Data.add(0);
                        march22Data.add(0);
                        april22Data.add(0);
                        may22Data.add(0);
                        june22Data.add(0);
                        july22Data.add(0);
                        august22Data.add(0);
                        september22Data.add(0);
                        october22Data.add(0);
                        november22Data.add(0);
                        december22Data.add(0);
                        january23Data.add(0);
                    }

                    // Get layoffs for 2022
                    ResultSet layoffs2022 = stmt.executeQuery("SELECT industry, SUM(total_layoffs) AS total_layoffs, reported_month FROM tech_layoffs WHERE reported_year = 2022 GROUP BY industry, reported_month");
                    while (layoffs2022.next()) {
                        String industry = layoffs2022.getString("industry");
                        String totalLayoffs = layoffs2022.getString("total_layoffs");
                        String month = layoffs2022.getString("reported_month");
                        int totalLayoffsInt = Integer.parseInt(totalLayoffs);

                        // Get the index of the industry in industryList array
                        int i = industryList.indexOf(industry);

                        // Assign the data value to the proper month array, in the same index as the industry index
                        switch(month) {
                            case "January":
                                january22Data.set(i, totalLayoffsInt);
                                break;
                            case "February":
                                february22Data.set(i, totalLayoffsInt);
                                break;
                            case "March":
                                march22Data.set(i, totalLayoffsInt);
                                break;
                            case "April":
                                april22Data.set(i, totalLayoffsInt);
                                break;
                            case "May":
                                may22Data.set(i, totalLayoffsInt);
                                break;
                            case "June":
                                june22Data.set(i, totalLayoffsInt);
                                break;
                            case "July":
                                july22Data.set(i, totalLayoffsInt);
                                break;
                            case "August":
                                august22Data.set(i, totalLayoffsInt);
                                break;
                            case "September":
                                september22Data.set(i, totalLayoffsInt);
                                break;
                            case "October":
                                october22Data.set(i, totalLayoffsInt);
                                break;
                            case "November":
                                november22Data.set(i, totalLayoffsInt);
                                break;
                            case "December":
                                december22Data.set(i, totalLayoffsInt);
                                break;
                        }
                    }

                    // Get layoffs for 2023
                    ResultSet layoffs2023 = stmt.executeQuery("SELECT industry, SUM(total_layoffs) AS total_layoffs, reported_month FROM tech_layoffs WHERE reported_year = 2023 GROUP BY industry, reported_month");
                    while (layoffs2023.next()) {
                        String industry = layoffs2023.getString("industry");
                        String totalLayoffs = layoffs2023.getString("total_layoffs");
                        int totalLayoffsInt = Integer.parseInt(totalLayoffs);

                        // Get the index of the industry in industryList array
                        int i = industryList.indexOf(industry);

                        // Assign the data value to the January 2023 month array
                        january23Data.set(i, totalLayoffsInt);
                    }

                    for(int x = 0; x < industryList.size(); x++) {
                        System.out.println("For industry " + industryList.get(x) + " January has " + january22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " February has " + february22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " March has " + march22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " April has " + april22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " May has " + may22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " June has " + june22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " July has " + july22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " August has " + august22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " September has " + september22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " October has " + october22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " November has " + november22Data.get(x));
                        System.out.println("For industry " + industryList.get(x) + " December has " + december22Data.get(x));
                    }

                    conn.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                mainPanel.repaint();
                break;
            case "State":
                mainPanel.query = "State";
                try {
                    // Reinitialize
                    stateList.clear();
                    january22Data.clear();
                    february22Data.clear();
                    march22Data.clear();
                    april22Data.clear();
                    may22Data.clear();
                    june22Data.clear();
                    july22Data.clear();
                    august22Data.clear();
                    september22Data.clear();
                    october22Data.clear();
                    november22Data.clear();
                    december22Data.clear();
                    january23Data.clear();

                    Connection conn = DriverManager.getConnection("jdbc:derby:layoffs");
                    Statement stmt = conn.createStatement();
                    ResultSet stateNames = stmt.executeQuery("SELECT DISTINCT headquarter_location FROM tech_layoffs");
                    // Get all the state names
                    while (stateNames.next()) {
                        String state = stateNames.getString("headquarter_location");
                        stateList.add(state);
                    }

                    // Copy the size of stateList for each month's list, initialize it to 0
                    for (String s : stateList) {
                        january22Data.add(0);
                        february22Data.add(0);
                        march22Data.add(0);
                        april22Data.add(0);
                        may22Data.add(0);
                        june22Data.add(0);
                        july22Data.add(0);
                        august22Data.add(0);
                        september22Data.add(0);
                        october22Data.add(0);
                        november22Data.add(0);
                        december22Data.add(0);
                        january23Data.add(0);
                    }

                    // Get layoffs for 2022
                    ResultSet layoffs2022 = stmt.executeQuery("SELECT headquarter_location, SUM(total_layoffs) AS total_layoffs, reported_month FROM tech_layoffs WHERE reported_year = 2022 GROUP BY headquarter_location, reported_month");
                    while (layoffs2022.next()) {
                        String state = layoffs2022.getString("headquarter_location");
                        String totalLayoffs = layoffs2022.getString("total_layoffs");
                        String month = layoffs2022.getString("reported_month");
                        int totalLayoffsInt = Integer.parseInt(totalLayoffs);

                        // Get the index of the state in stateList array
                        int i = stateList.indexOf(state);

                        // Assign the data value to the proper month array, in the same index as the state index
                        switch(month) {
                            case "January":
                                january22Data.set(i, totalLayoffsInt);
                                break;
                            case "February":
                                february22Data.set(i, totalLayoffsInt);
                                break;
                            case "March":
                                march22Data.set(i, totalLayoffsInt);
                                break;
                            case "April":
                                april22Data.set(i, totalLayoffsInt);
                                break;
                            case "May":
                                may22Data.set(i, totalLayoffsInt);
                                break;
                            case "June":
                                june22Data.set(i, totalLayoffsInt);
                                break;
                            case "July":
                                july22Data.set(i, totalLayoffsInt);
                                break;
                            case "August":
                                august22Data.set(i, totalLayoffsInt);
                                break;
                            case "September":
                                september22Data.set(i, totalLayoffsInt);
                                break;
                            case "October":
                                october22Data.set(i, totalLayoffsInt);
                                break;
                            case "November":
                                november22Data.set(i, totalLayoffsInt);
                                break;
                            case "December":
                                december22Data.set(i, totalLayoffsInt);
                                break;
                        }
                    }

                    // Get layoffs for 2023
                    ResultSet layoffs2023 = stmt.executeQuery("SELECT headquarter_location, SUM(total_layoffs) AS total_layoffs, reported_month FROM tech_layoffs WHERE reported_year = 2023 GROUP BY headquarter_location, reported_month");
                    while (layoffs2023.next()) {
                        String state = layoffs2023.getString("headquarter_location");
                        String totalLayoffs = layoffs2023.getString("total_layoffs");
                        int totalLayoffsInt = Integer.parseInt(totalLayoffs);

                        // Get the index of the state in stateList array
                        int i = stateList.indexOf(state);

                        // Assign the data value to the January 2023 month array
                        january23Data.set(i, totalLayoffsInt);
                    }

                    for(int x = 0; x < stateList.size(); x++) {
                        System.out.println("For state " + stateList.get(x) + " January has " + january22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " February has " + february22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " March has " + march22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " April has " + april22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " May has " + may22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " June has " + june22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " July has " + july22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " August has " + august22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " September has " + september22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " October has " + october22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " November has " + november22Data.get(x));
                        System.out.println("For state " + stateList.get(x) + " December has " + december22Data.get(x));
                    }

                    conn.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                mainPanel.repaint();
                break;
        }
    }
}
