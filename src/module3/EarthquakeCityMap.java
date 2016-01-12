package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
//Parsing library
import parsing.ParseFeed;
//Processing library
import processing.core.PApplet;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author Your name here Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this. It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	private final int blue = color(0, 0, 255);
	private final int yellow = color(255, 255, 0);
	private final int red = color(255, 0, 0);

	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // Same feed, saved Aug 7, 2015, for
																				// working offline
		} else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}

		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		// The List you will populate with new SimplePointMarkers
		List<Marker> markers = new ArrayList<Marker>();

		// Use provided parser to collect properties for each earthquake
		// PointFeatures have a getLocation method
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		// These print statements show you (1) all of the relevant properties
		// in the features, and (2) how to get one property and use it

		if (earthquakes.size() > 0) {
			for (PointFeature quake : earthquakes) {
				markers.add(createMarker(quake));
			}
			map.addMarkers(markers);
		}
	}

	// A suggested helper method that takes in an earthquake feature and
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature quake) {
		SimplePointMarker m = new SimplePointMarker(quake.getLocation());

		if (Double.parseDouble(quake.properties.get("magnitude").toString()) < 4) {
			m.setColor(blue);
			m.setRadius(5);
		} else if (Double.parseDouble(quake.properties.get("magnitude").toString()) >= 4
				&& Double.parseDouble(quake.properties.get("magnitude").toString()) < 5) {
			m.setColor(blue);
			m.setColor(yellow);
			m.setRadius(10);
		} else if (Double.parseDouble(quake.properties.get("magnitude").toString()) >= 5) {
			m.setColor(red);
			m.setRadius(20);
		} else {
			System.out.println(quake.properties.get("magnitude").toString());
		}
		return m;
	}

	public void draw() {
		background(10);
		map.draw();
		addKey();
	}

	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() {
		// box
		rect(30, 50, 150, 300, 7);
		
		// text
		fill(0, 0, 0);
		text("Earthquake Key", 60, 80);
		text("5.0+ Magnitude", 70, 150);
		text("4.0+ Magnitude", 70, 200);
		text("Below 4.0", 70, 250);
		
		// circles
		fill(red);
		ellipse(50, 145, 20, 20);
		fill(yellow);
		ellipse(50, 195, 10, 10);
		fill(blue);
		ellipse(50, 245, 5, 5);
		
		// color for the box (order matters)
		fill(255,255,255);
	}
}
