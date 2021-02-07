import json
import pandas as pd
import os
import matplotlib.pyplot as plt
import logging

logger = logging.getLogger('logger1')

plt.rcdefaults()

def check_dir(path):
    if not os.path.exists(path):
        os.mkdir(path)
    return path

class Output():
    def __init__(self,student_list, poll_list, attendence,output_path):
        self.student_list = student_list
        self.poll_list = poll_list
        self.attendence = attendence
        self.output_path = output_path

    def create_report_file(self):
        absences = []
        anomalies = []
        dictionary = []
        for poll in self.poll_list:
            if poll.attended_students:
                for student in poll.absences:
                    absences.append(
                        {
                            "student no": student.student_no,
                            "student_email": student.email,
                            "student name": student.full_name,
                        }
                    )
                if poll.anomalies:
                    for student in poll.anomalies:
                        anomalies.append(
                            {
                                "student email": student["email"],
                                "student name": student["name"],
                            }
                        )

                dictionary.append(
                    {
                        "Students in BYS list but don't exist in this poll report (Absence)": absences,
                        "Students in this poll report but don't exist in BYS Student List (Anomalies)": anomalies,
                    }
                )

                jsonStr = json.dumps(dictionary, indent=4, ensure_ascii=False).encode(
                    "utf8"
                )
                with open(
                    os.path.join(
                        check_dir(self.output_path  + "/report_files"),
                        (poll.poll_name.replace(' ','_') + ".json"),
                    ),
                    "wb",
                ) as jsonfile:
                    jsonfile.write(jsonStr)


    def create_attendence_file(self, get_df=False):
        logger.info("Creating attendence file")
        df = pd.DataFrame.from_records([s.to_dict() for s in self.student_list])
        df["number of attendence polls"] = self.attendence.total_number
        df["attendance rate"] = (
            "attended "
            + df["attendence"].astype(str)
            + " of "
            + str(self.attendence.total_number)
            + " courses"
        )
        df["attendence percentage"] = df["attendence"] / self.attendence.total_number
        if not get_df:
            df.to_excel(
                os.path.join(
                    check_dir(self.output_path),
                    "attendence.xlsx",
                ),
                index=False,
            )
            logger.info('Created attendence file')
        else:
            return df


    def create_poll_files(self, get_df=False):
        logger.info('Creating poll files')
        df = pd.DataFrame.from_records([s.to_dict() for s in self.student_list]).drop(
            ["attendence"], axis=1
        )
        if get_df:
            dfs = {}
        for poll in self.poll_list:
            if poll.attended_students:
                df_temp = df.copy()
                indexes = [a.no +1 for a in poll.student_answers.keys()]
                columns = [a.question for a in poll.questions]
                answers = [a.corrects for a in poll.student_answers.values()]
                df_poll = pd.DataFrame(answers, columns=columns)
                df_poll["no"] = indexes
                df_poll["number of correctly answererd questions"] = [
                    sum([i for i in a if i > 0]) for a in answers
                ]
                df_poll["number of wrongly answererd questions"] = [
                    abs(sum([i for i in a if i < 0])) for a in answers
                ]
                df_poll["success rate"] = [
                    str(sum([i for i in a if i>0])) + " of " + str(len(poll.questions)) + " is correct"
                    for a in answers
                ]
                df_poll["success percentage"] = [sum([i for i in a if i>0]) / len(poll.questions) for a in answers]
                df_temp = pd.merge(df, df_poll, on="no", how="outer").fillna(0)
                df_temp["number of questions"] = len(poll.questions)
                if not get_df:
                    df_temp.to_excel(
                        os.path.join(
                            check_dir(self.output_path + "/poll_reports"),
                            poll.poll_name.replace(' ','_') + "_" + poll.date + ".xlsx",
                        ),
                        index=False,
                    )
                else:
                    df_temp["date"] = poll.date
                    dfs[poll.poll_name] = df_temp
        logger.info('Created poll files')
        if get_df:
            return dfs


    def create_global_files(self):
        logger.info('Creating global files')
        df_polls = self.create_poll_files(get_df=True)
        df_attendence = self.create_attendence_file(get_df=True)
        dfs = {}
        for key, df in df_polls.items():
            dfs[key] = (
                pd.merge(
                    df,
                    df_attendence[
                        [
                            "no",
                            "attendence",
                            "number of attendence polls",
                            "attendance rate",
                            "attendence percentage",
                        ]
                    ],
                    on="no",
                )
                .fillna(0)
                .to_json()
            )
        jsonStr = json.dumps(dfs, indent=4)
        with open(
            os.path.join(
                check_dir(
                    self.output_path,
                ),
                "global.json",
            ),
            "w",
        ) as jsonfile:
            jsonfile.write(jsonStr)
            logger.info('Created global file')


    def create_graphs(self):
        logger.info('Creating graphs')
        for poll in self.poll_list:
            if poll.student_answers:
                check_dir(self.output_path + '/plots/')
                check_dir(self.output_path + '/plots/' + poll.poll_name.replace(' ','_') + "_" + poll.date +  '/')
                path = self.output_path + '/plots/' + poll.poll_name.replace(' ','_') + "_" + poll.date +  '/'

                for idx, question in enumerate(poll.questions):
                    sorted_dict = {
                        k: v
                        for k, v in sorted(
                            question.choices.items(), key=lambda item: item[1], reverse=True
                        )
                    }

                    values = list(sorted_dict.values())
                    keys = list(sorted_dict.keys()) 
                    keys = [i.replace(' ', '\n') for i in keys]
                    fig = plt.figure()
                    ax = fig.add_subplot(111)          

                    plt.pie(values, labels=keys,startangle=90, shadow = True,
            radius = 1.2, autopct = '%1.1f%%')
                    
                    plt.legend(loc=4) 
                    plt.savefig(os.path.join(path,'question_' + str(idx + 1) +'.png'))
                    plt.clf()
                    
            if poll.attended_students:
                values = [len(poll.attended_students),len(poll.absences)]
                keys = ['Attended', 'Absence']
                
                plt.pie(values, labels=keys,startangle=90, shadow = True, explode = (0, 0.1), 
            radius = 1.2, autopct = '%1.1f%%')
                plt.legend(loc=4)
                plt.savefig(os.path.join(path,'attendence.png'))
                plt.clf()
        logger.info('Created graphs')



    def create_student_reports(self):
        logger.info('Creating student reports')
        df = pd.DataFrame.from_records([s.to_dict() for s in self.student_list]).drop(
            ["attendence"], axis=1
        )
        for poll in self.poll_list:
            if poll.attended_students:   
                for student, answers in poll.student_answers.items():
                    for i, question in enumerate(poll.questions):
                        df["question text " + str(i+1)] = question.question
                        df["given answer " + str(i+1)] = poll.student_answers[student].student_answers[i]
                        df["correct answer(s) " + str(i+1)] = ';'.join(question.correct_choice)
                        df["isCorrect " + str(i+1)] = poll.student_answers[student].corrects[i]

                for i in range(len(df)):
                    df_temp = df.loc[df['student no'] == df.iloc[i]['student no']].drop(['no'],axis=1)
                    df_temp.to_excel(
                        os.path.join(
                            check_dir(self.output_path + "/poll_student_reports"),
                            (poll.poll_name.replace(' ','_')+ "_" + poll.date)
                            + "_"
                            + (
                                df_temp.iloc[0]['first name']
                                + "_"
                                + df_temp.iloc[0]['last name']
                                + "_"
                                + df_temp.iloc[0]['student no']
                            )
                            + ".xlsx",
                        ),
                        index=False,
                    )
        logger.info('Created student reports')

    def create_analytics_file(self):
        logger.info('Creating global analytics file')
        df = pd.DataFrame.from_records([s.to_dict_short() for s in self.student_list])
        total_number_of_questions = 0
        for poll in self.poll_list:
            if poll.attended_students:
                total_number_of_questions += len(poll.questions)
                indexes = [a.no +1 for a in poll.student_answers]                
                answers = [sum([i for i in a.corrects if i > 0]) for a in poll.student_answers.values()]
                df_poll = pd.DataFrame(answers, columns=[(poll.poll_name.replace(' ','_') + "_" + poll.date)])
                df_poll["no"] = indexes
                df = pd.merge(df, df_poll, on="no", how="outer").fillna(0)

        poll_columns = [i for i in df.columns if 'Poll' in i ]

        df['The total number of correctly answered questions in all quizzes'] = df[poll_columns].sum(axis=1)
        df['Total number of questions in all quizzes'] = total_number_of_questions
        df['global accuracy'] = df['The total number of correctly answered questions in all quizzes']/df['Total number of questions in all quizzes'] * 100.0
        df.to_excel(
            os.path.join(check_dir(self.output_path),'global analytics.xlsx'), 
            index=False
        )
        logger.info('Created global analytics file')



    def create_results(self):
        self.create_attendence_file()
        self.create_poll_files()
        self.create_student_reports()
        self.create_report_file()
        self.create_global_files()
        self.create_analytics_file()
        self.create_graphs()