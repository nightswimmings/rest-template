package org.nightswimming.thesis.rest.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonPrettyParser {
	
	private final ObjectMapper mapper;
	
	public JsonPrettyParser(){
		mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	public String prettify(Object jsonEntity) throws JsonProcessingException{
		return mapper.writeValueAsString(jsonEntity);
	}
	public String prettify(String jsonString) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{
		return mapper.writeValueAsString(mapper.readValue(jsonString,Object.class));
	}
	
	public <T> T deserialize(String jsonString, Class<T> outClass) throws JsonParseException, JsonMappingException, IOException{
		return mapper.readValue(jsonString, outClass);
	}
}
