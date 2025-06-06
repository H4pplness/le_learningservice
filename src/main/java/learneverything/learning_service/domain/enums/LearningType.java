package learneverything.learning_service.domain.enums;

import learneverything.learning_service.application.exceptions.BaseException;
import learneverything.learning_service.application.exceptions.Error;
import learneverything.learning_service.database.entities.LearningEntity;
import learneverything.learning_service.database.entities.learning_entities.flashcard.FlashCardEntity;
import learneverything.learning_service.database.entities.learning_entities.question.QuestionEntity;
import learneverything.learning_service.database.entities.learning_entities.vocabulary.VocabularyEntity;
import learneverything.learning_service.database.repositories.FlashCardRepository;
import learneverything.learning_service.database.repositories.QuestionRepository;
import learneverything.learning_service.database.repositories.VocabularyRepository;
import learneverything.learning_service.domain.dtos.learning.LearningDTO;
import learneverything.learning_service.domain.dtos.learning.flashcard.FlashCardDTO;
import learneverything.learning_service.domain.dtos.learning.question.FillQuestionDTO;
import learneverything.learning_service.domain.dtos.learning.question.MultiChoiceQuestionDTO;
import learneverything.learning_service.domain.dtos.learning.question.QuestionDTO;
import learneverything.learning_service.domain.dtos.learning.question.SingleChoiceQuestionDTO;
import learneverything.learning_service.domain.mappers.FlashCardMapper;
import learneverything.learning_service.domain.mappers.ILearningMapper;
import learneverything.learning_service.domain.mappers.QuestionMapper;
import learneverything.learning_service.domain.mappers.VocabularyMapper;
import learneverything.learning_service.domain.services.learning_repository.ICRUDLearningService;
import learneverything.learning_service.domain.services.learning_repository.crud_learning.CRUDFlashCardService;
import learneverything.learning_service.domain.services.learning_repository.crud_learning.CRUDQuestionService;
import learneverything.learning_service.domain.services.learning_repository.crud_learning.CRUDVocabularyService;
import lombok.Getter;
import org.antlr.v4.runtime.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public enum LearningType {
    VOCABULARY("vocabulary", VocabularyEntity.class, VocabularyMapper.class, VocabularyRepository.class, CRUDVocabularyService.class, Vocabulary.class),

    // QUESTION
    QUESTION("question", QuestionEntity.class, QuestionMapper.class, QuestionRepository.class, CRUDQuestionService.class, QuestionDTO.class),
    SINGLE_CHOICE_QUESTION("single_choice_question",QuestionEntity.class, QuestionMapper.class,QuestionRepository.class, CRUDQuestionService.class, SingleChoiceQuestionDTO.class),
    MULTI_CHOICE_QUESTION("single_choice_question",QuestionEntity.class, QuestionMapper.class, QuestionRepository.class, CRUDQuestionService.class, MultiChoiceQuestionDTO.class),
    FILL_QUESTION("fill_question",QuestionEntity.class, QuestionMapper.class, QuestionRepository.class, CRUDQuestionService.class, FillQuestionDTO.class),

    // FLASHCARD
    FLASHCARD("flashcard", FlashCardEntity.class, FlashCardMapper.class, FlashCardRepository.class, CRUDFlashCardService.class, FlashCardDTO.class)
    ;

    LearningType(String name,
                 Class entityClass,
                 Class mapperClass,
                 Class repositoryClass,
                 Class crudServiceClass,
                 Class dtoClass) {
        this.name = name;
        this.entityClass = entityClass;
        this.mapperClass = mapperClass;
        this.repositoryClass = repositoryClass;
        this.crudServiceClass = crudServiceClass;
        this.dtoClass = dtoClass;
    }

    private String name;
    private Class<? extends LearningDTO> dtoClass;
    private Class<? extends LearningEntity> entityClass;
    private Class<? extends ILearningMapper> mapperClass;
    private Class<? extends JpaRepository> repositoryClass;
    private Class<? extends ICRUDLearningService> crudServiceClass;

    public static LearningType getLearningTypeByEntityClass(Class clazz){
        for (LearningType type : LearningType.values()) {
            if (type.getEntityClass().equals(clazz)) {
                return type;
            }
        }
        throw new BaseException(Error.INVALID_LESSON);
    }

    public static LearningType getLearningTypeByDtoClass(Class clazz){
        for (LearningType type : LearningType.values()) {
            if (type.getDtoClass().equals(clazz)) {
                return type;
            }
        }
        throw new BaseException(Error.INVALID_LESSON);
    }

    public static LearningType getLearningTypeByName(String name){
        for (LearningType type : LearningType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new BaseException(Error.INVALID_LESSON);
    }
}
