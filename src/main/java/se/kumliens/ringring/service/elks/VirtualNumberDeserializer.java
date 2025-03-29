package se.kumliens.ringring.service.elks;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import se.kumliens.ringring.model.VirtualNumber;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VirtualNumberDeserializer extends StdDeserializer<VirtualNumber> {

    public VirtualNumberDeserializer() {
        super(VirtualNumber.class);
    }

    @Override
    public VirtualNumber deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode dataNode = jsonParser.getCodec().readTree(jsonParser);
        //JsonNode dataNode = rootNode.get("data").get(0); // Assuming the JSON contains an array with one object.

        String id = dataNode.get("id").asText();
        String number = dataNode.get("number").asText();
        String status = dataNode.get("active").asText();
        String name = dataNode.get("name").asText();
        String category = dataNode.get("category").asText();
        String allocated = dataNode.get("allocated").asText();
        Integer cost = dataNode.get("cost").asInt();
        var voiceStartRaw = dataNode.get("voice_start");

        // Parse the `voice_start` field into a Map<String, List<String>>
        Map<String, List<String>> voiceStart = new HashMap<>();
        JsonNode voiceStartNode = jsonParser.getCodec().readTree(voiceStartRaw.traverse());
        voiceStartNode.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            String value = entry.getValue().asText();
            List<String> phoneNumbers = Arrays.stream(value.split(","))
                                              .map(String::trim)
                                              .collect(Collectors.toList());
            voiceStart.put(key, phoneNumbers);
        });

        return new VirtualNumber(id, number, status, name, category, allocated, cost, voiceStartRaw.asText(), voiceStart);
    }
}
