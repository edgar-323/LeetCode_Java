from file_utils import FileUtils

class Logger(object):
    # NOTE: If an error occurs within this class,
    #       it will be sent to stderr/stdout.
    #   
    # NOTE: Every instantiation of this class will
    #       clear the global `Logger.log_file`.

    __LOGGER__ = None

    def __init__(self):
        self.file_utils = FileUtils()
        self.log_file = (
                f'{self.file_utils.get_home_dir()}/'
                'GitHub/playground_setup/job.log')
        self.file_utils.create_file(self.log_file)

    def log(self, msg):
        self.file_utils.append_to_file(self.log_file, msg)
        return self

    def log_with_prefix(self, prefix, msg):
        assert type(prefix) is str, (
                'Log message prefix must be a string')
        assert prefix, (
                'Log message prefix must be a non-empty string')
        assert type(msg) is str, (
                'Log message must be a string')
        assert msg, (
                'Log message must be non-empty string')
        return self.log(f'{prefix} -> {msg}')

    def get_logger():
        if Logger.__LOGGER__ is None:
            Logger.__LOGGER__ = Logger()
        return Logger.__LOGGER__
