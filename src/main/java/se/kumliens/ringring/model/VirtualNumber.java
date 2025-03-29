package se.kumliens.ringring.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.kumliens.ringring.service.elks.VirtualNumberDeserializer;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = VirtualNumberDeserializer.class)
public record VirtualNumber(
        String id,
        String number,
        String status,
        String name,
        String category,
        String allocated,
        Integer cost,
        String voice_start,
        Map<String, List<String>> voiceStart
) {

}