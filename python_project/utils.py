from question_poll import QuestionPoll
import math
import difflib


def poll_find_absence(student_list, poll_list, option=True):
    for poll in poll_list:
        for student in student_list:
            if student not in poll.attended_students:
                if option:
                    poll.absences.append(student)
                if not option:
                    poll.attended_students.append(student)


def unmatched_students(student_list, student):
    # name = ''.join([a if a.isalnum() else break for a in student])
    # [i for i in datalist if any(j for j in filterList if j in i) ]
    name_list = [a.full_name for a in student_list]
    name_list_first = [a.first_name for a in student_list]
    name_list_last = [a.last_name for a in student_list]

    closest = difflib.get_close_matches(student.upper(), name_list, cutoff=0.5)
    closest_first_name = difflib.get_close_matches(
        student.upper(), name_list_first, cutoff=0.5
    )
    closest_last_name = difflib.get_close_matches(
        student.upper(), name_list_last, cutoff=0.5
    )

    if not closest:
        if len(closest_first_name) == 1:
            return [a for a in student_list if a.first_name == closest_first_name[0]][0]
        elif len(closest_last_name) == 1:
            return [a for a in student_list if a.last_name == closest_last_name[0]][0]
        else:
            return None
    if closest:
        return [a for a in student_list if a.full_name == closest[0]][0]


def check_poll_type(question):
    if "attending" in question.lower():
        return "attendence"
    else:
        return "poll"


def find_student(student_list, name):
    name = "".join([i for i in name if not i.isdigit()]).upper()
    student = [s for s in student_list if s.full_name in (name.replace("İ", "I"), name)]
    if student:
        return student[0]
    return None


def correct_answers(poll, student_answers, questions):
    student_answers = [
        [a for a in i.split(";")] if isinstance(i, str) else "" for i in student_answers
    ]
    student_answers_new = []
    for a in poll.questions:
        for idx, question in enumerate(questions):
            question = question.replace(" ", "")
            question_a = a.question.replace(" ", "")
            if question in question_a or question_a in question:
                student_answers_new.append(student_answers[idx])

    poll_answers = [[a for a in i.correct_choice] for i in poll.questions]
    corrects = []
    length = max(len(poll_answers), len(student_answers_new))

    for i in range(length):
        depth_len = max(len(student_answers_new[i]), len(poll_answers[i]))
        if length == len(student_answers_new):
            if len(student_answers_new[i]) == 1 and depth_len == 1:
                if student_answers_new[i] in poll_answers:
                    corrects.append(1)
                else:
                    corrects.append(-1)
            else:
                cnt = 0
                len_m = max(len(student_answers_new[i]), len(poll_answers[i]))
                tmp_cor = 0
                for j in range(len_m):
                    if len_m == len(student_answers_new[i]):
                        if student_answers_new[i][j] in poll_answers[i]:
                            tmp_cor += 1
                            break
                    else:
                        if poll_answers[i][j] in student_answers_new[i]:
                            tmp_cor += 1
                            break
                if tmp_cor > 0:
                    corrects.append(1)
                else:
                    corrects.append(-1)
        else:
            if len(poll_answers[i]) == 1:
                if poll_answers[i] in student_answers_new:
                    corrects.append(1)
                else:
                    corrects.append(-1)

    return corrects, 1 if sum(corrects) >= 1 else 0


def check_poll(poll, student_questions):
    # check all questions of a students and return true if it matches with a poll
    if len(poll.questions) == len(student_questions):
        valid = []
        for idx, a in enumerate(poll.questions):
            for question in student_questions:
                question = question.replace(" ", "")
                question_a = a.question.replace(" ", "")
                if question in question_a or question_a in question:
                    valid.append(True)
                    break

        if len(valid) == len(poll.questions) and all(valid):
            return True
        else:
            return False


def check_unique_poll(poll, date, poll_list, student_questions):
    if abs((poll.date - date).total_seconds()) > 3600:
        count = (
            poll.poll_name.split("_")[1] + 1
            if len(poll.poll_name.split("_")) > 2
            else 1
        )
        new_poll = QuestionPoll(
            poll.poll_name + "_%s" % count, poll.poll_type, poll.questions
        )
        new_poll.date = date
        poll_list.append(new_poll)
        return new_poll
    else:
        return poll


def pad_results(poll_list):

    pad = lambda i1, i2: i1 + [-1] * (len(i1) - len(i2))
    answer_full = [
        [pad(i.corrects, poll.questions) for i in a]
        for a in poll.student_answers.values()
    ]

    for poll in poll_list:
        for question in poll.questions:
            for k, v in poll.student_answers.items():
                poll.student_answers[k] = pad(v.corrects, question.choices)


def check_choices(poll, questions, answers):
    # for question in poll.questions:
    #     choices = [a for a in answers]
    if len(poll.questions) == len(questions):
        for idx, question in enumerate(poll.questions):
            if questions[idx] in question.question:
                for answer in answers[idx].split(";"):
                    if not answer in question.choices:
                        question.choices[answer] = 1
                    else:
                        question.choices[answer] += 1
