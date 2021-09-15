package service;

import DAO.MongoDB.AbsDAO;
import DAO.MongoDB.TheaterDAO;
import com.mongodb.client.FindIterable;
import lombok.Data;
import model.Geo;
import model.Theater;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/theater")
public class TheaterService extends AbsDAO {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/geojson")
    public GeoJSON getListTheater() {
        FindIterable<Theater> list = new TheaterDAO().getListTheater();

        GeoJSON geoJSON = new GeoJSON();
        geoJSON.setType("FeatureCollection");
        geoJSON.setFeatures(new ArrayList<>());


        list.forEach(theater -> {
            Feature feature = new Feature();
            feature.setType("feature");
            feature.setGeometry(theater.getLocation().getGeo());
            geoJSON.getFeatures().add(feature);

            feature.setProperties(new Properties());
            feature.getProperties().setAddress(theater.getLocation().getAddress().getStreet1());
            feature.getProperties().setState(theater.getLocation().getAddress().getState());
            feature.getProperties().setCity(theater.getLocation().getAddress().getCity());
        });

        return geoJSON;
    }
}

@Data
class GeoJSON {
    private String type;
    private ArrayList<Feature> features;
}

@Data
class Feature {
    private String type;
    private Geo geometry;
    private Properties properties;
}
@Data
class Properties {
    private String address;
    private String State;
    private String city;
}


