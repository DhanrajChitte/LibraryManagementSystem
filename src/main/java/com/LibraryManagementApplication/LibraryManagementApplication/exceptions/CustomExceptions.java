package com.LibraryManagementApplication.LibraryManagementApplication.exceptions;

public class CustomExceptions
{

    // Custom exception for 404 Resource Not Found
    public static class ResourceNotFoundException extends RuntimeException
    {
        public ResourceNotFoundException(String message)
        {
            super(message);
        }
    }

    // Custom exception for 400 Bad Request
    public static class BadRequestException extends RuntimeException
    {
        public BadRequestException(String message)
        {
            super(message);
        }
    }

    // Custom exception for 500 Internal Server Error
    public static class InternalServerException extends RuntimeException
    {
        public InternalServerException(String message)
        {
            super(message);
        }
    }

    public static class ForbiddenException extends RuntimeException
    {
        public ForbiddenException(String message)
        {
            super(message);
        }
    }

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {
            super(message);
        }
    }
}
