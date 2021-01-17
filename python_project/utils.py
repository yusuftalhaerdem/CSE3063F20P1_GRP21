from question_poll import QuestionPoll
import math

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