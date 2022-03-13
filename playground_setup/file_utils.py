import os
import shutil


class FileUtils:
    # NOTE: Errors within this class will be bubbled up
    #       to stderr/stdout.

    def __init__(self):
        pass

    def get_home_dir(self):
        return os.environ['HOME']

    def path_exists(self, path=None):
        try:
            os.stat(path)
        except FileNotFoundError:
            return False
        return True

    def is_file(self, file_path=None):
        return self.path_exists(file_path) and os.path.isfile(file_path)

    def is_dir(self, dir_path=None):
        return self.path_exists(dir_path) and os.path.isdir(dir_path)

    def validate_path(self, path):
        if not self.path_exists(path):
            raise FileNotFoundError(f'Invalid path: {path}')

    def validate_file_path(self, file_path=None):
        if not self.is_file(file_path):
            raise FileNotFoundError(f'Invalid file path: {file_path}')

    def validate_dir_path(self, dir_path=None):
        if not self.is_dir(dir_path):
            raise FileNotFoundError(f'Invalid dir path: {dir_path}')

    def create_file(self, file_path):
        with open(file_path, 'w+') as fh:
            fh.truncate(0)

    def create_dir(self, dir_path=None):
        assert type(dir_path) is str, 'Dir must be a string'
        assert len(dir_path) > 0, 'Dir must be non-emtpy'
        os.mkdir(dir_path)

    def copy_file(self, src=None, dst=None):
        self.validate_file_path(file_path=src)
        shutil.copyfile(src=src, dst=dst)

    def append_to_file(self, file_path=None, content=None):
        assert type(content) is str, 'Content must be a string'
        assert content, 'Content must a non-empty string'
        self.validate_file_path(file_path=file_path)
        with open(file_path, 'a+') as fh:
            fh.write(f'{content}\n')
