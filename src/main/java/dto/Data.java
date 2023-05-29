package dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "structure",
        "dataSets"
})
@Generated("jsonschema2pojo")
public class Data {

    @JsonProperty("structure")
    private Structure structure;
    @JsonProperty("dataSets")
    private List<DataSet> dataSets = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("structure")
    public Structure getStructure() {
        return structure;
    }

    @JsonProperty("structure")
    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    @JsonProperty("dataSets")
    public List<DataSet> getDataSets() {
        return dataSets;
    }

    @JsonProperty("dataSets")
    public void setDataSets(List<DataSet> dataSets) {
        this.dataSets = dataSets;
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