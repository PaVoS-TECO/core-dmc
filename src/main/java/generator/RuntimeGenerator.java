package generator;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.teco.pavos.core.grid.GeoGrid;
import edu.teco.pavos.core.grid.GeoRecRectangleGrid;
import edu.teco.pavos.core.grid.config.WorldMapData;
import edu.teco.pavos.core.web.WebServer;
import edu.teco.pavos.transfer.data.ObservationData;
import edu.teco.pavos.transfer.sender.util.TimeUtil;

public class RuntimeGenerator {
	
	private static volatile GeoGrid grid;
	public static void main(String[] args) {
		
		grid = new GeoRecRectangleGrid(new Rectangle2D.Double(- WorldMapData.LNG_RANGE, - WorldMapData.LAT_RANGE, WorldMapData.LNG_RANGE * 2, WorldMapData.LAT_RANGE * 2),  2, 2, 3);
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			grid.addObservation(randomLocation(), generateRandom(randomSensor(), "temperature_celsius", 40.0));
			grid.addObservation(randomLocation(), generateRandom(randomSensor(), "pM_10", 40.0));
		}, 1, 8, TimeUnit.SECONDS);
		new Thread(new WebServer()).start();
	}
	
	private static String randomSensor() {
		int num = new Random().nextInt(99999);
		return "sensor" + num;
	}
	
	private static Point2D.Double randomLocation() {
		double x = Math.random() * grid.getMapBounds().getX();
		double y = Math.random() * grid.getMapBounds().getY();
		return new Point2D.Double(x, y);
	}
	
	private static ObservationData generateRandom(String sensorID, String property, double max) {
		double value = Math.random() * max;
		return generate(sensorID, property, value);
	}
	
	private static ObservationData generate(String sensorID, String property, Double value) {
		ObservationData result = new ObservationData();
		result.setObservationDate(TimeUtil.getUTCDateTimeNowString());
		result.setSensorID(sensorID);
		result.addSingleObservation(property, value);
		System.out.println(result);
		return result;
	}
	
}
