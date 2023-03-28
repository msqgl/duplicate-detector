# Duplicate Files Checker

Duplicate Files Checker is a Java application that uses the SHA-256 hashing algorithm to identify duplicate files in a given directory. The application accepts the directory path as a command-line argument.

## Requirements

- Java 17 or higher

## Usage

Run the following command to execute the application:

```
java -jar duplicate-files-checker.jar <directory>
```

Replace `<directory>` with the path of the directory you want to check for duplicate files.

## Example
```
java -jar duplicate-files-checker.jar /path/to/directory
```

The application will scan the specified directory for duplicate files and print their paths to the console.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
