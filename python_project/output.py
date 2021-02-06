import json
import pandas as pd
import os
import matplotlib.pyplot as plt

plt.rcdefaults()


def check_dir(path):
    if not os.path.exists(path):
        os.mkdir(path)
    return path


def create_report_file(poll_list):
    absences = []
    anomalies = []
    dictionary = []
    for poll in poll_list:
        if poll.attended_students:
            for student in poll.absences:
                absences.append({'student no': student.student_no,
                                 'student_email': student.email, 'student name': student.full_name})
            if poll.anomalies:
                for student in poll.anomalies:
                    anomalies.append(
                        {'student email': student['email'], 'student name': student['name']})

            dictionary.append({"Students in BYS list but don't exist in this poll report (Absence)": absences,
                               "Students in this poll report but don't exist in BYS Student List (Anomalies)": anomalies})

            jsonStr = json.dumps(dictionary, indent=4,
                                 ensure_ascii=False).encode('utf8')
            with open('outputs/' + poll.poll_name + '.json', 'wb') as jsonfile:
                jsonfile.write(jsonStr)


def create_attendence_file(student_list, attendence, get_df=False):
    df = pd.DataFrame.from_records(
        [s.to_dict() for s in student_list]).drop(['answers'], axis=1)
    df['number of attendence polls'] = attendence.total_number
    df['attendance rate'] = 'attended ' + \
                            df['attendence'].astype(str) + ' of ' + \
                            str(attendence.total_number) + ' courses'
    df['attendence percentage'] = df['attendence'] / attendence.total_number
    if not get_df:
        df.to_excel(os.path.join(check_dir('outputs'),
                                 'attendence.xlsx'), index=False)
    else:
        return df


def create_poll_files(student_list, poll_list, get_df=False):
    df = pd.DataFrame.from_records([s.to_dict() for s in student_list]).drop([
        'answers', 'attendence'], axis=1)
    if get_df:
        dfs = {}
    for poll in poll_list:
        df_temp = df.copy()
        indexes = [a.no + 1 for a in poll.attended_students]
        columns = [a.question for a in poll.questions]
        answers = [[i.isCorrect for i in a.answers][0]
                   for a in poll.attended_students]
        df_poll = pd.DataFrame(answers, columns=columns)
        df_poll['no'] = indexes
        df_poll['number of questions'] = len(poll.questions)
        df_poll['success rate'] = [
            str(sum(a)) + ' of ' + str(len(poll.questions)) + ' is correct' for a in answers]
        df_poll['success percentage'] = [
            sum(a) / len(poll.questions) for a in answers]
        df_temp = pd.merge(df, df_poll, on='no', how='outer').fillna(0)
        if not get_df:
            df_temp.to_excel(os.path.join(check_dir('outputs'),
                                          poll.poll_name + '.xlsx'), index=False)
        else:
            df_temp['date'] = poll.date
            dfs[poll.poll_name] = df_temp

    if get_df:
        return dfs

#def std_list_to_df(list):


def create_global_files(student_list, poll_list, attendence):

    df_polls = create_poll_files(student_list, poll_list, get_df=True)
    df_attendence = create_attendence_file(
        student_list, attendence, get_df=True)
    '''dfs = {}         # old part
    for key, df in df_polls.items():
        dfs[key] = pd.merge(df, df_attendence[['no', 'attendence', 'number of attendence polls',
                                               'attendance rate', 'attendence percentage']], on='no').fillna(
            0).to_json()
    jsonStr = json.dumps(dfs, indent=4)
    with open('outputs/global.json', 'w') as jsonfile:
        jsonfile.write(jsonStr)'''



    # part0: read the global file - better to add as last part
    # df = read_ods(path, sheet_name)

    # part1: turn student_list into df

    l1 = len(student_list)
    name_list = []
    no_list = []

    index_list = range(0, l1)
    for i in range(0, l1):
        name_list.append(student_list[i].full_name)
    for i in range(0, l1):
        no_list.append(student_list[i].student_no)

    df_names = pd.DataFrame(name_list)
    df_nos = pd.DataFrame(no_list)
    df_index = pd.DataFrame(index_list)

    # combines the list together. --not sure if index is needed
    df_stu = pd.concat([df_index, df_names, df_nos], axis=1)
    df_stu.columns = ['index', 'student name', 'student number']


    # part2: choose the column order and collect them in one df

    # first, lets order the polls according to their dates (failed on ordering df_polls)
    '''dates = []
    for sample2 in df_polls:
        sample1 = df_polls[sample2]
        print("date for "+sample2+" is")
        date = sample1.date[0]

        if date is None:    # since attendance poll does not have a date, we need a update
            date = "2020-11-23 10:41:23"
            date = pd.Timestamp(date)

        dates.append(date)

    # dates.sort(reverse=False)
    '''

    # then combine it to df
    for sample2 in df_polls:    # gets the polls inside df_polls one by one
        sample1 = df_polls[sample2]

        # deletes duplicated rows.
        sample1 = sample1.drop_duplicates()
        sample1 = sample1.reset_index()
        del sample1['index']

        success_rate = sample1[sample1.columns[len(sample1.columns)-3]]
        lines_to_add = success_rate
        df_stu = pd.concat([df_stu, lines_to_add], axis=1)      # concat lines to df_stu


    # part3: correct the column names accordingly and compute the last columns.


    # part4: print the global file.
    print(df_stu)

    df_stu.to_excel("CSE3063_2020FALL_QuizGrading.xlsx")   # does not work with ods................




def create_graphs(poll_list):
    # create histogram.
    for poll in poll_list:
        if poll.attended_students:
            writer = pd.ExcelWriter(
                os.path.join(
                    check_dir('plots'), poll.poll_name + '.xlsx'), engine='xlsxwriter')
            for idx, question in enumerate(poll.questions):
                sorted_dict = {k: v for k, v in sorted(
                    question.choices.items(), key=lambda item: item[1], reverse=True)}

                values = list(sorted_dict.values())
                answer = list(sorted_dict.keys())
                df = pd.DataFrame(
                    {'question-' + str(idx + 1): values}, index=answer)
                df.to_excel(writer, sheet_name='question-' + str(idx + 1))

                workbook = writer.book
                worksheet = writer.sheets['question-' + str(idx + 1)]
                chart = workbook.add_chart({'type': 'bar'})
                chart.add_series({'values': '=question-%s!$B$2:$B$%s' % (str(idx + 1), str(1 + len(values))),
                                  'points': [
                                      {'fill': {'color': 'red'}}
                                  ]})
                chart.set_y_axis(
                    {'major_gridlines': {'visible': False}, 'text_axis': True})
                chart.set_x_axis(
                    {'name': question.question, 'text_axis': True})

                chart.set_legend({'values': answer})

                worksheet.insert_chart('D5', chart)

            writer.save()


def create_results(student_list, poll_list, attendence):
    # this will probably will be long, you will need to declare new variables etc.
    create_attendence_file(student_list, attendence)
    create_poll_files(student_list, poll_list)
    create_report_file(poll_list)
    create_global_files(student_list, poll_list, attendence)
    create_graphs(poll_list)
    # if there is no report file in output location, create a new json file

    # if there is already one report file, append new poll evaluations to it.
    # such as Q5 Q6 Q7

    # then return.

# def create_outputs(self, students, answers, polls):
#     self.create_results(students, answers)
#     self.create_statistics(answers, polls)
