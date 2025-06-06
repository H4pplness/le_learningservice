package learneverything.learning_service.domain.dtos.learning.question;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import learneverything.learning_service.domain.dtos.learning.LearningDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class QuestionDTO extends LearningDTO {
    String question;
    String description;
    String questionType;
}
