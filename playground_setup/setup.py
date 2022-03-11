#!/usr/bin/python3


import argparse  # Flag module.
import os


HOME_DIR = os.environ['HOME']
GITHUB_DIR = f'{HOME_DIR}/GitHub'
LOGGER = None

arg_parser = argparse.ArgumentParser(
        description=f'Setup {GITHUB_DIR}/Practice')
arg_parser.add_argument(
        '--exercise',
        type=str,
        default=None,
        help='Leetcode exercise title')
args = arg_parser.parse_args()


class FileUtils:
    # NOTE: Errors within this class will be bubbled up
    #       to stderr/stdout.

    def __init__(self):
        pass

    def file_path_exists(self, path=None):
        try:
            os.path(path)
        except FileNotFoundError:
            return False
        return True

    def validate_file_existence(self, path):
        if not file_path_exists(path):
            raise FileNotFoundError(
                    'Error occurred while performing stat '
                    f'operation on {path}')

    def create_file(self, path):
        with open(path, 'r+') as fh:
            fh.truncate(0)

    def append_to_file(self, file_path=None, content=None):
        self.validate_file_existence(file_path)
        assert content is not None
        assert type(content) is str
        assert len(content) > 0
        with open(file_path, 'a') as fh:
            fh.write(content)


class Logger(object):
    # NOTE: If an error occurs within this class,
    #       it will be sent to stderr/stdout.
    #
    # NOTE: Every instantiation of this class will
    #       clear the global `Logger.LOG_FILE`.

    LOG_FILE = f'{GITHUB_DIR}/practice_setup/logs.txt'

    def __init__(self):
        self.file_utils = FileUtils()
        self.file_utils.create_file(Logger.LOG_FILE)

    def log(self, msg):
        self.file_utils.append_to_file(Logger.LOG_FILE, msg)


class Exercise(object):

    EXERCISES_DIR = f'{github_dir}/exercises'

    def __init__(self, title, logger):
        self.title = title
        self.exercise_dir = f'{Exercise.EXERCISES_DIR}/{self.title}'
        self.exercise = f'{self.exercise_dir}/Solution.java'
        self.logger = logger
        self.file_utils = FileUtils()
        self.validate_exercise()

    def validate_exercise(self):
        self.logger.log(
                f'Validating LeetCode exercise: \"{self.title}\"')
        # LeetCode problem must be a non-empty, single-token,
        # alpha-numerical string.
        assert self.title.isalnum(), 'LeetCode exercise must be alpha-numeric'
        # Ensure LeetCode exercise exists.
        self.file_utils.validate_file_existence(self.exercise)
        self.logger.log(f"Finished validating \"{self.title}\".\n")

    def get_title(self):
        return self.title

    def get_exercise_dir(self):
        return self.exercise_dir

    def get_exercise(self):
        return self.exercise


def PlaygroundBuilder(object):

    PLAYGROUND_DIR = f'{GitHub}/PlayGround'

    def __init__(self, exercise):
        assert type(exercise) is Exercise
        self.exercise = exercise


def setup_practice_dir(leetcode_problem):
    practice_dir = f'{problems_dir}/_Practice_'
    if not file_path_exists(practice_dir):
        os.mkdir(practice_dir)
        log(f'Created {practice_dir}.')

def get_logger():
    global LOGGER
    if LOGGER is None:
        LOGGER = Logger()
    return LOGGER

exercise = Exercise(args.exercise, get_logger())
