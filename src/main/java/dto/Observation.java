package dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "keyPosition",
        "values"
})
@Generated("jsonschema2pojo")
public class Observation {

    @JsonProperty("id")
    private String id;
    @JsonProperty("keyPosition")
    private Integer keyPosition;
    @JsonProperty("values")
    private List<Date> values = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("keyPosition")
    public Integer getKeyPosition() {
        return keyPosition;
    }

    @JsonProperty("keyPosition")
    public void setKeyPosition(Integer keyPosition) {
        this.keyPosition = keyPosition;
    }

    @JsonProperty("values")
    public List<Date> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<Date> values) {
        this.values = values;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
