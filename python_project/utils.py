from question_poll import QuestionPoll
import math
import difflib

def poll_find_absence(student_list,poll_list):
    for poll in poll_list:
        for student in student_list:
            for answer in student.answers:
                if poll is not answer.poll:
                    poll.absences.append(student)

def unmatched_students(student_list, student):
    # name = ''.join([a if a.isalnum() else break for a in student])
    if '@' in student:
        student = student.split('@')[0]
# [i for i in datalist if any(j for j in filterList if j in i) ]
    name_list = [a.full_name for a in student_list]
    name_list_first = [a.first_name for a in student_list]
    name_list_last = [a.last_name for a in student_list]

    closest = difflib.get_close_matches(student.upper(), name_list,cutoff=0.5)
    closest_first_name = difflib.get_close_matches(student.upper(), name_list_first,cutoff=0.5)
    closest_last_name = difflib.get_close_matches(student.upper(), name_list_last,cutoff=0.5)

    if not closest:
        if len(closest_first_name) == 1:
            return [a for a in student_list if a.first_name == closest_first_name[0]][0]
        elif len(closest_last_name) == 1:
            return [a for a in student_list if a.last_name == closest_last_name[0]][0]
        else:
            return student
    if closest:
        return [a for a in student_list if a.full_name == closest[0]][0]

def check_poll_type(question):
    if 'are you attending' in question.lower():
        return 'attendence'
    else:
        return 'poll'

def find_student(student_list,name):
    name = ''.join([i for i in name if not i.isdigit()]).upper()
    student = [s for s in student_list if s.full_name in (name.replace('İ','I'),name)]
    if student:
        return student[0]
    return None


def correct_answers(poll, student_answers):
    student_answers = [[a for a in i.split(';')] if isinstance(i,str) else '' for i in student_answers ]
    poll_answers = [[a for a in i.correct_choice] for i in poll.questions]
    corrects = [1 if set(l1) == set(l2) else 0 for l1,l2 in zip(poll_answers,student_answers) ]

    return corrects


def find_poll(poll_list, student_questions):
    # check all questions of a students and return true if it matches with a poll
    for poll in poll_list:
        if len(poll.questions) == len(student_questions):
            valid = [a.question == student_questions[idx]
                     for idx, a in enumerate(poll.questions)]
            if len(valid) == len(student_questions):
                return poll
            else:
                return None
        else:
            return None


def check_unique_poll(poll,date,poll_list,student_questions):
    if abs((poll.date - date).total_seconds()) > 3600:
        count = poll.poll_name.split('_')[1] + 1 if len(poll.poll_name.split('_')) > 2 else 1
        new_poll = QuestionPoll(
            poll.poll_name + '_%s' % count ,poll.poll_type, poll.questions)
        new_poll.date = date
        poll_list.append(new_poll)
        return new_poll
    else:
        return poll

def check_choices(poll,questions,answers):
    # for question in poll.questions:
    #     choices = [a for a in answers]
    for idx,question in enumerate(poll.questions):
        if questions[idx] == question.question:
            for answer in answers[idx].split(';'):
                if not answer in question.choices:
                    question.choices[answer] =1
                else:
                    question.choices[answer] +=1