from file_utils import FileUtils
from logger import Logger


class Exercise(object):

    def __init__(self, title=None):
        self.title = title
        self.file_utils = FileUtils()
        self.all_exercises_dir = f'{self.file_utils.get_home_dir()}/GitHub/exercises'
        self.exercise_dir = f'{self.all_exercises_dir}/{self.title}'
        self.exercise = f'{self.exercise_dir}/Solution.java'
        self.logger = Logger.get_logger()
        self.validate_exercise()

    def validate_exercise(self):
        self.log(f'Validating LeetCode exercise: \"{self.title}\"')
        # LeetCode problem must be a non-empty, single-token,
        # alpha-numerical string.
        assert type(self.title) is str, 'LeetCode exercise must be a string'
        assert self.title.isalnum(), 'LeetCode exercise must be alpha-numeric'
        # Ensure LeetCode exercise exists.
        self.file_utils.validate_dir_path(self.all_exercises_dir)
        self.file_utils.validate_dir_path(self.exercise_dir)
        self.file_utils.validate_file_path(self.exercise)
        self.log(f'Finished validating: \"{self.title}\".\n')

    def log(self, msg):
        self.logger.log(f'Exercise: {msg}')

    def get_title(self):
        return self.title

    def get_exercise_dir(self):
        return self.exercise_dir

    def get_exercise(self):
        return self.exercise
