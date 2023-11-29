package somgrid;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Arrays;

public class SOMGridController {
    IRValueSOM map;
    @FXML
    GridPane grid;

    @FXML
    public void initialize() throws IOException {
        int gridSize = 4;
        map = new IRValueSOM(gridSize);
        // Bar Chart documentation https://www.tutorialspoint.com/javafx/bar_chart.htm
        for (int x = 0; x < gridSize; x++) {
            for (int y= 0; y<gridSize; y++) {
                CategoryAxis xAxis = new CategoryAxis();
                xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("side_left", "left", "front_left", "front_center_left", "front_center_right", "front_right","right")));
                xAxis.setLabel("IR Sensor");
                NumberAxis yAxis = new NumberAxis();
                yAxis.setAutoRanging(false);
                yAxis.setUpperBound(map.getMax());
                yAxis.setLabel("IR Value");
                BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
                barChart.setTitle("(" + x + "," + y + ")");
                XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                series1.getData().add(new XYChart.Data<>("side_left", map.getValue(x,y,0)));
                series1.getData().add(new XYChart.Data<>("left", map.getValue(x,y,1)));
                series1.getData().add(new XYChart.Data<>("front_left", map.getValue(x,y,2)));
                series1.getData().add(new XYChart.Data<>("front_center_left", map.getValue(x,y,3)));
                series1.getData().add(new XYChart.Data<>("front_center_right", map.getValue(x,y,4)));
                series1.getData().add(new XYChart.Data<>("front_right", map.getValue(x,y,5)));
                series1.getData().add(new XYChart.Data<>("right", map.getValue(x,y,6)));
                barChart.getData().add(series1);
                GridPane.setConstraints(barChart, x, y);
                barChart.setLegendVisible(false);
                grid.getChildren().add(barChart);
            }
        }
    }
}
