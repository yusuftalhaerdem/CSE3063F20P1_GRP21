import difflib
from utils import *
from answer import Answer
from student import Student
from datetime import datetime
from output import *
import logging
logger = logging.getLogger('logger1')


class Matching():
    def __init__(self,report_files, student_list,dates, poll_list, attendence,output_path):
        self.report_files = report_files
        self.student_list = student_list
        self.dates = dates
        self.poll_list = poll_list
        self.attendence = attendence
        self.output_path = output_path


    def matching(self):
        attendence_dates = []
        # connect answers.poll with one poll, if date does not match, create a new poll object and add it into polls
        logger.info('Matching answers with students')
        for idx_date,df in enumerate(self.report_files):
            idx = 0
            finded = False
            for poll in self.poll_list:
                poll_question_1 = df['question_1'].unique()
                indexes = [df.index[df['question_1'] == i] for i in poll_question_1]
                start_indexes = [min(i) for i in indexes]
                attend = False
                idx += 1
                if finded and len(indexes) == 1:
                    break
                
                for i in start_indexes:
                    cnt = 0
                    df_temp = df.copy()[i:]
                    finded = False
                    for row in df_temp.itertuples():
                        questions = [
                            str(getattr(row,index))
                            for index in row._fields
                            if "question" in index and not pd.isnull(getattr(row,index))
                        ]
                        type_of_poll = check_poll_type(questions[0])
                        prev_questions = questions
            
                        if not finded:
                            if cnt > 5:
                                break
                        if idx > 1:
                            if type_of_poll is "attendence":
                                break

                        cnt += 1
                        student = find_student(self.student_list, row.name)
                        if not student:
                            student = unmatched_students(self.student_list, row.name)
                            # Anomali burda bakılabilir
                            if not isinstance(student, Student):
                                valid_poll = check_poll(poll, questions)
                                if valid_poll:
                                    if row.name not in poll.anomalies:
                                        poll.anomalies.append(
                                            {"email": row.email, "name": row.name}
                                        )
                                continue

                        if student:
                            if student.email == "":
                                student.email = row.email
                            if type_of_poll == "poll":
                                student_answers = [
                                    str(getattr(row,index))
                                    for index in row._fields
                                    if "answer" in index and not pd.isnull(getattr(row,index))
                                ]
                                valid_poll = check_poll(poll, questions)
                                if valid_poll:
                                    finded = True
                                    poll.date = self.dates[idx_date]
                                    # poll = check_unique_poll(poll,date,poll_list,student_questions)

                                    check_choices(poll, questions, student_answers)

                                    corrects,iscorrect = correct_answers(poll, student_answers,questions)
                                    answer = Answer(student_answers,corrects, iscorrect)
                                    poll.student_answers[student] = answer
                                    poll.attended_students.append(student)
                                    
                                    logger.info('Matched ' + poll.poll_name + ' with' + student.full_name)

                                    date = row.date[:-9]
                                    if date not in attendence_dates and finded:
                                        self.attendence.total_number += 1
                                        attendence_dates.append(date)
                                        attend = True
                                    if finded and attend:
                                        logger.info('Student' +  student.full_name + ' is attended' + poll.poll_name + ' in ' + date)
                                        student.attendence +=1

                            else:

                                date = row.date[:-9]
                                if date not in attendence_dates:
                                    self.attendence.total_number += 1
                                    attendence_dates.append(date)
                                    attend = True

                                finded = True
                                if finded and attend:
                                    logger.info('Student' +  student.full_name + ' is attended' + poll.poll_name + ' in ' + date)
                                    student.attendence += 1
                            
        logger.info('Finding absent students')
        poll_find_absence(self.student_list, self.poll_list)
        # pad_results(poll_list)
        result = Output(self.student_list, self.poll_list, self.attendence, self.output_path)
        result.create_results()
