package guru.springframework.msscbeerservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelloMessage implements Serializable {

    private static final long serialVersionUID = 8902132023230261238L;
    private UUID id;
    private String message;

}
