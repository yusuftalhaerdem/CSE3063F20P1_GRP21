import pandas as pd
import xlrd
import json
import csv
import datetime

from student import Student
from answer import Answer
from poll import Poll

from input import Input
from output import Output

students = []
polls = []
answers = []
unassigned_answers = []
Input.read(Input(), 'inputs/poll_answers.xlsx', 'inputs/CES3063_Fall2020_rptSinifListesi.XLS',
           'inputs/CSE3063_20201123_Mon_zoom_PollReport.csv', polls, students, answers, unassigned_answers)

Output.create_outputs(Output(), students, answers, polls)

# reallocate this part into functions
'''
filepath = 'inputs/CES3063_Fall2020_rptSinifListesi.XLS'

if not filepath.lower().endswith("json"):
    df = pd.read_excel(filepath, index_col=0)
    thresh = len(df) * .7
    df = df.dropna(thresh=thresh, axis=1)
    df = df[df.isnull().sum(axis=1) < 3]
    df.columns = df.iloc[0]
    df = df.set_index('No')
    df = df.reset_index()
    df = df[df['Öğrenci No'] != 'Öğrenci No'].drop(columns=['No'])
    df.columns = ['student_no', 'first_name', 'last_name']
    df.to_json('temp.json', orient='records')

student_list = []
with open('temp.json', 'r') as json_file:
    student_data = json.loads(json_file.read())
    for s in student_data:
        student_list.append(Student(**s))

filepath_poll = 'inputs/CSE3063_20201123_Mon_zoom_PollReport.csv'.replace("\\", "/")

with open(filepath_poll) as csvfile:
    data_list = list(csv.reader(csvfile))[1:]
    data_list = [element[1:] for element in data_list]
    data_list = [list(filter(None, lst)) for lst in data_list]
    question_columns = ['question_' + str(x // 2 + 1) if x % 2 == 0 else 'answer_' + str(x // 2 + 1) for x in
                        range(len(data_list[0]) - 3)]
    data_list_student_info_columns = ['name', 'email', 'date']
    data_list_student_info = [element[:3] for element in data_list]
    data_list_question_columns = [element[3:] for element in data_list]
    df = pd.DataFrame(data_list_student_info, columns=data_list_student_info_columns)
    df_other = pd.DataFrame(data_list_question_columns, columns=question_columns)
    df_other_dict = df_other.to_dict('records')
    dictionary = df.to_dict('records')
    for idx, item in enumerate(dictionary):
        item.update({"Polls": df_other_dict[idx]})

    with open('person.json', 'w') as json_file:
        json.dump(dictionary, json_file)
'''