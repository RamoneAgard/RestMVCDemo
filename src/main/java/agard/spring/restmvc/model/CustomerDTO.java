package agard.spring.restmvc.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 50)
    private String customerName;

    @Email
    @Size(max = 255)
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
