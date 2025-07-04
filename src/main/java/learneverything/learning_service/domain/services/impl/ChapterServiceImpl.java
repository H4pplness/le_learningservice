package learneverything.learning_service.domain.services.impl;

import learneverything.learning_service.application.exceptions.BaseException;
import learneverything.learning_service.application.exceptions.Error;
import learneverything.learning_service.database.entities.ChapterEntity;
import learneverything.learning_service.database.entities.ClazzEntity;
import learneverything.learning_service.database.entities.LessonEntity;
import learneverything.learning_service.database.repositories.ChapterRepository;
import learneverything.learning_service.database.repositories.ClazzRepository;
import learneverything.learning_service.database.repositories.LessonRepository;
import learneverything.learning_service.domain.dtos.chapter.ChapterDTO;
import learneverything.learning_service.domain.dtos.chapter.CreateChapterRequestDTO;
import learneverything.learning_service.domain.dtos.chapter.UpdateChapterRequestDTO;
import learneverything.learning_service.domain.dtos.lesson.LessonDTO;
import learneverything.learning_service.domain.mappers.ChapterMapper;
import learneverything.learning_service.domain.mappers.LessonMapper;
import learneverything.learning_service.domain.services.ChapterService;
import learneverything.learning_service.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;
    private final ClazzRepository clazzRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Override
    public ChapterDTO get(Integer id) {
        ChapterEntity chapter = chapterRepository.findById(id).orElseThrow(()->new BaseException(Error.NOT_FOUND_LESSON,id.toString()));
        if (chapter.getStatus() == 0){
            throw new BaseException(Error.NOT_FOUND_LESSON,id.toString());
        }

        ChapterDTO chapterDto = chapterMapper.toDTO(chapter);

        // get the lessson by chapter
        List<LessonEntity> lessons = lessonRepository.getLessonByChapterId(id);
        // map each the lesson to lessonDTO
        if (!lessons.isEmpty()){
            List<LessonDTO> lessonDTOS = lessons.stream().map(lessonMapper::entityToDto).toList();
            chapterDto.setListLesson(lessonDTOS);
        }

        return chapterDto;
    }

    @Override
    public Object create(CreateChapterRequestDTO createChapterRequest) {
        // Check if the lesson exists
        ClazzEntity clazz = clazzRepository.findById(createChapterRequest.getClazzId()).orElseThrow(()->new BaseException(Error.NOT_FOUND_LESSON,createChapterRequest.getClazzId().toString()));
        String userId = CommonUtils.getUserId();
        if (!userId.equals(clazz.getAuthorId())){
            throw new BaseException(Error.FORBIDDEN);
        }

        ChapterEntity chapter = chapterMapper.createDTOToEntity(createChapterRequest);
        chapter.setStatus(1);

        return chapterRepository.save(chapter);
    }

    @Override
    public String delete(Integer id) {
        ChapterEntity chapter = chapterRepository.findById(id).orElseThrow(()->new BaseException(Error.NOT_FOUND_LESSON,id.toString()));
        ClazzEntity clazz = clazzRepository.findById(chapter.getClazzId()).orElseThrow(()->new BaseException(Error.NOT_FOUND_LESSON,chapter.getClazzId().toString()));
        String userId = CommonUtils.getUserId();
        if (!userId.equals(clazz.getAuthorId())){
            throw new BaseException(Error.FORBIDDEN);
        }

        chapter.setStatus(0);
        chapterRepository.save(chapter);

        return "Successful !";
    }

    @Override
    public Object update(Integer id, UpdateChapterRequestDTO updateChapterRequest) {
        // Check if the lesson exists
        ClazzEntity clazz = clazzRepository.findById(updateChapterRequest.getClazzId()).orElseThrow(()->new BaseException(Error.NOT_FOUND_LESSON,updateChapterRequest.getClazzId().toString()));
        String userId = CommonUtils.getUserId();
        if (!userId.equals(clazz.getAuthorId())){
            throw new BaseException(Error.FORBIDDEN);
        }

        ChapterEntity chapter = chapterMapper.updateDTOToEntity(updateChapterRequest);
        chapter.setId(id);
        chapter.setStatus(1);
        chapterRepository.save(chapter);

        return chapter;
    }
}
