import pandas as pd
from student import Student
from attendence_poll import AttendencePoll
from question_poll import QuestionPoll
from question import Question
from utils import *
from matching import *
import os
import csv
import json

student_list = []


def read_glob(path):
    with open(path, 'r') as jsonfile:
        jsonContent = jsonfile.read()
    alist = json.loads(jsonContent)
    for l in alist.values():
        df = pd.read_json(l)
    student_cols = ['no', 'student no', 'first name', 'last name', 'attendence',
                    'number of attendence polls', 'attendance rate',
                    'attendence percentage']
    print(df.columns)
    df_students = df[student_cols]
    df_questionns = df.drop(student_cols,axis=1)
    print(df_students)

def read_student_file(path):
    # read all student objects and add them into students array
    # No ayarlanıcak
    path = path.replace("\\", "/")
    df = pd.read_excel(path, index_col=0)
    thresh = len(df) * 0.7
    df = df.dropna(thresh=thresh, axis=1)
    df = df[df.isnull().sum(axis=1) < 3]
    df.columns = df.iloc[0]
    df = df[df["Öğrenci No"] != "Öğrenci No"]
    df = df.set_index("No")
    df = df.reset_index().reset_index().drop(['No'], axis=1)
    df.columns = ["no", "student_no", "first_name", "last_name"]
    global student_list
    if(not student_list):
        student_list = [Student(**kwargs)
                        for kwargs in df.to_dict(orient='records')]

    return student_list


student_list = read_student_file(
    'python_project/inputs/CES3063_Fall2020_rptSinifListesi.xls')


def read_answer_file(path):
    # read all answer objects and add them into answer array
    paths = []
    if not os.path.isfile(path):
        paths = os.listdir(path)
    else:
        paths.append(path)

    poll_type = 'poll'
    poll_list = []
    for p in paths:
        p = p.replace("\\", "/")
        ext = os.path.splitext(p)[-1].lower()
        if '.xls' in ext:
            df = pd.read_excel(p, header=None)
        elif ext == '.csv':
            with open(p) as csvfile:
                delimeter = csv.Sniffer().sniff(csvfile.read(), delimiters=";,")
                csvfile.seek(0)
                delimeter = delimeter.delimiter
            df = pd.read_csv(
                p, sep='^([^;]+);', engine='python', names=['names', 'values'])
            df = df.reset_index()
            df.loc[0]['names'] = df['index'][0]
            df = df.drop(['index'], axis=1)
        else:
            print("invalid file format!!!!!!!!!!!!!!")
        unique_name_indexes = [
            x for x in df.loc[pd.isna(df[df.columns[1]]), :].index]
        unique_names = df.loc[unique_name_indexes][df.columns[0]].values
        df_iter = df.copy()

        for idx, name in enumerate(unique_names):
            first_index = unique_name_indexes[idx]
            last_index = unique_name_indexes[idx +
                                             1] if idx < len(unique_name_indexes)-1 else len(df)
            temp_dict = df_iter[first_index:last_index].to_dict(orient='split')
            temp_dict['index'] = [x for x in range(
                len(temp_dict['index']) - 1)]
            temp_dict['columns'] = [temp_dict['data'][0]
                                    [0], temp_dict['data'][0][0] + ' answers']
            temp_dict['data'].pop(0)
            df_temp = pd.DataFrame(temp_dict['data'], index=temp_dict['index'],
                                   columns=temp_dict['columns']).replace('\n', '', regex=True)
            question_list = [Question(a[0].replace('  ', ' '), [choice.replace(
                '"', '').strip() for choice in a[1].split(';')]) for a in df_temp.to_numpy()]
            poll = QuestionPoll(df_temp.columns[0], poll_type, question_list)
            poll_list.append(poll)

        return poll_list


poll_list = read_answer_file('python_project/inputs/answer_monday.xlsx')

attendence = AttendencePoll('attendence')


def read_report_file(path):
    paths = []
    if not os.path.isfile(path):
        paths = os.listdir(path)
    else:
        paths.append(path)
    for path in paths:
        df = pd.read_csv(path, skiprows=1, header=None, sep=",",
                         index_col=0).dropna(thresh=0.8, axis=1)
        question_columns = [
            "question_" +
            str(x // 2 + 1) if x % 2 == 0 else "answer_" + str(x // 2 + 1)
            for x in range(len(df.iloc[0]) - 3)
        ]
        columns = ["name", "email", "date"] + question_columns
        df.columns = columns
        df = df.replace('\n', '', regex=True).replace('  ', ' ', regex=True)
        matching(df, student_list, poll_list, question_columns, attendence)


dictionary = read_report_file(
    'python_project/inputs/CSE3063_20201123_Mon_zoom_PollReport.csv')

# try to match answers with student objects

# if there is some unmatched students add them into unassigned_answers.


def extreme_matching(polls, students, answers, unassigned_answers):
    """  low priority  """
    # try matching in a way which is extreme. we may change its class in latter

    pass


def read(polls_location, students_location, answers_location, polls, students, answers, unassigned_answers):
    read_polls(polls_location, polls)
    read_students(students_location, students)
    read_answers(answers_location, answers)
    matching(polls, students, answers, unassigned_answers)
    extreme_matching(polls, students, answers, unassigned_answers)
