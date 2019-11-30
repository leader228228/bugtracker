package ua.edu.sumdu.nc.validation.replies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ua.edu.sumdu.nc.validation.BTRequest;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UpdateReplyRequest implements BTRequest {
    @Min(value = 0)
    @JsonProperty(required = true)
    private long replyId;
    @Length(min = 1, max = 4000)
    private String body;
}
