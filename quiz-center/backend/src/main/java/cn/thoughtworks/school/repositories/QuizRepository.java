package cn.thoughtworks.school.repositories;

import cn.thoughtworks.school.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  List<Quiz> findAllByOrderByIdDesc();

    @Query(value = "select q from Quiz q where q.type=:type ORDER BY q.id DESC")
  List<Quiz> findAllByTypeAndOrderByIdDesc(@Param("type") String type);

  @Query(value = "select q from Quiz q where (q.quizId in (select b.id from BasicQuiz b where b.makerId=:makerId and b.isAvailable=true) AND (q.type='BASIC_BLANK_QUIZ' OR q.type='MULTIPLE_CHOICE' OR q.type='SINGLE_CHOICE')) OR (q.quizId IN (SELECT h.id FROM HomeworkQuiz h WHERE h.makerId=:makerId and h.isAvailable=true ) AND q.type='HOMEWORK_QUIZ') OR (q.quizId IN (SELECT o.id FROM OnlineCodingQuiz o WHERE o.makerId=:makerId and o.isAvailable=true ) AND q.type='ONLINE_CODING_QUIZ') OR (q.quizId IN (SELECT o.id FROM OnlineLanguageQuiz o WHERE o.makerId=:makerId and o.isAvailable=true ) AND q.type='ONLINE_LANGUAGE_QUIZ') OR (q.quizId IN (SELECT l.id FROM LogicQuiz l WHERE l.makerId=:makerId and l.isAvailable=true ) AND q.type='LOGIC_QUIZ') OR (q.quizId IN (SELECT s.id FROM SubjectiveQuiz s WHERE s.makerId=:makerId and s.isAvailable=true ) AND q.type='SUBJECTIVE_QUIZ') ORDER BY q.id DESC")
  List<Quiz> findAllByMakerIdAndOrderByIdDesc(@Param("makerId")Long makerId);

//  @Query(value = "select q from Quiz q where (q.quizId in (select distinct b.id from BasicQuiz b JOIN b.tags t where b.makerId=:makerId and b.isAvailable=true AND t.name in :names) AND (q.type='BASIC_BLANK_QUIZ' OR q.type='MULTIPLE_CHOICE' OR q.type='SINGLE_CHOICE')) OR (q.quizId IN (SELECT distinct h.id FROM HomeworkQuiz h JOIN h.tags t WHERE h.makerId=:makerId and h.isAvailable=true AND t.name in :names) AND q.type='HOMEWORK_QUIZ') OR (q.quizId IN (SELECT distinct o.id FROM OnlineCodingQuiz o JOIN o.tags t WHERE o.makerId=:makerId and o.isAvailable=true AND t.name in :names) AND q.type='ONLINE_CODING_QUIZ') OR (q.quizId IN (SELECT distinct l.id FROM LogicQuiz l JOIN l.tags t WHERE l.makerId=:makerId and l.isAvailable=true AND t.name in :names) AND q.type='LOGIC_QUIZ') OR (q.quizId IN (SELECT distinct s.id FROM SubjectiveQuiz s JOIN s.tags t WHERE s.makerId=:makerId and s.isAvailable=true AND t.name in :names) AND q.type='SUBJECTIVE_QUIZ') ORDER BY q.id DESC")
//  List<Quiz> findAllByTagsAndMakerIdAndOrderByIdDesc(@Param("names")String[] names, @Param("makerId")Long makerId);

  @Query(value = "select q from Quiz q where (q.quizId in (select distinct b.id from BasicQuiz b JOIN b.tags t where b.isAvailable=true AND t.name in :names) AND (q.type='BASIC_BLANK_QUIZ' OR q.type='MULTIPLE_CHOICE' OR q.type='SINGLE_CHOICE')) OR (q.quizId IN (SELECT distinct h.id FROM HomeworkQuiz h JOIN h.tags t WHERE h.isAvailable=true AND t.name in :names) AND q.type='HOMEWORK_QUIZ') OR (q.quizId IN (SELECT distinct o.id FROM OnlineCodingQuiz o JOIN o.tags t WHERE o.isAvailable=true AND t.name in :names) AND q.type='ONLINE_CODING_QUIZ') OR (q.quizId IN (SELECT distinct o.id FROM OnlineLanguageQuiz o JOIN o.tags t WHERE o.isAvailable=true AND t.name in :names) AND q.type='ONLINE_LANGUAGE_QUIZ') OR (q.quizId IN (SELECT distinct s.id FROM SubjectiveQuiz s JOIN s.tags t WHERE s.isAvailable=true AND t.name in :names) AND q.type='SUBJECTIVE_QUIZ') ORDER BY q.id DESC")
  List<Quiz> findAllByTagsAndOrderByIdDesc(@Param("names")String[] names);
}
