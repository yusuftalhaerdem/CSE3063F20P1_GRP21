import json
import pandas as pd
import os

def check_dir(path):
    if not os.path.exists(path):
        os.mkdir(path)
    return path

def create_attendence_file(student_list,attendence,get_df=False):
    df = pd.DataFrame.from_records([s.to_dict() for s in student_list]).drop(['answers'],axis=1)
    df['number of attendence polls'] = attendence.total_number
    df['attendance rate'] = 'attended ' + df['attendence'].astype(str) + ' of ' + str(attendence.total_number) + ' courses'
    df['attendence percentage'] = df['attendence'] / attendence.total_number
    print(df.columns)
    if not get_df:
        df.to_excel(os.path.join(check_dir('outputs'),'attendence.xlsx'),index=False)
    else:
        return df


def create_poll_files(student_list,poll_list,get_df=False):
    df = pd.DataFrame.from_records([s.to_dict() for s in student_list]).drop(['answers','attendence'],axis=1)
    if get_df:
        dfs = {}
    for poll in poll_list:
        indexes = [a.no+1 for a in poll.attended_students]
        columns = [a.question for a in poll.questions]
        answers = [[i.isCorrect for i in a.answers][0] for a in poll.attended_students]
        df_poll = pd.DataFrame(answers,columns=columns)
        df_poll['no'] = indexes
        df_poll['number of questions'] = len(poll.questions)
        df_poll['success rate'] = [str(sum(a)) + ' of ' + str(len(poll.questions)) + ' is correct'for a in answers]
        df_poll['success percentage'] = [sum(a)/len(poll.questions) for a in answers]
        df = pd.merge(df,df_poll,on='no',how='outer').fillna(0)
        if not get_df:
            df.to_excel(os.path.join(check_dir('outputs'),poll.poll_name + '.xlsx'),index=False)
        else:
            df['date'] = poll.date
            dfs[poll.poll_name] = df

    if get_df:
        return dfs

def create_global_files(student_list, poll_list, attendence):
    df_polls = create_poll_files(student_list,poll_list,get_df=True)
    df_attendence = create_attendence_file(student_list,attendence,get_df=True)
    dfs = {}
    for key,df in df_polls.items():
        dfs[key] = pd.merge(df,df_attendence[['no','attendence','number of attendence polls','attendance rate','attendence percentage']],on='no').fillna(0).to_json()
    jsonStr = json.dumps(dfs)
    with open('outputs/global.json','w') as jsonfile:
        jsonfile.write(jsonStr)


def create_results(student_list, poll_list, attendence):
    # this will probably will be long, you will need to declare new variables etc.
    create_attendence_file(student_list,attendence)
    create_poll_files(student_list,poll_list)
    create_global_files(student_list,poll_list,attendence)
    # if there is no report file in output location, create a new json file

    # if there is already one report file, append new poll evaluations to it.
    # such as Q5 Q6 Q7

    # then return.
    pass

def create_statistics(answers, polls):
    # create histogram.

    # create some visual t,hings to show     a)  b)  c)  d)  e)
    #                                   Q1  %11 %13 %17 %19 %40     true answer will be highlighted.

    pass

# def create_outputs(self, students, answers, polls):
#     self.create_results(students, answers)
#     self.create_statistics(answers, polls)
