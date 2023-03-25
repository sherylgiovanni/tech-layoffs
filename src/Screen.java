import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Screen extends JPanel {

    public static String query = "";
    public int rectangleHeight = 1;
    public int rectangleWidth = 1;
    public int rows;
    public int columns;
    public Font normalFont;
    public int tooltipAmount;

    public Screen() {
        super();
        normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        tooltipAmount = 0;
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        int h = getHeight();
        int w = getWidth();
        int margin = 100;

        String title = "Tech Layoffs January 2022 - January 2023";

        // Set special font size and style
        Font font = new Font("Arial", Font.BOLD, 24);
        g.setFont(font);

        // Calculate the position of the text
        int stringWidth = g.getFontMetrics().stringWidth(title);
        int posX = (w - stringWidth) / 2; // center horizontally
        int posY = margin / 2 + font.getSize() / 2; // center vertically at the top

        // Draw the title
        g.drawString(title, posX, posY);

        // Set the font back to normal
        g.setFont(normalFont);

        if (query == "Industry") {
            // Define color thresholds
            int[] colorThresholds = {0, 500, 750, 1000, 1250, 1500, 1750, 2000};

            // Define colors for each threshold
            Color[] colors = {
                    new Color(255, 221, 57, 10),
                    new Color(255, 221, 57, 36),
                    new Color(255, 221, 57, 72),
                    new Color(255, 221, 57, 108),
                    new Color(255, 221, 57, 144),
                    new Color(255, 221, 57, 180),
                    new Color(255, 221, 57, 216),
                    new Color(255, 221, 57, 255)
            };

            // For legend text
            String[] legendText = {
                    "< 500",
                    "500 - 749",
                    "750 - 999",
                    "1000 - 1249",
                    "1250 - 1499",
                    "1500 - 1749",
                    "1750 - 1999",
                    ">= 2000"};

            rows = Main.industryList.size();
            columns = 13; // this is the total months from Jan 22 - Jan 23

            // Calculate the width and height of the rectangle, taking into account the margin
            rectangleWidth = (w - margin * 2) / columns;
            rectangleHeight = (h - margin * 2) / rows;

            // Calculate the total width of the color legend
            int legendWidth = colors.length * rectangleWidth;

            // Calculate the x-coordinate of the leftmost color rectangle to center the legend
            int legendX = (w - legendWidth) / 2;

            // Create a smaller font for the legend text
            Font smallFont = new Font("Arial", Font.PLAIN, 8);

            // Draw the color legend
            for (int i = 0; i < colors.length; i++) {
                Color color = colors[i];
                int colorX = legendX + i * rectangleWidth;
                int colorY = h - margin - rectangleHeight + 50; // Add space of 50 pixels between the grid and the legend
                int colorWidth = rectangleWidth;
                int colorHeight = rectangleHeight;
                g.setColor(color);
                g.fillRect(colorX, colorY, colorWidth, colorHeight);
                g.setColor(Color.BLACK);
                g.drawRect(colorX, colorY, colorWidth, colorHeight);

                // Draw the legend text with the smaller font
                g.setFont(smallFont);
                int textX = colorX;
                int textY = colorY - 10;
                String text = legendText[i];
                g.drawString(text, textX, textY);

                // Set the font back to normal
                g.setFont(normalFont);
            }

            // Draw the heatmap
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    // Get the data for this cell
                    ArrayList<Integer> data;
                    switch(column) {
                        case 0:
                            data = Main.january22Data;
                            break;
                        case 1:
                            data = Main.february22Data;
                            break;
                        case 2:
                            data = Main.march22Data;
                            break;
                        case 3:
                            data = Main.april22Data;
                            break;
                        case 4:
                            data = Main.may22Data;
                            break;
                        case 5:
                            data = Main.june22Data;
                            break;
                        case 6:
                            data = Main.july22Data;
                            break;
                        case 7:
                            data = Main.august22Data;
                            break;
                        case 8:
                            data = Main.september22Data;
                            break;
                        case 9:
                            data = Main.october22Data;
                            break;
                        case 10:
                            data = Main.november22Data;
                            break;
                        case 11:
                            data = Main.december22Data;
                            break;
                        case 12:
                            data = Main.january23Data;
                            break;
                        default:
                            data = Main.january22Data;
                            break;
                    }
                    int amount = data.get(row);

                    // Find the color for this data value
                    Color color = colors[0];
                    for (int i = 0; i < colorThresholds.length; i++) {
                        if (amount > colorThresholds[i]) {
                            color = colors[i];
                        }
                    }

                    // Calculate the x and y coordinates, taking into account the margin
                    int x = column * rectangleWidth + margin;
                    int y = row * rectangleHeight + margin;

                    // Draw the rectangle with the appropriate color
                    g.setColor(color);
                    g.fillRect(x, y, rectangleWidth, rectangleHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, rectangleWidth, rectangleHeight);
                }

                // Draw the row labels
                for (int r = 0; r < rows; r++) {
                    String label = Main.industryList.get(r);
                    int labelWidth = g.getFontMetrics().stringWidth(label);
                    int labelX = margin - labelWidth - 5;
                    int labelY = margin + (r + 1) * rectangleHeight - rectangleHeight / 2;
                    g.drawString(label, labelX, labelY);
                }

                // Draw the column labels
                for (int c = 0; c < columns; c++) {
                    String label = Main.monthList.get(c);
                    int labelWidth = g.getFontMetrics().stringWidth(label);
                    int labelX = margin + (c + 1) * rectangleWidth - rectangleWidth / 2 - labelWidth / 2;
                    int labelY = margin - 10;
                    g.drawString(label, labelX, labelY);
                }
            }
        } else if (query == "State") {
            int[] colorThresholds = {0, 500, 750, 1000, 1250, 1500, 1750, 2000};

            // Define colors for each threshold
            Color[] colors = {
                    new Color(0, 77, 232, 10),
                    new Color(0, 77, 232, 36),
                    new Color(0, 77, 232, 72),
                    new Color(0, 77, 232, 108),
                    new Color(0, 77, 232, 144),
                    new Color(0, 77, 232, 180),
                    new Color(0, 77, 232, 216),
                    new Color(0, 77, 232, 255)
            };

            // For legend text
            String[] legendText = {
                    "< 500",
                    "500 - 749",
                    "750 - 999",
                    "1000 - 1249",
                    "1250 - 1499",
                    "1500 - 1749",
                    "1750 - 1999",
                    ">= 2000"};

            rows = Main.stateList.size();
            columns = 13; // this is the total months from Jan 22 - Jan 23

            // Calculate the width and height of the rectangle, taking into account the margin
            rectangleWidth = (w - margin * 2) / columns;
            rectangleHeight = (h - margin * 2) / rows;

            // Calculate the total width of the color legend
            int legendWidth = colors.length * rectangleWidth;

            // Calculate the x-coordinate of the leftmost color rectangle to center the legend
            int legendX = (w - legendWidth) / 2;

            // Create a smaller font for the legend text
            Font smallFont = new Font("Arial", Font.PLAIN, 8);

            // Draw the color legend
            for (int i = 0; i < colors.length; i++) {
                Color color = colors[i];
                int colorX = legendX + i * rectangleWidth;
                int colorY = h - margin - rectangleHeight + 50; // Add space of 50 pixels between the grid and the legend
                int colorWidth = rectangleWidth;
                int colorHeight = rectangleHeight;
                g.setColor(color);
                g.fillRect(colorX, colorY, colorWidth, colorHeight);
                g.setColor(Color.BLACK);
                g.drawRect(colorX, colorY, colorWidth, colorHeight);

                // Draw the legend text with the smaller font
                g.setFont(smallFont);
                int textX = colorX;
                int textY = colorY - 10;
                String text = legendText[i];
                g.drawString(text, textX, textY);

                // Set the font back to normal
                g.setFont(normalFont);
            }

            // Draw the heatmap
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    // Get the data for this cell
                    ArrayList<Integer> data;
                    switch(column) {
                        case 0:
                            data = Main.january22Data;
                            break;
                        case 1:
                            data = Main.february22Data;
                            break;
                        case 2:
                            data = Main.march22Data;
                            break;
                        case 3:
                            data = Main.april22Data;
                            break;
                        case 4:
                            data = Main.may22Data;
                            break;
                        case 5:
                            data = Main.june22Data;
                            break;
                        case 6:
                            data = Main.july22Data;
                            break;
                        case 7:
                            data = Main.august22Data;
                            break;
                        case 8:
                            data = Main.september22Data;
                            break;
                        case 9:
                            data = Main.october22Data;
                            break;
                        case 10:
                            data = Main.november22Data;
                            break;
                        case 11:
                            data = Main.december22Data;
                            break;
                        case 12:
                            data = Main.january23Data;
                            break;
                        default:
                            data = Main.january22Data;
                            break;
                    }
                    int amount = data.get(row);

                    // Find the color for this data value
                    Color color = colors[0];
                    for (int i = 0; i < colorThresholds.length; i++) {
                        if (amount > colorThresholds[i]) {
                            color = colors[i];
                        }
                    }

                    // Calculate the x and y coordinates, taking into account the margin
                    int x = column * rectangleWidth + margin;
                    int y = row * rectangleHeight + margin;

                    // Draw the rectangle with the appropriate color
                    g.setColor(color);
                    g.fillRect(x, y, rectangleWidth, rectangleHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, rectangleWidth, rectangleHeight);
                }

                // Draw the row labels
                for (int r = 0; r < rows; r++) {
                    String label = Main.stateList.get(r);
                    int labelWidth = g.getFontMetrics().stringWidth(label);
                    int labelX = margin - labelWidth - 5;
                    int labelY = margin + (r + 1) * rectangleHeight - rectangleHeight / 2;
                    g.drawString(label, labelX, labelY);
                }

                // Draw the column labels
                for (int c = 0; c < columns; c++) {
                    String label = Main.monthList.get(c);
                    int labelWidth = g.getFontMetrics().stringWidth(label);
                    int labelX = margin + (c + 1) * rectangleWidth - rectangleWidth / 2 - labelWidth / 2;
                    int labelY = margin - 10;
                    g.drawString(label, labelX, labelY);
                }
            }
        }
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Calculate the column and row of the rectangle that the mouse is hovering over
                int column = (e.getX() - margin) / rectangleWidth;
                int row = (e.getY() - margin) / rectangleHeight;

                // Calculate the x and y coordinates of the rectangle
                int x = column * rectangleWidth + margin;
                int y = row * rectangleHeight + margin;

                // Check if the mouse is within the rectangle
                if (e.getX() >= x && e.getX() < x + rectangleWidth &&
                        e.getY() >= y && e.getY() < y + rectangleHeight) {

                    // Get top three contributors for the cell that the mouse is hovering over
                    String data = getDataForColumn(column, row);

                    // Show a tooltip with the data
                    setToolTipText(data);
                } else {
                    // Clear the tooltip if the mouse is not over a rectangle
                    setToolTipText(null);
                }
            }
        });
    }

    private String getDataForColumn(int column, int row) {
        ArrayList<Integer> data;
        String topThree = "";
        String month = "";
        int year = 0;
        switch(column) {
            case 0:
                data = Main.january22Data;
                month = "January";
                year = 2022;
                break;
            case 1:
                data = Main.february22Data;
                month = "February";
                year = 2022;
                break;
            case 2:
                data = Main.march22Data;
                month = "March";
                year = 2022;
                break;
            case 3:
                data = Main.april22Data;
                month = "April";
                year = 2022;
                break;
            case 4:
                data = Main.may22Data;
                month = "May";
                year = 2022;
                break;
            case 5:
                data = Main.june22Data;
                month = "June";
                year = 2022;
                break;
            case 6:
                data = Main.july22Data;
                month = "July";
                year = 2022;
                break;
            case 7:
                data = Main.august22Data;
                month = "August";
                year = 2022;
                break;
            case 8:
                data = Main.september22Data;
                month = "September";
                year = 2022;
                break;
            case 9:
                data = Main.october22Data;
                month = "October";
                year = 2022;
                break;
            case 10:
                data = Main.november22Data;
                month = "November";
                year = 2022;
                break;
            case 11:
                data = Main.december22Data;
                month = "December";
                year = 2022;
                break;
            case 12:
                data = Main.january23Data;
                month = "January";
                year = 2023;
                break;
            default:
                data = Main.january22Data;
                month = "January";
                year = 2022;
                break;
        }
        int totalLayoffs = data.get(row);
        if(query == "Industry") {
            topThree = Main.topThreeBasedOnIndustry(month, year, Main.industryList.get(row));
        } else if (query == "State") {
            topThree = Main.topThreeBasedOnState(month, year, Main.stateList.get(row));
        }
        return "TOTAL: " + totalLayoffs + " | " + topThree;
    }
}