import json

class Config():
    def __init__(self):
        pass

    def inputs(self):
        with open('inputs/config.json') as f:
            data = json.load(f)

        report_files_path = data['report files']
        answer_files_path = data['answer files']
        student_list = data['student list']
        glob_file_path = data['glob file']
        output_path = data['output path']

        return report_files_path, answer_files_path,student_list, glob_file_path,output_path

