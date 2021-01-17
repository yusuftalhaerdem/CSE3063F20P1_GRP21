import difflib
from utils import *
from answer import Answer
from student import Student
from output import *
from datetime import datetime


def matching(df, student_list, poll_list, question_columns, attendence):
    # connect answers.poll with one poll, if date does not match, create a new poll object and add it into polls
    # CAUTION!! only bind poll and answer when all the questions are matched.
    for row in df.itertuples():
        student = find_student(student_list, row.name)
        if not student:
            student = unmatched_students(student_list,row.name)
            # Anomali burda bakılabilir
            if not isinstance(student,Student):
                student_questions = [getattr(row, column) for column in question_columns if 'question' in column]
                poll = find_poll(poll_list, student_questions)
                if row.name not in poll.anomalies:
                    poll.anomalies[row.name] =  row.email
                
        if student:
            if student.email == '':
                student.email = row.email
            type_of_poll = check_poll_type(getattr(row, question_columns[0]))
            if type_of_poll == 'poll':
                student_questions = [
                    getattr(row, column) for column in question_columns if 'question' in column]
                student_answers = [
                    getattr(row, column) for column in question_columns if 'answer' in column]
                poll = find_poll(poll_list, student_questions)

                if poll:
                    date = datetime.strptime(row.date, '%b %d, %Y %H:%M:%S')
                    if not poll.date:
                        poll.date = date    
                    poll = check_unique_poll(poll,date,poll_list,student_questions)
                    
                    check_choices(poll,student_questions,student_answers)

                    corrects = correct_answers(poll, student_answers)
                    answer = Answer(poll,date, student_answers, corrects)
                    student.answers.append(answer)
                    poll.attended_students.append(student)
                else:
                    pass

            else:
                date = datetime.strptime(row.date, '%b %d, %Y %H:%M:%S')
                if abs((attendence.last_date - date).total_seconds()) > 1800:
                    attendence.total_number += 1

                student.attendence += 1
                attendence.last_date = date

    poll_find_absence(student_list,poll_list)
    create_results(student_list,poll_list,attendence)





# try to match answers with student objects

# if there is some unmatched students add them into unassigned_answers.


def extreme_matching(polls, students, answers, unassigned_answers):
    """  low priority  """
    # try matching in a way which is extreme. we may change its class in latter

    pass
