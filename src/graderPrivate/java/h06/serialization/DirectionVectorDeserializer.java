package h06.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import h06.world.DirectionVector;

import java.io.IOException;


/**
 * A deserializer for {@link DirectionVector}s.
 *
 * @author Nhan Huynh
 */
public class DirectionVectorDeserializer extends JsonDeserializer<DirectionVector> {

    @Override
    public DirectionVector deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return switch (p.getText()) {
            case "UP" -> DirectionVector.UP;
            case "DOWN" -> DirectionVector.DOWN;
            case "LEFT" -> DirectionVector.LEFT;
            case "RIGHT" -> DirectionVector.RIGHT;
            default -> throw new IllegalArgumentException("Unknown direction vector: " + value);
        };
    }
}
