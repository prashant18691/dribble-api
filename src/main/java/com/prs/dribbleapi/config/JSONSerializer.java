package com.prs.dribbleapi.config;

import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prs.dribbleapi.dto.Company;


public class JSONSerializer implements Serializer<Company> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void close() {
        // TODO Auto-generated method stub
    }
    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
        // TODO Auto-generated method stub
    }
    @Override
    public byte[] serialize(String arg0, Company arg1) {
        byte[] value = null;
        try {
            value = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Can't serialize object: " + arg1, e);
        }
        return value;
    }
}
