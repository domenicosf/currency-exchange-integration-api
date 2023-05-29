package dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "keyPosition",
        "default",
        "values"
})
@Generated("jsonschema2pojo")
public class Currencies {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("keyPosition")
    private Integer keyPosition;
    @JsonProperty("default")
    private String _default;
    @JsonProperty("values")
    private List<CurrencyData> currencies = null;
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

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("keyPosition")
    public Integer getKeyPosition() {
        return keyPosition;
    }

    @JsonProperty("keyPosition")
    public void setKeyPosition(Integer keyPosition) {
        this.keyPosition = keyPosition;
    }

    @JsonProperty("default")
    public String getDefault() {
        return _default;
    }

    @JsonProperty("default")
    public void setDefault(String _default) {
        this._default = _default;
    }

    @JsonProperty("values")
    public List<CurrencyData> getValues() {
        return currencies;
    }

    @JsonProperty("values")
    public void setValues(List<CurrencyData> currencies) {
        this.currencies = currencies;
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

