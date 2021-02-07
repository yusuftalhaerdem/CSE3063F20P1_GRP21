import pandas as pd
from student import Student
from attendence_poll import AttendencePoll
from question_poll import QuestionPoll
from question import Question
from utils import *
from matching import Matching
import os
import csv
import json

class Input():
    def __init__(self):
        self.student_list = []
        self.attendence = AttendencePoll("attendence")
        self.poll_list = []

    def read_glob(self,path):
        if not os.path.exists(path):
            return
        with open(path, "r") as jsonfile:
            jsonContent = jsonfile.read()
        alist = json.loads(jsonContent)
        dfs = {}
        for k, l in alist.items():
            dfs[k] = pd.read_json(l)
        student_cols = [
            "no",
            "student no",
            "first name",
            "last name",
            "attendence",
            "email",
        ]
        attendence_cols = ["number of attendence polls"]

        keys = list(alist.keys())
        for k, df in dfs.items():
            df_students = df[student_cols]
            df_questions = df.drop(student_cols, axis=1)

            name_list = df["first name"].to_list()
            surname_list = df["last name"].to_list()
            fullname_list = [n1 + " " + n2 for n1, n2 in zip(name_list, surname_list)]
            df["fullname"] = fullname_list
            for fullname in fullname_list:
                student = find_student(student_list, fullname)
                student.attendence += df.loc[df["fullname"] == fullname][
                    "attendence"
                ].values[0]


    def read_student_file(self,path):
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
        df = df.reset_index().reset_index().drop(["No"], axis=1)
        df.columns = ["no", "student_no", "first_name", "last_name"]
        if not self.student_list:
            self.student_list = [Student(**kwargs) for kwargs in df.to_dict(orient="records")]

    def read_answer_file(self,path):
        # read all answer objects and add them into answer array
        paths = []
        if not os.path.isfile(path.replace("\\", "/")):
            paths = [os.path.join(path, i) for i in os.listdir(path.replace("\\", "/"))]
        else:
            paths.append(path.replace("\\", "/"))

        poll_type = "poll"
        for p in paths:
            with open(p) as f:
                text = list(filter(bool, [w.replace("\n", "") for w in f.readlines()][2:]))
                test = [w.strip().split("\t") for w in text]

                questions = {}
                poll_name = ""
                for idx in range(len(test) + 1):
                    i = test[idx] if idx < len(test) else [i for i in range(3)]
                    if len(i) > 1:
                        if questions:
                            question_list = [Question(k, v) for k, v in questions.items()]
                        questions = {}
                        if poll_name:
                            self.poll_list.append(
                                QuestionPoll(poll_name, poll_type, question_list)
                            )
                        poll_name = i[0]
                    elif "Answer" in i[0]:
                        questions[question].append(i[0].split(":")[1].strip())
                    else:
                        question = i[0].split('.',1)[1].strip()
                        questions[question] = []


    def read_report_file(self,path,output_path):
        paths = []
        report_files = []
        dates = []
        if not os.path.isfile(path):
            paths = [os.path.join(path, i) for i in os.listdir(path)]
        else:
            paths.append(path)
        for path in paths:
            names = list(pd.read_csv(path, nrows=0, skiprows=6)) + [i for i in range(20)]
            df = pd.read_csv(
                path, skiprows=6, header=None, sep=",", index_col=0, names=names
            ).dropna(thresh=0.8, axis=1, how="all")
            df_details = pd.read_csv(path, nrows=5, sep="!")
            date = df_details.iloc[2]["Poll Report"].split(",")[-2].strip()
            date = date.replace('-','_').replace(' ','_').replace(':','_')
            dates.append(date)
            question_columns = [
                "question_" + str(x // 2 + 1) if x % 2 == 0 else "answer_" + str(x // 2 + 1)
                for x in range(len(df.iloc[0]) - 3)
            ]
            columns = ["name", "email", "date"] + question_columns
            df.columns = columns
            df = df.replace("\n", "", regex=True).replace("  ", " ", regex=True)
            report_files.append(df)

        matching_o = Matching(report_files, self.student_list,dates, self.poll_list, self.attendence,output_path)
        matching_o.matching()

    def read(self,report_files_path, answer_files_path,student_list, glob_file_path,output_path):
        self.read_student_file(student_list)
        ##read_glob(glob_file_path)
        self.read_answer_file(answer_files_path)
        self.read_report_file(report_files_path,output_path)