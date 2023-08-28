package h06.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.awt.Point;
import java.io.IOException;

/**
 * A deserializer for {@link Point} array.
 *
 * @author Nhan Huynh
 */
public class PointArrayDeserializer extends JsonDeserializer<Point[]> {

    @Override
    public Point[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec mapper = p.getCodec();
        ArrayNode arrayNode = mapper.readTree(p);
        Point[] points = new Point[arrayNode.size()];
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode node = arrayNode.get(i);
            int x = node.get("x").asInt();
            int y = node.get("y").asInt();
            points[i] = new Point(x, y);
        }
        return points;
    }
}
