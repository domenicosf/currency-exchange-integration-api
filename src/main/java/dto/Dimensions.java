package dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "series",
        "observation"
})
@Generated("jsonschema2pojo")
public class Dimensions {

    @JsonProperty("series")
    private List<Currencies> currencies = null;
    @JsonProperty("observation")
    private List<Observation> observation = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("series")
    public List<Currencies> getCurrencies() {
        return currencies;
    }

    @JsonProperty("series")
    public void setCurrencies(List<Currencies> currencies) {
        this.currencies = currencies;
    }

    @JsonProperty("observation")
    public List<Observation> getObservation() {
        return observation;
    }

    @JsonProperty("observation")
    public void setObservation(List<Observation> observation) {
        this.observation = observation;
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
