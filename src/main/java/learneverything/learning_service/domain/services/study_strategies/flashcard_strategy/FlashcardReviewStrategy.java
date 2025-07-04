package learneverything.learning_service.domain.services.study_strategies.flashcard_strategy;

import learneverything.learning_service.database.entities.LearningProgressEntity;
import learneverything.learning_service.database.repositories.LearningProgressRepository;
import learneverything.learning_service.domain.dtos.learning.LearningDTO;
import learneverything.learning_service.domain.dtos.learning.flashcard.FlashCardDTO;
import learneverything.learning_service.domain.dtos.learning_result.LessonResultDTO;
import learneverything.learning_service.domain.dtos.lesson.LessonDTO;
import learneverything.learning_service.domain.services.LessonService;
import learneverything.learning_service.domain.services.study_strategies.IStudyStrategy;
import learneverything.learning_service.utils.StudyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * To review flashcard was memorized
 */

@Service
@RequiredArgsConstructor
public class FlashcardReviewStrategy implements IStudyStrategy {
    private final LessonService lessonService;
    private final LearningProgressRepository learningProgressRepository;

    @Override
    public List<Object> learn(Integer lessonId, String userId) {
        LessonDTO lesson = lessonService.get(lessonId);
        StudyUtils.validateStudyStrategy(lesson.getLearningType(),lesson.getId(), this.getClass());
        List<Long> learningIds = lesson.getListLearning()
                .stream()
                .map(LearningDTO::getId)
                .toList();

        Map<Long, LearningProgressEntity> learningProgressMap = learningProgressRepository.findByLearningIdIn(learningIds)
                .stream()
                .collect(Collectors.toMap(LearningProgressEntity::getLearningId, e->e));

        List<LearningDTO> learningDTOS = new ArrayList<>();

        for (LearningDTO learning : lesson.getListLearning()){
            if (learningProgressMap.containsKey(learning.getId())){
                LearningProgressEntity learningProgressEntity = learningProgressMap.get(learning.getId());
                if (learningProgressEntity.getProgress() >= 3){
                    FlashCardDTO flashCard = (FlashCardDTO) learning;
                    learningDTOS.add(learning);
                }
            }
        }

        return List.of();
    }

    @Override
    public Object evaluate(LessonResultDTO result, String userId) {
        return null;
    }
}
